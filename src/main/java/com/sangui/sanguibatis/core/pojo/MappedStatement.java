package com.sangui.sanguibatis.core.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-17
 * @Description: 一个 MappedStatement 对象对应一个 SQL 标签
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedStatement {
    /**
     * sql 语句
     */
    private String sql;

    /**
     * 要封装的结果集类型。只有查询的时候 resultType 是 有值，其他情况是null。
     */
    private String resultType;
}
