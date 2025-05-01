package dao;

import dominio.TipoLogradouro;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TipoLogradouroDAO {
    public List<TipoLogradouro> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT descricao_tip_log FROM tipos_logradouro";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            List<TipoLogradouro> listaTiposLog = new ArrayList<>();

            while (rs.next()) {
                String tipoLog = rs.getString("descricao_tip_log");
                try {
                    TipoLogradouro tipoLogradouro = TipoLogradouro.fromDescricao(tipoLog);
                    listaTiposLog.add(tipoLogradouro);
                } catch (IllegalArgumentException e) {
                    System.err.println("TipoLogradouro não encontrado: " + tipoLog);
                }
            }

            System.out.println("Número de Tipo Logradouro encontrados: " + listaTiposLog.size()); // Log de quantos estados foram encontrados
            return listaTiposLog;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar  Tipos Logradouro: " + e.getMessage());
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

    public int consultarIdTipoLogradouro(String descricao, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_tip_log FROM tipos_logradouro WHERE descricao_tip_log = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao); // Substitui o '?' pelo valor da descrição do tipo de logradouro

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_tip_log"); // Retorna o id do tipo de logradouro encontrado
            } else {
                return -1; // Caso o tipo de logradouro não seja encontrado, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID TipoLogradouro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Retorna -1 caso haja um erro ou não encontre o tipo de logradouro
    }

    /*public TipoLogradouro consultarIdTipoLogradouro(String descricao) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_tip_log FROM tipos_logradouro WHERE descricao_tip_log = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return TipoLogradouro.fromDescricao(descricao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID TipoLogradouro: " + e.getMessage());
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
}