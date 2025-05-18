package com.sangui.sanguibatis.core.transaction.impl;


import com.sangui.sanguibatis.core.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-17
 * @Description: Jdbc 事务管理器，实现了 Transaction 接口
 * @Version: 1.0
 */
public class JdbcTransaction implements Transaction {
    /**
     * 数据源属性
     */
    private DataSource dataSource;

    /**
     * 自动提交标志
     * true 表示采用自动提交
     * false 表示不采用自动提交
     */
    private boolean autoCommit;

    /**
     * 连接对象
     */
    private Connection connection;


    @Override
    public void openConnection(){
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                //System.out.println(autoCommit);
                connection.setAutoCommit(autoCommit);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }


    /**
     * 创建事务管理器对象
     * @param dataSource 数据源
     * @param autoCommit 自动提交标志
     */
    public JdbcTransaction(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @Override
    public void commit(){
        try {
            connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
