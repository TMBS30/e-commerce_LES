package dao;

import dominio.EntidadeDominio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
