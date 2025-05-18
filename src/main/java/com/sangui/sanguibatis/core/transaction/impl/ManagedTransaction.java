package com.sangui.sanguibatis.core.transaction.impl;


import com.sangui.sanguibatis.core.transaction.Transaction;

import java.sql.Connection;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-17
 * @Description: Managed 事务管理器，实现了 Transaction 接口
 * @Version: 1.0
 */
public class ManagedTransaction implements Transaction {
    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }

    @Override
    public void openConnection() {

    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
