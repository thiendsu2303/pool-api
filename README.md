# Pool API

This project implements a REST API in Java with Spring Boot. The API provides two POST endpoints for managing and querying numeric pools. It does not use any external database or libraries for quantile calculation.

## Table of Contents

1. [Features](#features)
2. [Setup](#setup)
3. [Running the Application](#running-the-application)
4. [API Endpoints](#api-endpoints)
5. [Testing](#testing)
6. [Performance Considerations](#performance-considerations)
7. [Contributing](#contributing)
8. [License](#license)

## Features

- **Append or Insert Pool Values**: Adds values to an existing pool or creates a new pool if it doesn't exist.
- **Query Pool Values**: Retrieves the quantile and total count of elements for a specified pool and percentile.
- **No External Dependencies**: Simple implementation with no database or third-party libraries for quantile calculation.

## Setup

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher

### Clone the Repository

```bash
git clone https://github.com/thiendsu2303/pool-api.git
cd pool-api
```

## Running the Application
### Build the project:
```bash
mvn clean install
```
### Run the application:
```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080.

## API Endpoints
### 1. Append or Insert Pool
#### Endpoint: POST /api/pool/append

#### Request Body:
```json
{
  "poolId": 123546,
  "poolValues": [1, 7, 2, 6]
}
```
#### Response:
```json
{
  "status": "appended" 
}
```
#### Description:
Adds values to an existing pool or creates a new pool if it doesn't exist. The response indicates whether values were appended to an existing pool or inserted as a new pool.

### 2. Query Pool
#### Endpoint: POST /api/pool/query

#### Request Body:

```json
{
  "poolId": 123546,
  "percentile": 99.5
}
```
#### Response:

```json
{
  "quantile": 7.0,      
  "totalCount": 10      
}
```
#### Description:
Queries the specified pool and returns the quantile for the given percentile along with the total count of elements in the pool.

## Testing
### Unit Tests
Unit tests are located in src/test/java/com/example/pool_api/controller/PoolControllerTest.java.

Unit tests are located in src/test/java/com/example/pool_api/controller/PoolControllerTest.java.

Example test cases:

- Test Insert New Pool: Validates inserting a new pool.
- Test Append to Existing Pool: Validates appending values to an existing pool.
- Test Query Pool with Valid Percentile: Validates querying a pool with a valid percentile.
- Test Handle Non-Existent Pool: Validates response when querying a non-existent pool.
- Test Query Pool with Out-of-Bounds Percentile: Validates response for an out-of-bounds percentile.
- Test Handle Empty Pool: Validates response when querying an empty pool.
## Contributing
Contributions are welcome! Please submit a pull request with your changes. Make sure to follow the coding style and include tests for new features or bug fixes.

## License
This project is licensed under the MIT License - see the LICENSE file for details.