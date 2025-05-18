package com.sangui.sanguibatis.core.sqlsession;


import com.sangui.sanguibatis.core.datasource.impl.JndiDataSource;
import com.sangui.sanguibatis.core.datasource.impl.PooledDataSource;
import com.sangui.sanguibatis.core.datasource.impl.UnPooledDataSource;
import com.sangui.sanguibatis.core.pojo.MappedStatement;
import com.sangui.sanguibatis.core.transaction.Transaction;
import com.sangui.sanguibatis.core.transaction.impl.JdbcTransaction;
import com.sangui.sanguibatis.utils.Resources;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sangui.sanguibatis.core.Const.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-16
 * @Description: SqlSessionFactory 构建器对象，通过 SqlSessionFactoryBuilder 的 build 方法来解析 sanguibatis-config.xml 文件，
 * 然后创建 SqlSessionFactory 对象
 * @Version: 1.0
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactoryBuilder() {
    }

    /**
     * 解析 sanguibatis-config.xml 文件，来构建 SqlSessionFactory 对象
     * @param inputStream 指向 sanguibatis-config.xml 文件的输入流
     * @return SqlSessionFactory 对象
     */
    public SqlSessionFactory build (InputStream inputStream){
        SqlSessionFactory sqlSessionFactory = null;
        try {
            // 解析sanguibatis.xml文件
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            String xPath = "//environments";
            Element environments = (Element) document.selectSingleNode(xPath);
            String defaultEnvironmentId = environments.attributeValue("default");
            //System.out.println("defaultEnvironmentId = " + defaultEnvironmentId);
            xPath = "//environment";
            Element defaultEnvironment = (Element) document
                    .selectSingleNode(xPath + "[@id='" + defaultEnvironmentId + "']");

            // 获取数据源
            Element dataSourceElement = defaultEnvironment.element("dataSource");
            DataSource dataSource = getDataSource(dataSourceElement);

            // 获取事务管理器
            Element transactionManager =  defaultEnvironment.element("transactionManager");
            Transaction transaction = getTransaction(transactionManager,dataSource);

            // 获取 mappedStatementMap
            xPath = "//mapper";
            List<Node> nodes = document.selectNodes(xPath);
            List<String> sqlMapperXMLPathList = new ArrayList<String>();
            for (Node node : nodes) {
                Element mapper = (Element) node;
                String mapperResource = mapper.attributeValue("resource");
                sqlMapperXMLPathList.add(mapperResource);
            }
            Map<String, MappedStatement> mappedStatementMap = getMappedStatementMap(sqlMapperXMLPathList);


            sqlSessionFactory =  new SqlSessionFactory(transaction, mappedStatementMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sqlSessionFactory;
    }

    private Map<String, MappedStatement> getMappedStatementMap(List<String> list){
        Map<String, MappedStatement> mappedStatementMap = new HashMap<String, MappedStatement>();
        for (String sqlMapperXMLPath : list) {
            try {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(Resources.getResourceAsStream(sqlMapperXMLPath));
                Element mapper = (Element) document.selectSingleNode("mapper");
                String namespace = mapper.attributeValue("namespace").trim();
                List<Element> elements = mapper.elements();
                for (Element element : elements) {
                    String id = element.attributeValue("id").trim();
                    String resultType = element.attributeValue("resultType");
                    String sql = element.getText().trim();
                    MappedStatement mappedStatement = new MappedStatement(sql,resultType);
                    mappedStatementMap.put(namespace + "." + id,mappedStatement);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return mappedStatementMap;
    }

    /**
     * 获取数据源方法
     * @param dataSourceElement 输入一个数据源的 Element
     * @return 返回一个数据源
     */
    private DataSource getDataSource(Element dataSourceElement) {
        String dataSourceType = dataSourceElement.attributeValue("type").trim().toUpperCase();

        Element driverElement = (Element) dataSourceElement.selectSingleNode("property[@name='driver']");
        String driver = driverElement.attributeValue("value");

        Element urlElement = (Element) dataSourceElement.selectSingleNode("property[@name='url']");
        String url = urlElement.attributeValue("value");

        Element usernameElement = (Element) dataSourceElement.selectSingleNode("property[@name='username']");
        String username = usernameElement.attributeValue("value");

        Element passwordElement = (Element) dataSourceElement.selectSingleNode("property[@name='password']");
        String password = passwordElement.attributeValue("value");

        DataSource dataSource = null;
        if (UN_POOLED_DATASOURCE.equals(dataSourceType)){
            dataSource = new UnPooledDataSource(driver, url, username, password);
        }else if (POOLED_DATASOURCE.equals(dataSourceType)){
            dataSource = new PooledDataSource();
        }else if (JNDI_DATASOURCE.equals(dataSourceType)){
            dataSource = new JndiDataSource();
        }
        return dataSource;
    }
    private Transaction getTransaction(Element transactionManager, DataSource dataSource) {
        String transactionManagerType = transactionManager.attributeValue("type").trim().toUpperCase();
        Transaction transaction = null;
        if (JDBC_TRANSACTION.equals(transactionManagerType)){
            transaction = new JdbcTransaction(dataSource,false);
        } else if (MANAGED_TRANSACTION.equals(transactionManagerType)) {
            transaction = new JdbcTransaction(dataSource,true);
        }
        return transaction;
    }
}
