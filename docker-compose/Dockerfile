FROM openjdk:17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM build
WORKDIR /app
COPY --from=build /app/target/contact-hub.jar /app/contact-hub.jar
EXPOSE 9092
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "contact-hub.jar"]
