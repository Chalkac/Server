package com.rtu.chalkac.domain.users.service;

import com.rtu.chalkac.domain.users.model.Users;
import com.rtu.chalkac.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users getUser(String id){
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user 404"));
    }
}
