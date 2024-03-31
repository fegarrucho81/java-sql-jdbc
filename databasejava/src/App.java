import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("C:\\Users\\felip\\OneDrive\\Área de Trabalho\\FELIPE\\projetos vsc\\sqljava\\databasejava\\bin\\config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/alunos";
        String usuario = prop.getProperty("usuario");
        String senha = prop.getProperty("senha");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {

            Scanner fi = new Scanner(System.in);

            boolean continuar = true;

            while(continuar) {
                System.out.println("\nEscolha uma opção: ");
                System.out.println("1- Adicionar itens à tabela.");
                System.out.println("2- Remover itens da tabela.");
                System.out.println("3- Consultar a tabela.");
                System.out.println("4- Sair.");

                int opcao = fi.nextInt();

                switch(opcao) {
                    case 1:
                        System.out.println("Digite o novo ID: ");
                        int novoId = fi.nextInt();
                        System.out.println("Digite o novo nome: ");
                        String novoNome = fi.next();
                        adicionarTabela(conexao, novoId, novoNome);
                        break;
                    case 2:
                        System.out.println("Digite o ID para exclusão: ");
                        int idExclusao = fi.nextInt();
                        removerTabela(conexao, idExclusao);
                        break;
                    case 3:
                        consultarTabela(conexao);
                        break;
                    case 4:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida. Digite entre as opções 1 à 4.");
                        break;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void adicionarTabela(Connection conexao, int novoId, String novoNome) throws SQLException {
            String insercaoSQL = "INSERT INTO registros (id, nome) VALUES (?, ?)";
             try (PreparedStatement insercao = conexao.prepareStatement(insercaoSQL)) {
                insercao.setInt(1, novoId);
                insercao.setString(2, novoNome);
                insercao.executeUpdate();
                System.out.println("Novo registro com ID " + novoId + " adicionado com sucesso.");
            }
    }

    public static void removerTabela(Connection conexao, int idExclusao) throws SQLException {
        String exclusaoSQL = "DELETE FROM registros WHERE id = ?";
            try (PreparedStatement exclusao = conexao.prepareStatement(exclusaoSQL)) {
                exclusao.setInt(1,idExclusao);
                int linhasAfetadas = exclusao.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Registro com ID " + idExclusao + " excluído com sucesso");
                } else {
                    System.out.println("Nenhum registro encontrado com este ID");
                }
            }
    }

    public static void consultarTabela(Connection conexao) throws SQLException {
        String consultaSQL = "SELECT * FROM registros";

            try (PreparedStatement consulta = conexao.prepareStatement(consultaSQL);
                ResultSet resultado = consulta.executeQuery()) {

                    System.out.println("\nTabela");
                    while (resultado.next()) {
                        int id = resultado.getInt("id");
                        String nome = resultado.getString("nome");

                        System.out.println("ID: " + id + ", Nome: " + nome);
                        System.out.println("---------------------------");
                    }
                }
    }

}