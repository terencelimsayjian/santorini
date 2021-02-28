# Build Server
FROM gradle:6.7.1-jdk11 as builder1
WORKDIR /app
COPY backend ./backend
WORKDIR backend
RUN ./gradlew bootJar --no-daemon

# Build Client
FROM node:14.16.0-alpine3.10 as builder2
WORKDIR /app
COPY frontend ./frontend
WORKDIR frontend
RUN npm run build

# Run
FROM openjdk:11.0-jre-slim

COPY --from=builder1 /app/backend/build/libs/santoriniserver.jar .
COPY --from=builder2 /app/frontend/build ./resources/static

EXPOSE 8080
ENTRYPOINT java -jar santoriniserver.jar --spring.profiles.active=prod
