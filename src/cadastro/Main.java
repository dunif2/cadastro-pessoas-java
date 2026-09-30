package cadastro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Classe principal do programa. E o metodo main() daqui que a execucao
 * do sistema comeca.
 */
public class Main {

    public static void main(String[] args) {

        // Tenta deixar a interface com a aparencia (Look and Feel) do
        // sistema operacional. Se der algum erro, o programa continua
        // rodando normalmente, so que com a aparencia padrao do Java.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception erro) {
            System.out.println("Nao foi possivel aplicar o look and feel do sistema.");
        }

        // Garante que a tabela "pessoas" ja existe no banco de dados
        // antes da tela ser aberta.
        ConexaoBD.criarTabelaSeNaoExistir();

        // Telas Swing devem ser criadas dentro da Event Dispatch Thread
        // (EDT). Isso e uma boa pratica recomendada pela propria
        // documentacao do Java para evitar problemas de concorrencia
        // na interface grafica.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaPrincipal tela = new TelaPrincipal();
                tela.setVisible(true);
            }
        });
    }
}
