FROM gradle:8.4-focal as build

WORKDIR /workspace

COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
COPY settings.gradle.kts ./settings.gradle.kts

RUN gradle clean build -x test

FROM bellsoft/liberica-openjdk-debian:17

WORKDIR /app

COPY --from=build /workspace/build/libs/eureka-server-1.0.jar ./eureka-server.jar

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]