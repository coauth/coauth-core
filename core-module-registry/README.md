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
4. Docker (For development mode)

### Installation notes
- The application runs on 8081 port by default. You can change it in application.properties file.
- For dev mode the application uses quarkus devservices for running database
- You need to set the environment variables, namely.
  - POSTGRESQL_USER=<POSTGRESQL_USER>
  - POSTGRESQL_PASSWORD=<POSTGRESQL_PASSWORD>

Example for Linux:
```shell script
export POSTGRESQL_USER=postgres
export POSTGRESQL_PASSWORD=postgres
```

Example For Windows:
```powershell
[Environment]::SetEnvironmentVariable('POSTGRESQL_USER', 'postgres', [System.EnvironmentVariableTarget]::Process)
[Environment]::SetEnvironmentVariable('POSTGRESQL_PASSWORD', 'postgres', [System.EnvironmentVariableTarget]::Process)
```

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```

