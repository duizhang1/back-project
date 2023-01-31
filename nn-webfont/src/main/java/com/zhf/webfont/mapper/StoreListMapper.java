package com.zhf.webfont.mapper;

import com.zhf.webfont.bo.StoreListWithIsStoreParam;
import com.zhf.webfont.po.StoreList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_store_list】的数据库操作Mapper
* @createDate 2023-01-31 00:02:19
* @Entity com.zhf.webfont.po.StoreList
*/
public interface StoreListMapper extends BaseMapper<StoreList> {

    List<StoreListWithIsStoreParam> getStoreListWithIsStore(@Param("userId") String uuid,@Param("articleId") String articleId);
}




