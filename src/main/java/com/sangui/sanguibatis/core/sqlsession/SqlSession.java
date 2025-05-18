package com.sangui.sanguibatis.core.sqlsession;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-16
 * @Description: 专门负责执行 sql 语句的 sql 对象
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlSession {
    private SqlSessionFactory sqlSessionFactory;

    /**
     * insert()
     * @param sqlId sql 语句的 id
     * @param pojo 实体类
     * @return 数据库改变条数
     */
    public int insert(String sqlId,Object pojo){
        int count = 0;
        try {
            Connection connection = sqlSessionFactory.getTransaction().getConnection();
            String sanguibatisSql = sqlSessionFactory.getMappedStatementMap().get(sqlId).getSql();
            String sql = sanguibatisSql.replaceAll("#\\{[0-9A-Za-z_$]*}", "?");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int curIndex = 0;
            int index = 1;
            while (true){
                int jinIndex = sanguibatisSql.indexOf("#", curIndex);
                if (jinIndex < 0){
                    break;
                }
                int youIndex = sanguibatisSql.indexOf("}",curIndex);
                String propertyName = sanguibatisSql.substring(jinIndex + 2, youIndex).trim();
                String getMethodName = "get" + propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
                Method getMethod = pojo.getClass().getDeclaredMethod(getMethodName);
                Object propertyValue = getMethod.invoke(pojo);
                // 设置setObject方法，此时数据库能放入任意类型的数据，未测试
                // preparedStatement.setObject(index++, propertyValue);
                // 设置setString方法，此时数据库只能放入varchar类型的数据
                preparedStatement.setString(index++, (String) propertyValue);
                curIndex = youIndex + 1;
            }
            count = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 给问号传值
        return count;
    }

    /**
     * selectOne()
     * @param sqlId sql 语句的 id
     * @param id 查询的id
     * @return 查询的一个结果
     */
    public Object selectOne(String sqlId, String id) {
        Object object = null;
        try {
            Connection connection = sqlSessionFactory.getTransaction().getConnection();
            String sanguibatisSql = sqlSessionFactory.getMappedStatementMap().get(sqlId).getSql();
            String sql = sanguibatisSql.replaceAll("#\\{[0-9A-Za-z_$]*}", "?");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String resultType = sqlSessionFactory.getMappedStatementMap().get(sqlId).getResultType();

            if (resultSet.next()){
                Class<?> resultTypeClass = Class.forName(resultType);
                object = resultTypeClass.newInstance();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++){
                    String columnName = resultSetMetaData.getColumnName(i);
                    String setMethodName = "set" + columnName.toUpperCase().charAt(0) + columnName.substring(1);
                    Method setMethod = resultTypeClass.getDeclaredMethod(setMethodName,String.class);
                    setMethod.invoke(object,resultSet.getString(columnName));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    /**
     * 提交事务
     */
    public void commit(){
        sqlSessionFactory.getTransaction().commit();
    }

    /**
     * 回滚事务
     */
    public void rollback(){
        sqlSessionFactory.getTransaction().rollback();
    }

    /**
     * 关闭事务
     */
    public void close(){
        sqlSessionFactory.getTransaction().close();
    }
}
