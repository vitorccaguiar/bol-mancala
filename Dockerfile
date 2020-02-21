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

CMD [ "sh", "-c", "java -Dserver.port=8086 -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom -jar ./target/demo.jar" ]
# CMD ["java -Dserver.port=8086", "-jar", "./target/demo.jar"]