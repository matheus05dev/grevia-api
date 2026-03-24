#!/bin/bash

# ==========================================================
# Script de Deploy Automático para Oracle Cloud (Ubuntu VM)
# ==========================================================

echo ">>> Iniciando preparativos para o Deploy na Oracle VM..."

# 1. Atualizando pacotes
echo ">>> Atualizando pacotes do Ubuntu..."
sudo apt update && sudo apt upgrade -y

# 2. Instalando Docker e Docker Compose (via repositório oficial Docker)
if ! command -v docker &> /dev/null
then
    echo ">>> Instalando o Docker..."
    sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
    
    # Adiciona o usuário atual ao grupo docker para não precisar usar sudo toda hora
    sudo usermod -aG docker $USER
    echo ">>> Docker instalado! Atenção: Você pode precisar relogar para poder executar o comando docker sem o sudo."
else
    echo ">>> Docker já está instalado."
fi

# 3. Abrindo portas no firewall local do Ubuntu (Iptables / UFW)
echo ">>> Configurando portas no Iptables (Oracle padrão)..."
# Oracle Cloud Ubuntu usa iptables por padrão para bloquear a maioria das portas. Vamos abrir a porta 8080.
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8080 -j ACCEPT
sudo netfilter-persistent save

echo ">>> IMPORTANTE: Lembre-se também de abrir a porta 8080 nas regras de Ingress (Security List) do painel web da Oracle Cloud."

# 4. Criando pasta de Uploads
echo ">>> Criando pasta de uploads local..."
mkdir -p uploads

# 5. Aviso finais
if [ ! -f .env ]; then
    echo "=========================================================="
    echo "⚠️  ATENÇÃO: O arquivo .env não foi listado neste diretório!"
    echo "1. Crie uma cópia do exemplo executando: cp .env.example .env"
    echo "2. Edite o arquivo gerado: nano .env (coloque senhas fortes!)"
    echo "3. Depois diso, inicie a aplicação com:"
    echo "   sudo docker compose -f docker-compose.prod.yml up -d --build"
    echo "=========================================================="
else
    echo ">>> O arquivo .env foi encontrado. Subindo a versão de produção..."
    sudo docker compose -f docker-compose.prod.yml up -d --build
fi

echo ">>> Script de configuração finalizado!"
