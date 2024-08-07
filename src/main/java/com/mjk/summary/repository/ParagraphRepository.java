package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mjk.summary.model.Paragraph;

public interface ParagraphRepository extends JpaRepository<Paragraph, Integer> {

}
