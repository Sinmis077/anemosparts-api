FROM gradle:9-jdk21-alpine AS build

LABEL authors="Ioannis Panagi"

WORKDIR /app

COPY gradlew .
COPY gradle gradle/
COPY build.gradle.kts settings.gradle.kts ./

RUN gradle dependencies --no-daemon || true

COPY src src/

RUN gradle build --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]