package dao;

import dominio.*;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompraDAO implements IDAO{

    public int salvar(Compra compra, Connection conn) throws SQLException {
        String sql = "INSERT INTO compra (cliente_id, id_endereco_entrega, id_endereco_cobranca, valor_subtotal, valor_frete, valor_desconto_total, valor_total_compra, status_id_compra, numero_pedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int idCompraGerado = 0;

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, compra.getClienteId());
            stmt.setInt(2, compra.getIdEnderecoEntrega());
            stmt.setInt(3, compra.getIdEnderecoCobranca());
            stmt.setDouble(4, compra.getValorSubtotal());
            stmt.setDouble(5, compra.getValorFrete().getValor());
            stmt.setDouble(6, compra.getValorDescontoTotal());
            stmt.setDouble(7, compra.getValorTotalCompra());
            stmt.setInt(8, compra.getStatusIdCompra());
            stmt.setString(9, compra.getNumeroPedido()); // Use o número do pedido do objeto Compra

            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idCompraGerado = generatedKeys.getInt(1);
                compra.setIdCompra(idCompraGerado); // Seta o ID gerado no objeto Compra (boa prática)
                return idCompraGerado;
            } else {
                throw new SQLException("Falha ao inserir compra, nenhum ID gerado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar a compra", e);
        } finally {
            if (stmt != null) stmt.close();
            if (generatedKeys != null) generatedKeys.close();
        }
    }

    public String gerarNumeroPedidoAleatorio() {
        Random random = new Random();
        int numeroAleatorioInt = random.nextInt(900000000);
        long numeroAleatorio = 100000000L + numeroAleatorioInt;
        return String.valueOf(numeroAleatorio);
    }

    public void gerarCupomTroca(double valorCompra, int idCliente, int idCompra) throws SQLException {
        String codigoCupom = "TROCA-" + System.currentTimeMillis();
        CupomTroca cupomTroca = new CupomTroca(codigoCupom, valorCompra, idCliente, idCompra);
        CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
        cupomTrocaDAO.salvar(cupomTroca);
        System.out.println("[DEBUG - CompraDAO.gerarCupomTroca] Cupom de troca gerado e salvo para o pedido ID " + idCompra + ", código: " + codigoCupom);
    }
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    public void atualizarStatus(int idCompra, int novoStatusId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE compra SET status_id_compra = ? WHERE id_compra = ?";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, novoStatusId);
            stmt.setInt(2, idCompra);
            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("[DEBUG - CompraDAO.atualizarStatus] Linhas afetadas na atualização do status do pedido ID " + idCompra + ": " + linhasAfetadas);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return null;
    }

    public List<Compra> consultarComprasPorCliente(int clienteId, Connection conn) throws SQLException {
        List<Compra> listaDePedidos = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id_compra, cliente_id, id_endereco_entrega, id_endereco_cobranca, valor_subtotal, valor_frete, valor_desconto_total, valor_total_compra, data_hora_compra, status_id_compra, numero_pedido, data_criacao, data_modificacao FROM compra WHERE cliente_id = ? ORDER BY data_hora_compra DESC";
    
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            rs = stmt.executeQuery();
    
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("id_compra"));
                compra.setClienteId(rs.getInt("cliente_id"));
                compra.setIdEnderecoEntrega(rs.getInt("id_endereco_entrega"));
                compra.setIdEnderecoCobranca(rs.getInt("id_endereco_cobranca"));
                compra.setValorSubtotal(rs.getDouble("valor_subtotal"));
                // Adapte conforme sua coluna de frete
                String valorFreteStr = rs.getString("valor_frete");
                TipoFrete valorFreteEnum = null;
                if (valorFreteStr != null && !valorFreteStr.isEmpty()) {
                    try {
                        valorFreteEnum = TipoFrete.fromString(valorFreteStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valor de frete inválido no banco: " + valorFreteStr);
                    }
                }
                compra.setValorFrete(valorFreteEnum);
                compra.setValorDescontoTotal(rs.getDouble("valor_desconto_total"));
                compra.setValorTotalCompra(rs.getDouble("valor_total_compra"));
                compra.setDataHoraCompra(rs.getTimestamp("data_hora_compra"));
                compra.setStatusIdCompra(rs.getInt("status_id_compra"));
                compra.setNumeroPedido(rs.getString("numero_pedido"));
                compra.setDataCriacao(rs.getTimestamp("data_criacao")); // Obtenha a data de criação
                compra.setDataModificacao(rs.getTimestamp("data_modificacao")); // Obtenha a data de modificação
                listaDePedidos.add(compra);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return listaDePedidos;
    }

    public List<Compra> consultarTodos() throws SQLException {
        List<Compra> listaDePedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM compra ORDER BY data_hora_compra DESC";

        try {
            conn = Conexao.createConnectionToMySQL();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();
                // ... (preencha os atributos da compra como no seu consultarComprasPorCliente) ...
                listaDePedidos.add(compra);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // ... (feche a conexão) ...
        }
        return listaDePedidos;
    }

    public List<Compra> consultarTodosComItens() throws SQLException {
        List<Compra> listaDePedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmtCompra = null;
        ResultSet rsCompra = null;
        PreparedStatement stmtFrete = null;
        ResultSet rsFrete = null;
        String sqlCompra = "SELECT * FROM compra ORDER BY data_hora_compra DESC";
        String sqlFrete = "SELECT valor_frete FROM frete WHERE id_endereco = ?";
        ItensCompraDAO itemCompraDAO = new ItensCompraDAO();
        StatusCompraDAO statusCompraDAO = new StatusCompraDAO();

        try {
            conn = Conexao.createConnectionToMySQL();
            stmtCompra = conn.prepareStatement(sqlCompra);
            rsCompra = stmtCompra.executeQuery();

            while (rsCompra.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rsCompra.getInt("id_compra"));
                compra.setClienteId(rsCompra.getInt("cliente_id"));
                compra.setIdEnderecoEntrega(rsCompra.getInt("id_endereco_entrega"));
                compra.setIdEnderecoCobranca(rsCompra.getInt("id_endereco_cobranca"));
                compra.setValorSubtotal(rsCompra.getDouble("valor_subtotal"));
                compra.setValorDescontoTotal(rsCompra.getDouble("valor_desconto_total"));
                compra.setValorTotalCompra(rsCompra.getDouble("valor_total_compra"));
                compra.setDataCriacao(rsCompra.getTimestamp("data_criacao"));
                compra.setDataModificacao(rsCompra.getTimestamp("data_modificacao"));
                compra.setStatusIdCompra(rsCompra.getInt("status_id_compra"));
                compra.setNumeroPedido(rsCompra.getString("numero_pedido"));

                // Buscar valor do frete
                stmtFrete = conn.prepareStatement(sqlFrete);
                stmtFrete.setInt(1, compra.getIdEnderecoEntrega());
                rsFrete = stmtFrete.executeQuery();
                double valorFreteBanco = 0.0;
                if (rsFrete.next()) {
                    valorFreteBanco = rsFrete.getDouble("valor_frete");
                }
                compra.setValorFrete(determinarTipoFrete(valorFreteBanco));

                // Buscar itens da compra
                List<ItemCompra> itens = itemCompraDAO.consultarPorCompraId(compra.getIdCompra());
                compra.setItensCompra(itens);

                // Buscar descrição do status
                StatusCompra status = statusCompraDAO.consultarPorId(compra.getStatusIdCompra());
                if (status != null) {
                    compra.setStatusDescricao(status.getDescricao());
                }

                listaDePedidos.add(compra);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rsFrete != null) try { rsFrete.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmtFrete != null) try { stmtFrete.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (rsCompra != null) try { rsCompra.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmtCompra != null) try { stmtCompra.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listaDePedidos;
    }

    private TipoFrete determinarTipoFrete(double valorFreteBanco) {
        if (valorFreteBanco == TipoFrete.EXPRESS.getValor()) {
            return TipoFrete.EXPRESS;
        } else if (valorFreteBanco == TipoFrete.PADRAO.getValor()) {
            return TipoFrete.PADRAO;
        } else if (valorFreteBanco == TipoFrete.ECONOMICO.getValor()) {
            return TipoFrete.ECONOMICO;
        } else {
            // Lógica padrão caso o valor não corresponda a nenhum tipo conhecido
            return TipoFrete.PADRAO; // Ou lance uma exceção, ou defina um valor padrão
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
