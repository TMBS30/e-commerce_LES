package dao;

import dominio.EntidadeDominio;
import dominio.Item;
import util.Conexao;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public String salvar(Item item, Connection conn) {
        String sql = "INSERT INTO item (id_livro, quantidade_item, valor_custo_item, valorVenda, data_entrada_item, id_fornec) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getLivroId());
            stmt.setInt(2, item.getQuantidade());
            stmt.setDouble(3, item.getValorCusto());
            stmt.setDouble(4, item.getValorVenda());
            stmt.setTimestamp(5, item.getDataEntrada()); // Usa a data já definida no objeto 'item'
            stmt.setInt(6, item.getIdFornecedor());

            stmt.executeUpdate();
            return "Item de estoque salvo com sucesso.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao salvar item de estoque: " + e.getMessage();
        }
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }


    public Item buscarPorLivroId(int livroId) throws SQLException {
        Item item = null;
        String sql = "SELECT * FROM item WHERE id_livro = ?";

        try (Connection conn = Conexao.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                item = new Item();
                item.setId(rs.getInt("id_item"));
                item.setLivroId(rs.getInt("id_livro"));
                item.setQuantidade(rs.getInt("quantidade_item"));
                item.setValorVenda(rs.getDouble("valorVenda"));

                // ... (outros atributos, se necessário)
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return item;
    }

    public Double buscarMenorPrecoPorLivroId(int livroId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Double precoLivro = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "SELECT MIN(valorVenda) AS preco_livro FROM item WHERE id_livro = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                precoLivro = rs.getDouble("preco_livro");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Fechar recursos
        }
        return precoLivro;
    }

    public Item consultarItemMaisRecentePorLivro(int idLivro) throws SQLException {
        Item item = null;
        String sql = "SELECT * FROM item WHERE id_livro = ? ORDER BY data_entrada_item DESC LIMIT 1";

        try (Connection conn = Conexao.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivro);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                item = new Item();
                item.setId(rs.getInt("id_item"));
                item.setLivroId(rs.getInt("id_livro"));
                item.setQuantidade(rs.getInt("quantidade_item"));
                item.setValorCusto(rs.getDouble("valor_custo_item"));
                item.setValorVenda(rs.getDouble("valorVenda"));
                item.setDataEntrada(rs.getTimestamp("data_entrada_item"));
                item.setIdFornecedor(rs.getInt("id_fornec"));

                // ... (mapeie outros atributos da tabela 'item' para o objeto Item)
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
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
