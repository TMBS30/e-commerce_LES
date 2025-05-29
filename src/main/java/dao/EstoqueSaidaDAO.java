package dao;

import dominio.EntidadeDominio;
import dominio.EstoqueSaida;

import java.sql.*;
import java.util.List;

public class EstoqueSaidaDAO implements IDAO{
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public void salvar(EstoqueSaida estoqueSaida, Connection conn) throws SQLException {
        String sql = "INSERT INTO saida_estoque (id_livro, quantidade_saida, data_saida, motivo_saida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, estoqueSaida.getIdLivro());
            pstmt.setInt(2, estoqueSaida.getQuantidade());
            pstmt.setDate(3, java.sql.Date.valueOf(estoqueSaida.getDataSaida()));
            pstmt.setString(4, estoqueSaida.getMotivoSaida().toString());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                estoqueSaida.setIdSaida(generatedKeys.getInt(1));
            }
        }
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
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
