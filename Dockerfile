FROM maven:3.8.3-openjdk-11-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:11

ARG JAR_FILE=/app/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]