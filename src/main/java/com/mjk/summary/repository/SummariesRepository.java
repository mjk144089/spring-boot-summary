package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mjk.summary.model.Summaries;

@Repository
public interface SummariesRepository extends JpaRepository<Summaries, Integer> {

}
