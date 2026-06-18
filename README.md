<div align="center">
  <h1>🌱 Grevia API</h1>
  <p><strong>Plataforma de gerenciamento inteligente de plantas com recomendações de cuidados e gamificação.</strong></p>
  
  ![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.11-6DB33F?style=for-the-badge&logo=spring)
  ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)
  ![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
  ![JWT](https://img.shields.io/badge/JWT-Security-black?style=for-the-badge&logo=json-web-tokens)

</div>

---

## 📖 Sobre o Projeto

> 🎓 **Nota:** Este é um projeto acadêmico desenvolvido para a Universidade Paulista (UNIP).

A **Grevia** nasceu para descomplicar o cultivo de plantas, oferecendo recomendações inteligentes e planos de cuidados personalizados. O motor analisa o tipo de terreno e de planta para sugerir as melhores espécies, enquanto os planos de cuidado gerenciam frequências de rega, registros de manutenção e muito mais. Tudo isso atrelado a um sistema de **gamificação** que recompensa os usuários que cuidam bem de suas plantas!

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação JWT**: Registro, login e recuperação de senha segura.
- 🌿 **Gestão de Plantas**: CRUD completo de plantas do usuário.
- 🧠 **Recomendações Inteligentes**: Motor que sugere espécies baseado em características do terreno e tipo.
- 📅 **Planos de Cuidado**: Cronogramas personalizados com frequências dinâmicas.
- 📜 **Registros de Cuidado**: Histórico de manutenções realizadas em cada planta.
- 🏆 **Gamificação**: Sistema de pontuação, streaks (ofensivas) e níveis de jardineiro.
- 🛡️ **Rate Limiting**: Proteção de endpoints contra abusos utilizando Bucket4j.
- 🗣️ **Feedback Comunitário**: Envio de feedbacks e sugestões pelos usuários.

---

## 🛠️ Stack Tecnológica

| Categoria | Tecnologia |
|---|---|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 3.5.11 (Web, Security, Data JPA, Mail, Validation, Actuator) |
| **Banco de Dados** | MySQL 8.0 |
| **Segurança** | Spring Security + JWT (JJWT 0.12.3) |
| **Mapeamento/Boilerplate**| MapStruct 1.5.5 + Lombok |
| **E-mail** | Spring Mail + Resend SDK 3.1.0 |
| **Proteção da API** | Bucket4j 8.10.1 (Rate Limiting) |
| **Documentação** | Springdoc OpenAPI 2.8.15 (Swagger UI) |
| **Testes** | JUnit 5, Mockito, MockMvc |
| **Infraestrutura** | Docker + Docker Compose |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
Antes de começar, certifique-se de ter instalado em sua máquina:
- **Java 21**
- **Docker e Docker Compose** (Opcional, mas recomendado para o banco de dados)
- **Maven** (Caso não utilize o Wrapper do projeto)

### 1. Clonando o Repositório
```bash
git clone https://github.com/matheus05dev/grevia-api.git
cd grevia-api
```

### 2. Configurando as Variáveis de Ambiente
Crie um arquivo `.env` na raiz do projeto (ou configure as variáveis no seu sistema) baseado nas seguintes chaves:
```env
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=greviadb
MYSQLUSER=user
MYSQLPASSWORD=admin
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=seu_email@gmail.com
SPRING_MAIL_PASSWORD=sua_senha_de_app
```

### 3. Rodando com Docker (Recomendado)
Para subir o banco de dados e a aplicação simultaneamente:
```bash
docker-compose up -d
```

### 4. Rodando Localmente (via Maven)
Caso prefira rodar apenas o banco no Docker e a aplicação na sua IDE ou terminal:
```bash
docker-compose up -d db
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 📚 Documentação da API

A documentação interativa (Swagger) pode ser acessada após iniciar o projeto em:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Resumo dos Principais Endpoints

| Recurso | Rota | Método | Descrição |
|---|---|---|---|
| **Auth** | `/api/auth/register` | `POST` | Cria uma nova conta |
| **Auth** | `/api/auth/login` | `POST` | Retorna o token JWT |
| **Usuário** | `/api/users/me` | `GET` | Retorna o perfil do usuário logado |
| **Plantas** | `/api/plants` | `POST` | Cadastra uma nova planta |
| **Cuidados** | `/api/plants/{id}/cares/{id}/complete`| `POST` | Marca um cuidado como concluído e ganha pontos |

*Para mais detalhes, consulte os arquivos na pasta `/docs`.*

---

## 🎮 Sistema de Gamificação

A Grevia incentiva o cuidado com as plantas através de um sistema de recompensas!

**Pontuação por Cuidado:**
- **Base:** +10 pontos
- **No dia exato:** +15 pontos adicionais
- **Multiplicador de Ofensiva (Streak):** `currentStreak × 2` (manter a consistência aumenta os pontos ganhos)

**Níveis de Jardineiro:**
Seu título evolui conforme seu engajamento:
- 🌱 **Nível 1:** Jardineiro Iniciante (0 pts)
- 🌿 **Nível 2:** Jardineiro Aprendiz (50 pts)
- 🌻 **Nível 3:** Jardineiro Dedicado (200 pts)
- 🌳 **Nível 4:** Mestre Botânico (500 pts)

---

## 🤝 Como Contribuir

Como este é um projeto open-source, contribuições são muito bem-vindas!

1. Faça um **Fork** do projeto
2. Crie uma **Branch** para sua feature (`git checkout -b feature/MinhaFeature`)
3. Faça o **Commit** de suas alterações (`git commit -m 'feat: adicionando nova funcionalidade'`)
4. Faça o **Push** para a branch (`git push origin feature/MinhaFeature`)
5. Abra um **Pull Request**

---

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">
  Feito com ❤️ por <a href="https://github.com/matheus05dev">Matheus Nunes</a>.
</div>
