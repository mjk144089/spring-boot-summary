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
    @Column(name = "uid")
    private String uid;

    @Column(name = "sessionId", nullable = false, length = 255, unique = true)
    private String sessionId;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;

    @ManyToOne
    @JoinColumn(name = "uid", insertable = false, updatable = false, referencedColumnName = "uid")
    private Users user;
}
