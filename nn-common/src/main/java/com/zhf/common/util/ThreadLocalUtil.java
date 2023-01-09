package com.zhf.common.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/10 0:39
 */
@Component
public class ThreadLocalUtil {

    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();
    public static final String CURRENT_USER = "currentUser";

    public Object get(String key){
        if (threadLocal.get() == null){
            return null;
        }
        return threadLocal.get().get(key);
    }

    public void set(String key,Object value){
        if (threadLocal.get() == null){
            threadLocal.set(new HashMap<>(2));
        }
        threadLocal.get().put(key,value);
    }

    public void remove(){
        threadLocal.remove();
    }
}
