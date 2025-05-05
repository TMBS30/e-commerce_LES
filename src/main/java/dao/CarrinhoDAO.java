package dao;

import dominio.*;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CarrinhoDAO implements IDAO{

    //CARRINHO SERA SALVO, NO CADASTRO DO CLIENTE, POIS CARRINHO, SERVE APENAS PARA ASSOCIAR UM CARRINO A UM CLIENTE
    //QUEM VAI ARMAZENAR OS ITENS NO CARRINHO, É CARRINHO_ITEM
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public String salvar(Carrinho carrinho, Connection conn) throws SQLException {
        String sql = "INSERT INTO carrinho (bloqueado_carrinho, data_bloqueio_carrinho, valor_carrinho, cliente_id, ultima_atividade) VALUES (?, ?, ?, ?, NOW())";
        PreparedStatement mysql = null;

        try {
            mysql = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            mysql.setBoolean(1, carrinho.isBloqueado());
            mysql.setDate(2, carrinho.getDataBloqueio() != null ? Date.valueOf(carrinho.getDataBloqueio()) : null);
            mysql.setDouble(3, carrinho.getValorCarrinho());
            mysql.setInt(4, carrinho.getClienteId());

            mysql.executeUpdate();

            ResultSet generatedKeys = mysql.getGeneratedKeys();
            if (generatedKeys.next()) {
                carrinho.setId(generatedKeys.getInt(1));
            }

            return "Carrinho salvo com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar carrinho", e);
        } finally {
            if (mysql != null) mysql.close();
        }
    }

    public Carrinho consultarPorId(int idCarrinho, Connection conn) throws SQLException {
        String sql = "SELECT * FROM carrinho WHERE id_carrinho = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCarrinho);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Carrinho carrinho = new Carrinho();
                carrinho.setId(rs.getInt("id_carrinho"));
                carrinho.setBloqueado(rs.getBoolean("bloqueado_carrinho"));
                Date dataBloqueio = rs.getDate("data_bloqueio_carrinho");
                carrinho.setDataBloqueio(dataBloqueio != null ? String.valueOf(dataBloqueio.toLocalDate()) : null);
                carrinho.setValorCarrinho(rs.getDouble("valor_carrinho"));
                carrinho.setClienteId(rs.getInt("cliente_id"));
                return carrinho;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar carrinho", e);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    public boolean alterarQuantidadeItemCarrinho(int itemId, int novaQuantidade) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "UPDATE carrinho_item SET quantidade = ? WHERE id_carrinho_item = ? AND quantidade >= 0"; // Garante quantidade não negativa
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, itemId);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                // Atualizar a 'ultima_atividade' do carrinho associado ao item
                atualizarUltimaAtividadePeloItemId(itemId, conn);
            }
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao alterar a quantidade do item do carrinho.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private void atualizarUltimaAtividadePeloItemId(int itemId, Connection conn) throws SQLException {
        String sql = "UPDATE carrinho c JOIN carrinho_item ci ON c.id_carrinho = ci.id_carrinho SET c.ultima_atividade = NOW() WHERE ci.id_carrinho_item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }

    private void atualizarUltimaAtividade(int carrinhoId, Connection conn) throws SQLException {
        String sql = "UPDATE carrinho SET ultima_atividade = NOW() WHERE id_carrinho = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carrinhoId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Método ajustado no CarrinhoDAO
    public Carrinho consultarCarrinhoPorCliente(int clienteId) {
        Carrinho carrinho = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CarrinhoItem> itensCarrinho = new ArrayList<>();
        Set<Integer> carrinhoItemIdsAdicionados = new HashSet<>(); // Para rastrear IDs de carrinho_item já adicionados

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) throw new SQLException("Erro ao conectar ao banco de dados");

            String sql = "SELECT c.id_carrinho, c.bloqueado_carrinho, c.data_bloqueio_carrinho, c.valor_carrinho, c.cliente_id, c.ultima_atividade, " +
                    "ci.id AS id_carrinho_item, ci.quantidade, ci.id_livro AS livro_id_carrinho_item, " +
                    "i.id_item, i.quantidade_item, i.valor_custo_item, i.valorVenda, i.data_entrada_item, i.id_fornec, " +
                    "l.id_livro, l.titulo, l.caminho_imagem, e.nome_edit AS editora_nome " +
                    "FROM carrinho c " +
                    "LEFT JOIN carrinho_item ci ON c.id_carrinho = ci.id_carrinho " +
                    "LEFT JOIN livro l ON ci.id_livro = l.id_livro " +
                    "LEFT JOIN item i ON i.id_livro = l.id_livro " +
                    "LEFT JOIN editora e ON l.id_edit = e.id_edit " +
                    "WHERE c.cliente_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            rs = stmt.executeQuery();

            carrinho = new Carrinho();
            carrinho.setItens(itensCarrinho);

            while (rs.next()) {
                if (carrinho.getId() == 0) {
                    carrinho.setId(rs.getInt("id_carrinho"));
                    carrinho.setBloqueado(rs.getBoolean("bloqueado_carrinho"));
                    Date dataBloqueio = rs.getDate("data_bloqueio_carrinho");
                    carrinho.setDataBloqueio(dataBloqueio != null ? String.valueOf(dataBloqueio.toLocalDate()) : null);
                    carrinho.setValorCarrinho(rs.getDouble("valor_carrinho"));
                    carrinho.setClienteId(rs.getInt("cliente_id"));
                    Timestamp ultimaAtividade = rs.getTimestamp("ultima_atividade");
                    carrinho.setUltimaAtividade(ultimaAtividade != null ? new java.util.Date(ultimaAtividade.getTime()) : new java.util.Date());
                }

                int carrinhoItemId = rs.getInt("id_carrinho_item");
                if (carrinhoItemId == 0 || carrinhoItemIdsAdicionados.contains(carrinhoItemId)) {
                    continue; // Já processamos este carrinho_item
                }

                Livro livro = new Livro();
                livro.setId(rs.getInt("livro_id_carrinho_item"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setCaminhoImagem(rs.getString("caminho_imagem"));
                Editora editora = new Editora();
                editora.setNome(rs.getString("editora_nome"));
                livro.setEditora(editora);

                Item item = new Item();
                item.setId(rs.getInt("id_item"));
                item.setQuantidade(rs.getInt("quantidade_item"));
                item.setValorCusto(rs.getDouble("valor_custo_item"));
                item.setValorVenda(rs.getDouble("valorVenda"));
                item.setDataEntrada(rs.getTimestamp("data_entrada_item"));
                item.setIdFornecedor(rs.getInt("id_fornec"));
                item.setLivroId(livro.getId());
                item.setLivro(livro);

                CarrinhoItem carrinhoItem = new CarrinhoItem(
                        rs.getInt("id_carrinho"),
                        rs.getInt("livro_id_carrinho_item"),
                        rs.getInt("quantidade"),
                        item,
                        carrinhoItemId
                );
                itensCarrinho.add(carrinhoItem);
                carrinhoItemIdsAdicionados.add(carrinhoItemId);
            }
            // Atualizar a 'ultima_atividade' ao consultar o carrinho
            if (carrinho != null && carrinho.getId() > 0) {
                atualizarUltimaAtividade(carrinho.getId(), conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o carrinho por cliente", e);
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
        return carrinho;
    }

    public Carrinho buscarCarrinhoPorClienteId(int clienteId, Connection conn) throws SQLException {
        String sql = "SELECT id_carrinho FROM carrinho WHERE cliente_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrinho carrinho = new Carrinho();
                    carrinho.setId(rs.getInt("id_carrinho"));
                    return carrinho;
                } else {
                    return null; // Retorna null se o carrinho não for encontrado
                }
            }
        }
    }

    public void excluir(int clienteId, Connection conn) throws SQLException {
        String sql = "DELETE FROM carrinho_item WHERE id_carrinho = (SELECT id_carrinho FROM carrinho WHERE cliente_id = ?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            stmt.executeUpdate();
            System.out.println("DEBUG: Itens do carrinho do cliente " + clienteId + " excluídos.");
            // Atualizar a 'ultima_atividade' do carrinho após a exclusão dos itens
            atualizarUltimaAtividadePeloClienteId(clienteId, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir itens do carrinho.", e);
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    private void atualizarUltimaAtividadePeloClienteId(int clienteId, Connection conn) throws SQLException {
        String sql = "UPDATE carrinho SET ultima_atividade = NOW() WHERE cliente_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
        }
    }

    public void removerTodosItensDoCarrinho(int carrinhoId, Connection conn) throws SQLException {
        String sql = "DELETE FROM carrinho_item WHERE id_carrinho = ?";
        PreparedStatement stmt = null; // Declare stmt fora do try-with-resources
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carrinhoId);
            stmt.executeUpdate();
            // Atualizar a 'ultima_atividade' do carrinho após remover todos os itens
            atualizarUltimaAtividade(carrinhoId, conn);
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    /*public void excluirCarrinhosInativos() {
        long tempoInatividadeLimite = TimeUnit.MINUTES.toMillis(4);
        String sqlSelecionarInativos = "SELECT id_carrinho FROM carrinho WHERE TIMESTAMPDIFF(MINUTE, ultima_atividade, NOW()) > 4";
        String sqlExcluirItens = "DELETE FROM carrinho_item WHERE id_carrinho = ?";
        String sqlExcluirCarrinho = "DELETE FROM carrinho WHERE id_carrinho = ?";
        Connection conn = null;
        PreparedStatement pstmtSelecionar = null;
        PreparedStatement pstmtExcluirItens = null;
        PreparedStatement pstmtExcluirCarrinho = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) throw new SQLException("Erro ao conectar ao banco de dados");

            pstmtSelecionar = conn.prepareStatement(sqlSelecionarInativos);
            rs = pstmtSelecionar.executeQuery();

            pstmtExcluirItens = conn.prepareStatement(sqlExcluirItens);
            pstmtExcluirCarrinho = conn.prepareStatement(sqlExcluirCarrinho);

            while (rs.next()) {
                int idCarrinho = rs.getInt("id_carrinho");

                // Excluir itens do carrinho
                pstmtExcluirItens.setInt(1, idCarrinho);
                pstmtExcluirItens.executeUpdate();

                // Excluir o carrinho
                pstmtExcluirCarrinho.setInt(1, idCarrinho);
                pstmtExcluirCarrinho.executeUpdate();

                System.out.println("Carrinho inativo (ID: " + idCarrinho + ") removido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir carrinhos inativos.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtSelecionar != null) pstmtSelecionar.close();
                if (pstmtExcluirItens != null) pstmtExcluirItens.close();
                if (pstmtExcluirCarrinho != null) pstmtExcluirCarrinho.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/


    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}
