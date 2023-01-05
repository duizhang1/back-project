package com.zhf.webfont.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 10276
 * @Date 2023/1/4 0:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

}
