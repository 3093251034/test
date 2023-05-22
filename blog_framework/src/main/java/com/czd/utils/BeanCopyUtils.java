package com.czd.utils;

import com.czd.domain.entity.Article;
import com.czd.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils(){

    }

    //泛型

    public static <V> V copyBean(Object source, Class<V> clazz){

        V result = null;
        try {
            //创建目标对象
            result = clazz.newInstance();

            //实现属性拷贝
            BeanUtils.copyProperties(source,result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("aa");

        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
