package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjk.summary.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    /* 이메일 중복 여부 확인 메소드 */
    boolean existsByEmail(String email);
}
