package dao;

import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.Conexao;
import dominio.DataUtils;  // Importando a classe DataUtils para usar o método de conversão
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


public class LoggerDAO {
    //    public static void log(EntidadeDominio entidade) throws Exception {
//    	Class<?> clazz = entidade.getClass();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = dateFormat.format(new Date());
//
//        saveLogToDatabase(formattedDate,logMessage);
//    }
    public void saveLogToDatabase(String logMessage) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            // Cria a conexão com o banco de dados
            conn = Conexao.createConnectionToMySQL();

            // Obtém a data atual
            Date dataAtual = new Date();
            // Converte a data atual para o formato desejado
            String dataConvertida = DataUtils.converterData(dataAtual);

            // Exibe a data e a mensagem do log no console
            System.out.println(dataConvertida + " " + logMessage);

            // Consulta SQL para salvar o log no banco de dados
            String sql = "INSERT INTO logs (descricao, data) VALUES (?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, logMessage);  // A 'logMessage' será salva na coluna 'descricao'
            statement.setString(2, dataConvertida); // A 'data' é inserida no banco

            // Executa a consulta para salvar o log
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Em caso de erro, loga a exceção
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Fecha as conexões de forma segura
            closeResources(conn, statement);
        }
    }

    // Método para fechar as conexões de forma segura
    private void closeResources(Connection conn, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
