package com.mjk.summary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/learn")
public class LearnController {
    @GetMapping("/challenges")
    public String getChallengesPage(
            @RequestParam(name = "order", required = false, defaultValue = "recent") String order,
            @RequestParam(name = "category", required = false, defaultValue = "all") String category,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page) {
        return "learn/challenge_list.html";
    }

    @GetMapping("/summary")
    public String getSummaryPage() {
        return "learn/summary.html";
    }

}
