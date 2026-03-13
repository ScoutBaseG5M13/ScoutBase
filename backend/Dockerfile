# Stage 1: Build the app
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline
COPY . .
# Running the exact build command you requested
RUN mvn clean install -DskipTests


# Stage 2: Create the final image
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]