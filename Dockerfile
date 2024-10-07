#FROM maven:3.9.8-amazoncorretto-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn package -DskipTests
#FROM amazoncorretto:21.0.4
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM maven:3.9.8-amazoncorretto-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#CMD ["mvn", "spring-boot:run"]

## Su dung base image tu openjdk
#FROM openjdk:17
#
## Dat bien moi truong cho JAR file
#ARG JAR_FILE=target/*.jar
#
## Copy file JAR vao container
#COPY ${JAR_FILE} electronic-ecommerce.jar
#
## Chay ung dung Spring Boot
#ENTRYPOINT ["java", "-jar", "electronic-ecommerce.jar"]
#
## Mo cong 8080 de lang nghe ket noi HTTP
#EXPOSE 8080