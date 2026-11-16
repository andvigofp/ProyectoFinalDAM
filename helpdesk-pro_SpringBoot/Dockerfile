# ==========================================================
# Dockerfile - Backend HelpDesk Pro (Spring Boot + Java 25)
# Compilación en dos etapas: la imagen final solo lleva el JAR.
# ==========================================================

# ---- Etapa 1: compilación con Maven ----
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Se copia primero el pom.xml para aprovechar la caché de Docker:
# si no cambian las dependencias, no se vuelven a descargar.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---- Etapa 2: ejecución (imagen ligera solo con el JRE) ----
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
