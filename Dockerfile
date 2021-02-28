# Build Server
FROM gradle:6.7.1-jdk11 as builder1
COPY backend ./backend
RUN ls
WORKDIR backend
RUN ./gradlew bootJar

# Build Client
FROM node:14.16.0-alpine3.10 as builder2
COPY frontend ./frontend
WORKDIR frontend
RUN npm run build

# Run
FROM openjdk:11.0-jre-slim

COPY --from=builder1 /backend/build/libs/santoriniserver.jar .
COPY --from=builder2 /frontend/build ./resources/static

EXPOSE 8080
ENTRYPOINT java -jar santoriniserver.jar --spring.profiles.active=prod
