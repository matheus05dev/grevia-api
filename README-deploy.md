# Guia de Deploy — Oracle Cloud (OCI) / Ubuntu

Como colocar a Grevia API online usando Docker em um servidor Ubuntu (como os gratuitos da Oracle Cloud).

---

## 🛠️ Pré-requisitos na VM

1. Acesse sua VM via SSH
2. Atualize o sistema:
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```
3. Instale Docker e Docker Compose:
   ```bash
   sudo apt install docker.io docker-compose -y
   sudo systemctl enable docker
   sudo systemctl start docker
   sudo usermod -aG docker $USER
   ```
   > **Dica:** Desconecte e reconecte o SSH para o grupo `docker` fazer efeito.

---

## 1. Clone o Repositório

```bash
git clone https://github.com/SeuUsuario/GreviaAPI.git
cd GreviaAPI
```

---

## 2. Configure as Variáveis de Ambiente

Crie o arquivo `.env` na raiz do projeto:

```bash
nano .env
```

Cole o seguinte conteúdo (substituindo pelos valores reais):

```env
# Banco de Dados
DB_NAME=greviadb
DB_USER=grevia_user
DB_PASSWORD=sua_senha_do_banco
DB_ROOT_PASSWORD=sua_senha_root

# E-mail (Gmail)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=contactgrevia@gmail.com
SPRING_MAIL_PASSWORD=sua_senha_de_app_do_google

# Resend (E-mail transacional)
RESEND_API_KEY=re_SuaChaveAqui

# Cloudinary (Upload de imagens)
CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=seu_api_secret
```

Salve e saia (`CTRL+O` → `Enter` → `CTRL+X`).

> **Importante:** O `docker-compose.prod.yml` lê esse `.env` automaticamente.

---

## 3. Suba a Aplicação

```bash
docker-compose -f docker-compose.prod.yml up -d --build
```

**O que vai acontecer:**
1. O MySQL inicia e cria o schema `greviadb`
2. O Maven baixa dependências e empacota a API (multi-stage build)
3. O Spring Boot inicia e conecta ao banco automaticamente

---

## 4. Verificando o Funcionamento

Acompanhe os logs em tempo real:

```bash
docker logs -f grevia-api
```

Se aparecer `Started GreviaApplication in X seconds`, a API está acessível na porta **8080** do IP público da VM. 🚀

### Testando:

```bash
curl http://localhost:8080/actuator/health
# Deve retornar: {"status":"UP", ...}
```

---

## 5. Liberando a Porta (Firewall)

### Oracle Cloud (OCI)

Vá no painel Web: **VCN → Subnets → Security Lists** → Adicione uma **Ingress Rule**:
- **Source CIDR:** `0.0.0.0/0`
- **Protocol:** TCP
- **Destination Port:** `8080`

### Firewall do Ubuntu (iptables)

```bash
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8080 -j ACCEPT
sudo netfilter-persistent save
```

---

## 6. Comandos Úteis

| Ação | Comando |
|---|---|
| Ver logs | `docker logs -f grevia-api` |
| Parar tudo | `docker-compose -f docker-compose.prod.yml down` |
| Reiniciar | `docker-compose -f docker-compose.prod.yml restart` |
| Rebuild e subir | `docker-compose -f docker-compose.prod.yml up -d --build` |
| Ver containers | `docker ps` |
| Ver uso de disco | `docker system df` |
