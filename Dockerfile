# ==========================================
# Étape 1 : Build
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Copier tout
COPY . .

# Donner les droits APRÈS copie complète
RUN chmod +x mvnw

# Build Maven avec profile prod et skip tests
RUN ./mvnw -Pprod clean package -Dmaven.test.skip=true -DskipTests

# ==========================================
# Étape 2 : Run
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Port exposé pour Cloud Run
ENV PORT=8080
EXPOSE 8080

# Copier le jar final du build
COPY --from=builder /build/target/*.jar app.jar

# EntryPoint
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=prod"]
