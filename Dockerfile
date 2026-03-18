# Estágio 1: Build da aplicação (só precisa do código fonte)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiando apenas o pom.xml e scripts do maven para fazer cache das dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiando o restante do código fonte
COPY src ./src
# Fazendo o build do JAR ignorando os testes unitários (para ir mais rápido no OCI)
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final enxuta (só para rodar, sem o maven)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR do estágio 1 para a raiz do container de execução
COPY --from=build /app/target/*.jar grevia-api.jar

# Expõe a porta que a aplicação roda
EXPOSE 8080

# Comando para iniciar o servidor Spring Boot
ENTRYPOINT ["java", "-jar", "grevia-api.jar"]
