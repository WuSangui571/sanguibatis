# sanguibatis

`sanguibatis` 是一个轻量级的仿 MyBatis 框架，仅实现了核心功能，属于“阉割版”示例。

---

## 特性

- **简易配置**：仿照 MyBatis 的 XML 配置格式，使用习惯几乎一致。
- **事务管理**：仅支持 JDBC 事务 （`MANAGED` 尚未实现）。
- **数据源**：仅支持非连接池（`UNPOOLED`），`POOLED` 和 `JNDI` 尚未实现。
- **基本配置项**：只实现了四个必需的属性：`driver`、`url`、`username`、`password`。
- **API 支持**：目前只提供 `Session.selectOne()` 和 `Session.insert()` 两个操作。

---

## 快速开始

1. 在类路径下编写以下两个 XML 配置文件：
   - **核心配置**：`sanguibatis-config.xml`
   - **SQL 映射**：任意命名的 `xxxMapper.xml`

2. 核心配置示例（`sanguibatis-config.xml`）：

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <configuration>

      <environments default="development">
        <!-- 开发环境 -->
        <environment id="development">
          <transactionManager type="JDBC"/>
          <dataSource type="UNPOOLED">
            <property name="driver"   value="com.mysql.cj.jdbc.Driver"/>
            <property name="url"      value="jdbc:mysql://localhost:3306/your_db"/>
            <property name="username" value="root"/>
            <property name="password" value="password"/>
          </dataSource>
        </environment>

        <!-- 测试环境 -->
        <environment id="test">
          <transactionManager type="JDBC"/>
          <dataSource type="POOLED">
            <property name="driver"   value="com.mysql.cj.jdbc.Driver"/>
            <property name="url"      value="jdbc:mysql://localhost:3306/your_db"/>
            <property name="username" value="root"/>
            <property name="password" value="password"/>
          </dataSource>
        </environment>
      </environments>

      <mappers>
        <mapper resource="yourMapper.xml"/>
      </mappers>

    </configuration>
    ```

3. Mapper 示例（`TestMapper.xml`）：

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <mapper namespace="Test">

      <insert id="insertOne">
        INSERT INTO t_test (id, name, age)
        VALUES (#{id}, #{name}, #{age})
      </insert>

      <select id="selectOne" resultType="com.sangui.pojo.TestEntity">
        SELECT * FROM t_test WHERE id = #{id}
      </select>

    </mapper>
    ```

4. Java 调用示例：

    ```java
    // 构建 SqlSessionFactory
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    InputStream config = Resources.getResourceAsStream("sanguibatis-config.xml");
    SqlSessionFactory factory = builder.build(config);
    
    // Select 示例
    try (SqlSession session = factory.openSession()) {
        TestEntity entity = session.selectOne("Test.selectOne", "1004");
        System.out.println(entity);
    }
    
    // Insert 示例
    try (SqlSession session = factory.openSession()) {
        TestEntity newEntity = new TestEntity("1007", "yes", "21");
        int rows = session.insert("Test.insertOne", newEntity);
        System.out.printf("影响行数：%d%n", rows);
        session.commit();
    }
    ```

---

## 使用限制

- **字段类型**：目前仅支持所有表字段均为 `VARCHAR` 类型。
- **方法支持**：仅实现 `selectOne()` 和 `insert()`，其他 CRUD 尚未提供。
- **事务 & 连接池**：只实现了 JDBC 事务与非连接池数据源，其它模式留待后续扩展。

---

## 后续计划

- 支持更多 SQL 操作：`update`、`delete`、`selectList` 等。
- 增加连接池 (`POOLED`) 和 JNDI 数据源。
- 完善事务管理，增加 `MANAGED` 模式支持。
- 多种结果映射和类型转换器扩展。

---

欢迎试用与反馈！  