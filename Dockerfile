# Use an official OpenJDK runtime as a parent image
FROM openjdk:17.0.2

# Set the working directory in the container
WORKDIR /bitespeed-sampleproject

# Copy the project files to the working directory
COPY . .

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Package the application using the Maven Wrapper
RUN ./mvnw clean install


EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/bitespeed-sampleproject.jar"]

# Make the JAR file executable
RUN chmod +x ./target/bitespeed-sampleproject.jar
