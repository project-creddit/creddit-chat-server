package com.creddit.credditchatserver.controller;

import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody User user) throws Exception{
        userService.createUser(user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
