package dao;

import dominio.TipoResidencia;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoResidenciaDAO {
    public List<TipoResidencia> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT descricao_tip_res FROM tipos_residencia";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            List<TipoResidencia> listaTiposRes = new ArrayList<>();

            while (rs.next()) {
                String tipoRes = rs.getString("descricao_tip_res");
                try {
                    TipoResidencia tipoResidencia = TipoResidencia.fromDescricao(tipoRes);
                    listaTiposRes.add(tipoResidencia);
                } catch (IllegalArgumentException e) {
                    //System.err.println("TipoEndereco não encontrado: " + tipoEnd);
                }
            }

            System.out.println("Número de tipo Residencia encontrados: " + listaTiposRes.size()); // Log de quantos estados foram encontrados
            return listaTiposRes;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar  tipo Residencia: " + e.getMessage());
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

    public int consultarIdTipoResidencia(String descricao, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_tip_res FROM tipos_residencia WHERE descricao_tip_res = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao); // Substitui o '?' pelo valor da descrição do tipo de residência
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_tip_res"); // Retorna o ID do tipo de residência encontrado
            } else {
                System.err.println("TipoResidencia não encontrado: " + descricao);
                return -1; // Caso o tipo de residência não seja encontrado, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID TipoResidencia: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Retorna -1 caso haja erro ou o tipo de residência não seja encontrado
    }

    /*public int consultarIdTipoResidencia(String descricao, Connection conn){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_tip_res FROM tipos_residencia WHERE descricao_tip_res = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_tip_res");
            } else {
                System.err.println("TipoResidencia não encontrado: " + descricao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID do TipoResidencia: " + e.getMessage());
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
        return -1;
    }*/
}