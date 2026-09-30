package cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Essa classe e o DAO (Data Access Object) da entidade Pessoa.
 *
 * "DAO" e so um nome chique para dizer que essa classe e a unica
 * responsavel por conversar com o banco de dados usando comandos SQL.
 * A tela (TelaPrincipal) nunca monta um comando SQL sozinha, ela sempre
 * pede para o PessoaDAO fazer isso. Assim, se um dia precisarmos trocar
 * a forma como os dados sao gravados, so mexemos aqui.
 *
 * Os metodos daqui NAO tratam os erros do banco (SQLException) sozinhos:
 * eles repassam o erro com "throws SQLException" para quem chamou (a tela).
 * Assim a tela fica sabendo que deu errado e consegue avisar o usuario,
 * em vez de mostrar "sucesso" quando na verdade nada foi gravado.
 */
public class PessoaDAO {

    /*
     * Insere uma pessoa nova no banco de dados.
     * O objeto "pessoa" chega sem id, porque o id e gerado automaticamente
     * pelo banco (por causa do AUTOINCREMENT que colocamos na tabela).
     */
    public void inserir(Pessoa pessoa) throws SQLException {

        String sql = "INSERT INTO pessoas (nome, cpf, telefone, email, endereco, data_nascimento) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // O try-with-resources fecha a conexao e o comando sozinho no final,
        // mesmo se der erro. Se der erro, a SQLException sobe para a tela.
        try (Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Cada "?" do comando SQL acima e preenchido aqui, na mesma ordem
            comando.setString(1, pessoa.getNome());
            comando.setString(2, pessoa.getCpf());
            comando.setString(3, pessoa.getTelefone());
            comando.setString(4, pessoa.getEmail());
            comando.setString(5, pessoa.getEndereco());
            comando.setString(6, pessoa.getDataNascimento());

            // executeUpdate() e usado para INSERT, UPDATE e DELETE
            comando.executeUpdate();

            System.out.println("Pessoa inserida com sucesso: " + pessoa.getNome());
        }
    }

    /*
     * Atualiza os dados de uma pessoa que ja existe no banco.
     * O id que esta dentro do objeto "pessoa" e usado para saber
     * qual linha da tabela deve ser alterada.
     */
    public void atualizar(Pessoa pessoa) throws SQLException {

        String sql = "UPDATE pessoas SET nome = ?, cpf = ?, telefone = ?, "
                + "email = ?, endereco = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, pessoa.getNome());
            comando.setString(2, pessoa.getCpf());
            comando.setString(3, pessoa.getTelefone());
            comando.setString(4, pessoa.getEmail());
            comando.setString(5, pessoa.getEndereco());
            comando.setString(6, pessoa.getDataNascimento());
            comando.setInt(7, pessoa.getId());

            comando.executeUpdate();

            System.out.println("Pessoa atualizada com sucesso: " + pessoa.getNome());
        }
    }

    /*
     * Remove do banco de dados a pessoa que tem o id informado.
     */
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM pessoas WHERE id = ?";

        try (Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);
            comando.executeUpdate();

            System.out.println("Pessoa de id " + id + " excluida com sucesso.");
        }
    }

    /*
     * Busca todas as pessoas cadastradas no banco de dados e devolve
     * o resultado em uma lista de objetos Pessoa. Essa lista e usada
     * la na tela principal para preencher a tabela (JTable).
     */
    public List<Pessoa> listarTodos() throws SQLException {

        List<Pessoa> listaDePessoas = new ArrayList<Pessoa>();

        String sql = "SELECT * FROM pessoas ORDER BY nome";

        try (Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()) {

            // O metodo next() anda uma linha por vez no resultado da consulta.
            // Ele devolve "true" enquanto ainda existir linha para ler.
            while (resultado.next()) {

                Pessoa pessoa = new Pessoa();
                pessoa.setId(resultado.getInt("id"));
                pessoa.setNome(resultado.getString("nome"));
                pessoa.setCpf(resultado.getString("cpf"));
                pessoa.setTelefone(resultado.getString("telefone"));
                pessoa.setEmail(resultado.getString("email"));
                pessoa.setEndereco(resultado.getString("endereco"));
                pessoa.setDataNascimento(resultado.getString("data_nascimento"));

                listaDePessoas.add(pessoa);
            }
        }

        return listaDePessoas;
    }
}
