package com.czd.service;

import com.czd.domain.ResponseResult;
import com.czd.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
