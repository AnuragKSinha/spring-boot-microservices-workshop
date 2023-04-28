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
