package com.rtu.chalkac.domain.users.dto.response;

import com.rtu.chalkac.domain.users.model.Users;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDto {
    private String userId;
    private String nickname;
    private LocalDate date;
    private long subscribeCount;
    private String profileUrl;

    public UserResponseDto(Users user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.date = user.getDate();
        this.subscribeCount = user.getSubscribeCount();
        this.profileUrl = user.getProfileUrl();
    }
}
