FROM openjdk:21-jdk-slim
ARG JAR_FILE=codelace/target/codelace-0.0.1.jar
COPY ${JAR_FILE} codelace.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "codelace.jar"]
