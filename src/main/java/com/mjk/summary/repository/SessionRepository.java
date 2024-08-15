package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjk.summary.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Session findBySessionId(String sessionId);
}
