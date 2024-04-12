FROM maven:3-eclipse-temurin-21-alpine as build
COPY . .
RUN mvn clean package -DskipTests

FROM mcr.microsoft.com/playwright/java:v1.43.0-jammy-amd64
RUN mkdir /apk
WORKDIR /apk
COPY --from=build /target/*.jar pricety.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","pricety.jar"]