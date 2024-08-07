package com.mjk.summary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "category")
@ToString
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100, unique = true)
    private String category;

    @Column(columnDefinition = "int unsigned default '0'")
    private int count;
}
