FROM openjdk:8-jdk-alpine
ADD target/demo.jar demo.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","demo.jar"]