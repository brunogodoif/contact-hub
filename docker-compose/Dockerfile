FROM maven:3.8.4 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/contact-hub.jar /app/contact-hub.jar
EXPOSE 9092
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "contact-hub.jar"]