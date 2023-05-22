package com.czd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 19:24:16
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
