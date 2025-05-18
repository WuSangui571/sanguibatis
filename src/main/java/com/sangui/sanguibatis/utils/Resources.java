package com.sangui.sanguibatis.utils;


import java.io.InputStream;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-16
 * @Description: sanguibatis提供的一个资源工具类，作用是专门完成类路径中的资源的加载
 * @Version: 1.0
 */
public class Resources {
    /**
     * 构造方法私有化，避免new对象
     */
    private Resources() {}

    /**
     * 从类路径当中加载资源
     * @param resource 放在类路径中的资源文件
     * @return 指向资源文件的一个输入流
     */
    public static InputStream getResourceAsStream(String resource){
        return Resources.class.getClassLoader().getResourceAsStream(resource);
    }

}
