package dao;

import dominio.*;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoDAO implements IDAO{

    //CARRINHO SERA SALVO, NO CADASTRO DO CLIENTE, POIS CARRINHO, SERVE APENAS PARA ASSOCIAR UM CARRINO A UM CLIENTE
    //QUEM VAI ARMAZENAR OS ITENS NO CARRINHO, É CARRINHO_ITEM
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public String salvar(Carrinho carrinho, Connection conn) throws SQLException {
        String sql = "INSERT INTO carrinho (bloqueado_carrinho, data_bloqueio_carrinho, valor_carrinho, cliente_id) VALUES (?, ?, ?, ?)";
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
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao alterar a quantidade do item do carrinho.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Fechar recursos
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

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) throw new SQLException("Erro ao conectar ao banco de dados");

            String sql = "SELECT c.id_carrinho, c.bloqueado_carrinho, c.data_bloqueio_carrinho, c.valor_carrinho, " +
                    "ci.id, ci.quantidade, ci.id_livro, " +
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

            List<CarrinhoItem> itensCarrinho = new ArrayList<>();
            carrinho = new Carrinho();
            carrinho.setItens(itensCarrinho);

            while (rs.next()) {
                if (carrinho.getId() == 0) {
                    carrinho.setId(rs.getInt("id_carrinho"));
                    carrinho.setBloqueado(rs.getBoolean("bloqueado_carrinho"));
                    carrinho.setDataBloqueio(rs.getString("data_bloqueio_carrinho"));
                    carrinho.setValorCarrinho(rs.getDouble("valor_carrinho"));
                }

                if (rs.getInt("id_item") == 0) continue;

                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
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
                item.setDataEntrada(rs.getString("data_entrada_item"));
                item.setIdFornecedor(rs.getInt("id_fornec"));
                item.setLivroId(livro.getId());
                item.setLivro(livro);

                CarrinhoItem carrinhoItem = new CarrinhoItem(

                        rs.getInt("id_carrinho"),
                        rs.getInt("id_livro"),
                        rs.getInt("quantidade"),
                        item,
                        rs.getInt("id")
                );

                itensCarrinho.add(carrinhoItem);
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
                    throw new SQLException("Carrinho não encontrado para o cliente.");
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir itens do carrinho.", e);
        } finally {
            if (stmt != null) stmt.close();
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
