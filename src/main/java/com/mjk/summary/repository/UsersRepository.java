package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjk.summary.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    // 필요하다면 추가적인 쿼리 메소드 정의
}
