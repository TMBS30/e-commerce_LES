package dao;

import java.sql.Connection;
import util.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dominio.*;

public class EstadoDAO {
    public List<Estado> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Criando o PreparedStatement
            String sql = "SELECT nome_estado FROM estados";
            stmt = conn.prepareStatement(sql);

            // Executando a consulta
            rs = stmt.executeQuery();

            List<Estado> listaEstados = new ArrayList<>();

            while (rs.next()) {
                Estado estado = new Estado();
                //estado.setId(rs.getInt("id_estado"));
                estado.setNome(rs.getString("nome_estado"));
                listaEstados.add(estado);
            }

            System.out.println("Número de estados encontrados: " + listaEstados.size()); // Log de quantos estados foram encontrados
            return listaEstados;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar estados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*public List<Estado> consultarIdEstados() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_estado, nome_estado FROM estados";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Estado> listaEstados = new ArrayList<>();
            while (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setNome(rs.getString("nome_estado"));
                listaEstados.add(estado);
            }
            return listaEstados;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar estados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/

    public int consultarIdEstados(String nomeEstado, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_estado FROM estados WHERE nome_estado = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeEstado); // Substitui o '?' pelo nome do estado

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_estado"); // Retorna o id do estado encontrado
            } else {
                return -1; // Caso o estado não seja encontrado, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar estado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Retorna -1 caso haja um erro
    }
}
