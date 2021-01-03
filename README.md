## Solution components

![alt text](./images/solution-components.png "Solution components diagram")

We have 1 (mock) 3rd-party `Voucher Service`, which doesn't have any database.
2 other services, `Prepaid Data Service` (for purchasing, applying and querying vouchers ) and `Account Service` to hold information of a phone number.

`Prepaid Data Service` will call to `Account Service` to verify the phone number.

Business flow:
- User complete payment to get the Data package that they want (1GB, 2GB, ...)
- User now can get a Voucher corresponding to the Data package they've just bought.
- After purchasing a Voucher, user can use it immediately, or share it with other people.
- To view purchased Vouchers, user first need to do authentication, this can be done by OTP.
- After authentication, user now can view their purchased Vouchers.


NOTE: Although the image only includes arrows from the API Gateway to `Prepaid Data Service` and `Account Service`, you still can call directly to other services via the API Gateway.

## Frameworks, tools, libraries

In addition to the solution components, this is a list of all framework and libraries that are used in this project:

### Spring dependencies
- Spring Boot
- Spring Cloud API Gateway (can be changed to Zuul)
- Spring Cloud Starter Netflix Eureka (Server, Client)
- Spring Boot Starter Test (including JUnit 5)
- Spring Boot Starter AOP (join point to secure a controller)
- Spring Boot Starter OpenFeign (calling API like a Java method)
- Spring Boot Starter Data JPA 
- Spring Boot Starter Web


### Other Java Dependencies
- Model Mapper (mapping different object types)
- Java Faker (generate testing data)
- PostgreSQL (connect to Postgre DB)
- Rest Assured (testing controller method)
- Lombok (no more boilerplate code)
- H2 (self-contained testing)


## Entities relationship

![alt text](./images/database-diagram.png "Entity relationship diagram")

The system is designed in such a way that a voucher can be shared between any phone numbers or accounts (as long as it hasn't been applied).

## Project structure

### Entire project

- **account-service**: account micro service
- **api-gateway**: the api gateway
- **databases**: it has only one `database.yaml` file, you can use this file to spin up 2 Postgre DB for `Account Service` and `Prepaid Data Service`.
- **images**: contains images that are used in this README.md file
- **prepaid-data-service**: prepaid data micro service, including end points for purchasing, applying/sharing and viewing vouchers.

### Spring Boot project with Feign

![alt text](./images/project-structure.jpg "Project structure")

All of the Spring Boot projects follow this structure.

- **client**: classes of http client, Feign client in this case
- **configuration**: `@Configuration` classes
- **controller**: `@RestController` classes
- **dto**: model returned by the controller
- **entity**: `@Entity` classes
- **exception**: custom exception classes, `@ControllerAdvice` handle all exceptions before returning DTO to the user
- **model**: controller request models
- **repositroy**: `@JpaRepository` interfaces
- **security**: security related classes, in this project it contains code to generate, verify JWT token, and a custom `@SecuredEndPoint` annotation to secure the API to get vouchers.
- **service**: service interfaces and implementation classes
- **utility**: other classes
- `{project-name}Application.java`, Spring Boot generated classes, to start the Spring Boot application.
- **resources**: contains resources of project, static files, templates, `properties` or `yaml` files, ...
- **test**: code for testing, in this cases they are self-contained integration test.
- **target**: can be ignored, generated when we run `mvn clean installed`
- `mvnw`, `HELP.md`, `mvnw.cmd`, `*.iml` are Intellij files and can be ignored. 

The diagram below describes what kind of data is transferred back and forth between layers in a Spring Boot project:

![alt text](./images/layer-data-structure.png "Project structure")

## Starting project locally

### **Prerequisites**
- Java 8, I use `8.0.275-amzn` Open JDK
- Be able to pull Docker images from Docker hub, specifically `postgres:11.5-alpine`.
- Docker installed on your machine, my `docker` and `docker-compose` version

> docker-compose version
```
docker-compose version 1.25.0, build unknown
docker-py version: 4.1.0
CPython version: 3.8.6
OpenSSL version: OpenSSL 1.1.1f  31 Mar 2020
```

> docker version
```
Client:
 Version:       17.12.0-ce
 API version:   1.35
 Go version:    go1.9.2
 Git commit:    c97c6d6
 Built: Wed Dec 27 20:10:45 2017
 OS/Arch:       linux/amd64

Server:
 Engine:
  Version:      17.12.0-ce
  API version:  1.35 (minimum version 1.12)
  Go version:   go1.9.2
  Git commit:   c97c6d6
  Built:        Wed Dec 27 20:09:19 2017
  OS/Arch:      linux/amd64
  Experimental: false
```

- Maven installed, be able to download dependencies listed in the `pom.xml` files in the projects.

> mvn -v
```
Maven home: /opt/apache-maven-3.6.3
Java version: 1.8.0_275, vendor: Amazon.com Inc., runtime: /home/lov-truong/.sdkman/candidates/java/8.0.275-amzn/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.8.0-33-generic", arch: "amd64", family: "un
```

- Make sure you can run `mvn` and `docker-compose` commands (on Windows you might need to check the path variable of `$MVN_HOME` and `$JAVA_HOME`).

### Steps
Assuming you are currently in the project folder

1) Starting up the databases
```
cd databases
docker-compose -f database.yaml up
```

