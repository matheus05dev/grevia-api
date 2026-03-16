# Guia de Execução Local - Grevia API

Este guia ensina como executar, debugar e testar o projeto localmente.

## 🛠️ Pré-requisitos
Antes de começar, certifique-se de ter os seguintes itens na sua máquina:
1. **Java Development Kit (JDK) 21**. [Download (Temurin / Adoptium)](https://adoptium.net/temurin/releases?version=21).
2. **Docker e Docker Compose**, para rodar o banco de dados via container sem precisar instalar banco fixamente.
3. *(Opcional, porém recomendado)* Uma IDE especializada para Java (IntelliJ IDEA, VS Code com Packs da Spring, Eclipse).

## 🐳 Passo 1: Configurando o Banco de Dados

A API armazena seus dados em uma instância do MySQL. 

1. Abra um terminal na pasta raiz do projeto de sua gerência.
2. Inicie os contêineres:
   ```bash
   docker-compose up -d
   ```
*(Alternativa Exclusiva para Windows: A raiz do projeto conta com os utilitários de sistema `Startardbs.bat`. Esse script irá iniciar as execuções do daemon do Docker se houver necessidade na máquina a tempo do Boot).*

## 🏃 Passo 2: Executando a API 

Com o **Maven Wrapper (`mvnw`)** acoplado ao projeto, não vamos precisar instalar o "Maven" local.

Abra o respectivo terminal (por exemplo, Powershell no Windows, ou Konsole/Zsh no Linux) e execute:

**No Linux / macOS**:
```bash
./mvnw spring-boot:run
```

**No Windows**:
```bash
.\mvnw.cmd spring-boot:run
```

O ambiente será iniciado (normalmente exposto na porta padrão `8080`), fazendo varreduras e criando dependências.

## 📚 Passo 3: Conheça os Endpoints Com Swagger UI

Ao receber na tela informando a conclusão positiva (`Tomcat started on port 8080` ou semelhante), você pode navegar até:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)** *(Verifique as prop-files caso altere de porta)*

Lá haverá uma tela visual para debugar as rotas (OpenAPI):
- Primeiro execute um `/api/auth/register` criando os credenciamentos da sua conta.
- Faça o Login para pegar o Access Token e coloque ali onde avisa "Authorize" para ser fixado global.
- Use do ambiente livremente.

## 🛑 Como parar a Execução
- Para a aplicação Spring: De forma clássica o `Ctrl + C` encerrará o processo de Thread anexado.
- Para matar as instâncias do Docker, desative o compose com:
  ```bash
  docker-compose down
  ```
