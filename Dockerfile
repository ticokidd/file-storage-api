FROM openjdk:8-jdk-alpine
RUN addgroup -S demoapi && adduser -S demoapi -G demoapi
USER demoapi:demoapi
COPY target/file-storage-api-0.1.0.jar api.jar
ENTRYPOINT ["java", "-jar", "/api.jar"]
