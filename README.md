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
