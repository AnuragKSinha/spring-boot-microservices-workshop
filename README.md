# Why hard coded URLs are bad?
* Changes require code updates
* Dynamic URLs in the cloud
* Load balancing
* Multiple environments
# Solution 
* Service Discovery 
  * Client Side Service Discovery
    > The client will call the Service Discovery which will give back the info of Service client wants to interact to. And once Client get the info of service then Client makes the actual call to Service. It has 2 hops.
  * Service Side Service Discovery
    > The service side Service Discovery takes care of the routing.The client needs to tell the Service discovery that this is the message and it wants to send this message to X service. And in this there is only 1 hop.

## Spring Cloud uses client side Service Discovery
  > Technology using which Spring does that is Eureka. Which takes care of multiple hops.And its provided by Netflix OSS.Other Library created are Ribbon,Hystrix and Zuul
  * Eureka Client
    * Registers itself with Eureka Server 
    * It can also ask info of other services from Eureka Server in case it needs it
<img width="627" alt="Screenshot 2023-04-04 at 10 14 37 PM" src="https://user-images.githubusercontent.com/26598629/229860734-1570eb1c-3f09-4f2a-b52e-9765a5f336b6.png">

## Steps to making this work
  * Start up a Eureka Server
  * Have microservice register(publish) using Eureka client
  * Have microservice locate(consume) using Eureka client

## How to call service without using actual URLs?
* Using @LoadBalanced
  > It does the service discovery in a load balanced way
* When you use @LoadBalanced you are telling RestTemplate/WebClient dont go to the service directly whatever URL is given to you is not actual URL,the URL which is given is basically a hint about what service you need to discover.

## Is the load balancing good?
* The loadbalancing which is done here is a **client side load balancing** which is not good as the Client get the list of Service Instances from Service Discovery and then on client side the decision is done to which service it wants to interact to. Which is incorrect as all the clients can hit the same instance at a time and overload the same service instance. As Server should have the full control because server will only know the current load on each of its instances. And accordingly route the request to appropiate service instance. 
<img width="862" alt="Screenshot 2023-04-08 at 12 43 51 PM" src="https://user-images.githubusercontent.com/26598629/230708670-bc5e55f5-ee7e-4719-a3da-558cfc1cb952.png">

## How Fault tolerance works?
 * Heart Beat on regular interval
    * After registering the service with Service Discovery, the Service Discovery send heart beat to each the registered services. And checks if they are alive or not. 
 * If the service discovery is down
    * In this case the Eureka Client calls the service discovery and lets say it did not get any response then it uses its local cache to get the service url. 
     <img width="840" alt="Screenshot 2023-04-08 at 12 53 50 PM" src="https://user-images.githubusercontent.com/26598629/230709092-bf0222c8-9bb4-48f5-bba6-4eb1ec90ba11.png">


## Architecture after adding external DB API?
<img width="779" alt="Screenshot 2023-04-13 at 11 50 37 PM" src="https://user-images.githubusercontent.com/26598629/231850665-19cdd1cd-285a-4c9f-867a-11641bb21070.png">

## How do we make the microservice resilient? 
 * Issues with Microservices
    * Scenario 1: When the service goes down
          <img width="1035" alt="Screenshot 2023-04-14 at 12 01 50 AM" src="https://user-images.githubusercontent.com/26598629/231851603-94994733-0f0a-4d48-b693-6884aced0ae9.png">
      * Solution: Run Multiple instances
<img width="796" alt="Screenshot 2023-04-14 at 12 03 46 AM" src="https://user-images.githubusercontent.com/26598629/231851765-82cf8c4e-551d-465c-a879-f7ca5cd81683.png">
