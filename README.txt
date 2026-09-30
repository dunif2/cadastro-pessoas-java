SISTEMA DE CADASTRO DE PESSOAS - LEIA-ME
==========================================

O que e esse projeto
---------------------
Um sistema simples de cadastro de pessoas (nome, CPF, telefone, email,
endereco e data de nascimento), feito em Java com interface grafica
(Swing) e gravacao em banco de dados (SQLite, via JDBC).

E possivel Cadastrar, Listar, Alterar e Excluir pessoas. Os dados ficam
guardados no arquivo "cadastro.db", que e criado sozinho na primeira vez
que o programa roda.

Estrutura de pastas
---------------------
src/cadastro/    -> codigo-fonte (.java) do projeto
  Pessoa.java        -> classe modelo (entidade)
  ConexaoBD.java     -> abre a conexao com o banco e cria a tabela
  PessoaDAO.java     -> comandos SQL (inserir, atualizar, excluir, listar)
  TelaPrincipal.java -> interface grafica (Swing)
  Main.java          -> classe principal (metodo main)

lib/             -> biblioteca externa usada (driver JDBC do SQLite)
  sqlite-jdbc-3.46.1.3.jar

Como abrir no NetBeans
---------------------
1. No NetBeans, va em File > New Project > Java with Existing Sources.
2. Escolha um nome de projeto (ex: CadastroPessoas) e aponte a pasta
   "src" como pasta de codigo-fonte existente.
3. Depois de criado o projeto, clique com o botao direito em
   "Libraries" (dentro do projeto, na aba Projects) > Add JAR/Folder
   e selecione o arquivo lib/sqlite-jdbc-3.46.1.3.jar.
4. Defina a classe cadastro.Main como classe principal do projeto
   (botao direito no projeto > Properties > Run > Main Class).
5. Rode o projeto normalmente (F6 ou o botao de Play).

Como rodar pelo terminal (sem NetBeans)
---------------------
Linux/Mac:
    ./compilar_e_executar.sh

Windows:
    compilar_e_executar.bat

(Ou, na mao, os mesmos dois comandos que estao dentro desses scripts:
compilar com javac apontando o .jar no classpath, e depois executar
com java, tambem apontando o .jar no classpath.)

Observacoes
---------------------
- Nao precisa instalar nenhum servidor de banco de dados: o SQLite
  guarda tudo em um unico arquivo (cadastro.db) na pasta do projeto.
- Para trocar para outro banco (por exemplo, MySQL), bastaria trocar
  a URL de conexao e o driver dentro da classe ConexaoBD.java. O resto
  do codigo (PessoaDAO) usa apenas SQL padrao e nao precisaria mudar.
