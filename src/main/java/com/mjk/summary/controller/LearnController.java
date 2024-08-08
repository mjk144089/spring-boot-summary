package com.mjk.summary.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mjk.summary.model.Paragraph;
import com.mjk.summary.service.LearnService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/learn")
@RequiredArgsConstructor
public class LearnController {
    private final LearnService learnService;

    @GetMapping("/challenges")
    public String getChallengesPage(
            @RequestParam(name = "order", required = false, defaultValue = "recent") String order,
            @RequestParam(name = "category", required = false, defaultValue = "all") String category,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            Model model) {
        List<Paragraph> paragraphs = learnService.getAllParagraphs();
        model.addAttribute("paragraphs", paragraphs);
        return "learn/challenge_list.html";
    }

    @GetMapping("/summary")
    public String getSummaryPage(@RequestParam(name = "id", required = true) Integer id, Model model) {
        System.out.println(id);
        return "learn/summary.html";
    }

}
