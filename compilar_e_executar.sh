#!/bin/bash
# Script simples para compilar e rodar o projeto pelo terminal (Linux/Mac).
# Se for usar o NetBeans, nao precisa desse script, so importar o projeto
# (veja o README.txt).

# Garante que estamos na pasta do projeto (onde fica este script)
cd "$(dirname "$0")" || exit 1

mkdir -p bin
javac -encoding UTF-8 -cp "lib/sqlite-jdbc-3.46.1.3.jar" -d bin src/cadastro/*.java || { echo "Erro na compilacao. Veja as mensagens acima."; exit 1; }
java -cp "lib/sqlite-jdbc-3.46.1.3.jar:bin" cadastro.Main
