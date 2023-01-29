package com.zhf.webfont.service;

import com.zhf.webfont.po.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_label】的数据库操作Service
* @createDate 2023-01-14 22:14:32
*/
public interface LabelService extends IService<Label> {

    /**
     * 通过文章id获得label对象
     * @param articleId
     */
    List<Label> getLabelsFromArticleId(String articleId);

}
