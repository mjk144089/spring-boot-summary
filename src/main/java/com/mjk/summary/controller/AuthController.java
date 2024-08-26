package com.mjk.summary.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.firebase.auth.FirebaseAuthException;
import com.mjk.summary.model.Summaries;
import com.mjk.summary.model.Users;
import com.mjk.summary.service.AuthService;
import com.mjk.summary.service.LearnService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final LearnService learnService;

    @GetMapping("/sign-in")
    public String getSignInPage() {
        return "auth/sign_in.html";
    }

    @GetMapping("/sign-up")
    public String getSignUpPage() {
        return "auth/sign_up.html";
    }

    @GetMapping("/social-sign-up")
    public String getSocialSignUpPage() {
        return "auth/social_sign_up.html";
    }

    @GetMapping("/my-page")
    public String getMyPage(
            @RequestParam(name = "t", required = true) String t,
            Model model) {
        try {
            String uid = authService.findUidBySessionId(t);
            if (uid == null) {
                throw new Exception();
            }

            List<Summaries> data = learnService.getSummariesByUid(uid);
            model.addAttribute("data", data);
            return "auth/my_page.html";
        } catch (Exception e) {
            return "error.html";
        }
    }

    @PostMapping("/sign-up")
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

    @PostMapping("/social-sign-up")
    public String handleSignUpBySocialService(Users user, @RequestParam(name = "t") String idToken) {
        try {
            if (idToken == null) {
                throw new Exception();
            }

            String uid = authService.verifyFirebaseAccessToken(idToken);
            user.setUid(uid);
            user.setSocialLogin(true);

            Users createUserRecord = authService.saveUser(user);
            if (createUserRecord == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error.html";
        }

        return "auth/sign_in.html";
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, Object>> handleSignInByEmailAndPassword(
            HttpServletRequest request,
            HttpServletResponse response)
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
                String sessionId = authService.generateSessionId(uid);

                // 사용자 정보 가져오기
                Users user = authService.findUserById(uid);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("sessionId", sessionId);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("name", user.getName());
                userMap.put("birth", user.getBirth());
                userMap.put("email", user.getEmail());
                userMap.put("gender", user.getGender());
                userMap.put("profileImg", user.getProfileImg());
                userMap.put("socialLogin", user.isSocialLogin());
                responseBody.put("user", userMap);

                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(400))
                        .body(Collections.singletonMap("error", "토큰 없음"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(Collections.singletonMap("error", "토큰이 유효하지 않음"));
        }
    }

    @PostMapping("/social-sign-in")
    public ResponseEntity<Map<String, Object>> handleSignInBySocialService(
            HttpServletRequest request,
            HttpServletResponse response)
            throws FirebaseAuthException {
        try {
            String idToken = request.getHeader("Authorization");
            if (idToken != null && idToken.startsWith("Bearer ")) {
                idToken = idToken.substring(7);

                // firebase 토큰 검증
                String uid = authService.verifyFirebaseAccessToken(idToken);

                // 사용자 정보 가져오기
                Users user = authService.findUserById(uid);
                if (user == null) {
                    return ResponseEntity.status(HttpStatusCode.valueOf(404))
                            .body(Collections.singletonMap("error", "사용자 정보 없음"));
                }

                Map<String, Object> responseBody = new HashMap<>();

                // 세션 값 생성 및 저장
                String sessionId = authService.generateSessionId(uid);
                responseBody.put("sessionId", sessionId);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("name", user.getName());
                userMap.put("birth", user.getBirth());
                userMap.put("email", user.getEmail());
                userMap.put("gender", user.getGender());
                userMap.put("profileImg", user.getProfileImg());
                userMap.put("socialLogin", user.isSocialLogin());
                responseBody.put("user", userMap);

                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(400))
                        .body(Collections.singletonMap("error", "토큰 없음"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(Collections.singletonMap("error", "토큰이 유효하지 않음"));
        }
    }

    @GetMapping("/delete-account")
    public String handleDeleteAccount(@RequestParam(name = "t", required = true) String t) {
        try {
            String uid = authService.findUidBySessionId(t);
            if (uid == null) {
                throw new Exception();
            }
            authService.DeleteAccountByUid(uid);

            return "fo/main.html";
        } catch (Exception e) {
            return "error.html";
        }
    }

}
