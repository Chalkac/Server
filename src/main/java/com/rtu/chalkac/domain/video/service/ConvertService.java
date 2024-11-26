package com.rtu.chalkac.domain.video.service;

import com.rtu.chalkac.domain.video.dto.request.ConvertSaveRequestDto;
import com.rtu.chalkac.domain.video.dto.response.ConvertResponseDto;
import com.rtu.chalkac.domain.video.model.Video;
import com.rtu.chalkac.domain.video.repository.VideoRepository;
import com.rtu.chalkac.global.properties.AwsProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConvertService {

    private final AwsProperties awsProperties;
    private final VideoService videoService;
    private final VideoRepository videoRepository;

    private MediaConvertClient mediaConvertClient;

    @PostConstruct
    private void init() {
        this.mediaConvertClient = MediaConvertClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        awsProperties.getAccessKey(),
                                        awsProperties.getSecretKey()
                                )
                        )
                )
                .build();
    }

    public ConvertResponseDto startMediaConvertJob(String inputS3Url, String outputS3Url) {
        try {
            CreateJobRequest createJobRequest = CreateJobRequest.builder()
                    .role(awsProperties.getMediaRole()) // IAM Role ARN
                    .settings(createJobSettings(inputS3Url, outputS3Url))
                    .build();

            CreateJobResponse createJobResponse = mediaConvertClient.createJob(createJobRequest);
            return new ConvertResponseDto(createJobResponse.job().id()); // 작업 ID 반환
        } catch (MediaConvertException e) {
            throw new RuntimeException("Failed to create MediaConvert job: " + e.getMessage(), e);
        }
    }

    private JobSettings createJobSettings(String inputS3Url, String outputS3Url) {
        return JobSettings.builder()
                .inputs(Input.builder()
                        .fileInput(inputS3Url) // 입력 파일 경로
                        .audioSelectors(Map.of(
                                "Audio Selector 1", // 오디오 셀렉터 이름
                                AudioSelector.builder()
                                        .defaultSelection(AudioDefaultSelection.DEFAULT) // 기본 오디오 트랙 선택
                                        .build()
                        ))
                        .build())
                .outputGroups(
                        createHlsOutputGroup(outputS3Url)
                )
                .build();
    }

    private OutputGroup createHlsOutputGroup(String outputS3Url) {
        return OutputGroup.builder()
                .outputGroupSettings(OutputGroupSettings.builder()
                        .type(OutputGroupType.HLS_GROUP_SETTINGS)
                        .hlsGroupSettings(HlsGroupSettings.builder()
                                .destination(outputS3Url) // HLS 출력 경로
                                .segmentLength(10) // 각 세그먼트 길이 (초)
                                .minSegmentLength(2) // 최소 세그먼트 길이
                                .destinationSettings(DestinationSettings.builder()
                                        .s3Settings(S3DestinationSettings.builder()
                                                .accessControl(S3DestinationAccessControl.builder()
                                                        .cannedAcl(S3ObjectCannedAcl.BUCKET_OWNER_FULL_CONTROL) // ACL 설정
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .outputs(
                        createHlsOutput(480, "_480p"), // 480p 출력
                        createHlsOutput(720, "_720p"), // 720p 출력
                        createHlsOutput(1080, "_1080p") // 1080p 출력
                )
                .build();
    }

    private Output createHlsOutput(int height, String nameModifier) {
        return Output.builder()
                .containerSettings(ContainerSettings.builder()
                        .container(ContainerType.M3_U8) // HLS 컨테이너
                        .build())
                .videoDescription(VideoDescription.builder()
                        .codecSettings(VideoCodecSettings.builder()
                                .codec(VideoCodec.H_264) // H.264 코덱
                                .h264Settings(H264Settings.builder()
                                        .rateControlMode(H264RateControlMode.QVBR) // QVBR 설정
                                        .maxBitrate(8000000) // 최대 비트레이트
                                        .build())
                                .build())
                        .height(height) // 출력 해상도 높이
                        .build())
                .audioDescriptions(AudioDescription.builder()
                        .audioSourceName("Audio Selector 1")
                        .codecSettings(AudioCodecSettings.builder()
                                .codec(AudioCodec.AAC) // AAC 오디오 코덱
                                .aacSettings(AacSettings.builder()
                                        .bitrate(96000) // 오디오 비트레이트
                                        .sampleRate(44100) // 샘플링 레이트
                                        .codingMode(AacCodingMode.CODING_MODE_2_0) // 스테레오 설정
                                        .build())
                                .build())
                        .build())
                .nameModifier(nameModifier)
                .build();
    }

    public String getConvertStatus(String jobId){
        GetJobRequest jobRequest = GetJobRequest.builder().id(jobId).build();
        GetJobResponse jobResponse = mediaConvertClient.getJob(jobRequest);
        String status = "";
        if(jobResponse.job().status() == JobStatus.SUBMITTED) status = "SUBMITTED";
        else if(jobResponse.job().status() == JobStatus.ERROR) status = "ERROR";
        else if(jobResponse.job().status() == JobStatus.PROGRESSING) status = "PROGRESSING";
        else if(jobResponse.job().status() == JobStatus.COMPLETE) status = "COMPLETE";
        return status;
    }

    public void saveConvertUrl(ConvertSaveRequestDto dto){
        Video video = videoService.getVideo(dto.getVideoId());
        video.setConvertUrl(extractConvertUrl(dto.getJobId()));
        videoRepository.save(video);
    }

    private String extractConvertUrl(String jobId) {
        try {
            // MediaConvert 작업 상태 조회
            GetJobRequest jobRequest = GetJobRequest.builder().id(jobId).build();
            GetJobResponse jobResponse = mediaConvertClient.getJob(jobRequest);

            // 작업 성공 여부 확인
            if (jobResponse.job().status() != JobStatus.COMPLETE) {
                throw new RuntimeException("MediaConvert job is not complete. Current status: " + jobResponse.job().status());
            }

            // 작업의 출력 그룹에서 경로 추출
            return jobResponse.job().settings().outputGroups().stream()
                    .filter(outputGroup -> outputGroup.outputGroupSettings().hlsGroupSettings() != null) // HLS 그룹 필터링
                    .map(outputGroup -> outputGroup.outputGroupSettings().hlsGroupSettings().destination())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No HLS output group found in job settings"));
        } catch (MediaConvertException e) {
            throw new RuntimeException("Failed to get MediaConvert job details: " + e.getMessage(), e);
        }
    }
}
