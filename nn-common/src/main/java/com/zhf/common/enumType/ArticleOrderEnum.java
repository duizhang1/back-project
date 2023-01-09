package com.zhf.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 10276
 * @Date 2023/1/8 14:47
 */
@Getter
@AllArgsConstructor
public enum ArticleOrderEnum {
    /**
     * 排序方式
     */
    NEW("最新排序",1),
    HOT("最热排序",2);
    private final String key;
    private final Integer value;
}
