package dao;

import dominio.*;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO implements IDAO{
    public String salvar(Endereco endereco, Connection conn) throws SQLException {
        String sql = "INSERT INTO endereco(cep, logradouro, numero, bairro, cliente_id, tipo_endereco_id, tipo_logradouro_id, tipo_residencia_id, cidade_id, observacao) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement mysql = null;
        //Connection conn = null;
        try {
            //conn = Conexao.createConnectionToMySQL();
            CidadeDAO cidadeDAO = new CidadeDAO();
            EstadoDAO estadoDAO = new EstadoDAO();
            PaisDAO paisDAO = new PaisDAO();
            TipoEnderecoDAO tipoEnderecoDAO = new TipoEnderecoDAO();
            TipoLogradouroDAO tipoLogradouroDAO = new TipoLogradouroDAO();
            TipoResidenciaDAO tipoResidenciaDAO = new TipoResidenciaDAO();
            //mysql = conn.prepareStatement(sql);
            int cidadeId = cidadeDAO.consultarIdCidades(endereco.getCidade().getNome(), conn);
            int estadoId = estadoDAO.consultarIdEstados(endereco.getCidade().getEstado().getNome(), conn);
            int paisId = paisDAO.consultarIdPaises(endereco.getCidade().getEstado().getPais().getNome(), conn);
            int tipoEnderecoId = tipoEnderecoDAO.consultarIdTipoEndereco(endereco.getTipoEndereco().getDescricao(), conn);
            int tipoLogradouroId = tipoLogradouroDAO.consultarIdTipoLogradouro(endereco.getTipoLogradouro().getDescricao(), conn);
            int tipoResidenciaId = tipoResidenciaDAO.consultarIdTipoResidencia(endereco.getTipoResidencia().getDescricao(), conn);

            if (cidadeId == -1 || estadoId == -1 || paisId == -1 || tipoEnderecoId == -1 || tipoLogradouroId == -1 || tipoResidenciaId == -1) {
                return "Erro: Um ou mais IDs não encontrados!";
            }
            mysql = conn.prepareStatement(sql);
            mysql.setString(1, endereco.getCep());
            mysql.setString(2, endereco.getLogradouro());
            mysql.setInt(3, Integer.parseInt(endereco.getNumero()));
            mysql.setString(4, endereco.getBairro());
            mysql.setInt(5, endereco.getIdCliente());
            mysql.setInt(6, tipoEnderecoId);
            mysql.setInt(7, tipoLogradouroId);
            mysql.setInt(8, tipoResidenciaId);
            mysql.setInt(9, cidadeId);
            mysql.setString(10, endereco.getObservacao());
            mysql.executeUpdate();
            System.out.println("Endereço salvo com sucesso");
            return "Endereço salvo com sucesso";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar o endereco", e);
        } finally {
            if (mysql != null) mysql.close();
        }
    }


    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public String alterar(EntidadeDominio entidade) {
        int status=0;
        String sql = "UPDATE endereco SET cep=?, logradouro=?, numero=?, bairro=?,cliente_id=?, tipo_endereco_id=?, tipo_logradouro_id=?, tipo_residencia_id=?, observacao=? WHERE id=?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);

            mysql.setString(1,((Endereco)entidade).getCep());
            mysql.setString(2,((Endereco)entidade).getLogradouro());
            mysql.setString(3,((Endereco)entidade).getNumero());
            mysql.setString(4,((Endereco)entidade).getBairro());
            mysql.setInt(5,((Endereco)entidade).getIdCliente());
            mysql.setString(6,((Endereco)entidade).getTipoEndereco().getDescricao());
            mysql.setInt(7,((Endereco)entidade).getTipoLogradouro().getId());
            mysql.setInt(8,((Endereco)entidade).getTipoResidencia().getId());
            mysql.setString(9,((Endereco)entidade).getObservacao());
            mysql.setInt(10,((Endereco)entidade).getId());
            status = mysql.executeUpdate();

        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(mysql!=null) {
                    mysql.close();
                }
                if(conn!=null) {
                    conn.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Endereco alterado com sucesso";
    }

    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        List<EntidadeDominio> listaEnderecos = new ArrayList<>();
        String sql = "SELECT id,cep,logradouro,numero,bairro,cliente_id, descricao_tip_end, descricao_tip_log, descricao_tip_res FROM endereco as a INNER JOIN tipos_endereco as b ON a.tipo_endereco_id=b.id_tip_end INNER JOIN tipos_logradouro as c ON a.tipo_logradouro_id=c.id_tip_log INNER JOIN tipos_residencia as d ON a.tipo_residencia_id=d.id_tip_res WHERE cliente_id=?";

        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement mysql = conn.prepareStatement(sql);
            mysql.setInt(1,((Endereco) entidade).getIdCliente());

            ResultSet rs = mysql.executeQuery();

            while (rs.next()) {
                Endereco endereco = new Endereco();
                endereco.setId(rs.getInt(1));
                endereco.setCep(rs.getString(2));
                endereco.setLogradouro(rs.getString(3));
                endereco.setNumero(rs.getString(4));
                endereco.setBairro(rs.getString(5));
                endereco.setIdCliente(rs.getInt(6));
                endereco.setTipoEndereco(TipoEndereco.valueOf(rs.getString(7)));
                endereco.setTipoLogradouro(TipoLogradouro.valueOf(rs.getString(8)));
                endereco.setTipoResidencia(TipoResidencia.valueOf(rs.getString(9)));
                listaEnderecos.add(endereco);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEnderecos;
    }

    public List<Endereco> consultarPorCliente(int clienteId) {
        List<Endereco> enderecos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT e.id, e.cep, e.logradouro, e.numero, e.bairro, e.cidade_id, " +
                    "e.observacao, c.nome_cidade AS cidade_nome, es.nome_estado AS estado_nome, " +
                    "te.descricao_tip_end, tl.descricao_tip_log, tr.descricao_tip_res " +
                    "FROM endereco e " +
                    "INNER JOIN cidades c ON e.cidade_id = c.id_cidade " +
                    "INNER JOIN estados es ON c.id_estado = es.id_estado " +
                    "INNER JOIN tipos_endereco te ON e.tipo_endereco_id = te.id_tip_end " +
                    "INNER JOIN tipos_logradouro tl ON e.tipo_logradouro_id = tl.id_tip_log " +
                    "INNER JOIN tipos_residencia tr ON e.tipo_residencia_id = tr.id_tip_res " +
                    "WHERE e.cliente_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clienteId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Endereco endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCep(rs.getString("cep"));
                endereco.setLogradouro(rs.getString("logradouro"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setBairro(rs.getString("bairro"));

                // Cidade e Estado
                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cidade_id"));
                cidade.setNome(rs.getString("cidade_nome"));
                endereco.setCidade(cidade);
                Estado estado = new Estado();
                estado.setNome(rs.getString("estado_nome"));
                endereco.getCidade().setEstado(estado);

                endereco.setObservacao(rs.getString("observacao"));

                // Tipos de Endereço, Logradouro e Residência
                TipoEndereco tipoEndereco = TipoEndereco.fromDescricao(rs.getString("descricao_tip_end"));
                TipoLogradouro tipoLogradouro = TipoLogradouro.fromDescricao(rs.getString("descricao_tip_log"));
                TipoResidencia tipoResidencia = TipoResidencia.fromDescricao(rs.getString("descricao_tip_res"));

                endereco.setTipoEndereco(tipoEndereco);
                endereco.setTipoLogradouro(tipoLogradouro);
                endereco.setTipoResidencia(tipoResidencia);

                enderecos.add(endereco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar os endereços por cliente", e);
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

        return enderecos;  // Retorna a lista de endereços encontrados
    }

    public Endereco consultarPorId(int idEndereco) {
        Endereco endereco = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT e.id, e.cep, e.logradouro, e.numero, e.bairro, e.cidade_id, " +
                    "e.observacao, c.nome_cidade AS cidade_nome, es.nome_estado AS estado_nome, " +
                    "te.descricao_tip_end, tl.descricao_tip_log, tr.descricao_tip_res " +
                    "FROM endereco e " +
                    "INNER JOIN cidades c ON e.cidade_id = c.id_cidade " +
                    "INNER JOIN estados es ON c.id_estado = es.id_estado " +
                    "INNER JOIN tipos_endereco te ON e.tipo_endereco_id = te.id_tip_end " +
                    "INNER JOIN tipos_logradouro tl ON e.tipo_logradouro_id = tl.id_tip_log " +
                    "INNER JOIN tipos_residencia tr ON e.tipo_residencia_id = tr.id_tip_res " +
                    "WHERE e.id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEndereco);

            rs = stmt.executeQuery();

            if (rs.next()) {
                endereco = new Endereco();
                endereco.setId(rs.getInt("id"));
                endereco.setCep(rs.getString("cep"));
                endereco.setLogradouro(rs.getString("logradouro"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setBairro(rs.getString("bairro"));

                // Cidade e Estado
                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cidade_id"));
                cidade.setNome(rs.getString("cidade_nome"));
                endereco.setCidade(cidade);
                Estado estado = new Estado();
                estado.setNome(rs.getString("estado_nome"));
                endereco.getCidade().setEstado(estado);

                endereco.setObservacao(rs.getString("observacao"));

                // Tipos de Endereço, Logradouro e Residência
                TipoEndereco tipoEndereco = TipoEndereco.fromDescricao(rs.getString("descricao_tip_end"));
                TipoLogradouro tipoLogradouro = TipoLogradouro.fromDescricao(rs.getString("descricao_tip_log"));
                TipoResidencia tipoResidencia = TipoResidencia.fromDescricao(rs.getString("descricao_tip_res"));

                endereco.setTipoEndereco(tipoEndereco);
                endereco.setTipoLogradouro(tipoLogradouro);
                endereco.setTipoResidencia(tipoResidencia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o endereço por ID", e);
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

        return endereco; // Retorna o endereço encontrado (ou null se não encontrado)
    }

    public String excluir(EntidadeDominio entidade) {
        String sql = "DELETE from endereco WHERE id = ?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);
            mysql.setInt(1, ((Endereco) entidade).getId());
            mysql.execute();

            System.out.println("Endereco excluído com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao excluir o endereco: " + e.getMessage();
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
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        Endereco endereco = null;
        String sql = "SELECT * FROM endereco WHERE id=?";
        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement mysql = conn.prepareStatement(sql);
            mysql.setInt(1,((Endereco) entidade).getId());
            ResultSet rs = mysql.executeQuery();

            while (rs.next()) {
                endereco = new Endereco();
                endereco.setId(rs.getInt(1));
                endereco.setCep(rs.getString(2));
                endereco.setLogradouro(rs.getString(3));
                endereco.setNumero(rs.getString(1));
                endereco.setBairro(rs.getString(5));
                endereco.setIdCliente(Integer.parseInt(rs.getString(6)));
            }
        }catch(Exception e) {
            System.out.println(e);
        }
        System.out.println(endereco);
        return endereco;
    }
}

