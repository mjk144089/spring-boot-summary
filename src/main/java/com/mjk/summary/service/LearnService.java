package com.mjk.summary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mjk.summary.repository.ParagraphRepository;
import com.mjk.summary.model.Paragraph;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnService {
    private final ParagraphRepository paragraphRepository;

    public List<Paragraph> getAllParagraphs() {
        return paragraphRepository.findAll();
    }
}
