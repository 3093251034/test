package com.czd.domain.vo;

//前端响应的字段

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    private Long id;

    //标题
    private String title;

    //访问量
    private Long viewCount;
}
