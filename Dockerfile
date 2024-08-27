FROM gradle:jdk-21-and-22-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle clean build --rerun-tasks --no-build-cache

#ENTRYPOINT ["gradlew", "bootRun", "--args=\"--spring.data.mongodb.host=mongo --spring.data.redis.host=redis\""]

FROM amazoncorretto:21-alpine-jdk

RUN mkdir /app && addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /home/gradle/src/build/libs/*.jar /app/application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/application.jar", "--spring.data.mongodb.host=mongo", "--spring.data.redis.host=redis"]