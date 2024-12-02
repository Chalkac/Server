package com.rtu.chalkac.domain.users.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserNicknameRequestDto {
    private String userId;
    private String nickname;
}
