package dao;

import dominio.EntidadeDominio;
import dominio.Item;
import util.Conexao;

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
