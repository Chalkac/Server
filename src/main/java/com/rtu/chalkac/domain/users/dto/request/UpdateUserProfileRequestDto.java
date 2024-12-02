package com.rtu.chalkac.domain.users.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserProfileRequestDto {
    private String userId;
    private String profileUrl;
}
