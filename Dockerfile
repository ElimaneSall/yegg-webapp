# ==========================================
# Étape 1 : Construction (Build)
# ==========================================
# Utilisez l'image officielle Java 17 (modifiez en 21 si votre JHipster utilise Java 21)
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Copier le wrapper Maven et les fichiers de dépendances
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY package.json .
COPY package-lock.json .

# Donner les droits d'exécution au wrapper
RUN chmod +x ./mvnw

# Copier le reste du code source
COPY . .

# Compiler le projet en mode production (Angular + Spring Boot) en ignorant les tests pour aller plus vite
RUN ./mvnw -Pprod clean package -DskipTests

# ==========================================
# Étape 2 : Image finale d'exécution (Run)
# ==========================================
# On utilise une image JRE (Java Runtime Environment) plus légère
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Cloud Run écoute par défaut sur le port 8080
ENV PORT=8080
EXPOSE $PORT

# Copier uniquement le fichier .jar final généré à l'étape 1
# JHipster génère généralement le fichier dans le dossier target/
COPY --from=builder /build/target/*.jar app.jar

# Commande de démarrage de l'application avec le profil production
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=prod"]
