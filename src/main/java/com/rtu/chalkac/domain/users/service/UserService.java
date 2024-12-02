package com.rtu.chalkac.domain.users.service;

import com.rtu.chalkac.domain.users.dto.request.UpdateUserNicknameRequestDto;
import com.rtu.chalkac.domain.users.dto.request.UpdateUserProfileRequestDto;
import com.rtu.chalkac.domain.users.dto.response.UserResponseDto;
import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users getUser(String id){
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user 404"));
    }

    // 유저 정보 불러오기
    public UserResponseDto findUser(String userId){
        return new UserResponseDto(getUser(userId));
    }

    // 유저 정보 수정하기 (닉네임)
    @Transactional
    public void updateNickname(UpdateUserNicknameRequestDto dto){
        Users user = getUser(dto.getUserId());
        user.setNickname(dto.getNickname());
        userRepository.save(user);
    }

    // 유저 정보 수정하기 (프로필)
    @Transactional
    public void updateProfile(UpdateUserProfileRequestDto dto){
        Users user = getUser(dto.getUserId());
        user.setProfileUrl(dto.getProfileUrl());
        userRepository.save(user);
    }

    // 유저 탈퇴
    @Transactional
    public void deleteUser(String userId){
        Users user = getUser(userId);
        userRepository.delete(user);
    }
}