2) Starting up the Eureka service registry server
```
cd service-discovery
mvn clean install
mvn spring-boot:run -X
```

3) Starting up 3rd-party voucher service
```
cd voucher-service
mvn clean install
mvn spring-boot:run -X
```

4) Starting up Account service
```
cd account-service
mvn clean install
mvn spring-boot:run -X
```

5) Starting up Prepaid Data service
```
cd prepaid-data-service
mvn clean install
### 3rd-party Voucher Service

mvn spring-boot:run -X
```

6) Starting up API Gateway
```
cd api-gateway
mvn clean install
mvn spring-boot:run -X
```

NOTE:
- *Please make sure that `mvn clean install` is successful*
- Each step can be in one tab in Terminal

## CURLs
In this project, URLs of Eureka service registry server and the API Gateway are hardcoded.

- Eureka: http://localhost:8761
- Api Gateway: http://localhost:8760 (use this URL to access Eureka Dashboard)

Ports of `account-database` and `prepaid-database` are also hard coded as 5432 and 5431, respectively.

NOTE:
- **Please replace the ports if they are already in used**.
- For the API get vouchers, please read `Important Notes`.
- We are calling the URL endpoints via the API Gateway.

### 3rd-party Voucher Service
- Happy case
> curl -v --location GET 'http://localhost:8760/voucher-service/api/voucher?delay=1'
```
* Could not resolve host: GET
* Closing connection 0
curl: (6) Could not resolve host: GET
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#1)
> GET /voucher-service/api/voucher?delay=1 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Content-Type: application/json
< Content-Length: 36
< Date: Mon, 04 Jan 2021 03:51:37 GMT
< 
* Connection #1 to host localhost left intact
6da65d89-a1fc-4664-8c5e-2bf337749468% 
```

- Unhappy case
> curl -v --location GET 'http://localhost:8760/voucher-service/api/voucher?delay=-1'
```
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> GET /voucher-service/api/voucher?delay=-1 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 500 Internal Server Error
< Content-Length: 0
< Date: Mon, 04 Jan 2021 03:50:10 GMT
< 
* Connection #0 to host localhost left intact
```

### Account Service

*Create account*
> curl -v --location --request POST 'http://localhost:8760/account-service/api/account' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "phoneNumber": "12345678",
    "address": "vietnam"
}'

- happy case
```
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /account-service/api/account HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 59
> 
* upload completely sent off: 59 out of 59 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 201 Created
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:09:00 GMT
< 
* Connection #0 to host localhost left intact
{"phoneNumber":"12345678","address":"vietnam"}% 
```
- unhappy case
```
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /account-service/api/account HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 59
> 
* upload completely sent off: 59 out of 59 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 400 Bad Request
< Content-Type: application/json
< Content-Length: 52
< Date: Mon, 04 Jan 2021 04:10:05 GMT
< 
* Connection #0 to host localhost left intact
There is already an account having that phone number%
```

*Get account*
> curl -v --location GET 'http://localhost:8760/account-service/api/account/1234' \
--header 'Accept: application/json'

