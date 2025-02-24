# IT Support Ticket System

A Spring Boot application for managing IT support tickets with role based access control, audit logging, and a Swing-based client interface.

## Features

- Role-based ticket management (Employee and IT Support roles)
- ticket status tracking
- Comment system for ticket updates
- Audit logging for all ticket changes
- Search functionality
- Swing based desktop client
- RESTful API with OpenAPI documentation

## Prerequisites

- Java JDK 17
- Maven 3.6
- Oracle Database 21c Express Edition
- Oracle SQL\*Plus (for database setup to execute SQL scripts)

## Database Setup (Only for local setup, don't need if using Docker)

1. Connect to Oracle as SYSDBA:

   ```sql
   sqlplus / as sysdba
   ```

2. Create the user and grant permissions:

   ```sql
   CREATE USER C##ITICKET IDENTIFIED BY iticket25;
   GRANT CONNECT, RESOURCE, DBA TO C##ITICKET;
   GRANT CREATE SESSION TO C##ITICKET;
   GRANT UNLIMITED TABLESPACE TO C##ITICKET;
   ```

## Application Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/medAmineRg/IT-Support-Ticket.git
   cd IT-Support-Ticket
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

## Running with Docker

1. Pull the Docker image:

   ```bash
   docker pull mohamed99amine/it-support-ticket:1.0.0
   ```

2. Run using docker-compose:

   ```bash
   docker-compose up -d
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

4. Launch the Swing client:
   ```bash
   java -cp target/IT-Support-Ticket-0.0.1-SNAPSHOT.jar me.medev.itsupportticket.client.TicketClientApp
   ```

## Environment Variables

The application uses the following environment variables:

```properties
ORACLE_USER=C##ITICKET        # Oracle database username
ORACLE_PASSWORD=iticket25     # Oracle database password
ORACLE_URL=jdbc:oracle:thin:@localhost:1521:XE  # Database URL
SERVER_PORT=8081              # Application server port
```

## Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── me/medev/itsupportticket/
│   │   │       ├── client/     # Swing client
│   │   │       ├── controller/ # REST controllers
│   │   │       ├── model/      # Domain models
│   │   │       ├── repository/ # Data access
│   │   │       └── service/    # Business logic
│   │   └── resources/          # Configuration files
│   └── test/                   # Test cases
├── docs/                       # Documentation
```


## API Documentation

Once the application is running, access the OpenAPI documentation at: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## Testing

Run the test suite:

```bash
mvn test
```