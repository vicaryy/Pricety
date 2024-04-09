FROM maven:3-eclipse-temurin-21-alpine as build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
COPY --from=build /target/*.jar dockerTest.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","dockerTest.jar"]