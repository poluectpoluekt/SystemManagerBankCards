FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app
COPY . /app/.
RUN mvn clean package
FROM openjdk:17-jdk-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/target/SystemManagerBankCards.jar /app/app.jar
RUN chown -R appuser:appgroup /app
USER appuser
ENTRYPOINT ["java", "-jar", "/app/app.jar"]