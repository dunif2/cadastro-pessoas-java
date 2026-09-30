package cadastro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Classe responsavel por cuidar da conexao com o banco de dados.
 *
 * Escolhemos o SQLite para esse trabalho porque ele guarda o banco de
 * dados inteiro dentro de um unico arquivo (cadastro.db), que fica salvo
 * na mesma pasta do programa. Assim nao precisamos instalar nenhum
 * servidor de banco de dados (tipo MySQL) para o projeto funcionar,
 * basta ter o driver JDBC do SQLite no classpath (pasta lib).
 *
 * Se quisesse trocar para outro banco, como o MySQL, bastaria trocar
 * a URL_BANCO e o driver JDBC usado no projeto. O resto do codigo
 * (PessoaDAO) nao mudaria quase nada, porque ele usa apenas comandos
 * SQL padrao (INSERT, UPDATE, DELETE, SELECT).
 */
public class ConexaoBD {

    // Nome do arquivo que vai guardar o banco de dados
    private static final String NOME_ARQUIVO_BANCO = "cadastro.db";

    // URL de conexao do JDBC. O driver do SQLite entende o formato jdbc:sqlite:<arquivo>
    private static final String URL_BANCO = "jdbc:sqlite:" + NOME_ARQUIVO_BANCO;

    /*
     * Abre e devolve uma nova conexao com o banco de dados.
     * Cada metodo do PessoaDAO chama esse metodo para conseguir sua
     * propria conexao, usa ela e depois fecha (usando try-with-resources).
     */
    public static Connection conectar() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL_BANCO);
        return conexao;
    }

    /*
     * Cria a tabela "pessoas" no banco de dados, caso ela ainda nao exista.
     * Esse metodo e chamado uma unica vez, quando o programa comeca a rodar
     * (veja a classe Main).
     */
    public static void criarTabelaSeNaoExistir() {

        // Comando SQL que cria a tabela com as colunas que o cadastro precisa.
        // O "IF NOT EXISTS" evita erro caso a tabela ja tenha sido criada antes.
        String sql = "CREATE TABLE IF NOT EXISTS pessoas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT, "
                + "telefone TEXT, "
                + "email TEXT, "
                + "endereco TEXT, "
                + "data_nascimento TEXT"
                + ")";

        // O try-with-resources fecha a conexao e o Statement sozinho no
        // final do bloco, mesmo se der algum erro no meio do caminho.
        try (Connection conexao = conectar();
                Statement statement = conexao.createStatement()) {

            statement.execute(sql);
            System.out.println("Tabela 'pessoas' verificada/criada com sucesso.");

        } catch (SQLException erro) {
            System.out.println("Erro ao criar a tabela no banco de dados: " + erro.getMessage());
        }
    }
}
