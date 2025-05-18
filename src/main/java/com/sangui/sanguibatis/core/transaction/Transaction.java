package com.sangui.sanguibatis.core.transaction;


import java.sql.Connection;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-16
 * @Description: 事务管理器接口，所有的事务管理器都应该遵循该规范，
 * 比如JDBC事务管理器，MANAGED事务管理器都应该实现这个接口。
 * Transaction 提供管理事务的方法。
 * @Version: 1.0
 */
public interface Transaction {
    // 提供控制事务的方法。

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

    /**
     * 关闭事务
     */
    void close();

    /**
     * 开启数据库连接，内部只会开一次
     */
    void openConnection();

    /**
     * 获取Connection对象
     */
    Connection getConnection();
}
