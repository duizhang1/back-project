package com.zhf.webfont.service.impl;

import com.zhf.webfont.service.OssService;
import com.zhf.webfont.util.QiNiuUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/13 21:33
 */
@Service
public class OssServiceImpl implements OssService {

    @Resource
    private QiNiuUtil qiNiuUtil;

    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("token",qiNiuUtil.getToken());
        map.put("prefix",qiNiuUtil.getOssPrefix());
        return map;
    }
}
