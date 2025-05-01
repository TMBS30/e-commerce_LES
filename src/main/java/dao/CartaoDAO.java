package dao;

import dominio.BandeiraCartao;
import dominio.Cartao;
import dominio.Cliente;
import dominio.EntidadeDominio;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import org.apache.commons.lang3.StringUtils;


public class CartaoDAO implements IDAO{
    public String salvar(Cartao cartao, Connection conn) throws SQLException  {

        String sql = "INSERT INTO cartao(numero,nomeTitular,codSeguranca,data_vencimento,preferencial,cliente_id,bandeiraCartao_id) VALUES (?,?,?,?,?,?,?)";
        //Connection conn = null;
        PreparedStatement mysql = null;

        try {
            // criar uma conexao com o banco de dados
            //conn = Conexao.createConnectionToMySQL();

            BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
            int bandeiraId = bandeiraCartaoDAO.consultarIdBandeira(cartao.getBandeiraCartao().getDescricao(), conn);

            if (bandeiraId == -1) {
                return "Erro: Um ou mais IDs não encontrados!";
            }
            // foi criada uma prepareStatement para executar uma Query
            mysql = conn.prepareStatement(sql);
            String numeroMascarado = mascaraNumeroCartao((cartao).getNumero());
            mysql.setString(1,numeroMascarado);
            mysql.setString(2, cartao.getNomeTitular());
            String codSegurancaMascarado = mascaraCodSeguranca((cartao).getCodSeguranca());
            mysql.setString(3,codSegurancaMascarado);
            mysql.setString(4,cartao.getDataVencimento());
            mysql.setBoolean(5,cartao.isPreferencial());
            mysql.setInt(6,cartao.getIdCliente());
            mysql.setInt(7,cartao.getBandeiraCartao().getId());
            mysql.executeUpdate();
            System.out.println("Cartão salvo com sucesso");
            return "Cartão salvo com sucesso";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar o cartão", e);
        } finally {
            if (mysql != null) mysql.close();
        }
    }

    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    public String alterar(EntidadeDominio entidade, Connection conn) {
        int status = 0;
        String sql = "UPDATE cartao SET numero=?,"
                + "nomeTitular=?, codSeguranca=?, preferencial=?, bandeiraCartao_id=?, data_vencimento=? WHERE id=?";
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);

            // Atribuindo os valores da entidade ao PreparedStatement
            Cartao cartao = (Cartao) entidade;

            BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
            int bandeiraId = bandeiraCartaoDAO.consultarIdBandeira(cartao.getBandeiraCartao().getDescricao(), conn);

            if (bandeiraId == -1) {
                throw new RuntimeException("Erro: Bandeira não encontrada!");
            }

