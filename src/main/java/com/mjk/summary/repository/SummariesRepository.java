package com.mjk.summary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjk.summary.model.Summaries;
import java.util.List;

@Repository
public interface SummariesRepository extends JpaRepository<Summaries, Integer> {
    Summaries findByParagraphIdAndUid(int paragraphId, String uid);

    List<Summaries> findByParagraphId(int paragraphId);

    List<Summaries> findByUid(String uid);
}
