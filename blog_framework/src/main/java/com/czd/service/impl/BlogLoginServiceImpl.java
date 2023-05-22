package com.czd.service.impl;

import com.czd.domain.ResponseResult;
import com.czd.domain.entity.LoginUser;
import com.czd.domain.entity.User;
import com.czd.domain.vo.BlogUserLoginVo;
import com.czd.domain.vo.UserInfoVo;
import com.czd.service.BlogLoginService;
import com.czd.utils.BeanCopyUtils;
import com.czd.utils.JwtUtil;
import com.czd.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户或密码错误");
        }

        // 获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin"+userId,loginUser);
        //把token和userInfo封装，返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {

        //获取token解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取userId
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //删除redis中的用户信息
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("bloglogin"+userId);
        return ResponseResult.okResult();
    }
}
