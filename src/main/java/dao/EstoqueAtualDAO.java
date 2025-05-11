package dao;

import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dominio.*;
public class EstoqueAtualDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public void salvar(EstoqueAtual estoqueAtual, Connection conn) throws SQLException {
        String sql = "INSERT INTO estoque_atual (id_livro, quantidade_atual) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estoqueAtual.getIdLivro());
            pstmt.setInt(2, estoqueAtual.getQuantidadeAtual());
            pstmt.executeUpdate();
        }
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    public void atualizar(EstoqueAtual estoqueAtual, Connection conn) throws SQLException {
        String sql = "UPDATE estoque_atual SET quantidade_atual = ? WHERE id_livro = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estoqueAtual.getQuantidadeAtual());
            pstmt.setInt(2, estoqueAtual.getIdLivro());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
    }

    public EstoqueAtual consultarPorLivroId(int idLivro, Connection conn) throws SQLException {
        String sql = "SELECT id_livro, quantidade_atual FROM estoque_atual WHERE id_livro = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    EstoqueAtual estoqueAtual = new EstoqueAtual();
                    estoqueAtual.setIdLivro(rs.getInt("id_livro"));
                    estoqueAtual.setQuantidadeAtual(rs.getInt("quantidade_atual"));
                    return estoqueAtual;
                }
            }
            return null; // Retorna null se não encontrar o livro no estoque atual
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
