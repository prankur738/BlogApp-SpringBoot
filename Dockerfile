# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/blogApplication-0.0.1-SNAPSHOT.jar .

# Expose the port your app runs on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "blogApplication-0.0.1-SNAPSHOT.jar"]

