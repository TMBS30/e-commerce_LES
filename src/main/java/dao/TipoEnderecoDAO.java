package dao;

import dominio.TipoEndereco;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoEnderecoDAO {

    public List<TipoEndereco> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT descricao_tip_end FROM tipos_endereco";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            List<TipoEndereco> listaTiposEnd = new ArrayList<>();

            while (rs.next()) {
                String tipoEnd = rs.getString("descricao_tip_end");
                try {
                    TipoEndereco tipoEndereco = TipoEndereco.fromDescricao(tipoEnd);
                    listaTiposEnd.add(tipoEndereco);
                } catch (IllegalArgumentException e) {
                    //System.err.println("TipoEndereco não encontrado: " + tipoEnd);
                }
            }

            System.out.println("Número de tipos enderecos encontrados: " + listaTiposEnd.size()); // Log de quantos estados foram encontrados
            return listaTiposEnd;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar  tipos enderecos: " + e.getMessage());
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

    public int consultarIdTipoEndereco(String descricao, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_tip_end FROM tipos_endereco WHERE descricao_tip_end = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao); // Substitui o '?' pelo valor da descrição do tipo de endereço
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_tip_end"); // Retorna o ID do tipo de endereço encontrado
            } else {
                return -1; // Caso o tipo de endereço não seja encontrado, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID TipoEndereco: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Retorna -1 caso haja erro ou o tipo de endereço não seja encontrado
    }

    /*public TipoEndereco consultarIdTipoEndereco(String descricao) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_tip_end FROM tipos_endereco WHERE descricao_tip_end = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, descricao);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return TipoEndereco.fromDescricao(descricao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID TipoEndereco: " + e.getMessage());
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