package dao;

import dominio.EntidadeDominio;
import dominio.StatusCompra;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusCompraDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
    }
    public List<StatusCompra> consultarTodos() throws SQLException {
        List<StatusCompra> listaStatus = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_status_compra, descricao_status_compra FROM status_compra";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_status_compra");
                String descricao = rs.getString("descricao_status_compra");
                // Encontre o Enum correspondente pelo ID
                for (StatusCompra status : StatusCompra.values()) {
                    if (status.getId() == id) {
                        listaStatus.add(status);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Feche a conexão
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listaStatus;
    }

    public StatusCompra consultarPorId(int idStatus) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_status_compra, descricao_status_compra FROM status_compra WHERE id_status_compra = ?";
        StatusCompra statusEncontrado = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idStatus);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_status_compra");
                String descricao = rs.getString("descricao_status_compra");
                // Encontre o Enum correspondente pelo ID
                for (StatusCompra status : StatusCompra.values()) {
                    if (status.getId() == id) {
                        statusEncontrado = status;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return statusEncontrado;
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
