package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    public static Connection createConnectionToMySQL() throws Exception{
        final String url = "jdbc:mysql://127.0.0.1:3306/cadastroCliente";
        final String username = "root";
        final String password = "root";//"Thi300902";

        // Carregando o driver JDBC
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Estabelecendo e retornando a conexão
        return DriverManager.getConnection(url, username, password);
    }
}
