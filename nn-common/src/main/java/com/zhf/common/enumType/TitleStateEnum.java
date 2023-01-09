package com.zhf.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author 10276
 * @Date 2023/1/7 1:05
 */
@AllArgsConstructor
@Getter
public enum TitleStateEnum {
    /**
     * 文章审核状态
     */
    REVIEWING("审核中",1),
    PUBLISHED("已发布",2),
    REJECT("未通过",3);

    private final String key;
    private final Integer value;



}
