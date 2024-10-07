# Base image
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw package

FROM eclipse-temurin:21-jre AS prod

WORKDIR /app

COPY --from=builder /app/target/java-intellij-idea-and-maven-1.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

CMD ["--help"]
