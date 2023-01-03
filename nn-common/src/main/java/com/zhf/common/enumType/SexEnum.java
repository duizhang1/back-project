package com.zhf.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author 10276
 * @Date 2023/1/3 17:50
 */
@AllArgsConstructor
@Getter
public enum SexEnum {
    /**
     * 男
     */
    MALE("男",1),
    /**
     * 女
     */
    FEMALE("女",2);

    private String sex;
    private Integer value;
}
