# Spring MVC

Here is the example of a web application built with Spring MVC 4.0.0 and Spring Data 1.8, integrating with the MongoDB document database.

### Maven

The maven dependency for the spring framework is given below.

```xml
	<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>4.0.0</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>4.0.0</version>
		</dependency>
```

The maven dependency for mongoDB is 

```xml

		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-mongodb</artifactId>
			<version>1.8.0.RELEASE</version>
		</dependency>

```



the following configuration is required

```xml

	<!-- Host value for connecting and quering the documents in the database -->
	<bean id="mongo" class="org.springframework.data.mongodb.core.MongoFactoryBean">
		<property name="host" value="localhost" />
	</bean>
	
	<!-- MongoTemplate for connecting and quering the documents in the database -->
	<bean id="mongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
		<constructor-arg name="mongo" ref="mongo" />
		<constructor-arg name="databaseName" value="test" />
	</bean>

```


references:
http://blog.manishchhabra.com/2013/04/spring-data-mongodb-example-with-spring-mvc-3-2/
