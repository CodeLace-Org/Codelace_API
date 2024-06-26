FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/codelace-0.0.1.jar
COPY ${JAR_FILE} codelace.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "codelace.jar"]
