# ==========================================
# Étape 1 : Build
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Copier tout directement
COPY . .

# Donner les droits APRÈS copie complète
RUN chmod +x mvnw

# Build
RUN ./mvnw -Pprod clean package -DskipTests

# ==========================================
# Étape 2 : Run
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV PORT=8080
EXPOSE 8080

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=prod"]
