package com.zhf.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author 10276
 * @Date 2023/1/29 13:55
 */
@AllArgsConstructor
@Getter
public enum LikeStateEnum {
    //点赞和取消点赞，记录状态
    LIKE("点赞",1),
    DISLIKE("取消点赞",2);
    private final String key;
    private final Integer value;
}
