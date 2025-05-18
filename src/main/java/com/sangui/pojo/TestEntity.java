package com.sangui.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-18
 * @Description: t_test 数据表 pojo
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    private String id;
    private String name;
    private String age;
}
