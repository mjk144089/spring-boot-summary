package com.mjk.summary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "summaries")
@ToString
@Data
public class Summaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int summaryId;

    @Column(name = "paragraph_id", nullable = false)
    private int paragraphId;

    @Column(nullable = false, length = 255)
    private String uid;

    @Column(nullable = false)
    private int score;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "user_written_text", nullable = false, columnDefinition = "TEXT")
    private String userWrittenText; // 사용자가 작성한 글
}
