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

        System.out.println(order);
        System.out.println(category);
        System.out.println(page);
        return "learn/challenge_list.html";
    }

}
