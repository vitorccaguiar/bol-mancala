# Bol.com Mancala

---
### Requirements

**With Docker**
- Only need to install Docker

**Without Docker**
- JDK 8
- Angular CLI
- MongoDB

---

### Set up

**With Docker**
- Run the command 'docker-compose up --build'

**Without Docker**

- Backend
    - Uncomment the lines from 'api/src/main/resources/application.properties' in order to MongoDB work locally (the mongo client should be executing)
    - Generate the JAR file through maven and run it (api folder). It is also possible by pressing play in your IDE like Eclipse, VS Code, so on.

- Frontend

    - Run npm install inside the client folder
    - Run 'ng s' inside the client folder

---
### Technologies

- **Server Side Language:** Java 8
- **Server framework:** Spring Boot, Spring MVC, Spring Websocket, Spring Data, JUnit, Mockito
- **Database:** MongoDB
- **Client framework:** Angular 8
- **Others:** Docker, Maven

### Screenshots

---
**Register:**

![](./screenshots/Register.png)

---
**Lobby:**

![](./screenshots/Lobby.png)

---
**Match:**

![](./screenshots/Match.png)
