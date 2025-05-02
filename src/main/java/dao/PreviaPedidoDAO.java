package dao;

import dominio.CarrinhoItem;
import dominio.EntidadeDominio;
import dominio.PreviaPedido;

import java.sql.*;
import java.util.List;

public class PreviaPedidoDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    /*public String salvar(PreviaPedido previa, List<CarrinhoItem> itens, Connection conn) {
        PreparedStatement stmt = null;
        PreparedStatement stmtItem = null;
        ResultSet rs = null;

        try {
            // 1. Inserir na tabela previa_pedido
            String sqlPrevia = "INSERT INTO previa_pedido (id_cliente, id_endereco_entrega, id_frete, id_cupom, valor_subtotal, valor_frete, valor_desconto, valor_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlPrevia, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, previa.getIdCliente());
            stmt.setInt(2, previa.getIdEnderecoEntrega());
            stmt.setInt(3, previa.getIdFrete());

            if (previa.getIdCupom() != null) {
                stmt.setInt(4, previa.getIdCupom());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setDouble(5, previa.getValorSubtotal());
            stmt.setDouble(6, previa.getValorFrete());
            stmt.setDouble(7, previa.getValorDesconto());
            stmt.setDouble(8, previa.getValorTotal());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            int idPrevia = 0;
            if (rs.next()) {
                idPrevia = rs.getInt(1);
            } else {
                throw new SQLException("Falha ao obter ID gerado da prévia.");
            }

            // 2. Inserir os itens da prévia
            String sqlItem = "INSERT INTO previa_pedido_item (id_previa_pedido, id_livro_ppi, quantidade_ppi, valor_unitario_ppi, valor_total_ppi) VALUES (?, ?, ?, ?, ?)";
            stmtItem = conn.prepareStatement(sqlItem);

            for (CarrinhoItem item : itens) {
                double precoUnitario = item.getValorVenda(); // já disponível
                double totalItem = precoUnitario * item.getQuantidade();

                stmtItem.setInt(1, idPrevia);
                stmtItem.setInt(2, item.getIdLivro());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, precoUnitario);
                stmtItem.setDouble(5, totalItem);
                stmtItem.addBatch();
            }

            stmtItem.executeBatch();

            return "Prévia do pedido salva com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar prévia do pedido", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (stmtItem != null) stmtItem.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

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
