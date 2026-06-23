# Build from repository root:
#   docker build -f worker/Dockerfile -t worker .
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

RUN mvn -f worker/pom.xml package -DskipTests -B -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/worker/target/worker-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
