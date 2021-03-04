# Java Springboot Demo
## Introduction

Java Springboot Demo is one of a set of microservices, named Jumps, developed to generate a microservice communication test tool in order to support multi hands-on and webinars around microservices in Kubernetes.

## Quick Start Java Springboot Demo

Once the demo project has been uploaded, it is required to execute the following process:

- Download Maven Dependencies
- Package the application
- Execute the Application

## Java Springboot Test

Java Springboot Demo included a set of test in order to check all features. 

## Test Demo App API Locally

- GET method to reach /

```
$ curl localhost:8443 
{"message":"/ - Greetings from Spring Boot!","code":200}
```

- GET method to reach /jump

```
$ curl localhost:8443/jump
{"message":"/jump - Greetings from Spring Boot!","code":200}
```

- POST method with JUMP Object in the body to make multi jumps through Java Springboot Demo

```
$ curl -XPOST -H "Content-type: application/json" -v -d '{
    "message": "Hello",
    "last_path": "/jump",
    "jump_path": "/jump",
    "jumps": [
        "http://localhost:8443"
    ]
}' 'localhost:8443/jump'
{"message":"/jump - Greetings from Spring Boot!","code":200}
```

```
$ curl -XPOST -H "Content-type: application/json" -v -d '{
    "message": "Hello",
    "last_path": "/jump",
    "jump_path": "/jump",
    "jumps": [
        "http://localhost:8443",
        "http://localhost:8443"
    ]
}' 'localhost:8443/jump'
{"message":"/jump - Greetings from Spring Boot!","code":200}
```

curl -XPOST -H "Content-type: application/json" -v -d '{
    "message": "Hello",
    "last_path": "/jump",
    "jump_path": "/jump",
    "jumps": [
      "http://localhost:8443",
      "http://localhost:8442",
      "http://localhost:8443"
    ]

}' 'localhost:8442/jump'


## Author Information

Asier Cidon @Red Hat

asier.cidon@gmail.com
