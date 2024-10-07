# Base image
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw package

FROM eclipse-temurin:21-jre AS prod

ARG app_name=app

WORKDIR /app

COPY --from=builder /app/target/java-intellij-idea-and-maven-1.0-SNAPSHOT.jar /app/${app_name}.jar