            mysql.setString(1, cartao.getNumero());
            mysql.setString(2, cartao.getNomeTitular());
            mysql.setString(3, cartao.getCodSeguranca());
            mysql.setBoolean(4, cartao.isPreferencial());
            mysql.setInt(5, cartao.getBandeiraCartao().getId()); // Passando o ID da bandeira, não a descrição
            mysql.setString(6, cartao.getDataVencimento());
            mysql.setInt(7, cartao.getId());  // ID do cartão a ser atualizado
            mysql.executeUpdate(); // Executando a atualização no banco

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mysql != null) {
                    mysql.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Cartão alterado com sucesso";
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Cliente cliente = (Cliente) entidade;
            int clienteId = cliente.getId();  // Usando o getId() do Cliente para obter o ID

            conn = Conexao.createConnectionToMySQL();

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Consulta SQL
            String sql = "SELECT c.id, c.numero, c.nomeTitular, c.codSeguranca, c.data_vencimento, c.preferencial, b.descricao_band " +
                    "FROM cartao AS c " +
                    "INNER JOIN bandeiras_cartao AS b ON c.bandeiraCartao_id = b.id_band " +
                    "WHERE c.cliente_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);  // Definindo o parâmetro para a consulta SQL

            rs = stmt.executeQuery();

            // Criando a lista de cartões
            List<EntidadeDominio> listaCartoes = new ArrayList<>();

            while (rs.next()) {
                Cartao cartao = new Cartao();

                cartao.setId(rs.getInt("id"));
                cartao.setNumero(rs.getString("numero"));
                cartao.setNomeTitular(rs.getString("nomeTitular"));
                cartao.setCodSeguranca(rs.getString("codSeguranca"));
                cartao.setDataVencimento(rs.getString("data_vencimento"));
                cartao.setPreferencial(rs.getBoolean("preferencial"));

                String descricaoBandeira = rs.getString("descricao_band");
                cartao.setBandeiraCartao(BandeiraCartao.valueOf(descricaoBandeira != null ? descricaoBandeira : "Bandeira não identificada"));

                listaCartoes.add(cartao); // Adiciona o cartao à lista
            }

            System.out.println("Número de Cartões encontrados: " + listaCartoes.size()); // Log de quantos cartões foram encontrados
            return listaCartoes; // Retorna a lista de cartões
        } catch (SQLException e) {
            System.err.println("Erro ao consultar Cartões: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null; // Retorna null em caso de erro
    }


    public List<Cartao> consultarCartoesPorCliente(int clienteId) {
        List<Cartao> cartoes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT c.id, c.numero, c.nomeTitular, c.codSeguranca, c.data_vencimento, c.preferencial, " +
                    "b.descricao_band AS bandeira " +
                    "FROM cartao c " +
                    "JOIN bandeiras_cartao b ON c.bandeiraCartao_id = b.id_band " +
                    "WHERE c.cliente_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cartao cartao = new Cartao();
                cartao.setId(rs.getInt("id"));
                cartao.setNumero(rs.getString("numero"));
                cartao.setNomeTitular(rs.getString("nomeTitular"));
                cartao.setCodSeguranca(rs.getString("codSeguranca"));
                cartao.setDataVencimento(rs.getString("data_vencimento"));
                cartao.setPreferencial(rs.getBoolean("preferencial"));

                BandeiraCartao bandeira = BandeiraCartao.fromDescricao(rs.getString("bandeira"));
                cartao.setBandeiraCartao(bandeira);

                cartao.setIdCliente(clienteId);
                cartoes.add(cartao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar cartões por cliente", e);
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

        return cartoes;
    }

    public Cartao consultarCartaoPorId(int cartaoId) {
        Cartao cartao = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            String sql = "SELECT c.id, c.numero, c.nomeTitular, c.codSeguranca, c.data_vencimento, c.preferencial, " +
                    "b.descricao_band AS bandeira " +
                    "FROM cartao c " +
                    "JOIN bandeiras_cartao b ON c.bandeiraCartao_id = b.id_band " +
                    "WHERE c.id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartaoId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cartao = new Cartao();
                cartao.setId(rs.getInt("id"));
                cartao.setNumero(rs.getString("numero"));
                cartao.setNomeTitular(rs.getString("nomeTitular"));
                cartao.setCodSeguranca(rs.getString("codSeguranca"));
                cartao.setDataVencimento(rs.getString("data_vencimento"));
                cartao.setPreferencial(rs.getBoolean("preferencial"));

                BandeiraCartao bandeira = BandeiraCartao.fromDescricao(rs.getString("bandeira"));
                cartao.setBandeiraCartao(bandeira);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar cartão por ID", e);
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
        return cartao;
    }


    public String excluir(EntidadeDominio entidade) {
        String sql = "DELETE from cartao WHERE id = ?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);
            mysql.setInt(1, ((Cartao) entidade).getId());
            int linhasAfetadas = mysql.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Cartao excluído com sucesso");
                return null; // Sucesso
            } else {
                System.out.println("Cartão não encontrado.");
                return "Cartão não encontrado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao excluir o cartao: " + e.getMessage();
        } finally {
            try {
                if (mysql != null) mysql.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Cartao selecionar(EntidadeDominio entidade) {
        Cartao cartao = null;
        String sql = "SELECT * FROM cartao WHERE id=?";
        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement mysql = conn.prepareStatement(sql);
            mysql.setInt(1,((Cartao) entidade).getId());
            ResultSet rs = mysql.executeQuery();

            while (rs.next()) {
                cartao = new Cartao();
                cartao.setId(rs.getInt(1));
                cartao.setNumero(rs.getString(2));
                cartao.setNomeTitular(rs.getString(3));
                cartao.setCodSeguranca(rs.getString(4));
                cartao.setPreferencial(rs.getBoolean(5));
            }
        }catch(Exception e) {
            System.out.println(e);
        }

        return cartao;
    }
    private String repetirCaracter(char c, int vezes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vezes; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    private String mascaraNumeroCartao(String numeroCartao) {
        // Mascarar o número do cartão para exibir apenas os últimos 4 dígitos
        int tamanho = numeroCartao.length();
        String ultimosQuatroDigitos = numeroCartao.substring(tamanho - 4, tamanho);
        String mascara = repetirCaracter('*', Math.max(0, tamanho - 4)) + ultimosQuatroDigitos;
        return mascara;
    }
    
    private String mascaraCodSeguranca(String codSeguranca) {
        int tamanho = codSeguranca.length();
        String mascara = repetirCaracter('*', tamanho);
        return mascara;
    }
}