# ===============================
# Stage 1: Build
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ===============================
# Stage 2: Runtime
# ===============================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy WAR (NOT jar)
COPY --from=build /app/target/*.war /app/app.war

# Verify file exists
RUN ls -lh /app

EXPOSE 4040

ENV SPRING_PROFILES_ACTIVE=docker
ENV ADMIN_PASSWORD=admin123

ENTRYPOINT ["java", "-jar", "/app/app.war"]
