package dao;

import dominio.CupomTroca;
import dominio.EntidadeDominio;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupomTrocaDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public void salvar(CupomTroca cupomTroca) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        // O SQL permanece o mesmo, pois agora id_compra pode ser NULL no DB
        String sql = "INSERT INTO cupom_troca (codigo_cupom, valor_cupom, id_cliente, id_compra) VALUES (?, ?, ?, ?)";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Mantenha Statement.RETURN_GENERATED_KEYS, é boa prática

            stmt.setString(1, cupomTroca.getCodigoCupom());
            stmt.setDouble(2, cupomTroca.getValorCupom());
            stmt.setInt(3, cupomTroca.getIdCliente()); // Assumindo que CupomTroca tem getIdCliente() direto
            if (cupomTroca.getIdCompra() != 0) {
                stmt.setInt(4, cupomTroca.getIdCompra());
            } else {
                // Se for 0, ou seja, para um cupom de troco de valor excedente, define como NULL
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cupomTroca.setIdCupomTroca(rs.getInt(1));
                System.out.println("[DEBUG - CupomTrocaDAO.salvar] Cupom de troca salvo com ID: " + cupomTroca.getIdCupomTroca() + " e código: " + cupomTroca.getCodigoCupom());
            }

        } catch (SQLException e) {
            System.err.println("ERRO SQL ao salvar cupom de troca: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("ERRO inesperado ao salvar cupom de troca: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<CupomTroca> listarTodosPorCliente(int idCliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CupomTroca> cuponsTroca = new ArrayList<>();
        String sql = "SELECT * FROM cupom_troca WHERE id_cliente = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CupomTroca cupom = new CupomTroca();
                cupom.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupom.setCodigoCupom(rs.getString("codigo_cupom"));
                cupom.setValorCupom(rs.getDouble("valor_cupom"));
                cupom.setIdCliente(rs.getInt("id_cliente"));
                cupom.setIdCompra(rs.getInt("id_compra"));
                if (rs.getTimestamp("data_criacao") != null) {
                    cupom.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime().toLocalDate());
                } else {
                    cupom.setDataCriacao(null);
                }
                cuponsTroca.add(cupom);
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cuponsTroca;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
    }

    public CupomTroca buscarPorPedidoCliente(int idCliente, int idCompra) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CupomTroca cupomTroca = null;
        String sql = "SELECT * FROM cupom_troca WHERE id_cliente = ? AND id_compra = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idCompra);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cupomTroca = new CupomTroca();
                cupomTroca.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupomTroca.setCodigoCupom(rs.getString("codigo_cupom"));
                cupomTroca.setValorCupom(rs.getDouble("valor_cupom"));
                cupomTroca.setIdCliente(rs.getInt("id_cliente"));
                cupomTroca.setIdCompra(rs.getInt("id_compra"));
                if (rs.getTimestamp("data_criacao") != null) {
                    cupomTroca.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime().toLocalDate());
                } else {
                    cupomTroca.setDataCriacao(null);
                }
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cupomTroca;
    }

    public CupomTroca buscarPorId(int idCupomTroca) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CupomTroca cupomTroca = null;
        String sql = "SELECT * FROM cupom_troca WHERE id_cupom_troca = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCupomTroca);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cupomTroca = new CupomTroca();
                cupomTroca.setIdCupomTroca(rs.getInt("id_cupom_troca"));
                cupomTroca.setCodigoCupom(rs.getString("codigo_cupom"));
                cupomTroca.setValorCupom(rs.getDouble("valor_cupom"));
                cupomTroca.setIdCliente(rs.getInt("id_cliente"));
                cupomTroca.setIdCompra(rs.getInt("id_compra"));
                if (rs.getTimestamp("data_criacao") != null) {
                    cupomTroca.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime().toLocalDate());
                } else {
                    cupomTroca.setDataCriacao(null);
                }
            }

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return cupomTroca;
    }


    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    public boolean excluir(int idCupomTroca, Connection conn) throws SQLException {
        System.out.println("DEBUG (CupomTrocaDAO): Tentando excluir cupom de troca ID " + idCupomTroca);
        if (conn != null) {
            System.out.println("DEBUG (CupomTrocaDAO): Conexão válida.");
            String sql = "DELETE FROM cupom_troca WHERE id_cupom_troca = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idCupomTroca);
                int linhasAfetadas = stmt.executeUpdate();
                System.out.println("DEBUG (CupomTrocaDAO): Linhas afetadas na exclusão: " + linhasAfetadas);
                return linhasAfetadas > 0;
            }
        } else {
            System.out.println("DEBUG (CupomTrocaDAO): Conexão nula.");
            return false;
        }
    }


    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
