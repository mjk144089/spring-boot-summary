package com.mjk.summary.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "paragraph")
@ToString
@Data
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Column(name = "avg_score", columnDefinition = "decimal(4,1) default '0.0'")
    private BigDecimal avgScore;

    @Column(columnDefinition = "int unsigned default '0'")
    private int count;

    @Column(name = "category_id")
    private Integer categoryId;
}
