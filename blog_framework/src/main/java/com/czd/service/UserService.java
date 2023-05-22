package com.czd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-27 12:31:35
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);
}
