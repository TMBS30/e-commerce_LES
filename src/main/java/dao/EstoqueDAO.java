package dao;

import dominio.EntidadeDominio;
import dominio.Estoque;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import dominio.*;

public class EstoqueDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public void salvar(Estoque estoque, Connection conn) throws SQLException {
        String sql = "INSERT INTO entrada_estoque (id_livro, tipo_entrada, quantidade_estoque, id_fornecedor, valor_custo, data_entrada) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, estoque.getIdLivro());
            pstmt.setString(2, estoque.getTipoEntrada().toString());
            pstmt.setInt(3, estoque.getQuantidadeEstoque());
            if (estoque.getIdFornecedor() != null) {
                pstmt.setInt(4, estoque.getIdFornecedor());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (estoque.getValorCusto() != null) {
                pstmt.setBigDecimal(5, estoque.getValorCusto());
            } else {
                pstmt.setNull(5, java.sql.Types.DECIMAL);
            }
            pstmt.setDate(6, java.sql.Date.valueOf(estoque.getDataEntrada()));
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                estoque.setIdEstoque(generatedKeys.getInt(1));
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

    public BigDecimal buscarValorCustoMaisRecente(int idLivro, Connection conn) throws SQLException {
        String sql = "SELECT valor_custo FROM entrada_estoque WHERE id_livro = ? ORDER BY data_entrada DESC LIMIT 1";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idLivro);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("valor_custo");
            }
            return null; // Retorna null se não houver entradas para o livro
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
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
