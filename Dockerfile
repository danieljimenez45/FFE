# --- Build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

RUN apk add --no-cache bash

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests -B

# --- Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S ffe && adduser -S ffe -G ffe \
    && mkdir -p /app/uploads/peliculas \
    && chown -R ffe:ffe /app

COPY --from=build --chown=ffe:ffe /app/target/*.jar app.jar

USER ffe
EXPOSE 3010

ENV PORT=3010
ENTRYPOINT ["java", "-jar", "app.jar"]
