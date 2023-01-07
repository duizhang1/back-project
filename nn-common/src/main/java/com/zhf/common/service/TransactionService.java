package com.zhf.common.service;

import java.util.function.Supplier;

/**
 * @Author 10276
 * @Date 2023/1/7 19:50
 */
public interface TransactionService {

    /**
     * 拥有返回值的开启事务
     * @param supplier
     * @param <T>
     * @return
     */
    <T> T doTransaction(Supplier<T> supplier);

    /**
     * 没有返回值的开启事务
     * @param task
     */
    void doTransaction(Runnable task);
}
