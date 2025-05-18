package com.sangui.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-12
 * @Description: 封装Car相关信息的普通的类
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private Long id;
    private String carNum;
    private String brand;
    private double guidePrice;
    private String produceTime;
    private String carType;
}
