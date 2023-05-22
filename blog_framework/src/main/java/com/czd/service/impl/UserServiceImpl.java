package com.czd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.User;
import com.czd.domain.vo.UserInfoVo;
import com.czd.mapper.UserMapper;
import com.czd.service.UserService;
import com.czd.utils.BeanCopyUtils;
import com.czd.utils.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-27 12:31:36
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {

        //解析token，获取用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装为userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }
}
