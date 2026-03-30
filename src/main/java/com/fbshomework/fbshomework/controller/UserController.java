package com.fbshomework.fbshomework.controller;

import com.fbshomework.fbshomework.entity.User;
import com.fbshomework.fbshomework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return "登录成功，欢迎 " + user.getUsername();
        }
        return "用户名或密码错误";
    }
}