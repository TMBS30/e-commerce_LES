package dao;

import dominio.CarrinhoItem;
import dominio.EntidadeDominio;
import dominio.Item;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoItemDAO implements IDAO{

    //VAI ARMAZENAR OS ITENS NO CARRINHO, É CARRINHO_ITEM, OU SEJA, AQUI QUE SERÁ SALVO EFETITAVEMENTE O CARRINHO

    public String salvar(CarrinhoItem item, Connection conn) throws SQLException {
        String sql = "INSERT INTO carrinho_item (id_carrinho, id_livro, quantidade) VALUES (?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, item.getIdCarrinho());
            stmt.setInt(2, item.getIdLivro());
            stmt.setInt(3, item.getQuantidade());
            stmt.executeUpdate();

            return "Item adicionado ao carrinho com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar item no carrinho", e);
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    public List<CarrinhoItem> consultarPorCarrinho(int idCarrinho, Connection conn) throws SQLException {
        String sql = "SELECT ci.id, ci.id_livro, ci.quantidade, i.valorVenda, i.id_item " +
                "FROM carrinho_item ci " +
                "JOIN item i ON ci.id_livro = i.livroId " +
                "WHERE ci.id_carrinho = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CarrinhoItem> itens = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCarrinho);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CarrinhoItem carrinhoItem = new CarrinhoItem();
                carrinhoItem.setId(rs.getInt("id"));
                carrinhoItem.setIdCarrinho(idCarrinho);
                carrinhoItem.setIdLivro(rs.getInt("id_livro"));
                carrinhoItem.setQuantidade(rs.getInt("quantidade"));

                Item item = new Item();
                item.setId(rs.getInt("id_item")); // ID do item no estoque
                item.setValorVenda(rs.getDouble("valorVenda"));
                item.setLivroId(rs.getInt("id_livro"));

                carrinhoItem.setItem(item);

                itens.add(carrinhoItem);
            }

            return itens;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar itens do carrinho", e);
        } finally {
            if (rs != null) rs.close();
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

    @Override
    public String excluir(EntidadeDominio entidade) {
        System.out.println(">> CarrinhoItemDAO.excluir() chamado com: " + entidade.getClass().getName());
        CarrinhoItem carrinhoItem = (CarrinhoItem) entidade;
        Connection conn = null;
        PreparedStatement psDeleteItem = null;

        try {
            conn = Conexao.createConnectionToMySQL();

            // Excluir item diretamente pelo ID (nova chave primária)
            String sqlDelete = "DELETE FROM carrinho_item WHERE id = ?";
            psDeleteItem = conn.prepareStatement(sqlDelete);
            psDeleteItem.setInt(1, carrinhoItem.getId());

            int linhasAfetadas = psDeleteItem.executeUpdate();
            System.out.println("Linhas afetadas: " + linhasAfetadas);

            if (linhasAfetadas > 0) {
                System.out.println("Item removido do carrinho com sucesso. ID do item: " + carrinhoItem.getId());
                return null;
            } else {
                return "Item não encontrado no carrinho.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao excluir item do carrinho: " + e.getMessage();
        } finally {
            try {
                if (psDeleteItem != null) psDeleteItem.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