- happy case
```
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> GET /account-service/api/account/1234 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: application/json
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:13:49 GMT
< 
* Connection #0 to host localhost left intact
{"phoneNumber":"1234","address":"vietnam"}%
```

- unhappy case
```
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> GET /account-service/api/account/123 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: application/json
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 404 Not Found
< Content-Type: application/json
< Content-Length: 53
< Date: Mon, 04 Jan 2021 04:14:58 GMT
< 
* Connection #0 to host localhost left intact
There is no existing account having that phone number%
```

### Prepaid Data Service

*Purchase voucher*
> curl --location --request POST 'http://localhost:8760/prepaid-data-service/api/prepaid-data/voucher/purchase?delay=2' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "phoneNumber": "1234",
    "dataAmount": 2
}'

- happy case
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/purchase?delay=2 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 50
> 
* upload completely sent off: 50 out of 50 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 201 Created
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:20:49 GMT
< 
* Connection #0 to host localhost left intact
{"code":"c18f2e62-f390-4482-bae8-a4f67074e4a8","dataAmount":2,"phoneNumber":"1234","applied":false}
```

- unhappy case (one of them)
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/purchase?delay=-1 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 50
> 
* upload completely sent off: 50 out of 50 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 500 Internal Server Error
< Content-Type: application/json
< Content-Length: 39
< Date: Mon, 04 Jan 2021 04:21:25 GMT
< 
* Connection #0 to host localhost left intact
Cannot get response from other services
```

*Apply/share voucher*
> curl -v --location --request POST 'http://localhost:8760/prepaid-data-service/api/prepaid-data/voucher/apply' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "code": "41a9263b-cae4-489b-ab32-31b778c088e1",
    "phoneNumber": "1234"
}'

- happy case
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/apply HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 81
> 
* upload completely sent off: 81 out of 81 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 202 Accepted
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:26:51 GMT
< 
* Connection #0 to host localhost left intact
{"code":"f0df1e64-024b-4b49-9ce4-aee412afd486","dataAmount":2,"phoneNumber":"1234","applied":true}
```

- unhappy case (one of them)
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/apply HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 81
> 
* upload completely sent off: 81 out of 81 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 400 Bad Request
< Content-Type: application/json
< Content-Length: 31
< Date: Mon, 04 Jan 2021 04:26:10 GMT
< 
* Connection #0 to host localhost left intact
This voucher is already applied
```

*Get JWT Token to view voucher*
> curl -v --location --request POST 'http://localhost:8760/prepaid-data-service/api/prepaid-data/voucher/get-view-token' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "phoneNumber": "1234",
    "otp": "1234"
}'

