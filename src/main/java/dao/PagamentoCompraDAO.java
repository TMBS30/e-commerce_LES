package dao;

import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PagamentoCompraDAO implements IDAO{

    public void salvar(int idCompra, List<Integer> idsCartoes, double valorTotalCompra, Integer idCupomUsado, double valorDescontoCupom, double valorFrete, Connection conn) throws SQLException {
        PreparedStatement stmt = null;

        try {
            // Lógica para salvar pagamentos com cartões
            if (idsCartoes != null && !idsCartoes.isEmpty()) {
                // *** Lógica para dividir o valorTotalCompra entre os cartões, se necessário ***
                double valorPorCartao = valorTotalCompra; // Exemplo simplificado: divide igualmente

                for (Integer idCartao : idsCartoes) {
                    String sqlCartao = "INSERT INTO pagamento_da_compra (id_compra, id_cartao, valor_pago, tipo_pagamento) VALUES (?, ?, ?, ?)";
                    stmt = conn.prepareStatement(sqlCartao);
                    stmt.setInt(1, idCompra);
                    stmt.setInt(2, idCartao);
                    stmt.setDouble(3, valorPorCartao); // Ajustar lógica de divisão
                    stmt.setString(4, "cartao");
                    stmt.executeUpdate();
                    stmt.close(); // Fechar o statement após cada execução
                    stmt = null; // Resetar para a próxima iteração
                }
            }

            // Lógica para salvar o uso de cupom
            if (idCupomUsado != null && valorDescontoCupom > 0) {
                String sqlCupom = "INSERT INTO pagamento_da_compra (id_compra, id_cupom_usado, valor_pago, tipo_pagamento) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sqlCupom);
                stmt.setInt(1, idCompra);
                stmt.setInt(2, idCupomUsado);
                stmt.setDouble(3, valorDescontoCupom);
                stmt.setString(4, "cupom");
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar pagamentos da compra", e);
        } finally {
            if (stmt != null && !stmt.isClosed()) stmt.close();
        }
    }

    public void salvarPagamentosComCartao(int idCompra, Map<Integer, Double> valoresPagosPorCartao,
                                          Integer idCupomUsado, double valorDescontoCupom,
                                          double valorFrete, Connection conn) throws SQLException {
        PreparedStatement stmt = null;

        try {
            if (valoresPagosPorCartao != null && !valoresPagosPorCartao.isEmpty()) {
                String sqlCartao = "INSERT INTO pagamento_da_compra (id_compra, id_cartao, valor_pago, tipo_pagamento) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sqlCartao);

                for (Map.Entry<Integer, Double> entry : valoresPagosPorCartao.entrySet()) {
                    Integer idCartao = entry.getKey();
                    Double valorPago = entry.getValue();

                    stmt.setInt(1, idCompra);
                    stmt.setInt(2, idCartao);
                    stmt.setDouble(3, valorPago); // Agora salva o valor específico do mapa
                    stmt.setString(4, "cartao");
                    stmt.addBatch(); // Adiciona ao lote para execução eficiente
                }
                stmt.executeBatch(); // Executa todas as inserções de uma vez
                stmt.close(); // Fechar o statement após a execução em lote
                stmt = null; // Resetar para garantir que não feche novamente no finally
            }

            if (idCupomUsado != null && valorDescontoCupom > 0) {
                String sqlCupom = "INSERT INTO pagamento_da_compra (id_compra, id_cupom_usado, valor_pago, tipo_pagamento) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sqlCupom);
                stmt.setInt(1, idCompra);
                stmt.setInt(2, idCupomUsado);
                stmt.setDouble(3, valorDescontoCupom);
                stmt.setString(4, "cupom");
                stmt.executeUpdate(); // Executa a inserção do cupom
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar pagamentos da compra", e);
        } finally {
            if (stmt != null && !stmt.isClosed()) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

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

    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
