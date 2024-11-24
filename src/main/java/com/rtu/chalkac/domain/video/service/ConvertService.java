package com.rtu.chalkac.domain.video.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.*;

@Service
@RequiredArgsConstructor
public class ConvertService {
    // 테스트용 MediaConvertClient 하드코딩
    private final MediaConvertClient mediaConvertClient = MediaConvertClient.builder()
            .region(Region.of("ap-northeast-2")) // 하드코딩된 AWS 리전
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                    "AKIAQKPIL3NSNOSK4VJO",        // 테스트용 Access Key ID
                                    "biK+hR8ZZeZKsINqsn5U4WIUXeN6lNwOYJ80byJm"     // 테스트용 Secret Access Key
                            )
                    )
            )
            .build();

    public String startMediaConvertJob(String inputS3Url, String outputS3Url) {
        try {
            CreateJobRequest createJobRequest = CreateJobRequest.builder()
                    .role("arn:aws:iam::022499023716:role/chalkac-mediaconvert-role") // IAM Role ARN
                    .settings(createJobSettings(inputS3Url, outputS3Url))
                    .build();

            CreateJobResponse createJobResponse = mediaConvertClient.createJob(createJobRequest);
            return createJobResponse.job().id(); // 작업 ID 반환
        } catch (MediaConvertException e) {
            throw new RuntimeException("Failed to create MediaConvert job: " + e.getMessage(), e);
        }
    }

    private JobSettings createJobSettings(String inputS3Url, String outputS3Url) {
        return JobSettings.builder()
                .inputs(Input.builder()
                        .fileInput(inputS3Url) // 입력 파일 경로
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

    public String getMediaConvertJobStatus(String jobId) {
        try {
            GetJobRequest getJobRequest = GetJobRequest.builder()
                    .id(jobId)
                    .build();

            GetJobResponse getJobResponse = mediaConvertClient.getJob(getJobRequest);
            return getJobResponse.job().statusAsString();
        } catch (MediaConvertException e) {
            throw new RuntimeException("Failed to get MediaConvert job status: " + e.getMessage(), e);
        }
    }
}
