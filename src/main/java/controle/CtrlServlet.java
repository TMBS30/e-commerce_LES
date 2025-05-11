package controle;

import java.io.IOException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.*;
import dominio.*;
import implementacao.IStrategy;
import implementacao.ValidarLogin;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import util.Conexao;

@WebServlet("/servlet")
public class CtrlServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("Action recebida: " + action);
        String id = request.getParameter("id");
        System.out.println("ID recebido: " + id);


        if (action == null || action.isEmpty()) {
            action = "home";
        }
        switch (action) {
            case "consultar":
                consultarCliente(request, response);
                break;
            case "home":
                homePage(request, response);
                break;
            case "voltarHomePage":
                voltarHomePage(request, response);
                break;
            case "voltarHomePageADM":
                voltarHomePageADM(request, response);
                break;
            case "consultarInfoPessoais":
                consultarInfoPessoais(request, response);
                break;
            case "consultarEndereco":
                consultarEnderecoPessoal(request, response);
                break;
            case "cadastro":
                cadastrarCliente(request, response);
                break;
            case "consultarCartao":
                consultarCartaoPessoal(request, response);
                break;
            case "editarCartaoForm":
                editarCartaoForm(request, response);
                System.out.println("Em GET editarCartaoForm");
                break;
            case "exibirCupons":
                exibirCupons(request, response);
                break;
            case "consultarProduto":
                consultarProduto(request, response);
                break;
           case "cartaoCompra":
                cartaoCompra(request, response);
                break;
            case "enderecoEntregaCompra":
                enderecoEntregaCompra(request, response);
                break;
            case "enderecoCobrancaCompra":
                enderecoCobrancaCompra(request, response);
                break;
            case "exibirPedidos":
                exibirPedidos(request, response);
                break;
            case "exibirPedidosADM":
                try {
                    exibirPedidosADM(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "solicitarTroca":
                solicitarTroca(request, response);
                System.out.printf("No switch case de troca");
                break;
            case "logout":
                logoutAction(request, response);
                break;
            case "esvaziarCarrinhoInativo":
                esvaziarCarrinhoInativo(request, response);
                break;
            case "pingAtividadeCarrinho":
                pingAtividadeCarrinho(request, response);
                break;
            case "exibirEstoque":
                exibirEstoque(request, response);
                break;
            case "addcliente":
                System.out.println("Redirecionando para addClienteForm.jsp");
                request.getRequestDispatcher("addClienteForm.jsp").forward(request, response);
                return;
            default:
                System.out.println("Ação desconhecida: " + action);
                break;
        }

        EstadoDAO estadoDAO = new EstadoDAO();
        List<Estado> estados = estadoDAO.consultar();

        CidadeDAO cidadeDAO = new CidadeDAO();
        List<Cidade> cidades = cidadeDAO.consultar();

        PaisDAO paisDAO = new PaisDAO();
        List<Pais> paises = paisDAO.consultar();

        TipoEnderecoDAO tipoEnderecoDAO = new TipoEnderecoDAO();
        List<TipoEndereco> tipoEnderecos = tipoEnderecoDAO.consultar();

        TipoLogradouroDAO tipoLogradouroDAO = new TipoLogradouroDAO();
        List<TipoLogradouro> tipoLogradouros = tipoLogradouroDAO.consultar();

        TipoResidenciaDAO tipoResidenciaDAO = new TipoResidenciaDAO();
        List<TipoResidencia> tipoResidencias = tipoResidenciaDAO.consultar();

        TipoTelefoneDAO tipoTelefoneDAO = new TipoTelefoneDAO();
        List<TipoTelefone> tipoTelefones = tipoTelefoneDAO.consultar();

        BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
        List<BandeiraCartao> bandeiraCartaoLista = bandeiraCartaoDAO.consultar();

        request.setAttribute("listaBandeiras", bandeiraCartaoLista);
        request.setAttribute("listaEstados", estados);
        request.setAttribute("listaCidades", cidades);
        request.setAttribute("listaPaises", paises);
        request.setAttribute("listaTiposEnd", tipoEnderecos);
        request.setAttribute("listaTiposLog", tipoLogradouros);
        request.setAttribute("listaTiposRes", tipoResidencias);
        request.setAttribute("listaTiposTel", tipoTelefones);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            action = "salvarCliente";
        }

        switch (action) {
            case "login":
                loginAction(request, response);
                return;
            case "salvarCliente":
                try {
                    salvarCliente(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "logout":
                logoutAction(request, response);
                break;
            case "salvarEndereco":
                salvarEndereco(request, response);
                break;
            case "salvarNovoEnderecoForm":
                salvarNovoEnderecoForm(request, response);
                break;
            case "salvarNovoEndereco":
                salvarNovoEndereco(request, response);
                break;
            case "salvarNovoCartaoForm":
                salvarNovoCartaoForm(request, response);
                break;
            case "salvarNovoCartao":
                salvarNovoCartao(request, response);
                break;
            case "excluirCartao":
                excluirCartao(request, response);
                break;
            case "editarCartao":
                editarCartao(request, response);
                break;
            case "registrarEntradaEstoque":
                registrarEntradaEstoque(request, response);
                break;
            case "adicionarCarrinho":
                adicionarCarrinho(request, response);
                break;
            case "alterarQuantidade": // Ação para a alteração via AJAX
                alterarQuantidadeCarrinho(request, response);
                break;
            case "removerItemCarrinho":
                removerItemCarrinho(request, response);
                break;
            case "pingAtividadeCarrinho":
                pingAtividadeCarrinho(request, response);
                break;
            case "aplicarCupom":
                aplicarCupom(request, response);
                break;
            case "salvarPreviaPedido":
                salvarPreviaPedido(request, response);
                break;
            case "salvarEnderecoEntregaCompra":
                salvarEnderecoEntregaCompra(request, response);
                break;
            case "salvarEnderecoCobrancaCompra":
                salvarEnderecoCobrancaCompra(request, response);
                break;
            case "salvarCartoesCompra":
                salvarCartoesCompra(request, response);
                break;
            case "confirmarCompra":
                confirmarCompra(request, response);
                break;
            case "atualizarStatusCompra":
                atualizarStatusCompra(request, response);
                break;
            case "autorizarTroca":
                try {
                    autorizarTroca(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                request.setAttribute("mensagemErro", "Ação não reconhecida.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        switch (action) {
            case "cadastro":
                cadastrarCliente(request, response);
                break;
            case "consultar":
                consultarCliente(request, response);
                break;
            case "excluir":
                excluirCliente(request, response);
                break;
            default:
                response.sendRedirect("erro.jsp");
        }
    }

    //COM CRIPTOGRAFIA
    private void loginAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email_login");
        String senha = request.getParameter("senha_login");

        AdministradorDAO admDAO = new AdministradorDAO();
        String senhaCriptografadaAdm = admDAO.senhaCriptografada(senha);
        Connection conn = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            Administrador admConsultado = admDAO.consultarPorEmailESenha(email, senhaCriptografadaAdm, conn);

            if (admConsultado != null) {
                request.getSession().setAttribute("admId", admConsultado.getIdAdm());
                request.getSession().setAttribute("nomeAdm", admConsultado.getNomeAdm());
                System.out.println("Login administrativo bem-sucedido.");
                voltarHomePageADM(request, response); // Chame o método para carregar os livros para o admin
                return;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar administrador: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao tentar logar. Tente novamente.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão (administrador): " + e.getMessage());
                }
            }
        }

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        cliente.setSenha(senha);

        ClienteDAO clienteDAO = new ClienteDAO();
        IStrategy validarLogin = new ValidarLogin();
        String resultado = validarLogin.processar(cliente);

        if (resultado.isEmpty()) {
            try {
                conn = Conexao.createConnectionToMySQL(); // Abre uma nova conexão para o cliente
                String senhaCriptografadaCliente = clienteDAO.senhaCriptografada(senha);
                Cliente clienteConsultado = clienteDAO.consultarPorEmailESenha(email, senhaCriptografadaCliente, conn);

                if (clienteConsultado != null) {
                    request.getSession().setAttribute("clienteId", clienteConsultado.getId());
                    request.getSession().setAttribute("nomeCliente", clienteConsultado.getNome());
                    request.getSession().setAttribute("primeiroAcesso", true);
                    System.out.println("Login de cliente bem-sucedido.");
                    homePage(request, response); // <--- CHAME O homePage AQUI
                    return;
                }else {
                    request.setAttribute("mensagemErro", "Usuário ou senha inválidos.");
                    System.out.println("Falha no login.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                System.err.println("Erro ao consultar cliente: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("mensagemErro", "Erro ao tentar logar. Tente novamente.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("Erro ao fechar a conexão (cliente): " + e.getMessage());
                    }
                }
            }
        } else {
            request.setAttribute("mensagemErro", resultado);
            System.out.println("Erro na validação do login.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
    }

    private void logoutAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalida a sessão atual, removendo todos os atributos associados a ela.
        request.getSession().invalidate();
        System.out.println("Usuário deslogado.");

        // Redireciona o usuário de volta para a página inicial (index.jsp).
        response.sendRedirect("index.jsp");
    }

    //SEM CRIPTOGRAFIA
    /*private void loginAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email_login");
        String senha = request.getParameter("senha_login");

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        cliente.setSenha(senha);


        ClienteDAO clienteDAO = new ClienteDAO();
        // Validação de login (verificando email e senha)
        IStrategy validarLogin = new ValidarLogin();
        String resultado = validarLogin.processar(cliente);

        if (resultado.isEmpty()) {
            // Consultar no banco se o email e senha correspondem
            Cliente clienteConsultado = clienteDAO.consultarPorEmailESenha(email, senha);
            if (clienteConsultado != null) {

                request.getSession().setAttribute("clienteId", clienteConsultado.getId());
                System.out.println("sucesso no login");
                request.getRequestDispatcher("home.jsp").forward(request, response);
            } else {
                request.setAttribute("mensagemErro", "Usuário ou senha inválidos.");
                System.out.println("falha no login");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else {
            // Se houver erro de validação
            request.setAttribute("mensagemErro", resultado);
            System.out.println("ERRO NO LOGIN");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }*/

    private void homePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.consultarTodos();

        ItemDAO itemDAO = new ItemDAO();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        for (Livro livro : livros) {
            // Buscar o menor preço do item associado a este livro
            Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
            if (menorPreco != null) {
                precoPorLivro.put(livro.getId(), menorPreco);
            }
        }

        request.setAttribute("livros", livros);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }


    private void voltarHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.consultarTodos();

        ItemDAO itemDAO = new ItemDAO();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        for (Livro livro : livros) {
            Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
            if (menorPreco != null) {
                precoPorLivro.put(livro.getId(), menorPreco);
            }
        }

        request.setAttribute("livros", livros);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private void voltarHomePageADM(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.consultarTodos();

        ItemDAO itemDAO = new ItemDAO();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        for (Livro livro : livros) {
            Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
            if (menorPreco != null) {
                precoPorLivro.put(livro.getId(), menorPreco);
            }
        }

        request.setAttribute("livros", livros);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("admHome.jsp").forward(request, response);
    }

    private void consultarInfoPessoais(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("consultarInfoPessoais.jsp").forward(request, response);
    }

    private void consultarEnderecoPessoal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("consultarEndereco.jsp").forward(request, response);
    }

    private void exibirEstoque(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            FornecedorDAO fornecedorDAO = new FornecedorDAO();
            List<Fornecedor> listaDeFornecedores = fornecedorDAO.consultarTodos(conn);
            request.setAttribute("listaDeFornecedores", listaDeFornecedores);

            LivroDAO livroDAO = new LivroDAO();
            List<Livro> listaDeLivros = livroDAO.consultarTodos();
            request.setAttribute("listaDeLivros", listaDeLivros);

            EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
            Map<Integer, Integer> estoqueAtualPorLivro = new HashMap<>(); // Mapa de idLivro para quantidade atual
            for (Livro livro : listaDeLivros) {
                EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(livro.getId(), conn);
                estoqueAtualPorLivro.put(livro.getId(), (estoqueAtual != null) ? estoqueAtual.getQuantidadeAtual() : 0);
            }
            request.setAttribute("estoqueAtualPorLivro", estoqueAtualPorLivro);

            RequestDispatcher dispatcher = request.getRequestDispatcher("estoque.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Erro ao acessar o banco de dados para exibir o estoque.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void registrarEntradaEstoque(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();

            int idLivro = Integer.parseInt(request.getParameter("id_livro"));
            int qtdeEntrada = Integer.parseInt(request.getParameter("qtde_entrada"));
            LocalDate dataEntrada = LocalDate.parse(request.getParameter("data_entrada"));
            BigDecimal valorCusto = new BigDecimal(request.getParameter("valor_custo"));
            String idFornecStr = request.getParameter("id_fornec");
            Integer idFornecedor = (idFornecStr != null && !idFornecStr.isEmpty()) ? Integer.parseInt(idFornecStr) : null;

            // Criar um objeto Estoque (entrada)
            Estoque entrada = new Estoque();
            entrada.setIdLivro(idLivro);
            entrada.setQuantidadeEstoque(qtdeEntrada);
            entrada.setTipoEntrada(TipoEntrada.COMPRA); // Assumindo que este formulário é para compra
            entrada.setDataEntrada(dataEntrada);
            entrada.setValorCusto(valorCusto);
            entrada.setIdFornecedor(idFornecedor);

            EstoqueDAO estoqueDAO = new EstoqueDAO();
            estoqueDAO.salvar(entrada, conn); // Salvar a entrada na tabela 'estoque'

            // Atualizar o estoque atual
            EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
            EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);

            if (estoqueAtual == null) {
                // Se não houver registro de estoque atual para este livro, criar um novo
                EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                novoEstoqueAtual.setIdLivro(idLivro);
                novoEstoqueAtual.setQuantidadeAtual(qtdeEntrada);
                estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
            } else {
                // Se já existir, atualizar a quantidade
                estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + qtdeEntrada);
                estoqueAtualDAO.atualizar(estoqueAtual, conn);
            }

            request.setAttribute("mensagemSucesso", "Entrada de estoque registrada com sucesso!");
            exibirEstoque(request, response); // Redirecionar para a página de estoque para exibir as mudanças

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao registrar entrada de estoque: " + e.getMessage());
            exibirEstoque(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("mensagemErro", "Erro de formato nos dados da entrada.");
            exibirEstoque(request, response);
        } catch (DateTimeParseException e) {
            request.setAttribute("mensagemErro", "Erro no formato da data de entrada.");
            exibirEstoque(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void consultarCartaoPessoal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("consultarCartao.jsp").forward(request, response);
    }

    private void editarCartaoForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("No metodo editarCartaoForm");
        request.getRequestDispatcher("editarCartao.jsp").forward(request, response);
    }

    private void editarCartao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idCartaoParam = request.getParameter("idCartao");
            System.out.println("\nID do Cartão para Editar (via parâmetro): " + idCartaoParam + "\n");

            if (idCartaoParam == null) {
                throw new ServletException("ID do cartão não encontrado.");
            }

            int idCartao = 0;
            try {
                idCartao = Integer.parseInt(idCartaoParam);
            } catch (NumberFormatException e) {
                throw new ServletException("Formato inválido para o ID do cartão.", e);
            }

            CartaoDAO cartaoDAO = new CartaoDAO();
            Cartao cartao = cartaoDAO.consultarCartaoPorId(idCartao);
            if (cartao == null) {
                throw new ServletException("Cartão não encontrado.");
            }

            String nomeTitular = request.getParameter("nomeTitular_editarCartao");
            System.out.println("nomeTitular: " + nomeTitular);
            String numeroCartao = request.getParameter("numeroCartao_editarCartao");
            System.out.println("numeroCartao: " + numeroCartao);
            String codSeguranca = request.getParameter("codSeguranca_editarCartao");
            System.out.println("codSeguranca: " + codSeguranca);
            String dataVencimento = request.getParameter("dataVencimento_editarCartao");
            System.out.println("dataVencimento: " + dataVencimento);
            String bandeiraCartao = request.getParameter("bandeira_editarCartao");
            System.out.println("bandeira: " + bandeiraCartao);
            String preferencialCartao = request.getParameter("preferencialCartao_editarCartao");
            System.out.println("preferencialCartao: " + preferencialCartao);

            boolean preferencial = "true".equals(preferencialCartao);

            cartao.setNomeTitular(nomeTitular);
            cartao.setNumero(numeroCartao);
            cartao.setCodSeguranca(codSeguranca);
            cartao.setDataVencimento(dataVencimento);
            cartao.setPreferencial(preferencial);
            cartao.setBandeiraCartao(BandeiraCartao.fromDescricao(bandeiraCartao));

            IFachada fachada = new Fachada();
            System.out.println("ID do cliente armazenado na sessão: " + cartao.getIdCliente());
            String msgCartaoEditado = fachada.alterarCartao(cartao);
            System.out.println("Resultado da edição do cartão: " + msgCartaoEditado);

            request.getRequestDispatcher("consultarCartao.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Erro ao editar cartão", e);
        }
    }

    private void salvarNovoCartaoForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("adicionarNovoCartao.jsp").forward(request, response);
    }

    private void salvarNovoCartao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idClienteParam = request.getParameter("idCliente");
            System.out.println("\nId do Cliente para Novo Endereco (via parâmetro): " + idClienteParam + "\n");

            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer clienteId = (Integer) session.getAttribute("clienteId");
                if (clienteId != null) {
                    System.out.println("ID do cliente recuperado da sessão: " + clienteId);
                } else {
                    System.out.println("ID do cliente não encontrado na sessão.");
                }
            } else {
                System.out.println("Sessão não encontrada.");
            }

            if (idClienteParam == null) {
                throw new ServletException("ID do cliente não encontrado.");
            }

            int idCliente = 0;
            try {
                idCliente = Integer.parseInt(idClienteParam);
            } catch (NumberFormatException e) {
                throw new ServletException("Formato inválido para o ID do cliente.", e);
            }

            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.consultarPorId(idCliente);
            if (cliente == null) {
                throw new ServletException("Cliente não encontrado.");
            }
            cliente.setId(idCliente);

            //CODIGO DO CARTAO
            String nomeTitular = request.getParameter("nomeTitular_novoCartao");
            System.out.println("nomeTitular: " + nomeTitular);
            String numeroCartao = request.getParameter("numeroCartao_novoCartao");
            System.out.println("numeroCartao: " + numeroCartao);
            String codSeguranca = request.getParameter("codSeguranca_novoCartao");
            System.out.println("codSeguranca: " + codSeguranca);
            String dataVencimento = request.getParameter("dataVencimento_novoCartao");
            System.out.println("dataVencimento: " + dataVencimento);
            String bandeiraCartao = request.getParameter("bandeira_novoCartao");
            System.out.println("bandeira: " + bandeiraCartao);
            String preferencialCartao = request.getParameter("preferencialCartao_novoCartao");
            System.out.println("preferencialCartao: " + preferencialCartao);

            boolean preferencial = "true".equals(preferencialCartao);

            Cartao cartaoSalvar = new Cartao();
            cartaoSalvar.setNomeTitular(nomeTitular);
            cartaoSalvar.setNumero(numeroCartao);
            cartaoSalvar.setCodSeguranca(codSeguranca);
            cartaoSalvar.setDataVencimento(dataVencimento);
            cartaoSalvar.setPreferencial(preferencial);
            cartaoSalvar.setBandeiraCartao(BandeiraCartao.fromDescricao(bandeiraCartao));

            IFachada fachada = new Fachada();
            System.out.println("03 idCliente armazenado na sessão: " + cliente.getId());
            String msgNovoCartao = fachada.salvarCartao(cartaoSalvar);
            System.out.println("04 idCliente armazenado na sessão: " + cliente.getId());
            System.out.println("Resultado do salvar (Res): " + msgNovoCartao);


            request.getRequestDispatcher("consultarCartao.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar cartao", e);
        }
    }


    private void excluirCartao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int cartaoId = Integer.parseInt(request.getParameter("id"));
            System.out.println("ID recebido para exclusão: " + cartaoId);

            Cartao cartao = new Cartao();
            cartao.setId(cartaoId);

            IFachada fachada = new Fachada();
            String resultado = fachada.excluir(cartao);

            if (resultado == null) {
                System.out.println("Exclusão realizada com sucesso.");
                request.getSession().setAttribute("mensagemSucesso", "Cartão excluído com sucesso.");
            } else {
                System.out.println("Erro ao excluir: " + resultado);
                request.getSession().setAttribute("mensagemErro", resultado);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Falha ao excluir o cartão.");
        }

        response.sendRedirect("consultarCartao.jsp");
    }

    private void cadastrarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addClienteForm.jsp").forward(request, response);
    }

    private void consultarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("consultaCliente.jsp").forward(request, response);
    }

    private void salvarCliente(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nome = request.getParameter("nome_cadastro");
        String cpf = request.getParameter("cpf_cadastro");
        String email = request.getParameter("email_cadastro");
        String senha = request.getParameter("senha_cadastro");
        String confirmarSenha = request.getParameter("confirmarSenha_cadastro");
        String generoCli = request.getParameter("genero_cadastro");
        String dataNascimento = request.getParameter("dataNascimento_cadastro");
        String tipoTel = request.getParameter("tipoTel_cadastro");
        String ddd = request.getParameter("ddd_cadastro");
        String numero = request.getParameter("numero_cadastro");
        TipoTelefone tipoTelefone;

        if (tipoTel != null && !tipoTel.isEmpty()) {
            try {
                tipoTelefone = TipoTelefone.fromDescricao(tipoTel);
            } catch (IllegalArgumentException e) {
                throw new ServletException("Tipo de telefone inválido: " + tipoTel, e);
            }
        } else {
            throw new ServletException("Tipo de telefone não informado.");
        }

        Genero genero = Genero.fromDescricao(generoCli);

        if (ddd == null || ddd.isEmpty() || numero == null || numero.isEmpty()) {
            throw new ServletException("DDD e número de telefone são obrigatórios.");
        }

        Telefone telefone = new Telefone();
        telefone.setNumero(numero);
        telefone.setDdd(ddd);
        telefone.setTipo(tipoTelefone);

        //Cliente
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);
        cliente.setSenha(senha);
        cliente.setConfirmarSenha(confirmarSenha);
        cliente.setGenero(genero);
        cliente.setDataNascimento(dataNascimento);
        cliente.setTelefone(telefone);

        IFachada fachada = new Fachada();
        String msg = fachada.salvar(cliente);
        System.out.println("Resultado do salvar cliente: " + msg);
        request.getSession().setAttribute("idCliente", cliente.getId());
        Integer idClienteGerado = (Integer) request.getSession().getAttribute("idCliente");
        System.out.println("idCliente armazenado na sessão: " + idClienteGerado);


        if (idClienteGerado != null) {
            Carrinho carrinho = new Carrinho();
            carrinho.setClienteId(idClienteGerado);
            carrinho.setValorCarrinho(0.00);
            carrinho.setBloqueado(false);

            CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
            Connection conn = null;
            try {
                conn = Conexao.createConnectionToMySQL();
                conn.setAutoCommit(false);

                carrinhoDAO.salvar(carrinho, conn);
                System.out.println("Carrinho criado para o cliente ID: " + idClienteGerado + ", Carrinho ID: " + carrinho.getId());

                conn.commit();
            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback();
                }
                e.printStackTrace();
                throw new ServletException("Erro ao criar carrinho para o cliente.", e);
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }
        }

        Cupom cupomBoasVindas = new Cupom();
        cupomBoasVindas.setValor(10.00);
        TipoCupom tipoPromocional = TipoCupom.fromDescricao("PROMOCIONAL");
        cupomBoasVindas.setTipoCupom(tipoPromocional);

        CupomDAO cupomDAO = new CupomDAO();
        int idCupomBoasVindas = cupomDAO.salvar(cupomBoasVindas);

        // Associar o cupom ao cliente
        if (idCupomBoasVindas > 0 && idClienteGerado != null) {
            cupomDAO.associarCupomAoCliente(idClienteGerado, idCupomBoasVindas);
            request.getSession().setAttribute("mensagemBoasVindas", "Você ganhou um cupom de boas vindas no valor de R$10,00!");
        }

        response.sendRedirect("cadastroClienteEndereco.jsp?idCliente=" + cliente.getId());
    }


    private void salvarEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String idClienteParam = request.getParameter("idCliente");
            System.out.println("\nId do Cliente para Endereco: " + idClienteParam + "\n");

            if (idClienteParam == null || idClienteParam.isEmpty()) {
                throw new ServletException("ID do cliente não encontrado.");
            }

            int idCliente = 0;
            try {
                idCliente = Integer.parseInt(idClienteParam);
            } catch (NumberFormatException e) {
                throw new ServletException("Formato inválido para o ID do cliente.", e);
            }

            Cliente cliente = new Cliente();
            cliente.setId(idCliente);

            //RESIDENCIAL
            String nomePaisRes = request.getParameter("nomePaisRes_cadastro");
            String nomeEstadoRes = request.getParameter("nomeEstadoRes_cadastro");
            String nomeCidadeRes = request.getParameter("nomeCidadeRes_cadastro");
            String tipoLogradouroRes = request.getParameter("tipoLogradouroRes_cadastro");
            String tipoResidenciaRes = request.getParameter("tipoResidenciaRes_cadastro");
            String cepRes = request.getParameter("cepRes_cadastro");
            String logradouroRes = request.getParameter("logradouroRes_cadastro");
            String numeroRes = request.getParameter("numeroRes_cadastro");
            String bairroRes = request.getParameter("bairroRes_cadastro");
            String observacaoRes = request.getParameter("observacaoRes_cadastro");

            Endereco enderecoRes = new Endereco();
            enderecoRes.setLogradouro(logradouroRes);
            enderecoRes.setNumero(numeroRes);
            enderecoRes.setBairro(bairroRes);
            enderecoRes.setCep(cepRes);
            enderecoRes.setObservacao(observacaoRes);
            if (tipoResidenciaRes == null || tipoResidenciaRes.isEmpty()) {
                throw new IllegalArgumentException("Nenhum tipo de residência selecionado.");
            } else {
                enderecoRes.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaRes));
            }
            enderecoRes.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroRes));

            Pais paisRes = new Pais();
            paisRes.setNome(nomePaisRes);

            Estado estadoRes = new Estado();
            estadoRes.setNome(nomeEstadoRes);
            estadoRes.setPais(paisRes);

            Cidade cidadeRes = new Cidade();
            cidadeRes.setNome(nomeCidadeRes);
            cidadeRes.setEstado(estadoRes);
            enderecoRes.setCidade(cidadeRes);

            //COBRANCA
            String nomePaisCob = request.getParameter("nomePaisCob_cadastro");
            String nomeEstadoCob = request.getParameter("nomeEstadoCob_cadastro");
            String nomeCidadeCob = request.getParameter("nomeCidadeCob_cadastro");
            String tipoLogradouroCob = request.getParameter("tipoLogradouroCob_cadastro");
            String tipoResidenciaCob = request.getParameter("tipoResidenciaCob_cadastro");
            String cepCob = request.getParameter("cepCob_cadastro");
            String logradouroCob = request.getParameter("logradouroCob_cadastro");
            String numeroCob = request.getParameter("numeroCob_cadastro");
            String bairroCob = request.getParameter("bairroCob_cadastro");
            String observacaoCob = request.getParameter("observacaoCob_cadastro");

            Endereco enderecoCob = new Endereco();
            enderecoCob.setLogradouro(logradouroCob);
            enderecoCob.setNumero(numeroCob);
            enderecoCob.setBairro(bairroCob);
            enderecoCob.setCep(cepCob);
            enderecoCob.setObservacao(observacaoCob);
            enderecoCob.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaCob));
            enderecoCob.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroCob));

            Pais paisCob = new Pais();
            paisCob.setNome(nomePaisCob);

            Estado estadoCob = new Estado();
            estadoCob.setNome(nomeEstadoCob);
            estadoCob.setPais(paisCob);

            Cidade cidadeCob = new Cidade();
            cidadeCob.setNome(nomeCidadeCob);
            cidadeCob.setEstado(estadoCob);
            enderecoCob.setCidade(cidadeCob);

            //ENTREGA
            String nomePaisEnt = request.getParameter("nomePaisEnt_cadastro");
            String nomeEstadoEnt = request.getParameter("nomeEstadoEnt_cadastro");
            String nomeCidadeEnt = request.getParameter("nomeCidadeEnt_cadastro");
            String tipoLogradouroEnt = request.getParameter("tipoLogradouroEnt_cadastro");
            String tipoResidenciaEnt = request.getParameter("tipoResidenciaEnt_cadastro");
            String cepEnt = request.getParameter("cepEnt_cadastro");
            String logradouroEnt = request.getParameter("logradouroEnt_cadastro");
            String numeroEnt = request.getParameter("numeroEnt_cadastro");
            String bairroEnt = request.getParameter("bairroEnt_cadastro");
            String observacaoEnt = request.getParameter("observacaoEnt_cadastro");

            Pais paisEnt = new Pais();
            paisEnt.setNome(nomePaisEnt);

            Estado estadoEnt = new Estado();
            estadoEnt.setNome(nomeEstadoEnt);
            estadoEnt.setPais(paisEnt);

            Cidade cidadeEnt = new Cidade();
            cidadeEnt.setNome(nomeCidadeEnt);
            cidadeEnt.setEstado(estadoEnt);

            Endereco enderecoEnt = new Endereco();
            enderecoEnt.setLogradouro(logradouroEnt);
            enderecoEnt.setNumero(numeroEnt);
            enderecoEnt.setBairro(bairroEnt);
            enderecoEnt.setCep(cepEnt);
            enderecoEnt.setCidade(cidadeEnt);
            enderecoEnt.setObservacao(observacaoEnt);
            enderecoEnt.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaEnt));
            enderecoEnt.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroEnt));

            String tipoEnderecoEnt = request.getParameter("tipoEnderecoEnt");
            String tipoEnderecoCob = request.getParameter("tipoEnderecoCob");
            String tipoEnderecoRes = request.getParameter("tipoEnderecoRes");

            if (tipoEnderecoRes != null && !tipoEnderecoRes.isEmpty()) {
                enderecoRes.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoRes));
            }

            if (tipoEnderecoCob != null && !tipoEnderecoCob.isEmpty()) {
                enderecoCob.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoCob));
            }

            if (tipoEnderecoEnt != null && !tipoEnderecoEnt.isEmpty()) {
                enderecoEnt.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoEnt));
            }

            System.out.println("Endereco Residencial: " + enderecoRes);
            System.out.println("Endereco Cobranca: " + enderecoCob);
            System.out.println("Endereco Entrega: " + enderecoEnt);
            System.out.println("02 idCliente armazenado na sessão: " + cliente.getId());

            cliente.setEnderecoResidencial(enderecoRes);
            cliente.setEnderecoCobranca(enderecoCob);
            cliente.setEnderecoEntrega(enderecoEnt);

            IFachada fachada = new Fachada();
            System.out.println("03 idCliente armazenado na sessão: " + cliente.getId());
            String msgRes = fachada.salvarEndereco(enderecoRes);
            String msgCob = fachada.salvarEndereco(enderecoCob);
            String msgEnt = fachada.salvarEndereco(enderecoEnt);
            System.out.println("04 idCliente armazenado na sessão: " + cliente.getId());

            System.out.println("Resultado do salvar (Res): " + msgRes);
            System.out.println("Resultado do salvar (Cob): " + msgCob);
            System.out.println("Resultado do salvar (Ent): " + msgEnt);
            response.sendRedirect("index.jsp");
        } catch (Exception e) {
            throw new ServletException("Erro ao salvar endereço", e);
        }
    }

    private void salvarNovoEnderecoForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("adicionarNovoEndereco.jsp").forward(request, response);
    }

    private void salvarNovoEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idClienteParam = request.getParameter("idCliente");
            System.out.println("\nId do Cliente para Novo Endereco (via parâmetro): " + idClienteParam + "\n");

            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer clienteId = (Integer) session.getAttribute("clienteId");
                if (clienteId != null) {
                    System.out.println("ID do cliente recuperado da sessão: " + clienteId);
                } else {
                    System.out.println("ID do cliente não encontrado na sessão.");
                }
            } else {
                System.out.println("Sessão não encontrada.");
            }

            if (idClienteParam == null) {
                throw new ServletException("ID do cliente não encontrado.");
            }


            int idCliente = 0;
            try {
                idCliente = Integer.parseInt(idClienteParam);
            } catch (NumberFormatException e) {
                throw new ServletException("Formato inválido para o ID do cliente.", e);
            }

            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.consultarPorId(idCliente);
            if (cliente == null) {
                throw new ServletException("Cliente não encontrado.");
            }

            cliente.setId(idCliente);

            String nomePaisNovoEnd = request.getParameter("nomePais_NovoEnd");
            System.out.println("SERVnomePais: " + nomePaisNovoEnd);
            String nomeEstadoNovoEnd = request.getParameter("nomeEstado_NovoEnd");
            System.out.println("nomeEstado: " + nomeEstadoNovoEnd);
            String nomeCidadeNovoEnd = request.getParameter("nomeCidade_NovoEnd");
            System.out.println("nomeCidade: " + nomeCidadeNovoEnd);
            String tipoLogradouroNovoEnd = request.getParameter("tipoLogradouro_NovoEnd");
            System.out.println("tipoLogradouro: " + tipoLogradouroNovoEnd);
            String tipoResidenciaNovoEnd = request.getParameter("tipoResidencia_NovoEnd");
            System.out.println("tipoResidencia: " + tipoResidenciaNovoEnd);
            String tipoEnderecoNovoEnd = request.getParameter("tipoEndereco_NovoEnd");
            System.out.println("tipoEndereco: " + tipoEnderecoNovoEnd);
            String cepNovoEnd = request.getParameter("cep_NovoEnd");
            System.out.println("cepRes: " + cepNovoEnd);
            String logradouroNovoEnd = request.getParameter("logradouro_NovoEnd");
            System.out.println("logradouro: " + logradouroNovoEnd);
            String numeroNovoEnd = request.getParameter("numero_NovoEnd");
            System.out.println("numero: " + numeroNovoEnd);
            String bairroNovoEnd = request.getParameter("bairro_NovoEnd");
            System.out.println("bairro: " + bairroNovoEnd);
            String observacaoNovoEnd = request.getParameter("observacao_NovoEnd");
            System.out.println("SERVobservacao: " + observacaoNovoEnd);

            Endereco enderecoNovoEnd = new Endereco();
            enderecoNovoEnd.setLogradouro(logradouroNovoEnd);
            enderecoNovoEnd.setNumero(numeroNovoEnd);
            enderecoNovoEnd.setBairro(bairroNovoEnd);
            enderecoNovoEnd.setCep(cepNovoEnd);
            enderecoNovoEnd.setObservacao(observacaoNovoEnd);
            if (tipoResidenciaNovoEnd == null || tipoResidenciaNovoEnd.isEmpty()) {
                throw new IllegalArgumentException("Nenhum tipo de residência selecionado.");
            } else {
                enderecoNovoEnd.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaNovoEnd));
            }
            enderecoNovoEnd.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroNovoEnd));
            enderecoNovoEnd.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoNovoEnd));

            Pais paisNovoEnd = new Pais();
            paisNovoEnd.setNome(nomePaisNovoEnd);

            Estado estadoNovoEnd = new Estado();
            estadoNovoEnd.setNome(nomeEstadoNovoEnd);
            estadoNovoEnd.setPais(paisNovoEnd);

            Cidade cidadeNovoEnd = new Cidade();
            cidadeNovoEnd.setNome(nomeCidadeNovoEnd);
            cidadeNovoEnd.setEstado(estadoNovoEnd);
            enderecoNovoEnd.setCidade(cidadeNovoEnd);

            Endereco enderecoNovo = new Endereco();
            enderecoNovo.setLogradouro(logradouroNovoEnd);
            enderecoNovo.setNumero(numeroNovoEnd);
            enderecoNovo.setBairro(bairroNovoEnd);
            enderecoNovo.setCep(cepNovoEnd);
            enderecoNovo.setCidade(cidadeNovoEnd);
            enderecoNovo.setObservacao(observacaoNovoEnd);
            enderecoNovo.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaNovoEnd));
            enderecoNovo.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroNovoEnd));
            enderecoNovo.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoNovoEnd));

            System.out.println("Endereco Novo: " + enderecoNovoEnd);
            System.out.println("02 idCliente armazenado na sessão: " + cliente.getId());

            cliente.setEndereco(enderecoNovoEnd);

            IFachada fachada = new Fachada();
            System.out.println("03 idCliente armazenado na sessão: " + cliente.getId());
            String msgNovoEnd = fachada.salvarEndereco(enderecoNovoEnd);
            System.out.println("04 idCliente armazenado na sessão: " + cliente.getId());

            System.out.println("Resultado do salvar (Res): " + msgNovoEnd);
            response.sendRedirect("consultarEndereco.jsp");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar endereço", e);
        }
    }

    private void excluirCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null) {
            System.out.println("Cliente com ID " + id + " excluído.");
        }

        response.sendRedirect("controller?action=consultar");
    }

    private void consultarProduto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idLivroStr = request.getParameter("id_livro");
        if (idLivroStr != null && !idLivroStr.isEmpty()) {
            try {
                int idLivro = Integer.parseInt(idLivroStr);
                LivroDAO livroDAO = new LivroDAO();
                Livro livro = livroDAO.consultarPorId(idLivro);
                ItemDAO itemDAO = new ItemDAO();
                Item item = itemDAO.buscarPorLivroId(idLivro);
                request.setAttribute("livro", livro);
                request.setAttribute("item", item);
                request.getRequestDispatcher("produtoVenda.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        response.sendRedirect("home.jsp");
    }

    /*private void adicionarCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou na servlet adicionarCarrinho");

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("clienteId") == null) {
                System.out.println("DEBUG: Sessão expirada ou cliente não logado");
                throw new ServletException("Sessão expirada ou cliente não logado.");
            }

            Integer clienteId = (Integer) session.getAttribute("clienteId");
            System.out.println("DEBUG: clienteId = " + clienteId);

            String idLivroParam = request.getParameter("idLivro");
            String quantidadeParam = request.getParameter("quantidade");

            if (idLivroParam == null || quantidadeParam == null) {
                System.out.println("DEBUG: Parâmetros ausentes: idLivroParam=" + idLivroParam + ", quantidadeParam=" + quantidadeParam);
                throw new ServletException("Parâmetros obrigatórios ausentes.");
            }

            int idLivro = Integer.parseInt(idLivroParam);
            int quantidadeDesejada = Integer.parseInt(quantidadeParam);
            System.out.println("DEBUG: idLivro = " + idLivro + ", quantidadeDesejada = " + quantidadeDesejada);

            Connection conn = null;
            try {
                conn = Conexao.createConnectionToMySQL();
                System.out.println("DEBUG: Conexão com banco estabelecida");

                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                Carrinho carrinho = carrinhoDAO.buscarCarrinhoPorClienteId(clienteId, conn);
                System.out.println("DEBUG: Resultado da busca do carrinho para cliente " + clienteId + ": " + carrinho);
                int idCarrinho = carrinho.getId();
                System.out.println("DEBUG: idCarrinho = " + idCarrinho);

                CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
                List<CarrinhoItem> itensCarrinho = carrinhoItemDAO.consultarPorCarrinho(idCarrinho, conn);
                CarrinhoItem itemExistente = null;
                for (CarrinhoItem item : itensCarrinho) {
                    if (item.getIdLivro() == idLivro) {
                        itemExistente = item;
                        break;
                    }
                }

                if (itemExistente != null) {
                    // Item já existe, atualizar a quantidade
                    int novaQuantidade = itemExistente.getQuantidade() + quantidadeDesejada;
                    carrinhoItemDAO.atualizarQuantidade(itemExistente.getId(), novaQuantidade, conn); // Use o ID do carrinho_item para atualizar
                    request.setAttribute("mensagem", "Quantidade do item atualizada no carrinho.");
                    System.out.println("DEBUG: Quantidade do item " + idLivro + " atualizada para " + novaQuantidade);
                } else {
                    // Item não existe, adicionar novo
                    CarrinhoItem novoItem = new CarrinhoItem();
                    novoItem.setIdCarrinho(idCarrinho);
                    novoItem.setIdLivro(idLivro);
                    novoItem.setQuantidade(quantidadeDesejada);

                    String msgCarrinho = carrinhoItemDAO.salvar(novoItem, conn);
                    System.out.println("DEBUG: Resultado do salvar no carrinho = " + msgCarrinho);
                    request.setAttribute("mensagem", "Item adicionado ao carrinho com sucesso.");
                }

                // Atualizar a 'ultima_atividade' do carrinho
                carrinhoDAO.consultarCarrinhoPorCliente(clienteId);

            } catch (SQLException e) {
                System.out.println("DEBUG: SQLException - " + e.getMessage());
                throw new ServletException("Erro ao adicionar item ao carrinho", e);
            } finally {
                if (conn != null) try { conn.close(); System.out.println("DEBUG: Conexão fechada"); } catch (SQLException ignored) {}
            }

            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            System.out.println("DEBUG: Forward realizado para carrinho.jsp");

        } catch (Exception e) {
            System.out.println("DEBUG: Exceção capturada - " + e.getMessage());
            throw new ServletException("Erro ao adicionar item ao carrinho", e);
        }
    }*/

    private void adicionarCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou na servlet adicionarCarrinho");

        Connection conn = null; // Declare a conexão aqui para o bloco finally
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("clienteId") == null) {
                System.out.println("DEBUG: Sessão expirada ou cliente não logado");
                request.setAttribute("mensagemErro", "Sessão expirada ou você não está logado. Por favor, faça login novamente.");
                request.getRequestDispatcher("login.jsp").forward(request, response); // Redireciona para login
                return;
            }

            Integer clienteId = (Integer) session.getAttribute("clienteId");
            System.out.println("DEBUG: clienteId = " + clienteId);

            String idLivroParam = request.getParameter("idLivro");
            String quantidadeParam = request.getParameter("quantidade");

            if (idLivroParam == null || quantidadeParam == null) {
                System.out.println("DEBUG: Parâmetros ausentes: idLivroParam=" + idLivroParam + ", quantidadeParam=" + quantidadeParam);
                request.setAttribute("mensagemErro", "Parâmetros obrigatórios ausentes para adicionar ao carrinho.");
                // Redireciona para uma página anterior ou de erro, dependendo da sua UI
                request.getRequestDispatcher("pagina_anterior_ou_home.jsp").forward(request, response);
                return;
            }

            int idLivro = Integer.parseInt(idLivroParam);
            int quantidadeDesejada = Integer.parseInt(quantidadeParam);
            System.out.println("DEBUG: idLivro = " + idLivro + ", quantidadeDesejada = " + quantidadeDesejada);

            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false); // INICIA A TRANSAÇÃO

            try {
                // --- 1. Checar disponibilidade de estoque ---
                EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);

                if (estoqueAtual == null || estoqueAtual.getQuantidadeAtual() < quantidadeDesejada) {
                    System.out.println("DEBUG: Estoque insuficiente para o livro " + idLivro + ". Quantidade disponível: " + (estoqueAtual != null ? estoqueAtual.getQuantidadeAtual() : 0));
                    request.setAttribute("mensagemErro", "Estoque insuficiente para o livro selecionado. Quantidade disponível: " + (estoqueAtual != null ? estoqueAtual.getQuantidadeAtual() : 0) + ".");
                    conn.rollback(); // Desfaz qualquer alteração se houver
                    request.getRequestDispatcher("home.jsp").forward(request, response);
                    return; // Interrompe o processamento
                }

                // --- 2. Gerenciar o item no carrinho ---
                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                Carrinho carrinho = carrinhoDAO.buscarCarrinhoPorClienteId(clienteId, conn); // Passa a conexão da transação
                System.out.println("DEBUG: Resultado da busca do carrinho para cliente " + clienteId + ": " + carrinho);
                int idCarrinho = carrinho.getId();
                System.out.println("DEBUG: idCarrinho = " + idCarrinho);

                CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
                List<CarrinhoItem> itensCarrinho = carrinhoItemDAO.consultarPorCarrinho(idCarrinho, conn); // Passa a conexão da transação
                CarrinhoItem itemExistente = null;
                for (CarrinhoItem item : itensCarrinho) {
                    if (item.getIdLivro() == idLivro) {
                        itemExistente = item;
                        break;
                    }
                }

                if (itemExistente != null) {
                    // Item já existe, atualizar a quantidade no carrinho
                    int novaQuantidadeNoCarrinho = itemExistente.getQuantidade() + quantidadeDesejada;

                    // IMPORTANTE: Se a quantidade adicionada já considerou o estoque antes,
                    // e agora estamos apenas aumentando a quantidade de um item já no carrinho,
                    // a validação de estoque já deveria ter ocorrido.
                    // Se o usuário pode manipular a quantidade no carrinho, você precisaria
                    // re-validar o estoque atual menos a quantidade já no carrinho mais a nova quantidade desejada.
                    // Por simplicidade, aqui assumimos que a 'quantidadeDesejada' já é a quantidade extra que o usuário quer adicionar.
                    carrinhoItemDAO.atualizarQuantidade(itemExistente.getId(), novaQuantidadeNoCarrinho, conn); // Passa a conexão da transação
                    request.setAttribute("mensagem", "Quantidade do item atualizada no carrinho.");
                    System.out.println("DEBUG: Quantidade do item " + idLivro + " atualizada para " + novaQuantidadeNoCarrinho);
                } else {
                    // Item não existe, adicionar novo ao carrinho
                    CarrinhoItem novoItem = new CarrinhoItem();
                    novoItem.setIdCarrinho(idCarrinho);
                    novoItem.setIdLivro(idLivro);
                    novoItem.setQuantidade(quantidadeDesejada);

                    String msgCarrinho = carrinhoItemDAO.salvar(novoItem, conn); // Passa a conexão da transação
                    System.out.println("DEBUG: Resultado do salvar no carrinho = " + msgCarrinho);
                    request.setAttribute("mensagem", "Item adicionado ao carrinho com sucesso.");
                }

                // --- 3. Reduzir estoque atual ---
                estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() - quantidadeDesejada);
                estoqueAtualDAO.atualizar(estoqueAtual, conn); // Passa a conexão da transação
                System.out.println("DEBUG: Estoque do livro " + idLivro + " reduzido em " + quantidadeDesejada + ". Nova quantidade: " + estoqueAtual.getQuantidadeAtual());

                // Atualizar a 'ultima_atividade' do carrinho
                // Se carrinhoDAO.consultarCarrinhoPorCliente(clienteId) não atualiza a data de atividade,
                // você precisaria de um método como: carrinhoDAO.atualizarUltimaAtividade(idCarrinho, conn);
                // Por enquanto, manterei como está se essa linha tiver algum efeito colateral desejado.
                //carrinhoDAO.consultarCarrinhoPorCliente(clienteId); // Se esta função *também* atualiza a data de última atividade do carrinho

                conn.commit(); // CONFIRMA A TRANSAÇÃO SE TUDO OCORREU BEM
                System.out.println("DEBUG: Transação commitada.");

            } catch (SQLException e) {
                System.out.println("DEBUG: SQLException - " + e.getMessage());
                if (conn != null) {
                    try {
                        conn.rollback(); // DESFAZ TODAS AS OPERAÇÕES SE HOUVER ERRO
                        System.out.println("DEBUG: Transação rollback.");
                    } catch (SQLException rbEx) {
                        System.err.println("Erro ao realizar rollback: " + rbEx.getMessage());
                    }
                }
                request.setAttribute("mensagemErro", "Erro ao adicionar item ao carrinho: " + e.getMessage());
                // Redireciona para uma página de erro ou de volta ao produto
                request.getRequestDispatcher("home.jsp").forward(request, response);
                return; // Interrompe o processamento
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // RESTAURA O AUTOCOMMIT PARA O PADRÃO
                        conn.close();
                        System.out.println("DEBUG: Conexão fechada");
                    } catch (SQLException ignored) {}
                }
            }

            // Se tudo correu bem, redireciona para a página do carrinho
            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            System.out.println("DEBUG: Forward realizado para carrinho.jsp");

        } catch (Exception e) {
            System.out.println("DEBUG: Exceção capturada - " + e.getMessage());
            throw new ServletException("Erro inesperado ao adicionar item ao carrinho", e);
        }
    }

    private void alterarQuantidadeCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemIdParam = request.getParameter("itemId");
        String quantidadeParam = request.getParameter("quantidade");
        Integer clienteId = (Integer) request.getSession().getAttribute("clienteId");

        if (itemIdParam != null && quantidadeParam != null && clienteId != null) {
            try {
                int itemId = Integer.parseInt(itemIdParam);
                int novaQuantidade = Integer.parseInt(quantidadeParam);

                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                boolean alterado = carrinhoDAO.alterarQuantidadeItemCarrinho(itemId, novaQuantidade);

                if (alterado) {
                    // Recupere o carrinho atualizado do banco de dados
                    Carrinho carrinhoAtualizado = carrinhoDAO.consultarCarrinhoPorCliente(clienteId);
                    double novoPrecoTotalItem = 0;
                    double novoSubtotal = 0;

                    if (carrinhoAtualizado != null) {
                        for (CarrinhoItem item : carrinhoAtualizado.getItens()) {
                            if (item.getId() == itemId) {
                                novoPrecoTotalItem = item.getItem().getValorVenda() * item.getQuantidade();
                            }
                            novoSubtotal += item.getItem().getValorVenda() * item.getQuantidade();
                        }

                        double frete = 20.0; // Mantenha o valor do frete como está ou recupere dinamicamente
                        double cupomDesconto = (request.getSession().getAttribute("cupomSelecionado") != null) ? ((Cupom) request.getSession().getAttribute("cupomSelecionado")).getValor() : 0.00;
                        double novoTotalCompra = novoSubtotal + frete - cupomDesconto;

                        // Envie os novos valores como JSON
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        String jsonResponse = String.format("{\"precoTotalItem\": \"%.2f\", \"subtotal\": \"%.2f\", \"totalCompra\": \"%.2f\"}", novoPrecoTotalItem, novoSubtotal, novoTotalCompra);
                        response.getWriter().write(jsonResponse);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return;
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter parâmetros: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (SQLException e) {
                System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            System.err.println("Parâmetros inválidos para alterarQuantidade.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /*private void removerItemCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            int idCarrinhoItem = Integer.parseInt(request.getParameter("id")); // Recebe o ID do carrinho_item
            System.out.println("DEBUG REMOVER: Tentando excluir item do carrinho com ID " + idCarrinhoItem);
            int clienteId = (int) request.getSession().getAttribute("clienteId");

            CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
            CarrinhoItem itemRemovido = carrinhoItemDAO.buscarPorId(idCarrinhoItem, conn);

            if (itemRemovido != null) {
                int idLivroRemovido = itemRemovido.getIdLivro();
                int quantidadeRemovida = itemRemovido.getQuantidade();
                int idCarrinho = itemRemovido.getIdCarrinho(); // Obtém o ID do carrinho
                System.out.println("DEBUG REMOVER: CarrinhoItem encontrado. Livro ID: " + idLivroRemovido + ", Quantidade: " + quantidadeRemovida + ", Carrinho ID: " + idCarrinho);

                // Buscar o Item original para obter o valorVenda
                ItemDAO itemDAO = new ItemDAO();
                Item itemOriginal = itemDAO.consultarItemMaisRecentePorLivro(idLivroRemovido);
                double valorVendaOriginal = 0.0;
                if (itemOriginal != null) {
                    valorVendaOriginal = itemOriginal.getValorVenda();
                    System.out.println("DEBUG REMOVER: Valor de venda original do livro: " + valorVendaOriginal);
                } else {
                    System.out.println("DEBUG REMOVER: Aviso: Não foi possível encontrar o item original para obter o valor de venda.");
                    request.getSession().setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o item original para obter o valor de venda.");
                }

                // 2. Remover o item do carrinho (usando a fachada)
                IFachada fachada = new Fachada();
                CarrinhoItem itemParaExcluir = new CarrinhoItem();
                itemParaExcluir.setId(idCarrinhoItem); // Usa o ID correto para exclusão
                String resultado = fachada.excluir(itemParaExcluir);

                if (resultado == null) {
                    System.out.println("DEBUG REMOVER: Item removido com sucesso do carrinho.");
                    request.getSession().setAttribute("mensagemSucesso", "Item removido com sucesso do carrinho.");

                    // 3. Adicionar a quantidade de volta ao estoque
                    Item estoqueAtual = itemDAO.consultarItemMaisRecentePorLivro(idLivroRemovido);

                    if (estoqueAtual != null) {
                        int novaQuantidadeEstoque = estoqueAtual.getQuantidade() + quantidadeRemovida;
                        Item novoItemEstoque = new Item();
                        novoItemEstoque.setLivroId(idLivroRemovido);
                        novoItemEstoque.setQuantidade(novaQuantidadeEstoque);
                        novoItemEstoque.setValorVenda(valorVendaOriginal); // Usando o valor de venda original
                        novoItemEstoque.setIdFornecedor(itemOriginal.getIdFornecedor());
                        novoItemEstoque.setDataEntrada(new java.sql.Timestamp(new java.util.Date().getTime()));
                        itemDAO.salvar(novoItemEstoque, conn);
                        System.out.println("DEBUG REMOVER: Estoque atualizado. Nova quantidade = " + novaQuantidadeEstoque + ", Valor de venda = " + valorVendaOriginal);
                    } else {
                        System.out.println("DEBUG REMOVER: Aviso: Não foi possível encontrar o estoque do livro para adicionar de volta.");
                        request.getSession().setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o estoque do livro para adicionar de volta.");
                    }

                    // Atualizar a 'ultima_atividade' após remover o item
                    CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                    carrinhoDAO.consultarCarrinhoPorCliente(clienteId);

                } else {
                    System.out.println("DEBUG REMOVER: Erro ao remover item do carrinho: " + resultado);
                    request.getSession().setAttribute("mensagemErro", resultado);
                }
            } else {
                System.out.println("DEBUG REMOVER: Erro: Item do carrinho não encontrado com ID: " + idCarrinhoItem);
                request.getSession().setAttribute("mensagemErro", "Erro: Item do carrinho não encontrado.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao remover item do carrinho.");
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); System.out.println("DEBUG: Conexão fechada"); } catch (SQLException ignored) {}
        }
        response.sendRedirect("carrinho.jsp");
    }*/

    /*private void removerItemCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false); // Iniciar transação

            int idCarrinhoItem = Integer.parseInt(request.getParameter("id")); // Recebe o ID do carrinho_item
            System.out.println("DEBUG REMOVER: Tentando excluir item do carrinho com ID " + idCarrinhoItem);
            int clienteId = (int) request.getSession().getAttribute("clienteId");

            CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
            CarrinhoItem itemRemovido = carrinhoItemDAO.buscarPorId(idCarrinhoItem, conn);

            if (itemRemovido != null) {
                int idLivroRemovido = itemRemovido.getIdLivro();
                int quantidadeRemovida = itemRemovido.getQuantidade();
                System.out.println("DEBUG REMOVER: CarrinhoItem encontrado. Livro ID: " + idLivroRemovido + ", Quantidade: " + quantidadeRemovida);

                // 1. Remover o item do carrinho
                CarrinhoItem itemParaExcluir = new CarrinhoItem();
                itemParaExcluir.setId(idCarrinhoItem); // Define o ID do item a ser excluído

                // **Importante: Remova a linha de criação de conexão dentro do seu DAO,
                // pois já estamos passando a conexão da Servlet.**
                String resultadoExclusao = carrinhoItemDAO.excluir(itemParaExcluir, conn);

                if (resultadoExclusao == null) {
                    int linhasAfetadas = 1; // Se a exclusão foi bem-sucedida pelo DAO, consideramos 1 linha afetada
                    System.out.println("DEBUG REMOVER: Item removido com sucesso do carrinho.");
                    request.getSession().setAttribute("mensagemSucesso", "Item removido com sucesso do carrinho.");

                    // 2. Adicionar a quantidade de volta ao estoque atual
                    EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                    EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivroRemovido, conn);

                    if (estoqueAtual != null) {
                        estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + quantidadeRemovida);
                        estoqueAtualDAO.atualizar(estoqueAtual, conn);
                        System.out.println("DEBUG REMOVER: Estoque atualizado. Nova quantidade = " + estoqueAtual.getQuantidadeAtual());
                    } else {
                        // Se não houver registro de estoque atual (o que seria estranho neste ponto), crie um novo
                        EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                        novoEstoqueAtual.setIdLivro(idLivroRemovido);
                        novoEstoqueAtual.setQuantidadeAtual(quantidadeRemovida);
                        estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
                        System.out.println("DEBUG REMOVER: Registro de estoque atual criado. Quantidade = " + quantidadeRemovida);
                        request.getSession().setAttribute("mensagemAviso", "Aviso: Registro de estoque atual criado ao remover item.");
                    }

                    // Atualizar a 'ultima_atividade' após remover o item
                    //CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                    //carrinhoDAO.consultarCarrinhoPorCliente(clienteId);

                    conn.commit(); // Confirmar transação
                } else {
                    System.out.println("DEBUG REMOVER: Erro ao remover item do carrinho: " + resultadoExclusao);
                    request.getSession().setAttribute("mensagemErro", resultadoExclusao);
                    conn.rollback(); // Desfazer transação
                }
            } else {
                System.out.println("DEBUG REMOVER: Erro: Item do carrinho não encontrado com ID: " + idCarrinhoItem);
                request.getSession().setAttribute("mensagemErro", "Erro: Item do carrinho não encontrado.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao remover item do carrinho.");
            if (conn != null) {
                try {
                    conn.rollback(); // Desfazer transação em caso de erro
                } catch (SQLException rbEx) {
                    System.err.println("Erro ao realizar rollback: " + rbEx.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); System.out.println("DEBUG: Conexão fechada"); } catch (SQLException ignored) {}
        }
        response.sendRedirect("carrinho.jsp");
    }*/

    private void removerItemCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false); // Iniciar transação

            int idCarrinhoItem = Integer.parseInt(request.getParameter("id")); // Recebe o ID do carrinho_item
            System.out.println("DEBUG REMOVER: Tentando excluir item do carrinho com ID " + idCarrinhoItem);
            int clienteId = (int) request.getSession().getAttribute("clienteId");

            CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
            CarrinhoItem itemRemovido = carrinhoItemDAO.buscarPorId(idCarrinhoItem, conn);

            if (itemRemovido != null) {
                int idLivroRemovido = itemRemovido.getIdLivro();
                int quantidadeRemovida = itemRemovido.getQuantidade();
                System.out.println("DEBUG REMOVER: CarrinhoItem encontrado. Livro ID: " + idLivroRemovido + ", Quantidade: " + quantidadeRemovida);

                // 1. Remover o item do carrinho
                CarrinhoItem itemParaExcluir = new CarrinhoItem();
                itemParaExcluir.setId(idCarrinhoItem);
                String resultadoExclusao = carrinhoItemDAO.excluir(itemParaExcluir, conn);

                if (resultadoExclusao == null) {
                    System.out.println("DEBUG REMOVER: Item removido com sucesso do carrinho.");
                    request.getSession().setAttribute("mensagemSucesso", "Item removido com sucesso do carrinho.");

                    // 2. Adicionar a quantidade de volta ao estoque atual
                    EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                    EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivroRemovido, conn);

                    if (estoqueAtual != null) {
                        estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + quantidadeRemovida);
                        estoqueAtualDAO.atualizar(estoqueAtual, conn);
                        System.out.println("DEBUG REMOVER: Estoque atualizado. Nova quantidade = " + estoqueAtual.getQuantidadeAtual());
                    } else {
                        EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                        novoEstoqueAtual.setIdLivro(idLivroRemovido);
                        novoEstoqueAtual.setQuantidadeAtual(quantidadeRemovida);
                        estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
                        System.out.println("DEBUG REMOVER: Registro de estoque atual criado. Quantidade = " + quantidadeRemovida);
                        request.getSession().setAttribute("mensagemAviso", "Aviso: Registro de estoque atual criado ao remover item.");
                    }

                    // 3. Registrar a reentrada na tabela 'entrada_estoque'
                    EstoqueDAO entradaEstoqueDAO = new EstoqueDAO();
                    // Buscar o valor de custo mais recente para este livro
                    BigDecimal valorCusto = entradaEstoqueDAO.buscarValorCustoMaisRecente(idLivroRemovido, conn);

                    if (valorCusto != null) {
                        Estoque reentrada = new Estoque();
                        reentrada.setIdLivro(idLivroRemovido);
                        reentrada.setTipoEntrada(TipoEntrada.RETORNO_CARRINHO);
                        reentrada.setQuantidadeEstoque(quantidadeRemovida);
                        reentrada.setValorCusto(valorCusto);
                        reentrada.setDataEntrada(LocalDate.now()); // Ou a data da exclusão do carrinho
                        entradaEstoqueDAO.salvar(reentrada, conn);
                        System.out.println("DEBUG REMOVER: Reentrada registrada em entrada_estoque para o livro " + idLivroRemovido + ", quantidade " + quantidadeRemovida + ", valor custo " + valorCusto);
                    } else {
                        System.out.println("DEBUG REMOVER: Aviso: Não foi possível encontrar o valor de custo para registrar a reentrada.");
                        request.getSession().setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o valor de custo para registrar a reentrada.");
                    }

                    conn.commit(); // Confirmar transação
                } else {
                    System.out.println("DEBUG REMOVER: Erro ao remover item do carrinho: " + resultadoExclusao);
                    request.getSession().setAttribute("mensagemErro", resultadoExclusao);
                    conn.rollback(); // Desfazer transação
                }
            } else {
                System.out.println("DEBUG REMOVER: Erro: Item do carrinho não encontrado com ID: " + idCarrinhoItem);
                request.getSession().setAttribute("mensagemErro", "Erro: Item do carrinho não encontrado.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao remover item do carrinho.");
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("Erro ao realizar rollback: " + rbEx.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
        response.sendRedirect("carrinho.jsp");
    }

    /*private void esvaziarCarrinhoInativo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("clienteId") != null) {
            int clienteId = (int) session.getAttribute("clienteId");
            Connection conn = null;
            try {
                conn = Conexao.createConnectionToMySQL();
                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                carrinhoDAO.removerTodosItensDoCarrinho(carrinhoDAO.buscarCarrinhoPorClienteId(clienteId, conn).getId(), conn);
                // Opcional: Você pode invalidar a sessão ou limpar outros atributos relacionados ao carrinho
                // session.removeAttribute("carrinhoId");
                System.out.println("Carrinho do cliente " + clienteId + " esvaziado por inatividade.");
                response.sendRedirect("carrinho.jsp?mensagem=carrinhoEsvaziado"); // Redirecionar com uma mensagem
            } catch (SQLException e) {
                System.err.println("Erro ao esvaziar carrinho por inatividade: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao esvaziar carrinho.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
            }
        } else {
            System.out.println("Sessão expirada ou cliente não logado ao tentar esvaziar carrinho por inatividade.");
            response.sendRedirect("index.jsp?mensagem=sessaoExpirada"); // Redirecionar para a página inicial ou login
        }
    }*/

    private void esvaziarCarrinhoInativo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("clienteId") != null) {
            int clienteId = (int) session.getAttribute("clienteId");
            Connection conn = null;
            try {
                conn = Conexao.createConnectionToMySQL();
                conn.setAutoCommit(false); // Iniciar transação

                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                int idCarrinho = carrinhoDAO.buscarCarrinhoPorClienteId(clienteId, conn).getId();

                CarrinhoItemDAO carrinhoItemDAO = new CarrinhoItemDAO();
                List<CarrinhoItem> itensDoCarrinho = carrinhoItemDAO.consultarPorCarrinho(idCarrinho, conn);

                EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                EstoqueDAO estoqueDAO = new EstoqueDAO(); // Usando seu EstoqueDAO para entrada_estoque

                for (CarrinhoItem item : itensDoCarrinho) {
                    int idLivro = item.getIdLivro();
                    int quantidade = item.getQuantidade();

                    // 1. Restituir ao estoque atual
                    EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);
                    if (estoqueAtual != null) {
                        estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + quantidade);
                        estoqueAtualDAO.atualizar(estoqueAtual, conn);
                        System.out.println("Carrinho inativo: Livro " + idLivro + " devolvido ao estoque atual. Quantidade: " + quantidade);
                    } else {
                        EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                        novoEstoqueAtual.setIdLivro(idLivro);
                        novoEstoqueAtual.setQuantidadeAtual(quantidade);
                        estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
                        System.out.println("Carrinho inativo: Criado registro de estoque atual para livro " + idLivro + ". Quantidade: " + quantidade);
                    }

                    // 2. Registrar a reentrada na tabela 'entrada_estoque'
                    BigDecimal valorCusto = estoqueDAO.buscarValorCustoMaisRecente(idLivro, conn);

                    if (valorCusto != null) {
                        dominio.Estoque reentrada = new dominio.Estoque(); // Use sua classe de domínio 'Estoque'
                        reentrada.setIdLivro(idLivro);
                        reentrada.setTipoEntrada(TipoEntrada.RETORNO_CARRINHO);
                        reentrada.setQuantidadeEstoque(quantidade);
                        reentrada.setValorCusto(valorCusto);
                        reentrada.setDataEntrada(LocalDate.now()); // Ou a data/hora da inatividade
                        estoqueDAO.salvar(reentrada, conn);
                        System.out.println("Carrinho inativo: Reentrada registrada em entrada_estoque para o livro " + idLivro + ", quantidade " + quantidade + ", valor custo " + valorCusto);
                    } else {
                        System.out.println("Carrinho inativo: Aviso: Não foi possível encontrar o valor de custo para registrar a reentrada do livro " + idLivro + ".");
                        request.getSession().setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o valor de custo para registrar a reentrada.");
                    }
                }

                // 3. Remover todos os itens do carrinho após a restituição ao estoque e registro da reentrada
                carrinhoDAO.removerTodosItensDoCarrinho(idCarrinho, conn);

                conn.commit(); // Confirmar transação

                System.out.println("Carrinho do cliente " + clienteId + " esvaziado por inatividade.");
                response.sendRedirect("carrinho.jsp?mensagem=carrinhoEsvaziado");

            } catch (SQLException e) {
                System.err.println("Erro ao esvaziar carrinho por inatividade: " + e.getMessage());
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException rbEx) {
                        System.err.println("Erro ao realizar rollback: " + rbEx.getMessage());
                    }
                }
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao esvaziar carrinho.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        } else {
            System.out.println("Sessão expirada ou cliente não logado ao tentar esvaziar carrinho por inatividade.");
            response.sendRedirect("index.jsp?mensagem=sessaoExpirada");
        }
    }

    private void pingAtividadeCarrinho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("clienteId") != null) {
            int clienteId = (int) session.getAttribute("clienteId");
            CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
            carrinhoDAO.consultarCarrinhoPorCliente(clienteId); // Isso atualizará a 'ultima_atividade'
            response.setStatus(HttpServletResponse.SC_OK); // Enviar uma resposta OK para o cliente
            System.out.println("Ping de atividade recebido para o cliente " + clienteId + ". 'ultima_atividade' atualizada.");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Se a sessão expirou
            System.out.println("Ping de atividade recebido para sessão inválida.");
        }
    }

    private void exibirCupons(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("cupons.jsp").forward(request, response);
    }

    private void salvarPreviaPedido(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        if (clienteId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
        Carrinho carrinho = carrinhoDAO.consultarCarrinhoPorCliente(clienteId);
        List<CarrinhoItem> itensCarrinho = (carrinho != null) ? carrinho.getItens() : new ArrayList<>();

        double subtotal = 0;
        for (CarrinhoItem item : itensCarrinho) {
            subtotal += item.getItem().getValorVenda() * item.getQuantidade();
        }

        String tipoFreteSelecionadoStr = request.getParameter("tipoFreteSelecionado");
        TipoFrete tipoFreteSelecionado = null;
        double valorFrete = 0.0;

        if (tipoFreteSelecionadoStr != null && !tipoFreteSelecionadoStr.isEmpty()) {
            try {
                tipoFreteSelecionado = TipoFrete.fromString(tipoFreteSelecionadoStr);
                valorFrete = tipoFreteSelecionado.getValor();
            } catch (IllegalArgumentException e) {
                System.err.println("Tipo de frete inválido: " + tipoFreteSelecionadoStr);
                valorFrete = 0.0; // Valor padrão em caso de erro
            }
        }

        session.setAttribute("previaPedidoItens", itensCarrinho);
        session.setAttribute("previaPedidoSubtotal", subtotal);
        session.setAttribute("previaPedidoTipoFreteSelecionadoStr", tipoFreteSelecionadoStr); // Salva a string para usar depois
        session.setAttribute("previaPedidoValorFrete", valorFrete); // Salva o valor para exibição imediata (opcional)
        session.setAttribute("previaPedidoTotal", subtotal + valorFrete); // Calcula o total com o frete (opcional para exibição)
        request.getRequestDispatcher("finalizarCompra.jsp").forward(request, response);
    }

    private void confirmarCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou na servlet confirmarCompra");

        Connection conn = null;
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("clienteId") == null) {
                System.out.println("DEBUG: Sessão expirada ou cliente não logado");
                throw new ServletException("Sessão expirada ou cliente não logado.");
            }

            Integer clienteId = (Integer) session.getAttribute("clienteId");
            Integer idEnderecoEntrega = Integer.parseInt(request.getParameter("idEnderecoEntrega"));
            Integer idEnderecoCobranca = Integer.parseInt(request.getParameter("idEnderecoCobranca"));
            double valorSubtotal = Double.parseDouble(request.getParameter("valorSubtotal"));
            double valorDesconto = Double.parseDouble(request.getParameter("valorDesconto"));
            double valorTotal = Double.parseDouble(request.getParameter("valorTotal"));
            Integer idCupom = null;
            if (request.getParameter("idCupom") != null && !request.getParameter("idCupom").isEmpty()) {
                try {
                    idCupom = Integer.parseInt(request.getParameter("idCupom"));
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter idCupom: " + request.getParameter("idCupom"));
                    idCupom = null;
                }
            }

            String tipoFreteSelecionadoStr = request.getParameter("tipoFreteSelecionado");
            TipoFrete valorFreteEnum = null;
            double valorFreteParaPagamento = 0.0;

            if (tipoFreteSelecionadoStr != null && !tipoFreteSelecionadoStr.isEmpty()) {
                try {
                    valorFreteEnum = TipoFrete.fromString(tipoFreteSelecionadoStr);
                    valorFreteParaPagamento = valorFreteEnum.getValor();
                } catch (IllegalArgumentException e) {
                    System.err.println("Tipo de frete inválido ao confirmar compra: " + tipoFreteSelecionadoStr);
                    valorFreteParaPagamento = 0.0;
                }
            } else {
                System.out.println("Nenhum tipo de frete selecionado ao confirmar compra.");
                valorFreteParaPagamento = 0.0;
            }

            String[] idsCartoesParam = request.getParameterValues("idsCartoes");
            List<Integer> idsCartoes = new ArrayList<>();
            if (idsCartoesParam != null) {
                for (String idStr : idsCartoesParam) {
                    idsCartoes.add(Integer.parseInt(idStr));
                }
            }

            List<ItemCompra> itensCompra = new ArrayList<>();
            Map<String, String[]> parametros = request.getParameterMap();
            int index = 0;
            while (parametros.containsKey("itens[" + index + "].idLivro")) {
                int idLivro = Integer.parseInt(parametros.get("itens[" + index + "].idLivro")[0]);
                int quantidade = Integer.parseInt(parametros.get("itens[" + index + "].quantidade")[0]);
                double valorUnitario = Double.parseDouble(parametros.get("itens[" + index + "].valorUnitario")[0]);
                ItemCompra item = new ItemCompra();
                item.setIdLivro(idLivro);
                item.setQuantidade(quantidade);
                item.setValorUnitarioNaCompra(valorUnitario);
                itensCompra.add(item);
                index++;
            }

            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false);
            System.out.println("DEBUG: Conexão com banco estabelecida e transação iniciada");

            CompraDAO compraDAO = new CompraDAO();
            String numeroPedidoGerado = compraDAO.gerarNumeroPedidoAleatorio();
            Compra compraParaSalvar = new Compra(clienteId, idEnderecoEntrega, idEnderecoCobranca, valorSubtotal, valorFreteEnum, valorDesconto, valorTotal, 1, numeroPedidoGerado); // Use o construtor correto
            int idCompraGerado = compraDAO.salvar(compraParaSalvar, conn);
            System.out.println("DEBUG: Compra salva com ID = " + idCompraGerado + " e Número do Pedido = " + compraParaSalvar.getNumeroPedido());
            ItensCompraDAO itensCompraDAO = new ItensCompraDAO();
            itensCompraDAO.salvar(idCompraGerado, itensCompra, conn);
            System.out.println("DEBUG: Itens da compra salvos");

            PagamentoCompraDAO pagamentoCompraDAO = new PagamentoCompraDAO();
            pagamentoCompraDAO.salvar(idCompraGerado, idsCartoes, valorTotal, idCupom, valorDesconto, valorFreteParaPagamento, conn);
            System.out.println("DEBUG: Pagamentos da compra salvos");

            CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
            carrinhoDAO.excluir(clienteId, conn);
            session.removeAttribute("carrinho");
            System.out.println("DEBUG: Carrinho do cliente limpo");

            conn.commit();
            System.out.println("DEBUG: Transação commitada");

            if (idCupom != null) {
                CupomDAO cupomDAO = new CupomDAO();
                boolean cupomExcluido = cupomDAO.excluirCupomDoCliente(clienteId, idCupom, conn);
                if (cupomExcluido) {
                    System.out.println("DEBUG: Cupom ID " + idCupom + " excluído para o cliente ID " + clienteId);
                } else {
                    System.out.println("DEBUG: Falha ao excluir o cupom ID " + idCupom + " para o cliente ID " + clienteId);
                }
                session.removeAttribute("cupomSelecionado");
            }

            response.sendRedirect("mensagemCompraFinalizada.jsp");
            System.out.println("DEBUG: Redirecionado para mensagemCompraFinalizada.jsp");

        } catch (SQLException e) {
            System.out.println("DEBUG: SQLException - " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("DEBUG: Transação rollback realizada devido a erro");
            }
            throw new ServletException("Erro ao confirmar a compra", e);
        } catch (Exception e) {
            System.out.println("DEBUG: Exceção capturada - " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("DEBUG: Transação rollback realizada devido a erro");
                } catch (SQLException ex) {
                    System.out.println("DEBUG: Erro no rollback - " + ex.getMessage());
                }
            }
            throw new ServletException("Erro ao confirmar a compra", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("DEBUG: Conexão fechada");
                } catch (SQLException e) {
                    System.out.println("DEBUG: Erro ao fechar a conexão - " + e.getMessage());
                }
            }
        }
    }

    private void exibirPedidos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou na servlet exibirPedidos");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteId") == null) {
            System.out.println("DEBUG: Sessão expirada ou cliente não logado");
            response.sendRedirect("login.jsp");
            return;
        }

        Integer clienteId = (Integer) session.getAttribute("clienteId");
        Connection conn = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            CompraDAO compraDAO = new CompraDAO();
            List<Compra> listaDePedidos = compraDAO.consultarComprasPorCliente(clienteId, conn);
            ItensCompraDAO itensCompraDAO = new ItensCompraDAO();
            LivroDAO livroDAO = new LivroDAO();
            EditoraDAO editoraDAO = new EditoraDAO();

            for (Compra pedido : listaDePedidos) {
                List<ItemCompra> itens = itensCompraDAO.consultarItensPorCompra(pedido.getIdCompra(), conn);
                List<ItemPedido> itensPedido = new ArrayList<>();

                for (ItemCompra item : itens) {
                    Livro livro = livroDAO.consultarPorId(item.getIdLivro());
                    if (livro != null) {
                        itensPedido.add(new ItemPedido(livro, item.getQuantidade(), item.getValorUnitarioNaCompra()));
                    }
                }
                pedido.setItensPedido(itensPedido);

                // --- Lógica para obter e definir a descrição do status ---
                String statusDescricao = obterDescricaoStatus(pedido.getStatusIdCompra());
                pedido.setStatusDescricao(statusDescricao); // Defina o novo atributo

                // --- Lógica para determinar se o botão "Trocar" deve ser exibido ---
                boolean troca = "ENTREGUE".equalsIgnoreCase(statusDescricao);
                pedido.setTroca(troca);
            }

            request.setAttribute("listaDePedidos", listaDePedidos);
            request.getRequestDispatcher("exibirPedidos.jsp").forward(request, response);

            System.out.println("DEBUG: Pedidos do cliente " + clienteId + " consultados e encaminhados para exibirPedidos.jsp");

        } catch (SQLException e) {
            System.err.println("Erro ao consultar pedidos do cliente: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar seus pedidos. Por favor, tente novamente mais tarde.");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                }
            }
        }
    }

    // Sua função para obter a descrição do status (certifique-se de que o pacote 'dominio' esteja correto)
    private String obterDescricaoStatus(int statusId) {
        for (dominio.StatusCompra status : dominio.StatusCompra.values()) {
            if (status.getId() == statusId) {
                return status.getDescricao();
            }
        }
        return "Status Desconhecido";
    }

    private void exibirPedidosADM(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        System.out.println("[DEBUG - CtrlServlet.exibirPedidosADM] Iniciando a exibição da página de pedidos do administrador.");
        CompraDAO compraDAO = new CompraDAO();
        List<Compra> listaDePedidos = compraDAO.consultarTodosComItens(); // Vamos criar este método no CompraDAO

        request.setAttribute("listaDePedidos", listaDePedidos);
        request.getRequestDispatcher("admExibirPedidos.jsp").forward(request, response);
        System.out.println("[DEBUG - CtrlServlet.exibirPedidosADM] Encaminhando para admExibirPedidos.jsp.");
    }

    private void atualizarStatusCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idCompra = 0;
        int novoStatusId = 0;
        try {
            idCompra = Integer.parseInt(request.getParameter("id_compra"));
            novoStatusId = Integer.parseInt(request.getParameter("status_id"));
            System.out.println("[DEBUG - CtrlServlet.atualizarStatusCompra] ID da Compra recebido: " + idCompra);
            System.out.println("[DEBUG - CtrlServlet.atualizarStatusCompra] Novo Status ID recebido: " + novoStatusId);

            CompraDAO compraDAO = new CompraDAO();
            compraDAO.atualizarStatus(idCompra, novoStatusId);
            System.out.println("[DEBUG - CtrlServlet.atualizarStatusCompra] Status atualizado no banco de dados para o pedido ID: " + idCompra + " com o status ID: " + novoStatusId);

            response.sendRedirect("servlet?action=exibirPedidosADM");
            System.out.println("[DEBUG - CtrlServlet.atualizarStatusCompra] Redirecionando para: servlet?action=exibirPedidosADM");

        } catch (NumberFormatException e) {
            System.err.println("[ERROR - CtrlServlet.atualizarStatusCompra] Erro ao converter parâmetros para inteiro: " + e.getMessage());
            // Você pode adicionar um redirecionamento de erro aqui, se desejar
        } catch (SQLException e) {
            System.err.println("[ERROR - CtrlServlet.atualizarStatusCompra] Erro de SQL ao atualizar o status: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo para mais detalhes
            // Você pode adicionar um redirecionamento de erro aqui, se desejar
        }
    }

    private void autorizarTroca(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String idCompraStr = request.getParameter("id_compra");
        String idClienteStr = request.getParameter("idCliente");
        String valorCompraStr = request.getParameter("valorCompra");

        if (idCompraStr != null && !idCompraStr.isEmpty() &&
                idClienteStr != null && !idClienteStr.isEmpty() &&
                valorCompraStr != null && !valorCompraStr.isEmpty()) {
            Connection conn = null;
            try {
                int idCompra = Integer.parseInt(idCompraStr);
                int idCliente = Integer.parseInt(idClienteStr);
                double valorCompra = Double.parseDouble(valorCompraStr);

                conn = Conexao.createConnectionToMySQL();
                conn.setAutoCommit(false); // Iniciar transação

                CompraDAO compraDAO = new CompraDAO();
                ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
                EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                EstoqueDAO estoqueDAO = new EstoqueDAO(); // Para registrar a entrada

                // 1. Buscar os itens do pedido para devolver ao estoque
                List<ItemPedido> itensDoPedido = itemPedidoDAO.consultarPorCompraId(idCompra, conn);

                // 2. Atualizar o estoque e registrar a entrada para cada item devolvido
                for (ItemPedido itemPedido : itensDoPedido) {
                    int idLivro = itemPedido.getLivro().getId();
                    int quantidadeDevolvida = itemPedido.getQuantidade();

                    // a) Atualizar o estoque atual
                    EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);
                    if (estoqueAtual != null) {
                        estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + quantidadeDevolvida);
                        estoqueAtualDAO.atualizar(estoqueAtual, conn);
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Livro " + idLivro + " devolvido ao estoque atual. Quantidade: " + quantidadeDevolvida);
                    } else {
                        EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                        novoEstoqueAtual.setIdLivro(idLivro);
                        novoEstoqueAtual.setQuantidadeAtual(quantidadeDevolvida);
                        estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Criado registro de estoque atual para livro " + idLivro + ". Quantidade: " + quantidadeDevolvida);
                    }

                    // b) Registrar a entrada na tabela 'entrada_estoque'
                    BigDecimal valorCusto = estoqueDAO.buscarValorCustoMaisRecente(idLivro, conn);

                    if (valorCusto != null) {
                        dominio.Estoque entrada = new dominio.Estoque();
                        entrada.setIdLivro(idLivro);
                        entrada.setTipoEntrada(TipoEntrada.TROCA); // Definindo o tipo de entrada como TROCA
                        entrada.setQuantidadeEstoque(quantidadeDevolvida);
                        entrada.setValorCusto(valorCusto);
                        entrada.setDataEntrada(LocalDate.now()); // Data da autorização da troca
                        estoqueDAO.salvar(entrada, conn);
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Entrada de troca registrada para o livro " + idLivro + ", quantidade " + quantidadeDevolvida + ", valor custo " + valorCusto);
                    } else {
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Aviso: Não foi possível encontrar o valor de custo para registrar a entrada de troca do livro " + idLivro + ".");
                        request.setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o valor de custo para registrar a entrada de troca.");
                    }
                }

                // 3. Atualizar o status da compra para "TROCA AUTORIZADA"
                compraDAO.atualizarStatus(idCompra, StatusCompra.TROCA_AUTORIZADA.getId());
                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Status do pedido " + idCompra + " atualizado para TROCA AUTORIZADA.");

                // 4. Gerar um código de cupom único
                String codigoCupom = gerarCodigoCupomUnico();
                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Código de cupom gerado: " + codigoCupom);

                // 5. Salvar o cupom no banco de dados
                compraDAO.gerarCupomTroca(valorCompra, idCliente, idCompra); // Passa o código gerado
                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Cupom de troca gerado para o pedido " + idCompra + ", código: " + codigoCupom + ", valor: " + valorCompra + ", cliente: " + idCliente);

                conn.commit(); // Confirmar transação
                response.sendRedirect("servlet?action=exibirPedidosADM&mensagemSucesso=Troca autorizada e cupom gerado com sucesso para o pedido " + idCompra + ", código: " + codigoCupom);
                return;

            } catch (NumberFormatException e) {
                if (conn != null) conn.rollback();
                response.sendRedirect("servlet?action=exibirPedidosADM&erro=Dados inválidos para autorizar a troca.");
                e.printStackTrace();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                response.sendRedirect("servlet?action=exibirPedidosADM&erro=Erro ao autorizar a troca ou gerar o cupom.");
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        } else {
            response.sendRedirect("servlet?action=exibirPedidosADM&erro=Dados incompletos para autorizar a troca.");
        }
    }

    private String gerarCodigoCupomUnico() {
        // Implemente uma lógica robusta para gerar códigos de cupom únicos
        return "TROCA-" + System.currentTimeMillis(); // Exemplo simples
    }

    private void cartaoCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("exibirCartoesCompra.jsp").forward(request, response);
    }

    private void enderecoCobrancaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("exibirEndCobrCompra.jsp").forward(request, response);
    }

    private void enderecoEntregaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("exibirEndEntCompra.jsp").forward(request, response);
    }

    private void salvarEnderecoEntregaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método salvarEnderecoEntregaCompra");

        try {
            HttpSession session = request.getSession();
            Integer clienteId = (Integer) session.getAttribute("clienteId");

            if (clienteId == null) {
                System.out.println("DEBUG: Cliente não logado ou sessão expirada.");
                response.sendRedirect("login.jsp"); // Redirecionar para login se não estiver logado
                return;
            }

            String idEnderecoEntregaSelecionadoParam = request.getParameter("idEnderecoEntregaSelecionado");

            if (idEnderecoEntregaSelecionadoParam != null && !idEnderecoEntregaSelecionadoParam.isEmpty()) {
                int idEnderecoEntregaSelecionado = Integer.parseInt(idEnderecoEntregaSelecionadoParam);
                System.out.println("DEBUG: ID do endereço de entrega selecionado = " + idEnderecoEntregaSelecionado);

                EnderecoDAO enderecoDAO = new EnderecoDAO();
                Endereco enderecoSelecionado = enderecoDAO.consultarPorId(idEnderecoEntregaSelecionado);

                if (enderecoSelecionado != null) {
                    session.setAttribute("enderecoSelecionado", enderecoSelecionado);
                    System.out.println("DEBUG: Endereço de entrega salvo na sessão.");
                } else {
                    System.out.println("DEBUG: Endereço com ID " + idEnderecoEntregaSelecionado + " não encontrado.");
                    request.setAttribute("mensagem", "Erro: Endereço de entrega não encontrado.");
                }
            } else {
                System.out.println("DEBUG: Nenhum endereço de entrega selecionado.");
                request.setAttribute("mensagem", "Por favor, selecione um endereço de entrega.");
            }

            // Redirecionar de volta para a página de finalização de compra
            String redirectURL = "finalizarCompra.jsp";
            if (request.getParameter("redirect") != null && !request.getParameter("redirect").isEmpty()) {
                redirectURL = request.getParameter("redirect");
                System.out.println("DEBUG: Redirecionando para = " + redirectURL);
            }
            response.sendRedirect(redirectURL);

        } catch (NumberFormatException e) {
            System.out.println("DEBUG: Erro de conversão de número - " + e.getMessage());
            request.setAttribute("mensagem", "Erro: Formato de ID de endereço inválido.");
            response.sendRedirect("finalizarCompra.jsp");
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao salvar endereço de entrega - " + e.getMessage());
            request.setAttribute("mensagem", "Erro ao processar o endereço de entrega.");
            response.sendRedirect("finalizarCompra.jsp");
        }
    }

    private void salvarEnderecoCobrancaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método salvarEnderecoCobrancaCompra");

        try {
            HttpSession session = request.getSession();
            Integer clienteId = (Integer) session.getAttribute("clienteId");

            if (clienteId == null) {
                System.out.println("DEBUG: Cliente não logado ou sessão expirada.");
                response.sendRedirect("login.jsp"); // Redirecionar para login se não estiver logado
                return;
            }

            String idEnderecoCobrancaSelecionadoParam = request.getParameter("idEnderecoCobrancaSelecionado");

            if (idEnderecoCobrancaSelecionadoParam != null && !idEnderecoCobrancaSelecionadoParam.isEmpty()) {
                int idEnderecoCobrancaSelecionado = Integer.parseInt(idEnderecoCobrancaSelecionadoParam);
                System.out.println("DEBUG: ID do endereço de cobrança selecionado = " + idEnderecoCobrancaSelecionado);

                EnderecoDAO enderecoDAO = new EnderecoDAO();
                Endereco enderecoSelecionado = enderecoDAO.consultarPorId(idEnderecoCobrancaSelecionado);

                if (enderecoSelecionado != null) {
                    session.setAttribute("enderecoCobrancaSelecionado", enderecoSelecionado);
                    System.out.println("DEBUG: Endereço de cobrança salvo na sessão.");
                } else {
                    System.out.println("DEBUG: Endereço com ID " + idEnderecoCobrancaSelecionado + " não encontrado.");
                    request.setAttribute("mensagem", "Erro: Endereço de cobrança não encontrado.");
                }
            } else {
                System.out.println("DEBUG: Nenhum endereço de cobrança selecionado.");
                request.setAttribute("mensagem", "Por favor, selecione um endereço de cobrança.");
            }

            // Redirecionar de volta para a página de finalização de compra
            String redirectURL = "finalizarCompra.jsp";
            if (request.getParameter("redirect") != null && !request.getParameter("redirect").isEmpty()) {
                redirectURL = request.getParameter("redirect");
                System.out.println("DEBUG: Redirecionando para = " + redirectURL);
            }
            response.sendRedirect(redirectURL);

        } catch (NumberFormatException e) {
            System.out.println("DEBUG: Erro de conversão de número - " + e.getMessage());
            request.setAttribute("mensagem", "Erro: Formato de ID de endereço de cobrança inválido.");
            response.sendRedirect("finalizarCompra.jsp");
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao salvar endereço de cobrança - " + e.getMessage());
            request.setAttribute("mensagem", "Erro ao processar o endereço de cobrança.");
            response.sendRedirect("finalizarCompra.jsp");
        }
    }

    private void salvarCartoesCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método salvarCartoesCompra");

        try {
            HttpSession session = request.getSession();
            Integer clienteId = (Integer) session.getAttribute("clienteId");

            if (clienteId == null) {
                System.out.println("DEBUG: Cliente não logado ou sessão expirada.");
                response.sendRedirect("login.jsp");
                return;
            }

            String[] idsCartoesSelecionadosParam = request.getParameterValues("idsCartoes");
            List<Cartao> cartoesSelecionados = new ArrayList<>();
            CartaoDAO cartaoDAO = new CartaoDAO();

            if (idsCartoesSelecionadosParam != null && idsCartoesSelecionadosParam.length > 0) {
                System.out.println("DEBUG: IDs dos cartões selecionados:");
                for (String idStr : idsCartoesSelecionadosParam) {
                    try {
                        int idCartao = Integer.parseInt(idStr);
                        System.out.println("DEBUG:   ID = " + idCartao);
                        // Buscar o objeto Cartao completo pelo ID
                        Cartao cartao = cartaoDAO.consultarCartaoPorId(idCartao);
                        if (cartao != null) {
                            cartoesSelecionados.add(cartao);
                        } else {
                            System.out.println("DEBUG: Cartão com ID " + idCartao + " não encontrado.");
                            request.setAttribute("mensagem", "Erro: Um dos cartões selecionados não foi encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("DEBUG: Erro ao converter ID do cartão: " + idStr);
                        request.setAttribute("mensagem", "Erro: ID de cartão inválido.");
                    }
                }
                session.setAttribute("cartoesSelecionados", cartoesSelecionados);
                System.out.println("DEBUG: Lista de objetos Cartao salva na sessão.");
            } else {
                session.removeAttribute("cartoesSelecionados");
                System.out.println("DEBUG: Nenhum cartão selecionado.");
                request.setAttribute("mensagem", "Por favor, selecione ao menos um cartão para pagamento.");
            }

            // Redirecionar de volta para a página de finalização de compra
            String redirectURL = "finalizarCompra.jsp";
            if (request.getParameter("redirect") != null && !request.getParameter("redirect").isEmpty()) {
                redirectURL = request.getParameter("redirect");
                System.out.println("DEBUG: Redirecionando para = " + redirectURL);
            }
            response.sendRedirect(redirectURL);

        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao salvar cartões selecionados - " + e.getMessage());
            request.setAttribute("mensagem", "Erro ao processar os cartões selecionados.");
            response.sendRedirect("finalizarCompra.jsp");
        }
    }

    /*private void aplicarCupom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método aplicarCupom");
        HttpSession session = request.getSession();
        CupomDAO cupomDAO = new CupomDAO();
        Cupom cupomSelecionado = null;

        String idCupomPromocionalStr = request.getParameter("idCupomPromocional");
        String idCupomTrocaStr = request.getParameter("idCupomTroca");

        if (request.getParameter("habilitarPromocionais") != null && idCupomPromocionalStr != null && !idCupomPromocionalStr.isEmpty()) {
            try {
                int idCupom = Integer.parseInt(idCupomPromocionalStr);
                cupomSelecionado = cupomDAO.consultarCupomPorId(idCupom);
                if (cupomSelecionado != null && cupomSelecionado.getTipoCupom().getDescricao().equalsIgnoreCase("PROMOCIONAL")) {
                    session.setAttribute("cupomSelecionado", cupomSelecionado);
                    System.out.println("DEBUG: Cupom promocional aplicado. ID: " + idCupom);
                } else {
                    session.removeAttribute("cupomSelecionado");
                    System.out.println("DEBUG: Cupom promocional inválido ou não encontrado.");
                    request.setAttribute("mensagemCupom", "Cupom promocional inválido.");
                }
            } catch (NumberFormatException e) {
                session.removeAttribute("cupomSelecionado");
                System.out.println("DEBUG: Erro ao converter ID do cupom promocional.");
                request.setAttribute("mensagemCupom", "ID de cupom promocional inválido.");
            }
        } else if (request.getParameter("habilitarTroca") != null && idCupomTrocaStr != null && !idCupomTrocaStr.isEmpty()) {
            try {
                int idCupom = Integer.parseInt(idCupomTrocaStr);
                cupomSelecionado = cupomDAO.consultarCupomPorId(idCupom);
                if (cupomSelecionado != null && cupomSelecionado.getTipoCupom().getDescricao().equalsIgnoreCase("TROCA")) {
                    session.setAttribute("cupomSelecionado", cupomSelecionado);
                    System.out.println("DEBUG: Cupom de troca aplicado. ID: " + idCupom);
                } else {
                    session.removeAttribute("cupomSelecionado");
                    System.out.println("DEBUG: Cupom de troca inválido ou não encontrado.");
                    request.setAttribute("mensagemCupom", "Cupom de troca inválido.");
                }
            } catch (NumberFormatException e) {
                session.removeAttribute("cupomSelecionado");
                System.out.println("DEBUG: Erro ao converter ID do cupom de troca.");
                request.setAttribute("mensagemCupom", "ID de cupom de troca inválido.");
            }
        } else {
            session.removeAttribute("cupomSelecionado");
            System.out.println("DEBUG: Nenhum cupom selecionado ou seleção desabilitada.");
        }

        response.sendRedirect("carrinho.jsp");
    }*/

    private void aplicarCupom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método aplicarCupom");
        HttpSession session = request.getSession();
        CupomDAO cupomDAO = new CupomDAO();
        CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
        Cupom cupomPromocionalSelecionado = null;
        CupomTroca cupomTrocaSelecionado = null;

        String idCupomPromocionalStr = request.getParameter("idCupomPromocional");
        String idCupomTrocaStr = request.getParameter("idCupomTroca");

        // Processar Cupom Promocional
        if (request.getParameter("habilitarPromocionais") != null && idCupomPromocionalStr != null && !idCupomPromocionalStr.isEmpty()) {
            try {
                int idCupom = Integer.parseInt(idCupomPromocionalStr);
                Cupom cupom = cupomDAO.consultarCupomPorId(idCupom);
                if (cupom != null && cupom.getTipoCupom().getDescricao().equalsIgnoreCase("PROMOCIONAL")) {
                    session.setAttribute("cupomSelecionado", cupom);
                    System.out.println("DEBUG: Cupom promocional aplicado. ID: " + idCupom);
                } else {
                    session.removeAttribute("cupomSelecionado");
                    System.out.println("DEBUG: Cupom promocional inválido ou não encontrado.");
                    request.setAttribute("mensagemCupom", "Cupom promocional inválido.");
                }
            } catch (NumberFormatException e) {
                session.removeAttribute("cupomSelecionado");
                System.out.println("DEBUG: Erro ao converter ID do cupom promocional.");
                request.setAttribute("mensagemCupom", "ID de cupom promocional inválido.");
            }
        } else {
            session.removeAttribute("cupomSelecionado");
        }

        // Processar Cupom de Troca
        if (request.getParameter("habilitarTroca") != null && idCupomTrocaStr != null && !idCupomTrocaStr.isEmpty()) {
            try {
                int idCupomTroca = Integer.parseInt(idCupomTrocaStr);
                CupomTroca cupomTroca = cupomTrocaDAO.buscarPorId(idCupomTroca);
                if (cupomTroca != null) {
                    session.setAttribute("cupomTrocaSelecionado", cupomTroca);
                    System.out.println("DEBUG: Cupom de troca aplicado. ID: " + idCupomTroca + ", Código: " + cupomTroca.getCodigoCupom() + ", Valor: " + cupomTroca.getValorCupom());
                } else {
                    session.removeAttribute("cupomTrocaSelecionado");
                    System.out.println("DEBUG: Cupom de troca não encontrado.");
                    request.setAttribute("mensagemCupomTroca", "Cupom de troca inválido.");
                }
            } catch (NumberFormatException e) {
                session.removeAttribute("cupomTrocaSelecionado");
                System.out.println("DEBUG: Erro ao converter ID do cupom de troca.");
                request.setAttribute("mensagemCupomTroca", "ID de cupom de troca inválido.");
            } catch (SQLException e) {
                System.err.println("ERROR: Erro ao buscar cupom de troca: " + e.getMessage());
                session.removeAttribute("cupomTrocaSelecionado");
                request.setAttribute("mensagemCupomTroca", "Erro ao verificar cupom de troca.");
            }
        } else {
            session.removeAttribute("cupomTrocaSelecionado");
        }

        response.sendRedirect("carrinho.jsp");
    }

    private void solicitarTroca(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.err.println("Dentro de solicitarTroca na servlet");
        String idCompraStr = request.getParameter("id_compra");
        if (idCompraStr != null && !idCompraStr.isEmpty()) {
            try {
                int idCompra = Integer.parseInt(idCompraStr);
                CompraDAO compraDAO = new CompraDAO();
                // Assumindo que o ID para "EM_TROCA" no seu Enum/Banco é 6
                compraDAO.atualizarStatus(idCompra, 6);
                response.sendRedirect("mensagemTrocaSolicitada.jsp?id_compra=" + idCompra);
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter id_compra para inteiro: " + e.getMessage());
                // Redirecionar para uma página de erro ou exibir uma mensagem
            } catch (SQLException e) {
                System.err.println("Erro ao atualizar o status da compra: " + e.getMessage());
                e.printStackTrace();
                // Redirecionar para uma página de erro ou exibir uma mensagem
            }
        } else {
            // id_compra não foi fornecido
            // Redirecionar para uma página de erro ou exibir uma mensagem
        }
    }
}



