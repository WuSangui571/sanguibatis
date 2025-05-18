package com.sanguibatis.test;


import com.sangui.pojo.TestEntity;
import com.sangui.sanguibatis.core.sqlsession.SqlSession;
import com.sangui.sanguibatis.core.sqlsession.SqlSessionFactory;
import com.sangui.sanguibatis.core.sqlsession.SqlSessionFactoryBuilder;
import com.sangui.sanguibatis.utils.Resources;
import org.junit.Test;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-17
 * @Description: 测试程序
 * @Version: 1.0
 */
public class SanguibatisTest {
    @Test
    public void testSelect() {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("sanguibatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(sqlSessionFactory);
        TestEntity testEntity = (TestEntity)sqlSession.selectOne("Test.selectOne", "1004");
        System.out.println(testEntity);
    }
    @Test
    public void testInsert() {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("sanguibatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(sqlSessionFactory);
        TestEntity testEntity = new TestEntity("1007","yes","21");
        int count = sqlSession.insert("Test.insertOne", testEntity);
        System.out.println("数据库改变条数：" + count);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testSqlSessionFactory() {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("sanguibatis-config.xml"));
        System.out.println(sqlSessionFactory);
    }
}
