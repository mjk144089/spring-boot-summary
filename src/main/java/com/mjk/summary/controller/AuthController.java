package com.mjk.summary.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.firebase.auth.FirebaseAuthException;
import com.mjk.summary.model.Users;
import com.mjk.summary.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
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
        try {
            // firebase에 새로운 사용자를 생성합니다
            String userId = authService.createUserByEmailAndPassword(user.getEmail(), password);
            if (userId != null) {
                user.setUid(userId);
            } else {
                throw new Exception();
            }

            // uid가 추가된 user데이터를 데이터베이스에 저장합니다
            Users createUserRecord = authService.saveUser(user);
            if (createUserRecord == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 에러페이지로 이동시킵니다
        }

        return "fo/main.html";
    }

    // 이메일 중복 여부 검사를 위한 Ajax요청을 처리합니다
    @GetMapping("/duplicate")
    public ResponseEntity<String> checkDuplicateEmail(@RequestParam(name = "email") String email) {
        boolean condition = authService.checkEmailDuplicate(email);
        return ResponseEntity.ok(condition ? "true" : "false");
    }

    @PostMapping("/sign_in")
    public ResponseEntity<String> handleSignInByEmailAndPassword(HttpServletRequest request)
            throws FirebaseAuthException {
        /**
         * 1. 클라이언트가 보낸 토큰 검증
         * 2. 토큰이 유효하다면 세션 값을 DB에 저장
         * 3. uid를 통해 사용자 정보 가져오기
         * 4. 쿠키로 만들어서 클라이언트에게 전송
         */
        try {
            String idToken = request.getHeader("Authorization");
            if (idToken != null && idToken.startsWith("Bearer ")) {
                idToken = idToken.substring(7);

                // firebase 토큰 검증
                String uid = authService.verifyFirebaseAccessToken(idToken);

                // 세션 값 생성 및 저장

                // 사용자 정보 가져오기

            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("토큰이 없어야");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("토큰이 이상해야");
        }
        return ResponseEntity.ok("로그인 완료");
    }

}