- happy case
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/get-view-token HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 48
> 
* upload completely sent off: 48 out of 48 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:29:00 GMT
< 
* Connection #0 to host localhost left intact
{"jwtToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWb3VjaGVyc1ZpZXdUb2tlbiIsImlhdCI6MTYwOTczNDU0MCwiZXhwIjoxNjA5NzY0NTQwLCJqdGkiOiJmMWI2NDQwNS1mYTIxLTQwMDEtYWQ0OC04ZTk0MWIzNGU1ZWUiLCJpc3MiOiJMb2NUcnVvbmciLCJwaG9uZU51bWJlciI6IjEyMzQiLCJvdHAiOiIxMjM0In0.yVjjReeHU2EMdQwGmttfvxELzyO-JvW-_kk7Os5M6AhKELOwNkMerBuOsvVW2G9Ozf1dl9kUdmC6OhK43qaQEw"}
```

- unhappy case
```
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> POST /prepaid-data-service/api/prepaid-data/voucher/get-view-token HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Content-Type: application/json
> Accept: application/json
> Content-Length: 53
> 
* upload completely sent off: 53 out of 53 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 500 Internal Server Error
< Content-Type: application/json
< Content-Length: 18
< Date: Mon, 04 Jan 2021 04:32:08 GMT
< 
* Connection #0 to host localhost left intact
Cannot get account
```

*Get Vouchers by phone number*

JWT token is expired after 5 minute by default

> curl -v --location --request GET 'http://localhost:8760/prepaid-data-service/api/prepaid-data/voucher/1234' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWb3VjaGVyc1ZpZXdUb2tlbiIsImlhdCI6MTYwOTczNDgxMywiZXhwIjoxNjA5NzY0ODEzLCJqdGkiOiJmYjdhN2VmZi03MGZiLTQ5NmQtYjExNy1kYmRkZDhlYTcwYjIiLCJpc3MiOiJMb2NUcnVvbmciLCJwaG9uZU51bWJlciI6IjEyMzQiLCJvdHAiOiIxMjM0In0.zo_ixGHNx4m3_S8Y2YwvjuHOQ_kzZ_l5seEQ0OImY4bGaxnqLkAZVRyY1OJRUAl1fHTATXGbBRs9yUU6eFb0HA'

- happy case
```
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> GET /prepaid-data-service/api/prepaid-data/voucher/1234 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: application/json
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWb3VjaGVyc1ZpZXdUb2tlbiIsImlhdCI6MTYwOTczNDgxMywiZXhwIjoxNjA5NzY0ODEzLCJqdGkiOiJmYjdhN2VmZi03MGZiLTQ5NmQtYjExNy1kYmRkZDhlYTcwYjIiLCJpc3MiOiJMb2NUcnVvbmciLCJwaG9uZU51bWJlciI6IjEyMzQiLCJvdHAiOiIxMjM0In0.zo_ixGHNx4m3_S8Y2YwvjuHOQ_kzZ_l5seEQ0OImY4bGaxnqLkAZVRyY1OJRUAl1fHTATXGbBRs9yUU6eFb0HA
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< transfer-encoding: chunked
< Content-Type: application/json
< Date: Mon, 04 Jan 2021 04:34:41 GMT
< 
* Connection #0 to host localhost left intact
[{"code":"f0df1e64-024b-4b49-9ce4-aee412afd486","dataAmount":2,"phoneNumber":"1234","applied":true},{"code":"41a9263b-cae4-489b-ab32-31b778c088e1","dataAmount":2,"phoneNumber":"1234","applied":true},{"code":"c18f2e62-f390-4482-bae8-a4f67074e4a8","dataAmount":2,"phoneNumber":"1234","applied":false},{"code":"6fec926e-148b-4c31-acef-eeda30751bbe","dataAmount":2,"phoneNumber":"1234","applied":false},{"code":"3c712c4d-5c2a-4b5b-bc9c-8cf04b108392","dataAmount":2,"phoneNumber":"1234","applied":false},{"code":"977d71ec-e38d-43ea-a5fb-9ee0c91a9434","dataAmount":2,"phoneNumber":"1234","applied":false}]
```

- unhappy case (one of them) where phone number does not exist
```
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1:8760...
* TCP_NODELAY set
* Connected to localhost (::1) port 8760 (#0)
> GET /prepaid-data-service/api/prepaid-data/voucher/123334 HTTP/1.1
> Host: localhost:8760
> User-Agent: curl/7.68.0
> Accept: application/json
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWb3VjaGVyc1ZpZXdUb2tlbiIsImlhdCI6MTYwOTczNDgxMywiZXhwIjoxNjA5NzY0ODEzLCJqdGkiOiJmYjdhN2VmZi03MGZiLTQ5NmQtYjExNy1kYmRkZDhlYTcwYjIiLCJpc3MiOiJMb2NUcnVvbmciLCJwaG9uZU51bWJlciI6IjEyMzQiLCJvdHAiOiIxMjM0In0.zo_ixGHNx4m3_S8Y2YwvjuHOQ_kzZ_l5seEQ0OImY4bGaxnqLkAZVRyY1OJRUAl1fHTATXGbBRs9yUU6eFb0HA
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 500 Internal Server Error
< Content-Type: application/json
< Content-Length: 18
< Date: Mon, 04 Jan 2021 04:35:08 GMT
< 
* Connection #0 to host localhost left intact
Cannot get account
```

## Troubleshoot
- Please contact me directly or add an issue.

## Important Notes
- As of now, the `secret key` to generate JWT token is created and stored in `Prepaid data service`, every instance of that service will have it own `secret key`. The key need to be global for all instances of `Prepaid data service` to use. Such a need can be fulfilled by a Global Spring Cloud config server.