FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir -p /app/build/db
COPY --from=build /home/gradle/src/build/libs/*.jar /app/logging-service.jar
ENTRYPOINT ["java","-jar","/app/logging-service.jar"]