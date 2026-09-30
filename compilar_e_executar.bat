@echo off
REM Script simples para compilar e rodar o projeto pelo terminal (Windows).
REM Se for usar o NetBeans, nao precisa desse script, so importar o projeto
REM (veja o README.txt).

REM Garante que estamos na pasta do projeto (onde fica este .bat), para os
REM caminhos src, lib e cadastro.db funcionarem mesmo se o script for
REM chamado de outra pasta.
cd /d "%~dp0"

if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib/sqlite-jdbc-3.46.1.3.jar" -d bin src\cadastro\*.java
if errorlevel 1 (
    echo.
    echo Erro na compilacao. Veja as mensagens acima.
    pause
    exit /b 1
)

java -cp "lib/sqlite-jdbc-3.46.1.3.jar;bin" cadastro.Main

REM Mantem a janela aberta no final, para dar tempo de ler qualquer erro.
pause
