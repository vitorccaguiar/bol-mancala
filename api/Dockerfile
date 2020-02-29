# Select image
FROM maven:3.5-jdk-11

# Copy the project files
COPY ./pom.xml ./pom.xml

# Build all dependencies for offline use
RUN mvn dependency:go-offline -B

# Copy other files
COPY ./src ./src

# Build for release
RUN mvn package

CMD ["java", "-jar", "./target/demo.jar"]