package com.mjk.summary.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mjk.summary.model.Paragraph;
import com.mjk.summary.model.Summaries;
import com.mjk.summary.service.AuthService;
import com.mjk.summary.service.LearnService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/learn")
@RequiredArgsConstructor
public class LearnController {
    private final LearnService learnService;
    private final AuthService authService;

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
        Optional<Paragraph> optionalParagraph = learnService.getParagraphById(id);
        // Optional에서 실제 값을 추출
        Paragraph paragraph = optionalParagraph.orElse(null);
        model.addAttribute("paragraph", paragraph);
        return "learn/summary.html";
    }

    @PostMapping("/summaries")
    public String handleFormSubmit(
            @RequestParam("inputValue") String inputValue,
            @RequestParam("score") int score,
            @RequestParam("paragraphId") int paragraphId,
            @RequestParam("session") String session) {
        try {
            Summaries summaries = new Summaries();

            // uid를 찾습니다
            String uid = authService.findUidBySessionId(session);
            if (uid == null) {
                return "/error";
            } else {
                summaries.setUid(uid);
            }

            // 값을 지정합니다
            summaries.setParagraphId(paragraphId);
            summaries.setScore(score);
            summaries.setUserWrittenText(inputValue);

            // DB에 저장합니다
            Summaries temp = learnService.saveUserWritten(summaries);

            return "fo/main.html";
        } catch (Exception e) {
            e.printStackTrace();
            return "/error";
        }
    }

}
