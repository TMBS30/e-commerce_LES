package dao;

import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dominio.*;
import util.Conexao;

public class ItensCompraDAO implements IDAO{

    public void salvar(int idCompra, List<ItemCompra> itens, Connection conn) throws SQLException {
        String sql = "INSERT INTO itens_da_compra (id_compra, id_livro, quantidade, valor_unitario_na_compra) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            for (ItemCompra item : itens) {
                stmt.setInt(1, idCompra);
                stmt.setInt(2, item.getIdLivro());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getValorUnitarioNaCompra());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar itens da compra", e);
        } finally {
            if (stmt != null) stmt.close();
        }
    }
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

    public List<ItemCompra> consultarItensPorCompra(int idCompra, Connection conn) throws SQLException {
        List<ItemCompra> itens = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_item_compra, id_compra, id_livro, quantidade, valor_unitario_na_compra FROM itens_da_compra WHERE id_compra = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCompra);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ItemCompra item = new ItemCompra();
                item.setIdItemCompra(rs.getInt("id_item_compra"));
                item.setIdCompra(rs.getInt("id_compra"));
                item.setIdLivro(rs.getInt("id_livro"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setValorUnitarioNaCompra(rs.getDouble("valor_unitario_na_compra"));
                itens.add(item);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return itens;
    }

    public List<ItemCompra> consultarPorCompraId(int compraId) throws SQLException {
        List<ItemCompra> itensCompra = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_item_compra, id_livro, quantidade, valor_unitario_na_compra FROM itens_da_compra WHERE id_compra = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, compraId);
            rs = stmt.executeQuery();

            LivroDAO livroDAO = new LivroDAO();

            while (rs.next()) {
                ItemCompra item = new ItemCompra();
                item.setIdItemCompra(rs.getInt("id_item_compra"));
                item.setIdCompra(compraId);
                int livroId = rs.getInt("id_livro");
                Livro livro = livroDAO.consultarPorId(livroId);
                item.setLivro(livro);
                item.setQuantidade(rs.getInt("quantidade"));
                item.setValorUnitarioNaCompra(rs.getDouble("valor_unitario_na_compra"));
                itensCompra.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return itensCompra;
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
