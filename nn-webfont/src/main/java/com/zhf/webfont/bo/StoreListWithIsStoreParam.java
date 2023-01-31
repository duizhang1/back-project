package com.zhf.webfont.bo;

import com.zhf.webfont.po.StoreList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 10276
 * @Date 2023/1/31 0:34
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class StoreListWithIsStoreParam extends StoreList {

    private Boolean isStore;

}
