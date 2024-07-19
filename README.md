# Coin-Operated Soda Machine

This is a Java-based application that simulates a coin-operated soda machine. The application uses Spring Boot and Redis for caching. It provides endpoints to interact with a soda machine, such as appending and querying pools, and integrates Redis for caching frequently accessed data.

## Table of Contents

1. [Features](#features)
2. [Setup](#setup)
3. [Running the Application](#running-the-application)
4. [API Endpoints](#api-endpoints)
5. [Testing](#testing)
6. [Performance Testing](#performance-testing)
7. [Contributing](#contributing)
8. [License](#license)

## Features

- **Append or Insert Pool Values**: Add new values to a specific pool or insert if it doesn't exist.
- **Query Pool Values**: Retrieve statistical data such as quantiles from a pool.
- **Redis Caching**: Use Redis for caching frequently accessed pool values to improve performance.

## Setup

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- Docker (for Redis setup)
- Redis server (local or remote)

### Clone the Repository

```bash
git clone https://github.com/thiendsu2303/pool-api.git
cd pool-api
