# Co-Auth Core API Gateway

This microservice is the main front facing gateway to communicate with any co-auth microservices.

## What it does
1. Co-Auth Core API Gateway validates API-keys and injects application details whenever transaction is initiated (first call) or verified (in last leg)
2. There-after handling routing to other co-auth microservices.

### Technologies
1. Java 17+
2. Spring Cloud Gateway

### Installation / Contribution / and more
Follow documentation available at [co-auth documentation website](https://documentation.coauth.dev).
