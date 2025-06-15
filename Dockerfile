FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/hiteen-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
