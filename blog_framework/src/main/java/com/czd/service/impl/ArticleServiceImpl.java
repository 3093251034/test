package com.czd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.Article;
import com.czd.domain.entity.Category;
import com.czd.domain.vo.ArticleDetailVo;
import com.czd.domain.vo.ArticleListVo;
import com.czd.domain.vo.HotArticleVo;
import com.czd.domain.vo.PageVo;
import com.czd.mapper.ArticleMapper;
import com.czd.service.ArticleService;
import com.czd.service.CategoryService;
import com.czd.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.czd.constants.PageConstants.HOT_ARTICLE_PAGE_SIZE;
import static com.czd.constants.PageConstants.HOT_ARTICLE_PAGE_START;
import static com.czd.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult hotArticleList() {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 1.必须是正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        //2.按浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //3.最多查10条消息
        Page<Article> page = new Page(HOT_ARTICLE_PAGE_START,HOT_ARTICLE_PAGE_SIZE);
        // 分页 需要配置拦截器
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        /**
         * 查询条件
         * 有categoryId，要与传入的相同
         * 文章状态正常
         * 置顶文章，对isTop降序
         */
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,lambdaQueryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords();

        articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        //获取categoryId，查询分类名称并设置在Article中
                        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                        return article;
                    }
                }).collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);



        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 根据id查询文章
        Article article = getById(id);
        //转换为VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据Vo查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());

        if (category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应对象
        return ResponseResult.okResult(articleDetailVo);
    }
}
