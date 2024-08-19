package com.mjk.summary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "summaries", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "paragraph_id", "uid" })
})
@ToString
@Data
public class Summaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private int summaryId;

    @Column(name = "paragraph_id", nullable = false)
    private int paragraphId;

    @Column(nullable = false, length = 255)
    private String uid;

    @Column(nullable = false)
    private int score;

    @Column(name = "user_written_text", nullable = false, columnDefinition = "TEXT")
    private String userWrittenText; // 사용자가 작성한 글
}
