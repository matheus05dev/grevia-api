# Guia de Execução Local — Grevia API

Passo a passo para rodar o banco de dados e a API no seu computador.

---

## 🛠️ Pré-requisitos

| Requisito | Versão | Link |
|---|---|---|
| Java JDK | 21 | [Download (Temurin)](https://adoptium.net/temurin/releases?version=21) |
| Docker + Docker Compose | Recente | [Download](https://www.docker.com/products/docker-desktop/) |
| IDE *(opcional)* | — | IntelliJ IDEA, VS Code ou Eclipse |

> **Nota:** Não é necessário instalar o Maven — o projeto inclui o Maven Wrapper (`mvnw`).

---

## 🐳 Passo 1: Subindo o Banco de Dados

A API utiliza MySQL 8.0 via Docker. Abra um terminal na raiz do projeto e execute:

```bash
docker-compose up -d
```

Isso irá:
- Baixar a imagem do MySQL 8.0 (na primeira vez)
- Criar o banco `greviadb` automaticamente
- Expor na porta `3306`

Para verificar se está rodando:
```bash
docker-compose ps
```

---

## ⚙️ Passo 2: Variáveis de Ambiente

A aplicação utiliza variáveis de ambiente para serviços externos. Para **desenvolvimento local**, os valores default do `application.properties` funcionam para o banco de dados.

Para funcionalidades de **e-mail**, configure as variáveis abaixo (via variáveis de ambiente ou no `application.properties`):

| Variável | Descrição | Obrigatória no Dev? |
|---|---|---|
| `SPRING_MAIL_USERNAME` | E-mail para envio (Gmail) | Só se testar e-mail |
| `SPRING_MAIL_PASSWORD` | Senha de App do email (Gmail) | Só se testar e-mail |

---

## 🏃 Passo 3: Executando a API

**No Windows (PowerShell):**
```bash
.\mvnw.cmd spring-boot:run
```

**No Linux / macOS:**
```bash
./mvnw spring-boot:run
```

Aguarde até ver a mensagem:
```
Started GreviaApplication in X seconds
```

A API estará disponível em **http://localhost:8080**.

---

## 📚 Passo 4: Acessando o Swagger UI

Com a API rodando, abra no navegador:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Primeiros passos no Swagger:

1. Execute `POST /api/auth/register` para criar sua conta
2. Execute `POST /api/auth/login` para obter o token JWT
3. Clique em **"Authorize"** (cadeado no topo) e cole: `Bearer <seu-token>`
4. Use os demais endpoints normalmente

---

## 🐳 Alternativa: Rodar Tudo com Docker

Se não quiser instalar o Java, rode a API inteira via Docker Compose:

```bash
docker-compose up -d --build
```

Isso irá buildar a API dentro de um container Maven e executar com JRE Alpine.

---

## 🛑 Parando os Serviços

| Ação | Comando |
|---|---|
| Parar a API (terminal) | `Ctrl + C` |
| Parar containers Docker | `docker-compose down` |
| Parar e remover volumes | `docker-compose down -v` |
