#Pull the image
FROM maven:3.9.9-eclipse-temurin-21 AS builder

#make directory for this builder
WORKDIR /app


#Copy maven files first as these are important for building this also serve purpose of caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Giving access to maven wrapper to execute
RUN chmod +x mvnw

#Download dependencies
RUN ./mvnw dependency:go-offline


#Copy src
COPY src src

#Build the image
RUN ./mvnw clean package -DskipTests


# Now building application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated jar from the builder stage

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]