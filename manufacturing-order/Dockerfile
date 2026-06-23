# Build from repository root:
#   docker build -f manufacturing-order/Dockerfile -t manufacturing-order .
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

RUN mvn -f common/pom.xml install -DskipTests -B -q && \
    mvn -f manufacturing-order/pom.xml package -DskipTests -B -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/manufacturing-order/target/manufacturing-order-*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
