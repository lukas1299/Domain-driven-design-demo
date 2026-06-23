FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

RUN mvn -f common/pom.xml install -DskipTests -B -q && \
    mvn -f customer-order/pom.xml package -DskipTests -B -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/customer-order/target/customer-order-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
