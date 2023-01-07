package com.zhf.common.util;

import com.zhf.common.service.TransactionService;
import com.zhf.common.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * 事务工具类，用来缩小事务开启的粒度
 * @Author 10276
 * @Date 2023/1/7 19:50
 */
@Component
public class TransactionUtil {

    private static TransactionService transactionService;

    /**
     * 用来注入spring中bean，然后在服务器启动后会注入到静态属性中。
     */
    @Resource
    private TransactionService willTransactionService;

    /**
     * PostConstruct在servlet容器启动后会开启一次
     * 利用这个特性，我们可以将已经注入的非静态的willTransactionService属性注入到静态属性中。
     */
    @PostConstruct
    public void setTransactionUtilBean(){
        transactionService = this.willTransactionService;
    }

    public static <T> T transaction(Supplier<T> supplier) {
        return transactionService.doTransaction(supplier);
    }

    public static void transaction(Runnable task) {
        transactionService.doTransaction(task);
    }

}
