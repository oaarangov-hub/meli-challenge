# Etapa de compilación
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and download the project dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Etapa final
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application jar file from the build stage
COPY --from=build /app/target/challenge-1.0.0.jar app.jar

# Expose the port on which the app will run
EXPOSE 9092

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
