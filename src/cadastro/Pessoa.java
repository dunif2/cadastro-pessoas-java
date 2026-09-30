package cadastro;

/*
 * Classe modelo (entidade) que representa uma pessoa cadastrada no sistema.
 *
 * Cada atributo aqui dentro corresponde a uma coluna da tabela "pessoas"
 * no banco de dados. Essa classe nao sabe nada sobre banco de dados nem
 * sobre a tela, ela so guarda os dados de uma pessoa. Quem sabe gravar
 * e ler do banco e a classe PessoaDAO.
 */
public class Pessoa {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private String dataNascimento;

    // Construtor vazio. Usado, por exemplo, quando vamos montar o objeto
    // aos poucos, chamando os metodos "set" depois.
    public Pessoa() {
    }

    // Construtor usado para cadastrar uma pessoa nova, que ainda nao tem id
    // (o id sera gerado automaticamente pelo banco de dados).
    public Pessoa(String nome, String cpf, String telefone, String email,
            String endereco, String dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
    }

    // ---------------------------------------------------
    // Getters e Setters de cada atributo
    // ---------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // Metodo util so para ajudar a enxergar o objeto quando testamos
    // o programa imprimindo coisas no console.
    @Override
    public String toString() {
        return "Pessoa [id=" + id + ", nome=" + nome + ", cpf=" + cpf + "]";
    }
}
