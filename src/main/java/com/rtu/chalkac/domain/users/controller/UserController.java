package com.rtu.chalkac.domain.users.controller;

import com.rtu.chalkac.domain.users.dto.request.UpdateUserNicknameRequestDto;
import com.rtu.chalkac.domain.users.dto.request.UpdateUserProfileRequestDto;
import com.rtu.chalkac.domain.users.dto.response.UserResponseDto;
import com.rtu.chalkac.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 정보 불러오기
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String userId) {
        UserResponseDto user = userService.findUser(userId);
        return ResponseEntity.ok(user);
    }

    // 유저 닉네임 수정
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@RequestBody UpdateUserNicknameRequestDto dto) {
        userService.updateNickname(dto);
        return ResponseEntity.ok().build();
    }

    // 유저 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserProfileRequestDto dto) {
        userService.updateProfile(dto);
        return ResponseEntity.ok().build();
    }

    // 유저 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
