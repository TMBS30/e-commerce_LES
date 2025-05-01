package dao;

import dominio.Editora;
import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EditoraDAO implements IDAO{
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

    public Editora consultarPorLivro(int idEditora, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Editora editora = null;

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Consulta correta usando o id_edit diretamente
            String sql = "SELECT e.id_edit, e.nome_edit " +
                    "FROM editora e " +
                    "WHERE e.id_edit = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEditora); // Usando idEditora e não idLivro
            rs = stmt.executeQuery();

            if (rs.next()) {
                editora = new Editora(rs.getInt("id_edit"), rs.getString("nome_edit"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar editora: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return editora;
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
