package com.mjk.summary.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mjk.summary.model.Users;
import com.mjk.summary.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/sign_in")
    public String getSignInPage() {
        return "auth/sign_in.html";
    }

    @GetMapping("/sign_up")
    public String getSignUpPage() {
        return "auth/sign_up.html";
    }

    @PostMapping("/sign_up")
    public String handleSignUpByEmailAndPassword(Users user, @RequestParam(name = "password") String password) {
        /**
         * 1. firebase에 회원 만들고 - 완료
         * 2. uid반환 받아서 user객체에 넣고 - 완료
         * 3. 프로필 사진 만들고
         * 4. DB에 저장
         */
        try {
            String userId = authService.createUserByEmailAndPassword(user.getEmail(), password);
            if (userId != null) {
                user.setUid(userId);
            } else {
                throw new Exception();
            }
            Users createUserRecord = authService.saveUser(user); // DB에 저장
            if (createUserRecord == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "fo/main.html";
    }

    @GetMapping("/duplicate")
    public ResponseEntity<String> checkDuplicateEmail(@RequestParam(name = "email") String email) { // Ajax
        /**
         * 1. 이메일이 중복되는지 확인하고
         * 2. condition 값 조정
         */
        boolean condition = authService.checkEmailDuplicate(email);
        return ResponseEntity.ok(condition ? "true" : "false");
    }

}
