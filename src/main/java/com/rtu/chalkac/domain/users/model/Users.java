package com.rtu.chalkac.domain.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @Column(name = "user_id", length = 100, nullable = false, unique = true)
    private String userId;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "subscribe_cnt", nullable = false)
    private Long subscribeCount;

    @Column(name = "profile_url")
    private String profileUrl;
}
