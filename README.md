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
    * Scenario 1: When the service goes down <img width="1035" alt="Screenshot 2023-04-14 at 12 01 50 AM" src="https://user-images.githubusercontent.com/26598629/231851603-94994733-0f0a-4d48-b693-6884aced0ae9.png">
          
      * Solution: Run Multiple instances <img width="796" alt="Screenshot 2023-04-14 at 12 03 46 AM" src="https://user-images.githubusercontent.com/26598629/231851765-82cf8c4e-551d-465c-a879-f7ca5cd81683.png">

    * Scenario 2: A microservice instance is slow <img width="1035" alt="Screenshot 2023-04-14 at 12 01 50 AM" src="https://user-images.githubusercontent.com/26598629/232208884-d03b2886-5e1c-45d6-b1fa-b07141532b03.png">
      * Threads are the problem as it holds the threads till the request is fulfilled and hence server runs out of threads to cater the incoming request.
      * When request comes in a thread is created and thread is taking time to process and even before the thread is done anyother request comes in and then another thread gets created.
      * If the request comes in in a faster rate than the threads in server fulfills the task and get removed.Now we have bunch of threads which are waiting soon what ends up happening is server resources get consumed. The maximum number of concurrent threads gets consumed which is configured.
      * __If you dont configure the thread count then server will crash as it will continue creating threads and server will run out of hardware resources__
      * Now what happens as the request come in and threads are not being freed up, the thread consumes all the resources that are available server hit the max thread count and server cannot do anything else this is how things work within thread server.  
      <img width="923" alt="Screenshot 2023-04-14 at 12 17 06 AM" src="https://user-images.githubusercontent.com/26598629/232209459-ccd194ea-b688-4f7c-8712-2113c5a0e604.png">
      
      * With context of microservice 
      > Service B is slow and Service A is fast: So all the request made from server to B will be stuck and all the request for A will be fast, And gradually as the Service B is slow so all the thread will be consumed by B and there will not be any thread left out to server A. And hence it will look like Service A is also slow. <img width="737" alt="Screenshot 2023-04-15 at 4 59 26 PM" src="https://user-images.githubusercontent.com/26598629/232213680-4db51fa4-ecab-4ae3-93ac-c7cd55f2f4d9.png">

     * Partial Solution: __Timeouts__ Removing threads which its taking too much time which will allow the other services to work. 
     > it solves the problem as long as the frequency of the request that is coming in is lesser than the threads that are getting removed because of timeout. If timeout is set for 3 sec and incoming request is coming in with a frequency of 3 request/sec. Then even after timeout only 1 incoming request will be servered hence this will end up in same situation in which pileup will happen and there will not be enough server resources to cater the incoming requests. 
     * Circuit Breaker Pattern
        *  Detect somthing is wrong
        *  Take temporary steps to avoid the situation getting worse. __Stop sending request to it as it will just comsume the threads__
        *  Deactivates the "problem" component so that it doesn’t affect downstream components.

## Circuit Breaker Pattern

