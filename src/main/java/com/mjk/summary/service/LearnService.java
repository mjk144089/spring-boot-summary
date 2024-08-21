package com.mjk.summary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mjk.summary.repository.ParagraphRepository;
import com.mjk.summary.repository.SummariesRepository;
import com.mjk.summary.model.Paragraph;
import com.mjk.summary.model.Summaries;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnService {
    private final ParagraphRepository paragraphRepository;
    private final SummariesRepository summariesRepository;

    public List<Paragraph> getAllParagraphs() { // 그냥 모든 글 가져오기
        return paragraphRepository.findAll();
    }

    public Optional<Paragraph> getParagraphById(Integer id) {
        return paragraphRepository.findById(id);
    }

    public Summaries saveUserWritten(Summaries summaries) {
        // 주어진 paragraphId와 uid로 엔티티 조회
        Summaries existingSummary = summariesRepository.findByParagraphIdAndUid(summaries.getParagraphId(),
                summaries.getUid());

        if (existingSummary != null) {
            // 기존 엔티티가 존재하면 업데이트
            existingSummary.setScore(summaries.getScore());
            existingSummary.setUserWrittenText(summaries.getUserWrittenText());
            return summariesRepository.save(existingSummary);
        } else {
            // 기존 엔티티가 없으면 새로 저장
            return summariesRepository.save(summaries);
        }
    }

    /**
     * paragraph ID값을 이용하여 사람들의 요약 내역을 불러옵니다
     */
    public List<Summaries> getSummariesByParagraphId(int paragraph_id) {
        return summariesRepository.findByParagraphId(paragraph_id);
    }
}
