# Co-Auth Core Module Registry

This microservice is responsible for the checking if the user is eligible for a Co-Auth module.

## What it does
1. Co-Auth Core API Gateway invokes this service everytime whenever there is a co-auth transaction is initiated (first call) or verified (in last leg)
2. This service validates the API key and returns the application details (in body) for which this API key is registered
3. If the API key is not valid, it returns a 401 unauthorized error

### Technologies
1. Java 17+
2. Quarkus
3. Postgres
4. Kafka
5. Infinispan

### Installation / Contribution / and more
Follow documentation available at [co-auth documentation website](https://documentation.coauth.dev).
