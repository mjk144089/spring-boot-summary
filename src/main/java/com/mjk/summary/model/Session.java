package com.mjk.summary.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "session")
@ToString
@Data
public class Session {
    @Id
    @Column(name = "sessionId", nullable = false, length = 255)
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = true)
    private Users user;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;
}
