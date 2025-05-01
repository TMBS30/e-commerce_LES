package controle;

import dao.ClienteDAO;
import dao.EnderecoDAO;
import dao.*;
import dominio.*;
import implementacao.*;
import util.Conexao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Fachada implements IFachada {
    private Map<String, IDAO> daos;
    private Map<String, List<IStrategy>> rns;
    public Fachada() {
        definirRNS();
        definirDAOS();
    }
    private void definirRNS() {
        rns = new HashMap<String, List<IStrategy>>();

        ValidarCliente vCliente = new ValidarCliente();
        ValidarCPF vCpf = new ValidarCPF();
        ValidarTelefone vTelefone = new ValidarTelefone();
        ValidarCartao vCartao = new ValidarCartao();
        ValidarEndereco vEndereco = new ValidarEndereco();

        List<IStrategy> rnsCliente = new ArrayList<IStrategy>(); //Cria instâncias de classes que implementam a interface IStrategy para validações específicas.
        rnsCliente.add(vCliente);
        rnsCliente.add(vCpf);
        rnsCliente.add(vTelefone);
        rnsCliente.add(vEndereco);
        rns.put(Cliente.class.getName(), rnsCliente); //adiciona a lista de estrategias a chave cliente.class.getname()

        List<IStrategy> rnsCartao = new ArrayList<IStrategy>();
        rnsCartao.add(vCartao);
        rns.put(Cartao.class.getName(), rnsCartao);

        List<IStrategy> rnsEndereco = new ArrayList<IStrategy>();
        rnsEndereco.add(vEndereco);
        rns.put(Endereco.class.getName(), rnsEndereco);
    }
    //associar cada entidade ao seu respectivo DAO.
    private void definirDAOS() {
        daos = new HashMap<String, IDAO>();
        daos.put(Cliente.class.getName(), new ClienteDAO());
        daos.put(Cartao.class.getName(), new CartaoDAO());
        daos.put(Endereco.class.getName(), new EnderecoDAO());
        daos.put(CarrinhoItem.class.getName(), new CarrinhoItemDAO());
        daos.put(Item.class.getName(), new ItemDAO());
        //daos.put(Carrinho.class.getName(), new ItemDAO());
        daos.put(Livro.class.getName(), new LivroDAO());
    }

    public String salvarCartao(Cartao cartao) throws Exception{
        String msg = executarValidacoesCartao(cartao);
        System.out.println("01 Mensagem de validação: " + msg);

        if (msg == null) {
            CartaoDAO cartaoDAO = new CartaoDAO();
            ClienteDAO clienteDAO = new ClienteDAO();
            Connection conn = Conexao.createConnectionToMySQL();


            try {
                Cliente cliente = clienteDAO.buscarUltimoCliente();
                System.out.println("\n01 Fachada ID (salvarCartao) Cliente: " + cliente + "\n");

                if (cliente != null) {
                    cartao.setIdCliente(cliente.getId());
                    System.out.println("ID Cliente associado ao cartao: " + cartao.getIdCliente());
                    String resultado = cartaoDAO.salvar(cartao, conn);
                    System.out.println("Resultado do salvar: " + resultado);
                    return resultado;
                } else {
                    return "Erro: Nenhum cliente encontrado para associar ao cartao.";
                }
            } finally {
                //if (conn != null) conn.close();
            }
        }else {
            System.out.println("01 Mensagem de validação: " + msg);
            return msg;
        }
    }

    public String alterarCartao(Cartao cartao) throws Exception {
        String msg = executarValidacoesCartao(cartao);
        System.out.println("01 Mensagem de validação (alterarCartao): " + msg);

        if (msg == null) {
            CartaoDAO cartaoDAO = new CartaoDAO();
            Connection conn = Conexao.createConnectionToMySQL();

            try {
                // Verificação se o cartão existe
                Cartao cartaoExistente = cartaoDAO.consultarCartaoPorId(cartao.getId());
                if (cartaoExistente == null) {
                    return "Erro: Cartão não encontrado.";
                }

                // Atualizar dados do cartão
                String resultado = cartaoDAO.alterar(cartao, conn);
                System.out.println("Resultado da alteração: " + resultado);
                return resultado;
            } finally {
                // if (conn != null) conn.close();
            }
        } else {
            System.out.println("01 Mensagem de validação (alterarCartao): " + msg);
            return msg;
        }
    }

    private String executarValidacoesCartao(Cartao cartao) {
        List<IStrategy> regras = rns.get(Cartao.class.getName());
        StringBuilder msg = new StringBuilder();

        if (regras != null) {
            for (IStrategy regra : regras) {
                String resultado = regra.processar(cartao);
                if (resultado != null && !resultado.isEmpty()) {
                    msg.append(resultado).append("\n");
                }
            }
        }

        return msg.length() > 0 ? msg.toString() : null;
    }

    @Override
    public String salvarEndereco(Endereco endereco) throws Exception {
        // Verifica se o endereço é válido
        String msg = executarValidacoesEndereco(endereco);
        System.out.println("01 Mensagem de validação: " + msg);

        if (msg == null) {
            EnderecoDAO enderecoDAO = new EnderecoDAO();
            ClienteDAO clienteDAO = new ClienteDAO();
            //Connection conn = null;
            Connection conn = Conexao.createConnectionToMySQL();

            try {
                Cliente cliente = clienteDAO.buscarUltimoCliente();
                System.out.println("\n01 Fachada ID (salvarEndereco) Cliente: " + cliente + "\n");

                if (cliente != null) {
                    endereco.setIdCliente(cliente.getId());
                    System.out.println("ID Cliente associado ao Endereco: " + endereco.getIdCliente());
                    String resultado = enderecoDAO.salvar(endereco, conn);
                    System.out.println("Resultado do salvar: " + resultado);
                    return resultado;
                } else {
                    return "Erro: Nenhum cliente encontrado para associar ao endereço.";
                }
            } finally {
                //if (conn != null) conn.close();
            }
        } else {
            System.out.println("01 Mensagem de validação: " + msg);
            return msg;
        }
    }

    private String executarValidacoesEndereco(Endereco endereco) {
        List<IStrategy> regras = rns.get(Endereco.class.getName());
        StringBuilder msg = new StringBuilder();

        if (regras != null) {
            for (IStrategy regra : regras) {
                String resultado = regra.processar(endereco);
                if (resultado != null && !resultado.isEmpty()) {
                    msg.append(resultado).append("\n");
                }
            }
        }

        return msg.length() > 0 ? msg.toString() : null;
    }



    @Override
    public String salvar(EntidadeDominio entidade) throws Exception {
        String classe = entidade.getClass().getName();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String metodo = stackTrace[2].getMethodName();

        String msg = executar(entidade);  // Chama as regras de negócio (RNS)

        if (msg == null) {
            IDAO dao = daos.get(classe);
            LoggerDAO log = new LoggerDAO();
            String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logMessage = formattedDate + " " + classe + "." + metodo;
            log.saveLogToDatabase(logMessage);  // Passando log completo

            Connection conn = Conexao.createConnectionToMySQL();  // Supondo que você tenha um método para criar a conexão

            try {
                // Salvar o cliente
                String resultadoCliente = dao.salvar(entidade, conn);

                EnderecoDAO enderecoDAO = new EnderecoDAO();

                // Verificar e salvar os endereços
                if (entidade instanceof Cliente) {
                    Cliente cliente = (Cliente) entidade;
                    int clienteId = cliente.getId();
                    System.out.println("\nFachada ID Cliente: " + clienteId + "\n");

                    // Salvar o endereço residencial, se não for null
                    Endereco enderecoResidencial = cliente.getEnderecoResidencial();
                    if (enderecoResidencial != null) {
                        enderecoResidencial.setIdCliente(clienteId);  // Atribuindo o idCliente
                        //EnderecoDAO enderecoDAO = new EnderecoDAO();
                        /*String resultadoResidencial = enderecoDAO.salvar(enderecoResidencial);
                        if (resultadoResidencial != null && resultadoResidencial.equals("Endereço residencial salvo com sucesso")) {
                            return "Erro ao salvar o endereço residencial";
                        }*/
                        enderecoDAO.salvar(enderecoResidencial, conn);
                        System.out.println("\nFachada ID Cliente com End: " + clienteId + "\n");
                    }

                    // Salvar o endereço de cobrança, se não for null
                    Endereco enderecoCobranca = cliente.getEnderecoCobranca();
                    if (enderecoCobranca != null) {
                        enderecoCobranca.setIdCliente(clienteId);  // Atribuindo o idCliente
                        //EnderecoDAO enderecoDAO = new EnderecoDAO();
                        /*String resultadoCobranca = enderecoDAO.salvar(enderecoCobranca);
                        if (resultadoCobranca != null && resultadoCobranca.equals("Endereço de cobrança salvo com sucesso")) {
                            return "Erro ao salvar o endereço de cobrança";
                        }*/
                        enderecoDAO.salvar(enderecoCobranca, conn);
                    }

                    // Salvar o endereço de entrega, se não for null
                    Endereco enderecoEntrega = cliente.getEnderecoEntrega();
                    if (enderecoEntrega != null) {
                        enderecoEntrega.setIdCliente(clienteId);  // Atribuindo o idCliente
                        //EnderecoDAO enderecoDAO = new EnderecoDAO();
                        /*String resultadoEntrega = enderecoDAO.salvar(enderecoEntrega);
                        if (resultadoEntrega != null && resultadoEntrega.equals("Endereço de entrega salvo com sucesso")) {
                            return "Erro ao salvar o endereço de entrega";
                        }*/
                        enderecoDAO.salvar(enderecoEntrega, conn);
                    }
                }
                return resultadoCliente;
            }catch (SQLException e){
                e.printStackTrace();
                return "Erro salvar o cliente ou endereco";
            }finally {
                if(conn != null)conn.close();
            }
        } else {
            return msg;
        }
    }



    @Override
    public String alterar(EntidadeDominio entidade) {
        String classe = entidade.getClass().getName();
        // Determinar qual método chamou alterar
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String metodo = stackTrace[2].getMethodName();

        IDAO dao = daos.get(classe);
        dao.alterar(entidade);

        // Criar o log com a data, classe e método
        LoggerDAO log = new LoggerDAO();
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Concatenando data, classe e método para gerar uma única string de log
        String logMessage = formattedDate + " " + classe + "." + metodo;

        // Passando a string concatenada para o método de log
        log.saveLogToDatabase(logMessage);

        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        //Busca o DAO apropriado para a classe da entidade no mapa daos
        String classe = entidade.getClass().getName();
        IDAO dao = daos.get(classe);
        return dao.consultar(entidade);
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        String classe = entidade.getClass().getName();
        IDAO dao = daos.get(classe);
        dao.excluir(entidade);
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        String classe = entidade.getClass().getName();
        IDAO dao = daos.get(classe);
        return dao.selecionar(entidade);
    }

    private String executar(EntidadeDominio entidade){
        String classe = entidade.getClass().getName();

        List<IStrategy> rnEntidade = rns.get(classe);

        StringBuilder sb = new StringBuilder();
        for(IStrategy s:rnEntidade) {
            String msg =s.processar(entidade);
            if(msg != null) {
                sb.append(msg);
            }
        }
        if(sb.length()>0) {
            return sb.toString();
        }else {
            return null;
        }
    }

    // Método para buscar um país por nome
    /*public Pais buscarPaisPorNome(String nomePais) {
        PaisDAO paisDAO = new PaisDAO();
        return paisDAO.buscarPorNome(nomePais);  // Retorna um objeto Pais encontrado no banco
    }

    // Método para buscar um estado por nome, associado a um país
    public Estado buscarEstadoPorNome(String nomeEstado, Pais pais) {
        EstadoDAO estadoDAO = new EstadoDAO();
        return estadoDAO.buscarPorNome(nomeEstado, pais);  // Retorna um objeto Estado relacionado ao País
    }

    // Método para buscar uma cidade por nome, associada a um estado
    public Cidade buscarCidadePorNome(String nomeCidade, Estado estado) {
        CidadeDAO cidadeDAO = new CidadeDAO();
        return cidadeDAO.buscarPorNome(nomeCidade, estado);  // Retorna um objeto Cidade relacionado ao Estado
    }*/

}