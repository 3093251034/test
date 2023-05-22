package com.czd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czd.domain.entity.LoginUser;
import com.czd.domain.entity.User;
import com.czd.mapper.UserMapper;
import com.czd.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User user = userMapper.selectOne(queryWrapper);

        // 判断是否查到用户，如果没查到，抛出空异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        // 返回用户信息

        // ToDo 查询权限信息封装

        return  new LoginUser(user);
    }
}
