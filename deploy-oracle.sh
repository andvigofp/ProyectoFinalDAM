#!/bin/bash
# ==========================================================
# HelpDesk Pro - Preparación de la VM de Oracle Cloud
# Ejecutar DENTRO de la VM Ubuntu (después de conectar por SSH):
#
#   chmod +x deploy-oracle.sh
#   ./deploy-oracle.sh
#
# Instala Docker, abre el puerto 8080 en el firewall de Ubuntu
# y deja la VM lista para hacer git clone y docker compose up.
# ==========================================================
set -e

echo "==> 1/4 Actualizando el sistema..."
sudo apt-get update -qq

echo "==> 2/4 Instalando Docker..."
curl -fsSL https://get.docker.com | sudo sh

# Permite usar docker sin sudo (hay que cerrar y reabrir la sesión SSH)
sudo usermod -aG docker "$USER"

echo "==> 3/4 Abriendo el puerto 8080 en el firewall de Ubuntu..."
# Las imágenes Ubuntu de Oracle traen iptables bloqueando todo menos SSH.
# La regla se inserta ANTES del REJECT para que funcione.
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8080 -j ACCEPT

# Hacer la regla persistente tras reinicios
sudo apt-get install -y -qq netfilter-persistent iptables-persistent
sudo netfilter-persistent save

echo "==> 4/4 Comprobando la instalación..."
sudo docker version --format 'Docker {{.Server.Version}} instalado correctamente'

echo ""
echo "=========================================================="
echo " VM LISTA. Ahora cierra esta sesión SSH y vuelve a entrar"
echo " (para que se aplique el grupo docker), y después:"
echo ""
echo "   git clone https://github.com/TU_USUARIO/TU_REPO.git"
echo "   cd TU_REPO"
echo "   docker compose up --build -d"
echo "=========================================================="
