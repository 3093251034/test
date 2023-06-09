package com.czd.controller;

import com.czd.domain.ResponseResult;
import com.czd.domain.entity.User;
import com.czd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){

        return userService.updateUserInfo(user);
    }
}
