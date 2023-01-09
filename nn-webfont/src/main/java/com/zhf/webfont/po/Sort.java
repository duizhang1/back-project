package com.zhf.webfont.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 10276
 * @Date 2023/1/7 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wf_sort")
public class Sort {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String sortName;
}
