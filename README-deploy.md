# Guia de Deploy - Oracle VM (OCI) / Ubuntu

Este guia explica como colocar a sua API do Grevia online usando Docker num servidor vazio (como os gratuitos da Oracle Cloud).

## Pré-requisitos na VM

1. Acesse sua VM via SSH.
2. Atualize o sistema: `sudo apt update && sudo apt upgrade -y`
3. Instale o Docker e Docker Compose:
   ```bash
   sudo apt install docker.io docker-compose -y
   sudo systemctl enable docker
   sudo systemctl start docker
   sudo usermod -aG docker $USER
   ```
   *Dica: Feche a conexão SSH e entre de novo para o `usermod` fazer efeito sem precisar de sudo no docker.*

## 1. Prepare o Repositório

Faça o clone do seu projeto para a VM:
```bash
git clone https://github.com/SeuUsuario/GreviaAPI.git
cd GreviaAPI
```

## 2. Configure as Variáveis de Ambiente

Crie um arquivo chamado `.env` na raiz do projeto dentro da VM para não expor senhas no repositório:
```bash
nano .env
```

Cole o seguinte conteúdo (ajustando com suas senhas reais):
```env
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=contactgrevia@gmail.com
SPRING_MAIL_PASSWORD=sua_senha_de_app_gerada_no_google
```
Salve e saia (`CTRL+O`, `Enter`, `CTRL+X`). O `docker-compose` lê esse arquivo automaticamente!

## 3. Subir a Aplicação

Rode o comando mágico abaixo para construir a imagem do Spring Boot usando o nosso `Dockerfile` de Multi-Stage (super leve e não precisa de Java instalado na VM!) e subir tudo orquestrado:

```bash
docker-compose -f docker-compose.prod.yml up -d --build
```

**O que vai acontecer?**
- O Banco de Dados (MySQL) vai iniciar e criar o schema `greviadb`.
- O Maven vai baixar as dependências e empacotar sua API dentro do container de _build_.
- O Spring Boot vai iniciar em seguida, conectando-se automaticamente ao banco.

## 4. Como Conferir se Está Funcionando

Veja os logs da API em tempo real:
```bash
docker logs -f grevia-api
```

Se aparecer "Started DemoApplication in X seconds", sua API já está acessível pela porta 8080 do IP público da sua VM! 🚀

> **Aviso da Oracle Cloud (OCI):** Não se esqueça de ir no painel Web da Oracle Cloud (VCN > Subnets > Security Lists) e liberar o "Ingress Rule" para abrir a porta **8080** no firewall, senão as requisições não chegam. Se estiver usando Iptables no Ubuntu, também precisa liberar lá: `sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8080 -j ACCEPT` e `sudo netfilter-persistent save`.
