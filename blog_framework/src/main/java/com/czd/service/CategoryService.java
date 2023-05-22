package com.czd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-15 20:02:50
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
