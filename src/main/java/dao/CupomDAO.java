package dao;

import dominio.EntidadeDominio;

import java.sql.*;
import java.util.List;
import dominio.Cupom;
import dominio.TipoCupom;
import util.Conexao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class CupomDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }
    public int salvar(Cupom cupom) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int idGerado = 0;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "INSERT INTO cupom (valor_cupom, id_tipo_cupom) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, cupom.getValor());
            stmt.setInt(2, cupom.getTipoCupom().getId());
            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idGerado = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar cupom.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Fechar recursos
        }
        return idGerado;
    }

    public void associarCupomAoCliente(int clienteId, int cupomId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "INSERT INTO cliente_cupom (cliente_id, cupom_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            stmt.setInt(2, cupomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao associar cupom ao cliente.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Fechar recursos
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

    public List<Cupom> listarCuponsPorTipo(String tipo) {
        List<Cupom> cupons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "SELECT c.id_cupom, c.valor_cupom, tc.id_tipo_cupom, tc.descricao_tipo_cupom " +
                    "FROM cupom c " +
                    "JOIN tipo_cupom tc ON c.id_tipo_cupom = tc.id_tipo_cupom " +
                    "WHERE tc.descricao_tipo_cupom = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cupom cupom = new Cupom();
                cupom.setId(rs.getInt("id_cupom"));
                cupom.setValor(rs.getDouble("valor_cupom"));
                String descricaoTipoCupom = rs.getString("descricao_tipo_cupom");
                TipoCupom tipoCupom = TipoCupom.fromDescricao(descricaoTipoCupom);
                cupom.setTipoCupom(tipoCupom);
                cupons.add(cupom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar cupons por tipo.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cupons;
    }

    public Cupom consultarCupomPorId(int cupomId) {
        Cupom cupom = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "SELECT c.id_cupom, c.valor_cupom, tc.id_tipo_cupom, tc.descricao_tipo_cupom " +
                    "FROM cupom c " +
                    "JOIN tipo_cupom tc ON c.id_tipo_cupom = tc.id_tipo_cupom " +
                    "WHERE c.id_cupom = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cupomId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cupom = new Cupom();
                cupom.setId(rs.getInt("id_cupom"));
                cupom.setValor(rs.getDouble("valor_cupom"));
                String descricaoTipoCupom = rs.getString("descricao_tipo_cupom");
                TipoCupom tipoCupom = TipoCupom.fromDescricao(descricaoTipoCupom);
                cupom.setTipoCupom(tipoCupom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar cupom por ID.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cupom;
    }

    public List<Cupom> listarCuponsPorTipoParaCliente(String tipo, int clienteId) {
        List<Cupom> cupons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "SELECT c.id_cupom, c.valor_cupom, tc.id_tipo_cupom, tc.descricao_tipo_cupom " +
                    "FROM cliente_cupom cc " +
                    "JOIN cupom c ON cc.cupom_id = c.id_cupom " +
                    "JOIN tipo_cupom tc ON c.id_tipo_cupom = tc.id_tipo_cupom " +
                    "WHERE tc.descricao_tipo_cupom = ? AND cc.cliente_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            stmt.setInt(2, clienteId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cupom cupom = new Cupom();
                cupom.setId(rs.getInt("id_cupom"));
                cupom.setValor(rs.getDouble("valor_cupom"));
                String descricaoTipoCupom = rs.getString("descricao_tipo_cupom");
                TipoCupom tipoCupom = TipoCupom.fromDescricao(descricaoTipoCupom);
                cupom.setTipoCupom(tipoCupom);
                cupons.add(cupom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar cupons por tipo para cliente.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
        return cupons;
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    public boolean excluirCupomDoCliente(int clienteId, int cupomId, Connection conn) throws SQLException {
        System.out.println("DEBUG (CupomDAO): Tentando excluir cupom ID " + cupomId + " para cliente ID " + clienteId);
        if (conn != null) {
            System.out.println("DEBUG (CupomDAO): Conexão válida.");
            String sql = "DELETE FROM cliente_cupom WHERE cliente_id = ? AND cupom_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, clienteId);
                stmt.setInt(2, cupomId);
                int linhasAfetadas = stmt.executeUpdate();
                System.out.println("DEBUG (CupomDAO): Linhas afetadas na exclusão: " + linhasAfetadas);
                return linhasAfetadas > 0;
            }
        } else {
            System.out.println("DEBUG (CupomDAO): Conexão nula.");
            return false;
        }
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
