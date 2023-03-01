package com.zhf.webfont.bo;

import com.zhf.webfont.po.UserSubscribe;
import lombok.Data;

/**
 * @Author 10276
 * @Date 2023/2/26 0:55
 */
@Data
public class UserSubscribeNotificationParam {

    private UserSubscribe userSubscribe;

    private String userId;

    private String username;

    private String avatar;

    private Boolean isFocus;

}
