# Perform the extraction in a separate builder container
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /builder
# This points to the built jar file in the target folder
# Adjust this to 'target/*.jar' if you're using Maven
ARG JAR_FILE=build/libs/*.jar
# Copy the jar file to the working directory and rename it to application.jar
COPY ${JAR_FILE} application.jar
# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# This is the runtime container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /application
# Copy the extracted jar contents from the builder container into the working directory in the runtime container
# Every copy step creates a new docker layer
# This allows docker to only pull the changes it really needs
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
# Config container to run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Start the application jar - this is not the uber jar used by the builder
# This jar only contains application code and references to the extracted jar files
# This layout is efficient to start up and CDS friendly
ENTRYPOINT ["java", "-jar", "application.jar"]