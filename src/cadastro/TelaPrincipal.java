package cadastro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/*
 * Tela principal do sistema de cadastro de pessoas.
 * Fluxo basico de uso:
 * 1) O usuario preenche os campos e clica em "Cadastrar" -> inserir().
 * 2) O usuario clica em uma linha da tabela
 * 3) Com uma pessoa selecionada, o usuario pode clicar em "Alterar" ou
 *    em "Excluir" para mudar ou remover aquele cadastro.
 */
public class TelaPrincipal extends JFrame {

    // usado para falar com o banco de dados
    private PessoaDAO pessoaDAO;

    // Campos de texto do formulario
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JTextField campoDataNascimento;

    // Botoes de acao
    private JButton botaoCadastrar;
    private JButton botaoAlterar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;

    // Tabela que mostra as pessoas ja cadastradas
    private JTable tabelaPessoas;
    private DefaultTableModel modeloDaTabela;

    // Guarda o id da pessoa selecionada na tabela.
    // Quando vale -1, quer dizer que nenhuma pessoa esta selecionada.
    private int idDaPessoaSelecionada = -1;

    public TelaPrincipal() {
        pessoaDAO = new PessoaDAO();

        montarJanela();
        montarTabela();
        carregarPessoasNaTabela();
    }

    /*
     * Monta a janela principal: define titulo, tamanho e organiza os
     * paineis (formulario em cima, tabela no meio, botoes embaixo).
     */
    private void montarJanela() {
        setTitle("Sistema de Cadastro de Pessoas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null); // abre a janela centralizada na tela

        setLayout(new BorderLayout(5, 5));

        add(montarPainelFormulario(), BorderLayout.NORTH);
        add(montarPainelTabela(), BorderLayout.CENTER);
        add(montarPainelBotoes(), BorderLayout.SOUTH);
    }

    /*
     * Monta o painel de cima, com os labels e campos de texto do
     * formulario de cadastro.
     */
    private JPanel montarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da pessoa"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        campoNome = new JTextField(25);
        campoCpf = new JTextField(15);
        campoTelefone = new JTextField(15);
        campoEmail = new JTextField(20);
        campoEndereco = new JTextField(30);
        campoDataNascimento = new JTextField(10);

        // Linha 0: Nome e CPF
        c.gridx = 0;
        c.gridy = 0;
        painel.add(new JLabel("Nome:"), c);
        c.gridx = 1;
        painel.add(campoNome, c);
        c.gridx = 2;
        painel.add(new JLabel("CPF:"), c);
        c.gridx = 3;
        painel.add(campoCpf, c);

        // Linha 1: Telefone e Email
        c.gridx = 0;
        c.gridy = 1;
        painel.add(new JLabel("Telefone:"), c);
        c.gridx = 1;
        painel.add(campoTelefone, c);
        c.gridx = 2;
        painel.add(new JLabel("Email:"), c);
        c.gridx = 3;
        painel.add(campoEmail, c);

        // Linha 2: Endereco e Data de nascimento
        c.gridx = 0;
        c.gridy = 2;
        painel.add(new JLabel("Endereco:"), c);
        c.gridx = 1;
        painel.add(campoEndereco, c);
        c.gridx = 2;
        painel.add(new JLabel("Nascimento (dd/mm/aaaa):"), c);
        c.gridx = 3;
        painel.add(campoDataNascimento, c);

        return painel;
    }

    /*
     * Monta o painel do meio, com a tabela (JTable) que lista todas
     * as pessoas cadastradas no banco de dados.
     */
    private JPanel montarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(javax.swing.BorderFactory.createTitledBorder("Pessoas cadastradas"));

        // Colunas que vao aparecer na tabela
        String[] colunas = { "ID", "Nome", "CPF", "Telefone", "Email", "Endereco", "Nascimento" };

