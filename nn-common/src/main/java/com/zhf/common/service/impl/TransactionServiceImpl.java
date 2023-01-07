package com.zhf.common.service.impl;


import com.zhf.common.service.TransactionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * 事务service，用来缩小事务开启的粒度
 * @Author 10276
 * @Date 2023/1/7 19:51
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T doTransaction(Supplier<T> supplier){
        return supplier.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doTransaction(Runnable task) {
        task.run();
    }
}
