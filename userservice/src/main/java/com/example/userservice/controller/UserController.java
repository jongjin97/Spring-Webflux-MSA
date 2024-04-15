package com.example.userservice.controller;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/userserivce")
public class UserController {
    private final UserService userService;
    @GetMapping("/hello")
    Mono<String> hello(){
        return Mono.just("Hello World");
    }

    @GetMapping("/")
    Mono<String> test(){
        return Mono.just("test");
    }

    @PostMapping("/signup")
    Mono<ResponseUser> signup(@RequestBody RequestUser requestUser){
        return userService.saveUser2(requestUser);
    }
}
