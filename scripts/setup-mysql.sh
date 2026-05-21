#!/bin/bash
# Execute após instalar o MySQL/MariaDB e configurar a senha no .env

set -e

DB_NAME="${DB_NAME:-desafio_crud}"

echo "Criando banco de dados: $DB_NAME"
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS ${DB_NAME};"

echo "Banco criado. Agora rode: mvn exec:java"
