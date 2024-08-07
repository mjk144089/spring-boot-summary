package com.mjk.summary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/accounts")
public class AuthController {
    @GetMapping("/sign_in")
    public String getSignInPage() {
        return "auth/sign_in.html";
    }

    @GetMapping("/sign_up")
    public String getSignUpPage() {
        return "auth/sign_up.html";
    }

}
