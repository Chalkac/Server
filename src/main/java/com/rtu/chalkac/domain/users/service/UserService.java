package com.rtu.chalkac.domain.users.service;

import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositrory userRepositrory;

    public Users getUser(String id){
        return userRepositrory.findById(id).orElseThrow(()-> new IllegalArgumentException("user 404"));
    }
}
