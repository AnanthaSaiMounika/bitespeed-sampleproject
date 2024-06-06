# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /bitespeed-sampleproject

# Copy the project files to the working directory
COPY . .

# Install Maven Wrapper
RUN apt-get update && apt-get install -y curl \
    && curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.6.6/maven-wrapper-0.6.6.jar

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Package the application using the Maven Wrapper
RUN ./mvnw clean package

# Run the application
CMD ["java", "-cp", ".mvn/wrapper/maven-wrapper.jar:target/bitespeed-sampleproject.jar", "org.apache.maven.wrapper.MavenWrapperMain"]
