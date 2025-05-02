package dao;

import dominio.Categoria;
import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements IDAO{
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

    public List<Categoria> consultarPorLivro(int idLivro, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Categoria> categorias = new ArrayList<>();

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT c.id_categoria, c.nome_categoria, c.descricao_categoria FROM categoria c " +
                    "INNER JOIN livro_categoria lc ON c.id_categoria = lc.id_categoria " +
                    "WHERE lc.id_livro = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idLivro);
            rs = stmt.executeQuery();

            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"), rs.getString("descricao_categoria")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar categorias: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categorias;
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
