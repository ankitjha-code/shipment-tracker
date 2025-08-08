# --- Stage 1: Build the application ---
# Use an official Maven image that includes Java 21 to build our project
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's layer caching.
# This way, dependencies are only re-downloaded if pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping the tests
RUN mvn package -DskipTests

# --- Stage 2: Create the final, lightweight container ---
# Use a lightweight Java Runtime image, which is smaller than a full JDK
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the compiled .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]