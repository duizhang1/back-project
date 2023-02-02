package com.zhf.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author 10276
 * @Date 2023/2/3 2:20
 */
@AllArgsConstructor
@Getter
public enum SubscribeStateEnum {
    /**
     * 关注状态
     */
    SUBSCRIBE("关注",1),
    UNSUBSCRIBE("取关",2);

    private final String key;
    private final Integer value;


}
