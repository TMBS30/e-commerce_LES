package dao;

import dominio.EntidadeDominio;
import dominio.ItemPedido;
import dominio.Livro;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO implements IDAO{
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

    public List<ItemPedido> consultarPorCompraId(int compraId, Connection conn) throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        //Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_item_compra, id_compra, id_livro, quantidade, valor_unitario_na_compra FROM itens_da_compra WHERE id_compra = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, compraId);
            rs = stmt.executeQuery();

            LivroDAO livroDAO = new LivroDAO(); // Crie uma instância de LivroDAO

            while (rs.next()) {
                ItemPedido item = new ItemPedido();
                item.setIdItemPedido(rs.getInt("id_item_compra"));
                item.setIdCompra(rs.getInt("id_compra"));
                int livroId = rs.getInt("id_livro");
                Livro livro = livroDAO.consultarPorId(livroId); // Busque o livro pelo ID
                item.setLivro(livro);
                item.setQuantidade(rs.getInt("quantidade"));
                item.setValorUnitario(rs.getDouble("valor_unitario_na_compra"));
                itensPedido.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Feche a conexão
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return itensPedido;
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
