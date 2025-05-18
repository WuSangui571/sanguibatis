package com.sangui.sanguibatis.core.sqlsession;


import com.sangui.sanguibatis.core.pojo.MappedStatement;
import com.sangui.sanguibatis.core.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-16
 * @Description: 一个数据库一般对应一个 SqlSessionFactory 对象。通过 SqlSessionFactory 对象可以获取SqlSession对象（开启会话），
 * 一个 SqlSessionFactory 对象，可以开启多个 SqlSession 对象
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlSessionFactory {
    /**
     * 事务管理器属性
     */
    private Transaction transaction;

    /**
     * 存放 sql 的 Map 集合
     * key 是 sqlId
     * Value 是 对应的 sql 标签信息属性
     */
    private Map<String, MappedStatement> mappedStatementMap;

    /**
     * 返回 SqlSession 对象
     * @param sqlSessionFactory sqlSessionFactory 对象
     * @return SqlSession 对象
     */
    public SqlSession openSession(SqlSessionFactory sqlSessionFactory) {
        transaction.openConnection();
        return new SqlSession(this);
    }
}
