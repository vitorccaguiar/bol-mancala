FROM maven:3.5-jdk-11
COPY ./pom.xml /usr/src/app/api/pom.xml
WORKDIR /usr/src/app/api/
# Build all dependencies for offline use
RUN mvn dependency:go-offline -B
COPY ./src /usr/src/app/api/src
RUN ls -la
RUN mvn package
CMD ["java", "-jar", "./target/api.jar"]
EXPOSE 8080