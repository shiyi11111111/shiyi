#ssm整合：
- 一、dao + service = model（Mybatis）

- 1、先搭建数据库表环境，以及创建对应的实体类，给项目导入pom依赖

- 2、关于数据库各种数据的配置文件database.properties
里面包含driver、url、username、password

- 3、创建mybatis的配置核心文件mybatis-config.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
    <typeAliases>
        <package name="com.shiyi.pojo"/>
    </typeAliases>
    <mappers>
        <mapper class="com.shiyi.dao.BookMapper"/>
    </mappers>
</configuration>
```
负责日志功能、别名、以及注册mapper

- 4、创建dao层，编写×××Mapper接口，以及其对应的xml去编写sql语句，该xml约束条件同第三点的类似，只需把configuration改成mapper，并且增加命名空间namespace，
接口的方法参数可使用@Param("bookId")配合xml的sql语句#{bookId}取值

- 5、创建service层，编写×××Service，以及对应的实现类×××ServiceImpl，去调用dao层

- 二、spring层（整合service层）
- 1、创建spring核心配置文件applicationContext.xml：把三个spring配置文件导入
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <import resource="classpath:spring-dao.xml"/>
    <import resource="classpath:spring-mvc.xml"/>
    <import resource="classpath:spring-service.xml"/>
</beans>
```

- 2、创建spring-dao.xml，里面配置：
- ①关联数据库配置文件
```xml
<context:property-placeholder location="classpath:database.properties"/>
```
- ②连接池DataSource(c3p0,dbcp,druid等等)
```xml
 <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <!-- ${ ×× } 里面写的是配置文件的名字，比如本项目的配置文件名字都以"jdbc."开头！-->
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>

        <!--    c3p0连接池的私有属性    -->
        <property name="minPoolSize" value="10"/>
        <property name="maxPoolSize" value="30"/>
        <!--   关闭连接后不自动commit     -->
        <property name="autoCommitOnClose" value="false"/>
        <!--   获取连接超时时间 10s    -->
        <property name="checkoutTimeout" value="10000"/>
        <!--   当获取连接失败的重试次数     -->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>

```
- ③sqlSessionFactory
```xml
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!-- 这里绑定mybatis核心配置文件 -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
```
- ④实现Dao接口可以注入到spring容器中：
- （1）手动模式：写一个dao接口的实现类，并让该实现类注入到Spring中
- 1）使用sqlSessionTemplate（即我们Mybatis工具类封装的sqlSession）：
```xml
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg index="0" ref="sqlSessionFactory"/>
    </bean>
```
然后在实现类上定义set方法
```java
    private SqlSessionTemplate sqlSessionTemplate;
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
```
随后在applicationContext.xml实现注入
```xml
    <bean id="UserMapper" class="com.shiyi.Service.UserMapperImpl">
        <property name="sqlSessionTemplate" ref="sqlSession"/>
    </bean>
```

- 2)实现类继承SqlSessionDaoSupport类，然后在applicationContext.xml实现注入
```xml
    <bean id="UserMapper2" class="com.shiyi.Service.UserMapperImpl2">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
    </bean>
```
然后在实现类可以直接使用getSqlSession方法即：return getSqlSession().getMapper(UserMapper.class).selectUser();

- （2）自动模式：不用定义实现类即可注入
```xml
<!--  配置dao接口扫描包，动态的实现了Dao接口可以注入到spring容器中  -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--     注入sqlSessionFactory，这里注意是value不是ref！    -->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--      要扫描的dao包      -->
        <property name="basePackage" value="com.shiyi.dao"/>
    </bean>
```

- 3、创建spring-service.xml
```xml
    <context:component-scan base-package="com.shiyi.service"/>

    <!--  将所有业务类注入到Spring中  -->
    <bean id="BookServiceImpl" class="com.shiyi.service.BookServiceImpl">
        <property name="bookMapper" ref="bookMapper"/>
    </bean>

    <!-- 声明式事务配置   -->
    <bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager" id="transactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- aop事务支持-->     
```

- 三、springMVC层（controller）：
- 1、给项目添加web框架支持，编写web.xml
- （1）dispatchServlet：
```xml
    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--  绑定上下文 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <!--配置启动等级-->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

- (2)乱码过滤：这里使用spring自带的过滤器
```xml
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```
注意这里：/*代表会过滤jsp页面，即所有页面，而/能过滤所有页面，但不包括jsp页面

- （3）设置Session过期时间：
```xml
    <session-config>
        <!-- 15分钟   -->
        <session-timeout>15</session-timeout>
    </session-config>
```

- 2、创建spring-mvc.xml：
- （1）注解驱动： 在spring中一般采用@RequesetMapping注解来完成映射关系，要想使@RequestMapping注解生效，必须向上下文中注册DefaultAnnotationHandlerMapping
和一个AnnotationMethodHandlerAdapter实例，这两个实例分别在类级别和方法级别处理。而annotation-driven配置帮助我们自动完成上述两个实例的注入
即控制器处理器和控制器适配器
```xml
<mvc:annotation-driven/>
```

- (2)静态资源过滤:
```xml
<mvc:default-servlet-handler/>
```

- （3)扫描controller包下的类
```xml
<context:component-scan base-package="com.shiyi.controller"/>
```

- (4)视图解析器：配置前缀和后缀，特别注意前缀最后的/
```xml
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
```


