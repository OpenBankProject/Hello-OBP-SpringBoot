## Open Bank API client implemented with Spring Boot
This is an OBP Client API using Spring Boot 1.4.x. 
 
## Prerequisites
 
 You need to obtain an API key, username and password for the OpenBank sandbox you're planning on using.
 With these at hand, update application.properties with the values
 
## Code
 
 The code is organized between main and test directories. The tests are covering the following functionality:
  - Authentication
  - Get account details
  - Get transactions for an account
  - Tag transactions
 
## Dependencies
 
The single external dependency is on a live OBP sandbox that needs to run in order for the tested functionality to be successful. This sample project is using the version 2.2.0 of the OBP API. It shouldn't be difficult to use a different API version with the caveat that some of the entities must be adapted to match the data in the target API version.
 
The OBP client API is abstracted via [Feign](http://projects.spring.io/spring-cloud/spring-cloud.html#spring-cloud-feign), a declarative REST client. See [ObpApiClient](src/main/java/com/tesobe/obp/api/ObpApiClient.java) and [DirectAuthenticationClient](src/main/java/com/tesobe/obp/api/DirectAuthenticationClient.java) for implementation details.
 
Internally, the project is also using Lombok to simplify the code and the Joda Money API for representing monetary values. 
 
## Test
 
 Run all tests with:
 ```./gradlew clean build```
 
## Run
 
 The standard command ```./gradlew bootRun``` will start a Tomcat container running on port 8080. No end-points are configured at the moment, please rely on tests to check functionality.