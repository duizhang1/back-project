package com.zhf.webfont.bo;

import com.zhf.webfont.po.ImRecord;
import com.zhf.webfont.po.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 10276
 * @Date 2023/2/17 20:31
 */
@Data
public class ImRecordItemParam{

    private ImRecord imRecord;

    private String username;

    private String avatar;

    private String toUserId;

    /**
     * 是否自己发送的消息
     */
    private Boolean own;

    /**
     * 未读消息的数量
     */
    private Integer count;

}
