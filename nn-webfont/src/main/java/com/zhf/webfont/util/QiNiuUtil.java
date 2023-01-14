package com.zhf.webfont.util;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author 10276
 * @Date 2023/1/13 20:55
 */
@Component
public class QiNiuUtil {

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.notnullBucket}")
    private String bucket;
    @Value("${qiniu.domainName}")
    private String domainName;
    @Value("${qiniu.folderName}")
    private String folderName;

    /**
     * 获得七牛云上传的token
     * @return
     */
    public String getToken(){
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.uploadToken(bucket);
    }

    /**
     * 获得文件上传后的前缀
     * @return
     */
    public String getOssPrefix(){
        return "http://" + domainName + "/";
    }

}
