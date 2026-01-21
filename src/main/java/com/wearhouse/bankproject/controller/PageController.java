package com.wearhouse.bankproject.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/signup")
    public String authTestPage() {
        return "signup";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
