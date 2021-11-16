package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
public class IndexController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository uSerRepository;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(@AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login =" + userDetails.getUser());
        return "세션정보확인";
    }

    @GetMapping({"","/"})
    public @ResponseBody String index(){
        return "index";
    }
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }
    @GetMapping("/loginForm") // config생성후 디폴트화면생성 x
    public String loginForm (){
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        uSerRepository.save(user);
        return "redirect:/loginForm";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){

        return "유저정보";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }



}
