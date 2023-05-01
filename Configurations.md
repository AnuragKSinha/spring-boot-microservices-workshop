# Goals
1. __Externalization:__ So that there is no need to restart the system
2. __Environment Specific:__ As diff diff env will have diff diff configurations 
3. __Consistent:__ As there are multiple instances of Microservice then its Important that same configuration is present in all the instances
4. __Version History:__ As its externalized so it not part of the code base so having a verion history is important
5. __Real Time management:__ the configuration should be picked up in real time and there should not be any need for server restart 

# Externalization
## Still its not externalised as this property file goes inside the jar
### Refer a value in other property
```
<<application.properties>>
my.greeting=Hello World
app.name=My app
app.description=Welcome to ${app.name}
```
### Refer a value in java
```java
  @Value("${api.key}")
	private String apiKey;
```
__Benefit of taking the values from the code and putting into a even if the property file is inside the jar because there are ways in which you can override it__


## Using External Property Sources
> if we have a application.property placed in the same folder in which compile jar of the Microservices is kept.So when Service starts using java -jar movie-info-service-0.0.1-SNAPSHOT.jar then it keeps up the values from the application.properties from the same folder and override the property file which is inside the jar.

```
anuragsinha@Anurags-MacBook-Pro target % ls
_application.properties_                         maven-archiver                                  surefire-reports
classes                                          maven-status                                    test-classes
generated-sources                               _movie-info-service-0.0.1-SNAPSHOT.jar_
generated-test-sources                           movie-info-service-0.0.1-SNAPSHOT.jar.original

```
> External application.properties override the application.properties values which are inside the jar.


## Externalise using command line
```
anuragsinha@Anurags-MacBook-Pro target % java -jar movie-info-service-0.0.1-SNAPSHOT.jar --spring.application.name="movie-info-service"

```
## 3 @Value annotation tricks
1. Providing a default message if you don't want your application to fail and fall back to use the default value in case actual value referred is missing.
2. Managing List 
3. Managing Maps

```
@RestController
public class HelloWorld {
	@Value("${my.greeting: default value}")
	private String greetingMessage;
	
	@Value("some static message")
	private String staticMessage;
	
	@Value("${my.list.values}")
	private List<String> listValues;
	
	@Value("#{${dbValues
	private Map<String,String> dbValues;
}
```
application.properties
```
my.greeting=Hello World
my.list.values=One,Two,Three
dbValues={connectionString: 'http://__',username: 'foo',password: 'pass'}
```
## @ConfigurationProperties vs @Value
> Having a ConfigurationProperties you can have as a Autowired rather than same @Value in multiple places. When we want to use a configuration in multiple places we use @ConfigurationProperties and when we want to use only in one place we use @Value

<img width="1311" alt="Screenshot 2023-04-27 at 11 03 54 PM" src="https://user-images.githubusercontent.com/26598629/234943227-4b3589f7-4e78-4dcb-8bb1-478f36ab0518.png">

## Drawback
1. Outside the source code control
2. Manual management across environments

# Environment Specific
## Spring Profiles
<img width="1339" alt="Screenshot 2023-04-28 at 11 33 09 PM" src="https://user-images.githubusercontent.com/26598629/235220979-2f9118f5-f070-4c42-b662-8cf4d1f3bdf8.png">

1. extn can be properties or yml
2. We need to specify this property in order to activate the profile __spring.profiles.active=test__ otherwise default profile is picked.
```
No active profile set, falling back to 1 default profile: "default"
```
<img width="823" alt="Screenshot 2023-04-28 at 11 52 43 PM" src="https://user-images.githubusercontent.com/26598629/235224459-a3a65e18-1742-4d94-a114-4839013fd6db.png">

> We can have more than 1 active profiles and the way it overrides is the order in which its defined.If QA is defined after test then Values from QA overrides the test profiles value.But anything that you specify in your profile always overrides your default profile. So in general the common values which are same in all the env we define those values in default.And env specific values are defined in env specific profile application.properties.

> __NOTE: Default profile is always active.__

> We have created one jar file which can be deployed in multiple envs using __Profiles__
<img width="1226" alt="Screenshot 2023-04-29 at 12 04 49 AM" src="https://user-images.githubusercontent.com/26598629/235226714-362c1430-edd6-4e6e-97e7-f41ac70e83a2.png">

Now we have env based configuration but all of them sitting inside your jar you just need to choose your profile were you are running using command line without having to modify the jar.And there is no manual effort required.

## Bean initialization based on Profile

<img width="1348" alt="Screenshot 2023-04-29 at 12 10 24 AM" src="https://user-images.githubusercontent.com/26598629/235227760-8eb9d056-b5ab-4c8a-adaa-c7870a5d68de.png">