        // Criamos um DefaultTableModel "na mao" e sobrescrevemos
        // isCellEditable para que o usuario nao edite a tabela direto,
        // ele so pode editar usando os campos de texto de cima.
        modeloDaTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaPessoas = new JTable(modeloDaTabela);
        tabelaPessoas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Esse "listener" e chamado toda vez que o usuario clica em uma
        // linha diferente da tabela. Usamos ele para preencher os campos
        // do formulario com os dados da pessoa clicada.
        tabelaPessoas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evento) {
                // getValueIsAdjusting() evita que o metodo rode varias vezes
                if (!evento.getValueIsAdjusting()) {
                    preencherFormularioComLinhaSelecionada();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaPessoas);
        scroll.setPreferredSize(new Dimension(780, 300));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    /*
     * Monta o painel de baixo, com os botoes de acao do sistema.
     */
    private JPanel montarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        botaoCadastrar = new JButton("Cadastrar");
        botaoAlterar = new JButton("Alterar");
        botaoExcluir = new JButton("Excluir");
        botaoLimpar = new JButton("Limpar campos");

        // Cada botao recebe um ActionListener "anonimo" que chama o
        // metodo correspondente quando o usuario clica nele.
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                cadastrarPessoa();
            }
        });

        botaoAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                alterarPessoa();
            }
        });

        botaoExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                excluirPessoa();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                limparCampos();
            }
        });

        painel.add(botaoCadastrar);
        painel.add(botaoAlterar);
        painel.add(botaoExcluir);
        painel.add(botaoLimpar);

        return painel;
    }

    /*
     * So um metodo auxiliar chamado no construtor para deixar o codigo
     * mais organizado. Deixamos ele aqui separado para o caso de
     * precisarmos configurar mais alguma coisa na tabela no futuro.
     */
    private void montarTabela() {
        // Ajusta a largura de cada coluna para a tabela ficar mais legivel
        tabelaPessoas.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelaPessoas.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaPessoas.getColumnModel().getColumn(2).setPreferredWidth(110);
        tabelaPessoas.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabelaPessoas.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabelaPessoas.getColumnModel().getColumn(5).setPreferredWidth(200);
        tabelaPessoas.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    /*
     * Busca no banco (atraves do PessoaDAO) todas as pessoas cadastradas
     * e recarrega a tabela com esses dados. Chamamos esse metodo sempre
     * que a lista de pessoas muda: ao abrir a tela, cadastrar, alterar
     * ou excluir alguem.
     */
    private void carregarPessoasNaTabela() {

        // Primeiro apagamos todas as linhas que a tabela tinha antes
        modeloDaTabela.setRowCount(0);

        List<Pessoa> listaDePessoas;
        try {
            listaDePessoas = pessoaDAO.listarTodos();
        } catch (SQLException erro) {
            mostrarErroDoBanco("carregar a lista de pessoas", erro);
            return;
        }

        for (int i = 0; i < listaDePessoas.size(); i++) {
            Pessoa pessoa = listaDePessoas.get(i);

            Object[] linha = new Object[] {
                    pessoa.getId(),
                    pessoa.getNome(),
                    pessoa.getCpf(),
                    pessoa.getTelefone(),
                    pessoa.getEmail(),
                    pessoa.getEndereco(),
                    pessoa.getDataNascimento()
            };

            modeloDaTabela.addRow(linha);
        }
    }

    /*
     * Le a linha selecionada na tabela e joga os valores dela dentro
     * dos campos de texto do formulario, para o usuario poder ver e,
     * se quiser, alterar os dados daquela pessoa.
     */
    private void preencherFormularioComLinhaSelecionada() {
        int linhaSelecionada = tabelaPessoas.getSelectedRow();

        // Se nao tem nenhuma linha selecionada (por exemplo, o usuario
        // desmarcou a linha com Ctrl+clique), esquecemos a pessoa que
        // estava selecionada. Sem isso, "Alterar" e "Excluir" continuariam
        // agindo sobre a pessoa antiga mesmo sem nenhuma linha marcada.
        if (linhaSelecionada == -1) {
            idDaPessoaSelecionada = -1;
            return;
        }

        idDaPessoaSelecionada = (Integer) modeloDaTabela.getValueAt(linhaSelecionada, 0);
        campoNome.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 1));
        campoCpf.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 2));
        campoTelefone.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 3));
        campoEmail.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 4));
        campoEndereco.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 5));
        campoDataNascimento.setText((String) modeloDaTabela.getValueAt(linhaSelecionada, 6));
    }

    /*
     * Acao do botao "Cadastrar": pega o que o usuario digitou nos campos,
     * monta um objeto Pessoa novo e manda o PessoaDAO inserir no banco.
     */
    private void cadastrarPessoa() {

        // Validacao bem simples: o nome nao pode ficar em branco
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O campo Nome e obrigatorio.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pessoa pessoa = new Pessoa(
                campoNome.getText().trim(),
                campoCpf.getText().trim(),
                campoTelefone.getText().trim(),
                campoEmail.getText().trim(),
                campoEndereco.getText().trim(),
                campoDataNascimento.getText().trim());

        // Se o banco der erro, avisamos o usuario e paramos por aqui,
        // sem mostrar a mensagem de sucesso e sem limpar os campos
        // (assim ele nao perde o que digitou).
        try {
            pessoaDAO.inserir(pessoa);
        } catch (SQLException erro) {
            mostrarErroDoBanco("cadastrar a pessoa", erro);
            return;
        }

        carregarPessoasNaTabela();
        limparCampos();

        JOptionPane.showMessageDialog(this, "Pessoa cadastrada com sucesso!");
    }

    /*
     * Acao do botao "Alterar": so funciona se uma pessoa estiver
     * selecionada na tabela (ou seja, idDaPessoaSelecionada != -1).
     */
    private void alterarPessoa() {

        if (idDaPessoaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma pessoa na tabela antes de alterar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O campo Nome e obrigatorio.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pessoa pessoa = new Pessoa(
                campoNome.getText().trim(),
                campoCpf.getText().trim(),
                campoTelefone.getText().trim(),
                campoEmail.getText().trim(),
                campoEndereco.getText().trim(),
                campoDataNascimento.getText().trim());

        pessoa.setId(idDaPessoaSelecionada);

        try {
            pessoaDAO.atualizar(pessoa);
        } catch (SQLException erro) {
            mostrarErroDoBanco("alterar a pessoa", erro);
            return;
        }

        carregarPessoasNaTabela();
        limparCampos();

        JOptionPane.showMessageDialog(this, "Pessoa alterada com sucesso!");
    }

    /*
     * Acao do botao "Excluir": pede confirmacao antes de apagar,
     * ja que essa acao nao pode ser desfeita.
     */
    private void excluirPessoa() {

        if (idDaPessoaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma pessoa na tabela antes de excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir esta pessoa?",
                "Confirmar exclusao",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                pessoaDAO.excluir(idDaPessoaSelecionada);
            } catch (SQLException erro) {
                mostrarErroDoBanco("excluir a pessoa", erro);
                return;
            }
            carregarPessoasNaTabela();
            limparCampos();
        }
    }

    /*
     * Mostra uma janela de erro quando alguma operacao no banco de dados
     * falha (por exemplo: arquivo do banco travado ou driver do SQLite
     * faltando). O texto "acao" diz o que o usuario estava tentando fazer.
     */
    private void mostrarErroDoBanco(String acao, SQLException erro) {
        System.out.println("Erro ao " + acao + ": " + erro.getMessage());
        JOptionPane.showMessageDialog(this,
                "Nao foi possivel " + acao + ".\n\nDetalhe do erro: " + erro.getMessage(),
                "Erro no banco de dados",
                JOptionPane.ERROR_MESSAGE);
    }

    /*
     * Limpa todos os campos do formulario e esquece qual pessoa
     * estava selecionada.
     */
    private void limparCampos() {
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoEndereco.setText("");
        campoDataNascimento.setText("");

        idDaPessoaSelecionada = -1;

        tabelaPessoas.clearSelection();
    }
}
