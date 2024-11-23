package com.rtu.chalkac.domain.users.service;

import com.rtu.chalkac.domain.users.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositrory userRepositrory;
}
