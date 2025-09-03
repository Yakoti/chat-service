# Use a lightweight JDK image to run the application
FROM openjdk:21-jdk-slim


# Set the working directory
WORKDIR /app

# Copy the JAR file from the target directory
COPY target/chat-service-0.0.1-SNAPSHOT.jar /app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
