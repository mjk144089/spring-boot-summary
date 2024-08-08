package com.mjk.summary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mjk.summary.repository.ParagraphRepository;
import com.mjk.summary.model.Paragraph;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnService {
    private final ParagraphRepository paragraphRepository;

    public List<Paragraph> getAllParagraphs() { // 그냥 모든 글 가져오기
        return paragraphRepository.findAll();
    }

    public Optional<Paragraph> getParagraphById(Integer id) {
        return paragraphRepository.findById(id);
    }
}
