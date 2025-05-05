package dao;

import dominio.EntidadeDominio;
import dominio.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO implements IDAO{
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

    public Fornecedor buscarPorId(int id, Connection conn) throws SQLException {
        String sql = "SELECT id_fornec, descricao_fornec FROM fornecedor WHERE id_fornec = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapear os resultados para o enum Fornecedor
                    for (Fornecedor fornecedor : Fornecedor.values()) {
                        if (fornecedor.getId() == rs.getInt("id_fornec")) {
                            return fornecedor;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<Fornecedor> consultarTodos(Connection conn) throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT id_fornec, descricao_fornec FROM fornecedor";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Mapear os resultados para o enum Fornecedor
                for (Fornecedor fornecedor : Fornecedor.values()) {
                    if (fornecedor.getId() == rs.getInt("id_fornec")) {
                        fornecedores.add(fornecedor);
                        break; // Já encontrou o enum correspondente
                    }
                }
            }
        }
        return fornecedores;
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
