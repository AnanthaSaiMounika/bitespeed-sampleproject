# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /bitespeed-sampleproject

# Copy the project files to the working directory
COPY . .

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Package the application using the Maven Wrapper
RUN ./mvnw clean package

# Run the application
CMD ["java", "-jar", "target/bitespeed-sampleproject.jar"]
