package com.zhf.webfont.service;

import com.zhf.webfont.bo.StoreListWithIsStoreParam;
import com.zhf.webfont.po.StoreList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 10276
* @description 针对表【wf_store_list】的数据库操作Service
* @createDate 2023-01-31 00:02:20
*/
public interface StoreListService extends IService<StoreList> {

    /**
     * 获得收藏夹列表并赋上文章是否收藏
     * @param articleId
     * @return
     */
    List<StoreListWithIsStoreParam> getStoreListWithIsStore(String articleId);

    /**
     * 创建收藏夹
     * @param storeList
     */
    void createStoreList(StoreList storeList);

    /**
     * 更新收藏记录
     * @param param
     */
    void updateArticleStore(Map<String,Object> param);
}