Based on the __active profile__ Spring Container will initialize the Repository Bean class. If its __dev__ then __LocalDataSourceBean__ is initialized and if its __prod__ then __DataSourceBean__ class is initialized.__If there is no @Profile annotation used then by default all the beans are initialized.__
## Environment Object
> In a Spring Boot application, the Environment object represents the environment in which the application is running, including information such as system properties, command line arguments, and application properties.
You can use the Environment object in your code to access properties and configuration information for your application. One way to access the Environment object is to inject it into your bean using the @Autowired annotation:

```
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public void doSomething() {
        String property = env.getProperty("my.property");
        // use the property value here
    }

}
```
In the above example, the MyBean class implements the EnvironmentAware interface and overrides the setEnvironment() method to store a reference to the Environment object. The doSomething() method uses the env.getProperty() method to retrieve the value of a property named my.property.

Note that you can also use the @Value annotation to inject property values directly into your bean, as long as the properties are defined in your application's properties or YAML file. For example:

```
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

    @Value("${my.property}")
    private String property;

    public void doSomething() {
        // use the property value here
    }

}
```

In the above example, the @Value annotation is used to inject the value of a property named my.property directly into the property field of the MyBean class.


Environment can be used to:
- look up profiles. It should not be done as it can affect testability.
- look up properties. It should not be done. Use @Value and ${}.

## Spring Cloud Config Server
> This will help us to move from single service to multiple microservices and have consistency in having configuration accross all the microservices.
### Recap on the Goals
1. Externalized: __Property Files__ and move the configuration outside of the Jar/code by placing the application.properties in same folder as jar.And also by providing the parameter via command line while executing the Jar to override the property values.
2. Environment Specific: Spring Profiles which works in conjunction of property files So that we can make different property file for different environments.
3. Consistency is not achieved as Jar will be deployed in multiple environments and any changes made needs to propagate to all the environments as all has its own copy. 
4. Version History: without consistency there is no sense of having version history
5. Real Time Management is not achieved

> As in microservice world there are multiple microservices deployed which might require same configuration values.That is when Config server comes into picture were we take our the configuration out of microservices and create a separate configuration service. And make Config Server as Single Source of truth and have that manage all the Information so no matter what the Microservices is it all refers to the same source of truth. 

<img width="1404" alt="Screenshot 2023-04-29 at 12 37 38 AM" src="https://user-images.githubusercontent.com/26598629/235305765-fd384c44-0726-4618-966c-4a779558ed02.png">

### Options for Config As a Service
<img width="1229" alt="Screenshot 2023-04-29 at 7 14 54 PM" src="https://user-images.githubusercontent.com/26598629/235306137-13765084-f99b-452f-9765-7b0dafb16014.png">

> With Use of Git Repo, Version History is taken care. 

### Whats the URL for Config Server?
> Once we start the config server we can access the URL as show below to view the configuration from git.Example: localhost:8888/application/default
> server.port=8888
> spring.cloud.config.server.git.uri=<GIT URL> 
<img width="1007" alt="Screenshot 2023-04-29 at 7 52 42 PM" src="https://user-images.githubusercontent.com/26598629/235307855-50a1935f-7f82-4e61-8010-4412acb5b539.png">

### Server Side Changes
spring.cloud.config.server.git.uri
	
### Client Side Changes
spring.cloud.config.uri
	
### How to create configuration specific to Microservices?
	1. define spring.application.name
	2. add microservice name related application.properties file in git repo where Spring Config Server is pointing to.
	Ex, Application name: spring-boot-config and application.properties file name is spring-boot-config.properties

### How will Microservices pickup latest changes made to property files?
> The config server will always pickup the latest changes by default.Its the client microservices which does not pick the latest changes from config server. And its by design bacause when you have bunch of microservices in your system then you dont want it to constantly looking for an update in the config because it takes time and resources and its intensive and its not often that you make changes frequently.The Goal of the microservices are to do whatever they were created to do, the business problem they are meant to solve and you dont want to take its valuable CPU cycles or memory to keep on checking if there is an update in the configuration. 

	1. By restarting the client server the microservices will pickup need configurations from the config server.
	By default. 
	2. Spring Boot Actuator provides an endpoint were POST request can be made to refresh the Client Server Configuration 
	and then Client will have latest changes from Config Server. 
	"http://localhost:8080/actuator/refresh"
	3. @RefreshScope needs to be used on the beans whose values you want to refresh when POST endpoint is hit.Refresh does not 
	happen for all the beans. 
