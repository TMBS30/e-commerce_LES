package dao;

import dominio.Dimensoes;
import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DimensoesDAO implements IDAO{
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

    public Dimensoes consultarPorLivro(int idLivro, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Dimensoes dimensoes = null;

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT d.id_dimensao, d.altura, d.largura, d.profundidade, d.peso " +
                    "FROM dimensoes d " +
                    "INNER JOIN livro l ON d.id_dimensao = l.id_dimensao " +
                    "WHERE l.id_livro = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idLivro);
            rs = stmt.executeQuery();

            if (rs.next()) {
                dimensoes = new Dimensoes(
                        rs.getInt("id_dimensao"),
                        rs.getDouble("altura"),
                        rs.getDouble("largura"),
                        rs.getDouble("profundidade"),
                        rs.getDouble("peso")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar dimensões: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return dimensoes;
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
