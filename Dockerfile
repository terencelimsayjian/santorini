# Build
FROM gradle:6.7.1-jdk11 as builder
WORKDIR /app
COPY . ./
RUN ./gradlew :santoriniserver:bootJar --no-daemon

# Run
FROM openjdk:11.0-jre-slim
COPY --from=builder /app/backend/build/libs/santoriniserver.jar .

EXPOSE 8080
ENTRYPOINT java -jar santoriniserver.jar --spring.profiles.active=prod
