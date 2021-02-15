# Build
FROM gradle:6.7.1-jdk11 as builder

WORKDIR /app
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY src ./src
COPY gradle ./gradle
RUN ./gradlew bootJar --profile --no-daemon
RUN cd build/libs

# Run
FROM openjdk:11.0-jre-slim

COPY --from=builder /app/build/libs/santorini.jar .
EXPOSE 8080

ENTRYPOINT java -jar santorini.jar
