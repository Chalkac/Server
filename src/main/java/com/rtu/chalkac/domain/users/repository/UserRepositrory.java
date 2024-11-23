package com.rtu.chalkac.domain.users.repository;

import com.rtu.chalkac.domain.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositrory extends JpaRepository<Users, String> {
}
