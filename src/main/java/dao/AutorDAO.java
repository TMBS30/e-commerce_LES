package dao;

import dominio.Autor;
import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO implements IDAO{
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

    public List<Autor> consultarPorLivro(int idLivro, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Autor> autores = new ArrayList<>();

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT a.id, a.nome, a.nacionalidade FROM autor a " +
                    "INNER JOIN livro_autor la ON a.id = la.id_autor " +
                    "WHERE la.id_livro = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idLivro);
            rs = stmt.executeQuery();

            while (rs.next()) {
                autores.add(new Autor(rs.getInt("id"), rs.getString("nome"), rs.getString("nacionalidade")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar autores: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return autores;
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
