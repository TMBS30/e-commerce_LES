package dao;

import dominio.CupomTroca;
import dominio.EntidadeDominio;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupomTrocaDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public void salvar(CupomTroca cupomTroca) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO cupom_troca (codigo_cupom, valor_cupom, id_cliente, id_compra) VALUES (?, ?, ?, ?)";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cupomTroca.getCodigoCupom());
            stmt.setDouble(2, cupomTroca.getValorCupom());
            stmt.setInt(3, cupomTroca.getIdCliente());
            stmt.setInt(4, cupomTroca.getIdCompra());
            stmt.executeUpdate();
            System.out.println("[DEBUG - CupomTrocaDAO.salvar] Cupom de troca salvo com código: " + cupomTroca.getCodigoCupom());

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<CupomTroca> listarTodosPorCliente(int idCliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CupomTroca> cuponsTroca = new ArrayList<>();
        String sql = "SELECT * FROM cupom_troca WHERE id_cliente = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CupomTroca cupom = new CupomTroca();
                cupom.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupom.setCodigoCupom(rs.getString("codigo_cupom"));
                cupom.setValorCupom(rs.getDouble("valor_cupom"));
                cupom.setIdCliente(rs.getInt("id_cliente"));
                cupom.setIdCompra(rs.getInt("id_compra"));
                cupom.setDataCriacao(rs.getTimestamp("data_criacao"));
                cuponsTroca.add(cupom);
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cuponsTroca;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
    }

    public CupomTroca buscarPorPedidoCliente(int idCliente, int idCompra) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CupomTroca cupomTroca = null;
        String sql = "SELECT * FROM cupom_troca WHERE id_cliente = ? AND id_compra = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idCompra);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cupomTroca = new CupomTroca();
                cupomTroca.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupomTroca.setCodigoCupom(rs.getString("codigo_cupom"));
                cupomTroca.setValorCupom(rs.getDouble("valor_cupom"));
                cupomTroca.setIdCliente(rs.getInt("id_cliente"));
                cupomTroca.setIdCompra(rs.getInt("id_compra"));
                cupomTroca.setDataCriacao(rs.getTimestamp("data_criacao"));
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cupomTroca;
    }

    public CupomTroca buscarPorId(int idCupomTroca) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CupomTroca cupomTroca = null;
        String sql = "SELECT * FROM cupom_troca WHERE id_cupom_troca = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCupomTroca);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cupomTroca = new CupomTroca();
                cupomTroca.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupomTroca.setCodigoCupom(rs.getString("codigo_cupom"));
                cupomTroca.setValorCupom(rs.getDouble("valor_cupom"));
                cupomTroca.setIdCliente(rs.getInt("id_cliente"));
                cupomTroca.setIdCompra(rs.getInt("id_compra"));
                cupomTroca.setDataCriacao(rs.getTimestamp("data_criacao"));
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cupomTroca;
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
