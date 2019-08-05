# ssm-simple-merge
整体思路:
* 先搭建spring环境
* 使用spring整合springMVC
* 最后使用spring整合MyBatis

-----------------------
* 创建maven-web项目
* 导入需要的maven坐标
```
<dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>compile</scope>
  </dependency>
   <!-- https://mvnrepository.com/artifact/org.springframework/spring-context -->
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.1.8.RELEASE</version>
      <scope>compile</scope>
  </dependency>

  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.13</version>
  </dependency>

  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.2</version>
  </dependency>

  <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.12.0</version>
  </dependency>

  <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.12.0</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/org.springframework/spring-tx -->
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.1.8.RELEASE</version>
  </dependency>

  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.1.8.RELEASE</version>
  </dependency>

  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.1.8.RELEASE</version>
  </dependency>

  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.1.8.RELEASE</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
  <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.2</version>
  </dependency>

  <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
  </dependency>

  <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.9.4</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>2.0.2</version>
  </dependency>
```

* 搭建spring环境   
按照所示的包结构，创建dao层，service层，controller层，pojo，在resource包中创建spring配置文件applicationContext.xml，导入最全约束
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans-3.2.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop-4.3.xsd
       http://www.springframework.org/schema/tx
       https://www.springframework.org/schema/tx/spring-tx-4.3.xsd">

<!--配置需要扫描的包-->
<context:component-scan base-package="com.cc">
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
</Beans>
```
这样spring环境就搭建完成了，可以使用如下测试代码进行测试
```
public class TestAccountServiceImpl {
   @Test
   public void test01(){
      ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
      IAccountService accountService = ac.getBean("accountService",IAccountService.class);
      accountService.findAllAccount();
   }
}
```

* 整合spring + springMVC：先导入需要的约束
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans-3.2.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--
    这里需要注意的是applicationContext.xml和spring-mvc.xml中扫描包的设置
    由于是ssm整合，将三个框架分为三层，springMVC->@Controller  spring->@Service  MyBatis->@Repository
    由于服务器启动时的加载配置文件的顺序为wen.xml->applicationContext.xml（spring配置文件）->spring-mvc.xml（springMVC的配置文件）
    1.applicationContext.xml中Controller会先进行扫描装配，但此时service还没有进行事务增强处理，得到的是没有经过事务增强的Service
    2.所以在applicationContext.xml中需要将Controller注解去除
    3.在spring-mvc.xml中则只扫描@Controller，而不扫描@Service，如果使用的是@Component，则需不扫描@Component，但在MVC架构下，建议使用@Service来定义服务层组件
    4.总结：在spring配置文件中去除对@Controller的扫描，避免将没有经过事务增强的Service放入容器
    -->
    
    <!--配置需要扫描的包-->
    <context:component-scan base-package="com.cc">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Service"/>
    </context:component-scan>

    <!--配置视图解析器-->
    <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--设置静态资源不过滤-->
    <mvc:resources mapping="/css/**" location="WEB-INF/css/"/>
    <mvc:resources mapping="/js/**" location="WEB-INF/js/"/>
    <mvc:resources mapping="/img/**" location="WEB-INF/img/"/>

    <!--开启mvc支持-->
    <mvc:annotation-driven/>

</beans>
```

* 先配置springMVC自己需要的环境
```
<!--配置前端控制器-->
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>

<!--解决中文乱码-->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

* 服务器启动时，加载web.xml，此时可以加载前端控制器，由于前端控制器的参数配置了spring-mvc.xml的配置文件，加载前端控制器时，同时可以加载spring-mvc.xml，同时，如果需要spring框架，那么也需要在启动tomcat时也要加载spring配置文件

* 每个web应用有且只有一个全局域对象ServletContext，并且在服务器启动的时候ServletContext就会被加载，服务器关闭时才会被销毁；那么利用Servlet中的监听器，一个监听ServletContext启动的监听器，当ServletContext启动时，监听器将会被执行，那么可以使用该监听器加载spring的配置文件
  * spring-web的jar包中，有一个ContextLoaderListener监听器类，默认只监听WEB-INF路径下的applicationContext.xml文件，若是需要修改配置文件的路径，可以使用<context-param>标签修改
	* 如下配置web.xml就可以在服务器启动时加载spring配置文件
```
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
</context-param>

<!--配置监听器在启动ServletContext的时候初始化spring的配置-->
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

* 整合spring + MyBatis：由于spring是mvc中的service层，接替了mybatis中的事务控制功能，并且也可以在spring中配置数据库连接池，将SqlSessionFactory放入容器等，所以整合是不需要mybatis对的配置文件的
* applicationContext.xml增加如下配置，并且将数据库连接的信息也抽出到一个配置文件中jdbc-properties.properties
```
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/spring?serverTimezone=GMT%2B8
jdbc.user=root
jdbc.password=0605123
```
```
<!--将jdbc数据源需要的信息加载-->
<bean id="propertyPlaceholderConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:jdbc-properties.properties"/>
</bean>

<!--配置数据库连接池-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driver}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.user}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--配置SqlSessionFactory工厂-->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!--配置AccountDao接口所在的包-->
<bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.cc.dao"/>
</bean>

<!--配置事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"></property>
</bean>

<!--开启事务注解支持-->
<tx:annotation-driven/>

<!--配置事务-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="find*" read-only="true" propagation="SUPPORTS"/>
        <tx:method name="*" propagation="SUPPORTS" read-only="false"/>
    </tx:attributes>
</tx:advice>

<!--配置AOP-->
<aop:config>
    <aop:pointcut id="pt1" expression="execution( * com.cc.service.impl.*.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"/>
</aop:config>
```
* 至此，ssm框架就整合完成了，可以使用tomcat启动并使用


* mybatis-spring.jar包完成了spring和mybatis之间的整合工作，一些详细使用可以参照【[mybatis-spring](http://www.mybatis.org/spring/zh/getting-started.html)】
* 如果使用注解@Repository标记Mapper接口，并使用@Select等注解对数据库操作方法进行标记，那么依照以下附件中的使用即可
```
@Repository
public interface UserMapper {
  @Select("SELECT * FROM users WHERE id = #{userId}")
  User getUser(@Param("userId") String userId);
}
```
* 对于使用复杂查询，使用resultMap或是resultType的场景，还是需要使用mybatis原来的方法：mapper接口+mapper.xml配置文件，并且这二者的使用一般是需要放在一个包下的，为了方便管理并不这样做，在spring的配置文件中添加这样的配置，以达到编译后放置在同一包下的效果
```
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">  
    <property name="dataSource" ref="dataSource" />  
    <!-- xml路径 -->  
    <property name="mapperLocations" value="classpath:com/edip/mapper/*.xml" />
</bean>  
  
<!-- mapper路径  Mapper接口所在包名，Spring会自动查找其下的类 -->  
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
    <property name="basePackage" value="com.edip.mapper" />  
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>  
</bean>
```
