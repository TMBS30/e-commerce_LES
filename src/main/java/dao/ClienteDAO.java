package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import util.Conexao;

import java.util.List;
import java.util.ArrayList;
import dominio.*;


public class ClienteDAO implements IDAO{
    public String salvar(EntidadeDominio entidade, Connection conn) {
        String clienteId = null;
        String sqlCliente = "INSERT INTO cliente(nome, cpf, email, senha, genero, dataNascimento, telefone_id, status_ativo) VALUES (?,?,?,?,?,?,?,?)";
        //Connection conn = null;
        PreparedStatement mysqlCliente = null;

        try {
            // Criar uma conexão com o banco de dados
            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false);  // Desabilita o autocommit para controle manual

            Cliente cliente = (Cliente) entidade;

            // Salvar telefone utilizando a mesma conexão
            Telefone telefone = cliente.getTelefone();
            TelefoneDAO telefoneDAO = new TelefoneDAO();
            String telefoneId = telefoneDAO.salvar(telefone, conn);
            if (telefoneId == null) {
                throw new Exception("Falha ao salvar o telefone.");
            }

            // Inserir cliente
            mysqlCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
            mysqlCliente.setString(1, cliente.getNome());
            mysqlCliente.setString(2, cliente.getCpf());
            mysqlCliente.setString(3, cliente.getEmail());
            String senhaSemHash = cliente.getSenha();
            String senhaHash = senhaCriptografada(senhaSemHash);
            mysqlCliente.setString(4, senhaHash);
            mysqlCliente.setString(5, cliente.getGenero().getDescricao());
            mysqlCliente.setString(6, cliente.getDataNascimento());
            mysqlCliente.setInt(7, Integer.parseInt(telefoneId));
            mysqlCliente.setBoolean(8, true);
            mysqlCliente.executeUpdate();

            // Recupera o ID gerado para o cliente
            ResultSet rs = mysqlCliente.getGeneratedKeys();
            if (rs.next()) {
                clienteId = rs.getString(1);
                cliente.setId(Integer.parseInt(clienteId));
            } else {
                throw new Exception("Erro ao obter ID do cliente.");
            }

            // Salvar endereços utilizando a mesma conexão
            EnderecoDAO enderecoDAO = new EnderecoDAO();
            if (cliente.getEnderecoResidencial() != null) {
                cliente.getEnderecoResidencial().setIdCliente(Integer.parseInt(clienteId));
                enderecoDAO.salvar(cliente.getEnderecoResidencial(), conn);
            }
            if (cliente.getEnderecoCobranca() != null) {
                cliente.getEnderecoCobranca().setIdCliente(Integer.parseInt(clienteId));
                enderecoDAO.salvar(cliente.getEnderecoCobranca(), conn);
            }
            if (cliente.getEnderecoEntrega() != null) {
                cliente.getEnderecoEntrega().setIdCliente(Integer.parseInt(clienteId));
                enderecoDAO.salvar(cliente.getEnderecoEntrega(), conn);
            }

            // Commit da transação
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        } finally {
            try {
                if (mysqlCliente != null) mysqlCliente.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clienteId;
    }

    public Cliente buscarUltimoCliente() throws Exception {
        Connection conn = Conexao.createConnectionToMySQL();
        String sql = "SELECT * FROM cliente ORDER BY id DESC LIMIT 1"; // Pega o último cliente salvo

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome")); // Exemplo, ajuste conforme necessário
                return cliente;
            }
        } finally {
            if (conn != null) conn.close();
        }
        return null;
    }

    public int alterarSenha(int id, String novaSenha) throws Exception {
        int status = 0;
        String sql = "UPDATE cliente SET senha = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);

            // Configurar os parâmetros da consulta
            String senhaHash = senhaCriptografada(novaSenha);
            mysql.setString(1, senhaHash);
            mysql.setInt(2,id);

            // Executar a atualização da senha
            status = mysql.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar as conexões e recursos
            try {
                if (mysql != null) {
                    mysql.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return status;
    }

    public String senhaCriptografada(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes());

            // Converte o hash de bytes para uma representação hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Lide com exceções ou propague-as conforme necessário
            return null;
        }
    }

    public String alterar(EntidadeDominio entidade) {
        int status=0;
        String sql = "UPDATE cliente SET nome=?,"
                + "cpf=?, email=?, genero=?, dataNascimento=?, ddd=?, telefone=?, tipo_telefone=?, status_ativo=? WHERE id=?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);

            mysql.setString(1,((Cliente) entidade).getNome());
            mysql.setString(2,((Cliente) entidade).getCpf());
            mysql.setString(3,((Cliente) entidade).getEmail());
            mysql.setString(4,((Cliente) entidade).getGenero().getDescricao());
            mysql.setString(5,((Cliente) entidade).getDataNascimento());
            /*java.util.Date dataNascimento = ((Cliente) entidade).getDataNascimento();
            if (dataNascimento != null) {
                java.sql.Date sqlDate = new java.sql.Date(dataNascimento.getTime());
                mysql.setDate(5, sqlDate);
            } else {
                mysql.setNull(5, java.sql.Types.DATE);  // Se a data for nula, você pode definir o campo como NULL
            }*/
            mysql.setString(6,((Cliente) entidade).getTelefone().getDdd());
            mysql.setString(7,((Cliente) entidade).getTelefone().getNumero());
            mysql.setString(8,((Cliente) entidade).getTelefone().getTipo().getDescricao());
            //mysql.setBoolean(9,((Cliente) entidade).getStatus());
            mysql.setInt(10,((Cliente) entidade).getId());	//id que vai ser atualizado
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
        return "Cliente alterado com sucesso";
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

            // Criando o PreparedStatement
            String sql = "SELECT c.id, c.nome, c.cpf, c.email, c.genero, c.dataNascimento, " +
                    "c.status_ativo, " +
                    "CONCAT(t.ddd, '-', t.numero) AS telefone_formatado " +
                    "FROM cliente c " +
                    "LEFT JOIN telefones t ON c.telefone_id = t.id";
            stmt = conn.prepareStatement(sql);

            // Executando a consulta
            rs = stmt.executeQuery();

            List</*Cliente*/EntidadeDominio> listaClientes = new ArrayList<>();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setGenero(Genero.valueOf(rs.getString("genero")));
                cliente.setDataNascimento(rs.getString("dataNascimento"));
                //cliente.setStatus(rs.getBoolean("status_ativo"));

                // Captura do telefone formatado
                String telefoneFormatado = rs.getString("telefone_formatado");
                cliente.setTelefoneFormatado(telefoneFormatado);
                //cliente.setStatus(rs.getBoolean("status_ativo"));



                listaClientes.add(cliente);
            }
            System.out.println("Número de clientes encontrados: " + listaClientes.size()); // Log de quantos estados foram encontrados
            return listaClientes;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar clientes: " + e.getMessage());
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
        return null;
    }

    public Cliente consultarPorEmailESenha(String email, String senha, Connection conn) {
        //Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Modificação da consulta para verificar email e senha
            String sql = "SELECT c.id, c.nome, c.cpf, c.email, c.genero, c.dataNascimento, c.status_ativo " +
                    "FROM cliente c WHERE c.email = ? AND c.senha = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
             stmt.setString(2, senha);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setGenero(Genero.valueOf(rs.getString("genero")));
                cliente.setDataNascimento(rs.getString("dataNascimento"));
                cliente.setStatus(rs.getBoolean("status_ativo"));
                return cliente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o cliente", e);
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
        return null;  // Se não encontrar o cliente
    }

    public Cliente consultarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id, nome, cpf, email, genero, dataNascimento, status_ativo " +
                    "FROM cliente WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setGenero(Genero.valueOf(rs.getString("genero")));
                cliente.setDataNascimento(rs.getString("dataNascimento"));
                cliente.setStatus(rs.getBoolean("status_ativo"));
                return cliente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o cliente por ID", e);
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
        return null;  // Se não encontrar o cliente
    }


    public String excluir(EntidadeDominio entidade) {
        String sql = "DELETE from clientes WHERE id = ?";
        Connection conn = null;
        PreparedStatement mysql = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            mysql = conn.prepareStatement(sql);
            mysql.setInt(1, ((Cliente) entidade).getId());
            mysql.execute();
            System.out.println("Cliente excluído com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao excluir o cliente: " + e.getMessage();
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

    public List<Cliente> consultar(/*EntidadeDominio entidade*/) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Criando o PreparedStatement
            String sql = "SELECT c.id, c.nome, c.cpf, c.email, c.genero, c.dataNascimento, " +
                    "c.status_ativo, " +
                    "CONCAT(t.ddd, '-', t.numero) AS telefone_formatado " +
                    "FROM cliente c " +
                    "LEFT JOIN telefones t ON c.telefone_id = t.id";
            stmt = conn.prepareStatement(sql);

            // Executando a consulta
            rs = stmt.executeQuery();

            List<Cliente> listaClientes = new ArrayList<>();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setGenero(Genero.valueOf(rs.getString("genero")));
                cliente.setDataNascimento(rs.getString("dataNascimento"));
                //cliente.setStatus(rs.getBoolean("status_ativo"));

                // Captura do telefone formatado
                String telefoneFormatado = rs.getString("telefone_formatado");
                cliente.setTelefoneFormatado(telefoneFormatado);



                listaClientes.add(cliente);
            }
            System.out.println("Número de clientes encontrados: " + listaClientes.size()); // Log de quantos estados foram encontrados
            return listaClientes;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar clientes: " + e.getMessage());
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
        return null;
    }

    public Cliente selecionar(EntidadeDominio entidade) {
        Cliente cliente = null;
        String sql = "SELECT * FROM clientes WHERE id=?";
        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement mysql = conn.prepareStatement(sql);
            mysql.setInt(1,((Cliente) entidade).getId());
            ResultSet rs = mysql.executeQuery();

            while (rs.next()) {
                cliente = new Cliente();
                Telefone telefone = new Telefone();
                cliente.setId(rs.getInt(1));
                cliente.setNome(rs.getString(2));
                cliente.setCpf(rs.getString(3));
                cliente.setEmail(rs.getString(4));
                cliente.setSenha(rs.getString(5));
                cliente.setGenero(Genero.valueOf(rs.getString(6)));
                cliente.setDataNascimento(rs.getString(7));
                //cliente.setDataNascimento(rs.getDate(7));
                String ddd = rs.getString(8);
                telefone.setDdd(ddd);
                String numeroTelefone = rs.getString(9);
                telefone.setNumero(numeroTelefone);
                telefone.setTipo(TipoTelefone.valueOf(rs.getString(10)));
                telefone.setNumero(numeroTelefone);
                cliente.setTelefone(telefone);
                //cliente.setStatus(rs.getBoolean(11));
            }
        }catch(Exception e) {
            System.out.println(e);
        }

        return cliente;
    }
}
