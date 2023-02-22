package com.zhf.webfont.mapper;

import com.zhf.webfont.po.ImRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 10276
* @description 针对表【wf_im_record】的数据库操作Mapper
* @createDate 2023-02-17 20:27:05
* @Entity com.zhf.webfont.po.ImRecord
*/
public interface ImRecordMapper extends BaseMapper<ImRecord> {

    ImRecord selectNewMessage(@Param("userId") String uuid,@Param("toUserId") String userId);
}




