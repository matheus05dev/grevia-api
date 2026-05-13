# Guia de Execução Local — Grevia API

Passo a passo para configurar seu ambiente e rodar a API Grevia no seu computador.

---

## 🛠️ Pré-requisitos

| Requisito | Versão | Link |
|---|---|---|
| Java JDK | 21 | [Download (Temurin)](https://adoptium.net/temurin/releases?version=21) |
| Docker + Docker Compose | Recente | [Download](https://www.docker.com/products/docker-desktop/) |
| IDE *(recomendado)* | — | IntelliJ IDEA, VS Code ou Cursor |

---

## 🐳 Passo 1: Banco de Dados

A API utiliza MySQL 8.0. A maneira mais fácil de rodar é via Docker:

```bash
docker-compose up -d
```

Isso irá subir um container MySQL na porta `3306` com as credenciais padrão definidas no `docker-compose.yml`.

---

## ⚙️ Passo 2: Configuração do Ambiente

O projeto utiliza variáveis de ambiente e arquivos de propriedades externos para segredos.

### 1. Variáveis de Ambiente (.env)
Copie o arquivo de exemplo e preencha com seus dados:
```bash
cp .env.example .env
```
*(No Windows use `copy .env.example .env`)*

### 2. Usuário Administrador Inicial
Para que a API crie um usuário admin automaticamente no primeiro boot:
1. Copie o arquivo `admin-config.properties.example` para `admin-config.properties`.
2. Altere as credenciais conforme desejado.

---

## 🏃 Passo 3: Executando a API

Utilize o Maven Wrapper incluso no projeto:

**Windows (PowerShell):**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

A API estará disponível em: **http://localhost:8080**  
Swagger UI: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 🧪 Passo 4: Executando Testes

É altamente recomendado rodar os testes antes de realizar qualquer alteração:

```bash
# Rodar todos os testes
./mvnw test

# Rodar apenas testes unitários
./mvnw test -Dtest="*ServiceTest"
```

---

## 📚 Documentação Interativa (Swagger)

1. Com a API rodando, acesse o link do Swagger.
2. Para testar endpoints protegidos:
    - Use `POST /api/auth/login` com as credenciais do admin (definidas no passo 2).
    - Copie o `token` retornado.
    - Clique em **Authorize** no topo da página.
    - Digite `Bearer SEU_TOKEN_AQUI`.

---

## 🐳 Alternativa: Docker Full Stack

Para rodar o banco + API em containers isolados sem instalar o Java localmente:

```bash
docker-compose up -d --build
```

---

## 🛑 Comandos Úteis

| Ação | Comando |
|---|---|
| Parar containers | `docker-compose stop` |
| Remover containers e rede | `docker-compose down` |
| Limpar banco de dados (volumes) | `docker-compose down -v` |
| Re-buildar a API | `./mvnw clean compile` |
