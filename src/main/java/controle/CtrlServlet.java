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
import java.time.LocalDate;

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
            case "exibirDashboardADM":
                HttpSession session = request.getSession(false); // Não cria nova sessão se não existir
                boolean isAdmin = false;
                if (session != null && session.getAttribute("admId") != null) {
                    isAdmin = true;
                }
                if (isAdmin) {
                    System.out.println("DEBUG: Administrador logado. Encaminhando para o dashboard.");

                    request.getRequestDispatcher("/dashboardAdmin.jsp").forward(request, response);
                } else {
                    System.out.println("DEBUG: Acesso negado. Redirecionando para página inicial.");
                    response.sendRedirect(request.getContextPath() + "/index.jsp?error=access_denied_dashboard");
                }
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
            case "alterarQuantidade":
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
                voltarHomePageADM(request, response);
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
                conn = Conexao.createConnectionToMySQL();
                String senhaCriptografadaCliente = clienteDAO.senhaCriptografada(senha);
                Cliente clienteConsultado = clienteDAO.consultarPorEmailESenha(email, senhaCriptografadaCliente, conn);

                if (clienteConsultado != null) {
                    request.getSession().setAttribute("clienteId", clienteConsultado.getId());
                    request.getSession().setAttribute("nomeCliente", clienteConsultado.getNome());
                    request.getSession().setAttribute("primeiroAcesso", true);
                    System.out.println("Login de cliente bem-sucedido.");
                    homePage(request, response);
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

        request.getSession().invalidate();
        System.out.println("Usuário deslogado.");

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
        ItemDAO itemDAO = new ItemDAO();
        EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();

        List<Livro> todosOsLivros = livroDAO.consultarTodos();
        List<Livro> livrosParaExibir = new ArrayList<>();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                System.err.println("Erro: Conexão nula ao tentar conectar ao banco de dados em homePage.");
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }



            for (Livro livro : todosOsLivros) {

                EstoqueAtual estoque = estoqueAtualDAO.consultarPorLivroId(livro.getId(), conn);

                if (estoque != null && estoque.getQuantidadeAtual() > 0) {
                    livrosParaExibir.add(livro);


                    Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());

                    if (menorPreco != null) {
                        precoPorLivro.put(livro.getId(), menorPreco);
                    } else {

                        precoPorLivro.put(livro.getId(), 0.0);
                    }
                }
            }

        } catch (SQLException e) {

            System.err.println("Erro SQL ao carregar livros para a home: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro ao carregar os livros. Por favor, tente novamente mais tarde.");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao carregar livros para a home: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao carregar os livros. Por favor, tente novamente mais tarde.");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão no homePage: " + e.getMessage());
                e.printStackTrace();
            }
        }


        request.setAttribute("livros", livrosParaExibir);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }


    private void voltarHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LivroDAO livroDAO = new LivroDAO();
        ItemDAO itemDAO = new ItemDAO();
        EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();

        List<Livro> todosOsLivros = livroDAO.consultarTodos();
        List<Livro> livrosParaExibir = new ArrayList<>();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                System.err.println("Erro: Conexão nula ao tentar conectar ao banco de dados em voltarHomePage.");
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            for (Livro livro : todosOsLivros) {

                EstoqueAtual estoque = estoqueAtualDAO.consultarPorLivroId(livro.getId(), conn);

                if (estoque != null && estoque.getQuantidadeAtual() > 0) {
                    livrosParaExibir.add(livro);

                    Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());

                    if (menorPreco != null) {
                        precoPorLivro.put(livro.getId(), menorPreco);
                    } else {

                        precoPorLivro.put(livro.getId(), 0.0);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao carregar livros para a voltarHomePage: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro ao carregar os livros. Por favor, tente novamente mais tarde.");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao carregar livros para a voltarHomePage: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao carregar os livros. Por favor, tente novamente mais tarde.");
            request.getRequestDispatcher("erro.jsp").forward(request, response);
            return;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão em voltarHomePage: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Passa a lista JÁ FILTRADA para a JSP
        request.setAttribute("livros", livrosParaExibir);
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
            Map<Integer, Integer> estoqueAtualPorLivro = new HashMap<>();
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

            Estoque entrada = new Estoque();
            entrada.setIdLivro(idLivro);
            entrada.setQuantidadeEstoque(qtdeEntrada);
            entrada.setTipoEntrada(TipoEntrada.COMPRA);
            entrada.setDataEntrada(dataEntrada);
            entrada.setValorCusto(valorCusto);
            entrada.setIdFornecedor(idFornecedor);

            EstoqueDAO estoqueDAO = new EstoqueDAO();
            estoqueDAO.salvar(entrada, conn);
            EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
            EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);

            if (estoqueAtual == null) {
                EstoqueAtual novoEstoqueAtual = new EstoqueAtual();
                novoEstoqueAtual.setIdLivro(idLivro);
                novoEstoqueAtual.setQuantidadeAtual(qtdeEntrada);
                estoqueAtualDAO.salvar(novoEstoqueAtual, conn);
            } else {

                estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + qtdeEntrada);
                estoqueAtualDAO.atualizar(estoqueAtual, conn);
            }

            request.setAttribute("mensagemSucesso", "Entrada de estoque registrada com sucesso!");
            exibirEstoque(request, response);

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
        String redirectPage = request.getParameter("redirect");
        if (redirectPage == null || redirectPage.isEmpty()) {
            redirectPage = "consultarCartao";
        }

        Integer clienteIdSessao = null;

        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                clienteIdSessao = (Integer) session.getAttribute("clienteId");
                if (clienteIdSessao != null) {
                    System.out.println("DEBUG SALVAR CARTÃO: ID do cliente recuperado da SESSÃO: " + clienteIdSessao);
                } else {
                    System.out.println("DEBUG SALVAR CARTÃO: ID do cliente NÃO encontrado na sessão.");
                }
            } else {
                System.out.println("DEBUG SALVAR CARTÃO: Sessão NÃO encontrada.");
            }


            if (clienteIdSessao == null) {
                System.out.println("DEBUG SALVAR CARTÃO: Cliente não logado. Redirecionando para login.");
                request.setAttribute("mensagemErro", "Você precisa estar logado para adicionar um cartão.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String nomeTitular = request.getParameter("nomeTitular_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: nomeTitular: " + nomeTitular);
            String numeroCartao = request.getParameter("numeroCartao_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: numeroCartao: " + numeroCartao);
            String codSeguranca = request.getParameter("codSeguranca_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: codSeguranca: " + codSeguranca);
            String dataVencimento = request.getParameter("dataVencimento_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: dataVencimento: " + dataVencimento);
            String bandeiraCartao = request.getParameter("bandeira_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: bandeira: " + bandeiraCartao);
            String preferencialCartao = request.getParameter("preferencialCartao_novoCartao");
            System.out.println("DEBUG SALVAR CARTÃO: preferencialCartao: " + preferencialCartao);

            boolean preferencial = "true".equals(preferencialCartao);

            Cartao cartaoSalvar = new Cartao();
            cartaoSalvar.setNomeTitular(nomeTitular);
            cartaoSalvar.setNumero(numeroCartao);
            cartaoSalvar.setCodSeguranca(codSeguranca);
            cartaoSalvar.setDataVencimento(dataVencimento);
            cartaoSalvar.setPreferencial(preferencial);
            cartaoSalvar.setBandeiraCartao(BandeiraCartao.fromDescricao(bandeiraCartao));

            cartaoSalvar.setIdCliente(clienteIdSessao);

            IFachada fachada = new Fachada();

            String msgNovoCartao = fachada.salvarCartao(cartaoSalvar, clienteIdSessao); // <-- ADICIONAR ESTA LINHA

            System.out.println("DEBUG SALVAR CARTÃO: ID Cliente associado ao cartão antes de chamar fachada: " + cartaoSalvar.getIdCliente());
            System.out.println("DEBUG SALVAR CARTÃO: Resultado do salvar (Res): " + msgNovoCartao);

            // Lógica de redirecionamento condicional
            if ("finalizarCompra".equals(redirectPage)) {

                response.sendRedirect("servlet?action=cartaoCompra");
            } else {

                response.sendRedirect(redirectPage + ".jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao salvar cartão: " + e.getMessage());

            String retornoUrl = "/adicionarNovoCartao.jsp?redirect=" + (redirectPage != null ? redirectPage : "");

            request.getRequestDispatcher(retornoUrl).forward(request, response);
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
        // Parâmetros de controle de redirecionamento e comportamento do formulário
        String redirectPage = request.getParameter("redirect");
        // Se o parâmetro 'redirect' não for especificado, volta para a tela de consulta de endereços por padrão
        if (redirectPage == null || redirectPage.isEmpty()) {
            redirectPage = "consultarEndereco";
        }

        // Parâmetro para indicar se o tipo de endereço é predefinido (usado no JSP)
        String tipoEnderecoPredefinido = request.getParameter("tipoEnderecoPredefinido");

        // NOVO: Parâmetro para identificar a origem específica dentro de um fluxo (ex: "cobranca" na finalização da compra)
        String origemFluxo = request.getParameter("origemFluxo"); // Ex: 'cobranca' ou 'entrega' ou null

        try {
            HttpSession session = request.getSession(false);
            Integer clienteId = null;

            if (session != null) {
                clienteId = (Integer) session.getAttribute("clienteId");
                if (clienteId != null) {
                    System.out.println("DEBUG SALVAR ENDEREÇO: ID do cliente recuperado da sessão: " + clienteId);
                } else {
                    System.out.println("DEBUG SALVAR ENDEREÇO: ID do cliente NÃO encontrado na sessão. Possível problema de sessão ou login.");
                }
            } else {
                System.out.println("DEBUG SALVAR ENDEREÇO: Sessão NÃO encontrada. Usuário não logado ou sessão expirada.");
            }

            if (clienteId == null) {
                throw new ServletException("Sessão inválida ou ID do cliente não encontrado. Por favor, faça login novamente.");
            }

            // --- INÍCIO DA LÓGICA DE CRIAÇÃO E POPULAÇÃO DO NOVO ENDEREÇO ---
            String nomePaisNovoEnd = request.getParameter("nomePais_NovoEnd");
            String nomeEstadoNovoEnd = request.getParameter("nomeEstado_NovoEnd");
            String nomeCidadeNovoEnd = request.getParameter("nomeCidade_NovoEnd");
            String tipoLogradouroNovoEnd = request.getParameter("tipoLogradouro_NovoEnd");
            String tipoResidenciaNovoEnd = request.getParameter("tipoResidencia_NovoEnd");
            // Este é o tipo de endereço que foi predefinido (se houver) ou selecionado no dropdown
            String tipoEnderecoNovoEnd = request.getParameter("tipoEndereco_NovoEnd");
            String cepNovoEnd = request.getParameter("cep_NovoEnd");
            String logradouroNovoEnd = request.getParameter("logradouro_NovoEnd");
            String numeroNovoEnd = request.getParameter("numero_NovoEnd");
            String bairroNovoEnd = request.getParameter("bairro_NovoEnd");
            String observacaoNovoEnd = request.getParameter("observacao_NovoEnd");

            Endereco enderecoNovo = new Endereco();
            enderecoNovo.setLogradouro(logradouroNovoEnd);
            enderecoNovo.setNumero(numeroNovoEnd);
            enderecoNovo.setBairro(bairroNovoEnd);
            enderecoNovo.setCep(cepNovoEnd);
            enderecoNovo.setObservacao(observacaoNovoEnd);

            // Validação e set dos Tipos de Entidade (Residência, Logradouro, Endereço)
            if (tipoResidenciaNovoEnd == null || tipoResidenciaNovoEnd.isEmpty()) {
                throw new IllegalArgumentException("Nenhum tipo de residência selecionado.");
            } else {
                enderecoNovo.setTipoResidencia(TipoResidencia.fromDescricao(tipoResidenciaNovoEnd));
            }
            if (tipoLogradouroNovoEnd == null || tipoLogradouroNovoEnd.isEmpty()) {
                throw new IllegalArgumentException("Nenhum tipo de logradouro selecionado.");
            } else {
                enderecoNovo.setTipoLogradouro(TipoLogradouro.fromDescricao(tipoLogradouroNovoEnd));
            }

            if (tipoEnderecoNovoEnd == null || tipoEnderecoNovoEnd.isEmpty()) {
                throw new IllegalArgumentException("Nenhum tipo de endereço selecionado.");
            } else {
                enderecoNovo.setTipoEndereco(TipoEndereco.fromDescricao(tipoEnderecoNovoEnd));
            }

            // População de País, Estado e Cidade no objeto Endereco
            Pais paisNovoEnd = new Pais();
            paisNovoEnd.setNome(nomePaisNovoEnd);

            Estado estadoNovoEnd = new Estado();
            estadoNovoEnd.setNome(nomeEstadoNovoEnd);
            estadoNovoEnd.setPais(paisNovoEnd); // Certifique-se de associar o país ao estado

            Cidade cidadeNovoEnd = new Cidade();
            cidadeNovoEnd.setNome(nomeCidadeNovoEnd);
            cidadeNovoEnd.setEstado(estadoNovoEnd); // Certifique-se de associar o estado à cidade
            enderecoNovo.setCidade(cidadeNovoEnd);

            // --- PONTO CRÍTICO: Associar o Endereço ao ID do Cliente da Sessão ---
            enderecoNovo.setIdCliente(clienteId);

            System.out.println("DEBUG SALVAR ENDEREÇO: Endereço final pronto para salvar: " + enderecoNovo);
            System.out.println("DEBUG SALVAR ENDEREÇO: ID do Cliente associado ao Endereço: " + enderecoNovo.getIdCliente());

            // Instancia a fachada e salva o novo endereço
            IFachada fachada = new Fachada();
            String msgNovoEnd = fachada.adicionarEnderecoParaClienteExistente(enderecoNovo);
            System.out.println("DEBUG SALVAR ENDEREÇO: Resultado do salvar pela fachada: " + msgNovoEnd);

            // --- Lógica de redirecionamento condicional ATUALIZADA ---
            if ("finalizarCompra".equals(redirectPage)) {
                // Se a origem do fluxo for "cobranca", retorna para a tela de cobrança
                if ("cobranca".equals(origemFluxo)) {
                    response.sendRedirect("servlet?action=enderecoCobrancaCompra");
                } else {
                    // Caso contrário (ex: origem "entrega" ou não especificada), retorna para a tela de entrega
                    response.sendRedirect("servlet?action=enderecoEntregaCompra");
                }
            } else if ("consultarEndereco".equals(redirectPage)) {
                // Se veio da tela de gerenciamento de endereços, volta para lá
                response.sendRedirect("servlet?action=consultarEndereco");
            } else {
                // Outros casos de redirecionamento genérico
                response.sendRedirect(redirectPage + ".jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao salvar endereço: " + e.getMessage());

            // Em caso de erro, repassa os parâmetros para o JSP para manter o contexto
            String retornoUrl = "adicionarNovoEndereco.jsp?redirect=" + (redirectPage != null ? redirectPage : "");
            if (tipoEnderecoPredefinido != null && !tipoEnderecoPredefinido.isEmpty()) {
                retornoUrl += "&tipoEnderecoPredefinido=" + tipoEnderecoPredefinido;
            }
            // Inclui o modoEdicao para garantir que o JSP se comporte corretamente em caso de erro
            boolean modoEdicaoCompleta = "true".equalsIgnoreCase(request.getParameter("modoEdicao"));
            if (modoEdicaoCompleta) {
                retornoUrl += "&modoEdicao=true";
            }
            // NOVO: Repassar também o parametro 'origemFluxo' em caso de erro
            if (origemFluxo != null && !origemFluxo.isEmpty()) {
                retornoUrl += "&origemFluxo=" + origemFluxo;
            }

            request.getRequestDispatcher(retornoUrl).forward(request, response);
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

                int quantidadeParaAdicionarAoCarrinho = quantidadeDesejada;
                if (itemExistente != null) {
                    quantidadeParaAdicionarAoCarrinho = quantidadeDesejada; // Se já existe, adiciona a quantidade desejada
                    carrinhoItemDAO.atualizarQuantidade(itemExistente.getId(), itemExistente.getQuantidade() + quantidadeParaAdicionarAoCarrinho, conn); // Passa a conexão da transação
                    request.setAttribute("mensagem", "Quantidade do item atualizada no carrinho.");
                    System.out.println("DEBUG: Quantidade do item " + idLivro + " atualizada para " + (itemExistente.getQuantidade() + quantidadeParaAdicionarAoCarrinho));
                } else {
                    // Item não existe, adicionar novo ao carrinho
                    CarrinhoItem novoItem = new CarrinhoItem();
                    novoItem.setIdCarrinho(idCarrinho);
                    novoItem.setIdLivro(idLivro);
                    novoItem.setQuantidade(quantidadeParaAdicionarAoCarrinho);

                    String msgCarrinho = carrinhoItemDAO.salvar(novoItem, conn); // Passa a conexão da transação
                    System.out.println("DEBUG: Resultado do salvar no carrinho = " + msgCarrinho);
                    request.setAttribute("mensagem", "Item adicionado ao carrinho com sucesso.");
                }

                // --- 3. Reduzir estoque atual ---
                estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() - quantidadeParaAdicionarAoCarrinho);
                estoqueAtualDAO.atualizar(estoqueAtual, conn); // Passa a conexão da transação
                System.out.println("DEBUG: Estoque do livro " + idLivro + " reduzido em " + quantidadeParaAdicionarAoCarrinho + ". Nova quantidade: " + estoqueAtual.getQuantidadeAtual());

                // --- 4. Registrar saída de estoque ---
                EstoqueSaidaDAO estoqueSaidaDAO = new EstoqueSaidaDAO();
                dominio.EstoqueSaida saida = new dominio.EstoqueSaida();
                saida.setIdLivro(idLivro);
                saida.setQuantidade(quantidadeParaAdicionarAoCarrinho);
                saida.setMotivoSaida(TipoSaida.SAIDA_CARRINHO); // Motivo da saída: SAIDA_CARRINHO
                saida.setDataSaida(LocalDate.now());
                estoqueSaidaDAO.salvar(saida, conn);
                System.out.println("DEBUG: Saída de " + quantidadeParaAdicionarAoCarrinho + " unidades do livro " + idLivro + " registrada em saida_estoque (motivo: SAIDA_CARRINHO).");

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

        Cupom cupomPromocional = (Cupom) session.getAttribute("cupomSelecionado");
        CupomTroca cupomTroca = (CupomTroca) session.getAttribute("cupomTrocaSelecionado");

        double descontoPromocional = (cupomPromocional != null) ? cupomPromocional.getValor() : 0.00;
        double descontoTroca = (cupomTroca != null) ? cupomTroca.getValorCupom() : 0.00;

        // ***** ALTERAÇÃO AQUI *****
        // Calcula o valor total dos cupons aplicados
        double totalCuponsAplicados = descontoPromocional + descontoTroca;

        // Calcula o valor total do pedido ANTES dos cupons (Subtotal + Frete)
        double valorTotalPedidoSemCupons = subtotal + valorFrete;

        double valorFinalAPagar;
        double valorTrocoGerado = 0.0; // Inicializa o troco como zero

        // Lógica para calcular valor final e troco
        if (totalCuponsAplicados > valorTotalPedidoSemCupons) {
            // Se o valor dos cupons é maior que o total do pedido
            valorFinalAPagar = 0.0; // O cliente não paga nada
            valorTrocoGerado = totalCuponsAplicados - valorTotalPedidoSemCupons;
        } else {
            // Se o valor dos cupons é menor ou igual ao total do pedido
            valorFinalAPagar = valorTotalPedidoSemCupons - totalCuponsAplicados;
            valorTrocoGerado = 0.0; // Não há troco
        }
        // ***** FIM ALTERAÇÃO AQUI *****

        // Arredonde os valores para duas casas decimais
        valorFinalAPagar = Math.round(valorFinalAPagar * 100.0) / 100.0;
        valorTrocoGerado = Math.round(valorTrocoGerado * 100.0) / 100.0;

        session.setAttribute("previaPedidoItens", itensCarrinho);
        session.setAttribute("previaPedidoSubtotal", subtotal);
        session.setAttribute("previaPedidoTipoFreteSelecionadoStr", tipoFreteSelecionadoStr);
        session.setAttribute("previaPedidoValorFrete", valorFrete);

        session.setAttribute("previaPedidoCupomPromocional", cupomPromocional);
        session.setAttribute("previaPedidoCupomTroca", cupomTroca);
        session.setAttribute("previaPedidoDescontoPromocional", descontoPromocional);
        session.setAttribute("previaPedidoDescontoTroca", descontoTroca);

        // Salva o total a pagar e, mais importante, o troco na sessão
        session.setAttribute("previaPedidoTotal", valorFinalAPagar);
        session.setAttribute("previaPedidoTrocoGerado", valorTrocoGerado); // ***** ESTE É CRUCIAL! *****

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

            // --- Correções para garantir que os parâmetros não sejam nulos antes de parsear ---
            String idEnderecoEntregaStr = request.getParameter("idEnderecoEntrega");
            Integer idEnderecoEntrega = null;
            if (idEnderecoEntregaStr != null && !idEnderecoEntregaStr.isEmpty()) {
                idEnderecoEntrega = Integer.parseInt(idEnderecoEntregaStr);
            } else {
                System.err.println("ERRO: idEnderecoEntrega está nulo ou vazio.");
                session.setAttribute("mensagemErro", "Endereço de entrega não selecionado.");
                response.sendRedirect("finalizarCompra.jsp"); // Redireciona de volta
                return;
            }

            String idEnderecoCobrancaStr = request.getParameter("idEnderecoCobranca");
            Integer idEnderecoCobranca = null;
            if (idEnderecoCobrancaStr != null && !idEnderecoCobrancaStr.isEmpty()) {
                idEnderecoCobranca = Integer.parseInt(idEnderecoCobrancaStr);
            } else {
                System.err.println("ERRO: idEnderecoCobranca está nulo ou vazio.");
                session.setAttribute("mensagemErro", "Endereço de cobrança não selecionado.");
                response.sendRedirect("finalizarCompra.jsp"); // Redireciona de volta
                return;
            }

            String valorSubtotalStr = request.getParameter("valorSubtotal");
            double valorSubtotal = 0.0;
            if (valorSubtotalStr != null && !valorSubtotalStr.isEmpty()) {
                valorSubtotal = Double.parseDouble(valorSubtotalStr);
            } else {
                System.err.println("AVISO: valorSubtotal está nulo ou vazio. Usando 0.0.");
            }

            // Este valorDescontoTotal é o total de todos os descontos (promocional + troca) aplicado na compra
            String valorDescontoTotalStr = request.getParameter("valorDescontoPromocional"); // Pegando o desconto promocional como 'valorDesconto'
            double valorDescontoPromocional = 0.0;
            if (valorDescontoTotalStr != null && !valorDescontoTotalStr.isEmpty()) {
                valorDescontoPromocional = Double.parseDouble(valorDescontoTotalStr);
            } else {
                System.err.println("AVISO: valorDescontoPromocional está nulo ou vazio. Usando 0.0.");
            }

            // Valor do desconto de troca, separado
            String valorDescontoTrocaStr = request.getParameter("valorDescontoTroca");
            double valorDescontoTroca = 0.0;
            if (valorDescontoTrocaStr != null && !valorDescontoTrocaStr.isEmpty()) {
                valorDescontoTroca = Double.parseDouble(valorDescontoTrocaStr);
            } else {
                System.err.println("AVISO: valorDescontoTroca está nulo ou vazio. Usando 0.0.");
            }
            double valorTotalDesconto = valorDescontoPromocional + valorDescontoTroca;


            // valorTotal é o total final da compra APÓS DESCONTOS de cupons e COM FRETE
            String valorTotalStr = request.getParameter("valorTotal");
            double valorTotalFinalComDescontosEFrete = 0.0;
            if (valorTotalStr != null && !valorTotalStr.isEmpty()) {
                valorTotalFinalComDescontosEFrete = Double.parseDouble(valorTotalStr);
            } else {
                System.err.println("AVISO: valorTotal está nulo ou vazio. Usando 0.0.");
            }

            Integer idCupomPromocional = null;
            Cupom cupomPromocionalSessao = (Cupom) session.getAttribute("previaPedidoCupomPromocional");
            if (cupomPromocionalSessao != null) {
                idCupomPromocional = cupomPromocionalSessao.getId();
                System.out.println("DEBUG: Cupom Promocional usado - ID: " + idCupomPromocional + ", Valor: " + valorDescontoPromocional);
            } else {
                System.out.println("DEBUG: Nenhum cupom promocional usado.");
            }

            Integer idCupomTroca = null;
            CupomTroca cupomTrocaSessao = (CupomTroca) session.getAttribute("previaPedidoCupomTroca");
            if (cupomTrocaSessao != null) {
                idCupomTroca = cupomTrocaSessao.getIdCupomTroca();
                System.out.println("DEBUG: Cupom de Troca usado - ID: " + idCupomTroca + ", Valor: " + valorDescontoTroca);
            } else {
                System.out.println("DEBUG: Nenhum cupom de troca usado.");
            }

            String tipoFreteSelecionadoStr = request.getParameter("tipoFreteSelecionado");
            TipoFrete valorFreteEnum = null;
            double valorFreteParaPagamento = 0.0;

            if (tipoFreteSelecionadoStr != null && !tipoFreteSelecionadoStr.isEmpty()) {
                try {
                    valorFreteEnum = TipoFrete.fromString(tipoFreteSelecionadoStr);
                    Double valorFreteSessao = (Double) session.getAttribute("previaPedidoValorFrete");
                    if (valorFreteSessao != null) {
                        valorFreteParaPagamento = valorFreteSessao;
                    } else {
                        valorFreteParaPagamento = valorFreteEnum.getValor();
                    }
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
            Map<Integer, Double> valoresPagosPorCartao = new HashMap<>();
            double somaValoresPagosCartoes = 0.0;

            if (idsCartoesParam != null) {
                for (String idStr : idsCartoesParam) {
                    if (idStr != null && !idStr.isEmpty()) {
                        Integer idCartao = Integer.parseInt(idStr);
                        idsCartoes.add(idCartao);

                        String valorPagoStr = request.getParameter("valorPagoCartao_" + idCartao);
                        if (valorPagoStr != null && !valorPagoStr.isEmpty()) {
                            try {
                                double valor = Double.parseDouble(valorPagoStr);
                                valoresPagosPorCartao.put(idCartao, valor);
                                somaValoresPagosCartoes += valor;
                            } catch (NumberFormatException e) {
                                System.err.println("ERRO: Valor inválido para o cartão ID " + idCartao + ": " + valorPagoStr);
                                session.setAttribute("mensagemErro", "Erro: Formato inválido para um dos valores de pagamento com cartão.");
                                response.sendRedirect("finalizarCompra.jsp");
                                return;
                            }
                        } else {
                            System.err.println("ERRO: Valor para o cartão ID " + idCartao + " é nulo ou vazio.");
                            session.setAttribute("mensagemErro", "Erro: Por favor, preencha o valor para todos os cartões selecionados.");
                            response.sendRedirect("finalizarCompra.jsp");
                            return;
                        }
                    }
                }
            }

            if (Math.abs(somaValoresPagosCartoes - valorTotalFinalComDescontosEFrete) > 0.01) {
                System.err.println("ERRO: A soma dos valores dos cartões (" + somaValoresPagosCartoes + ") não corresponde ao total da compra (" + valorTotalFinalComDescontosEFrete + ")");
                session.setAttribute("mensagemErro", "A soma dos valores informados para os cartões não corresponde ao total da compra. Por favor, ajuste.");
                response.sendRedirect("finalizarCompra.jsp");
                return;
            }

            boolean cupomUsado = (idCupomPromocional != null && valorDescontoPromocional > 0) || (idCupomTroca != null && valorDescontoTroca > 0);

            if (!idsCartoes.isEmpty()) {
                for (Map.Entry<Integer, Double> entry : valoresPagosPorCartao.entrySet()) {
                    Double valorPagoNesteCartao = entry.getValue();

                    if (idsCartoes.size() > 1 && valorPagoNesteCartao < 10.00 && !cupomUsado) {
                        System.err.println("ERRO: RN0034 - Ao usar múltiplos cartões, o valor mínimo por cartão é R$ 10,00. Cartão com valor: " + valorPagoNesteCartao);
                        session.setAttribute("mensagemErro", "Erro no pagamento: Ao usar múltiplos cartões, cada cartão deve pagar no mínimo R$ 10,00, a menos que cupons sejam utilizados.");
                        response.sendRedirect("finalizarCompra.jsp");
                        return;
                    }

                    if (idsCartoes.size() == 1 && valorPagoNesteCartao < 10.00 && !cupomUsado) {
                        System.err.println("ERRO: RN0035 - Pagamento com único cartão abaixo de R$10,00 sem uso de cupons. Valor pago: " + valorPagoNesteCartao);
                        session.setAttribute("mensagemErro", "Erro no pagamento: O valor mínimo para pagamento com cartão é R$ 10,00, a menos que cupons sejam utilizados.");
                        response.sendRedirect("finalizarCompra.jsp");
                        return;
                    }
                }
            } else if (valorTotalFinalComDescontosEFrete > 0) {
                System.err.println("ERRO: Nenhum cartão selecionado, mas valor remanescente para pagamento é R$" + valorTotalFinalComDescontosEFrete);
                session.setAttribute("mensagemErro", "Nenhum método de pagamento foi selecionado para cobrir o valor total da compra.");
                response.sendRedirect("finalizarCompra.jsp");
                return;
            }

            List<ItemCompra> itensCompra = new ArrayList<>();
            Map<String, String[]> parametros = request.getParameterMap();
            int index = 0;
            while (parametros.containsKey("itens[" + index + "].idLivro")) {
                String[] idLivroArray = parametros.get("itens[" + index + "].idLivro");
                String[] quantidadeArray = parametros.get("itens[" + index + "].quantidade");
                String[] valorUnitarioArray = parametros.get("itens[" + index + "].valorUnitario");

                int idLivro = 0;
                if (idLivroArray != null && idLivroArray.length > 0 && idLivroArray[0] != null && !idLivroArray[0].isEmpty()) {
                    idLivro = Integer.parseInt(idLivroArray[0]);
                } else {
                    System.err.println("ERRO: idLivro para o item do carrinho na posição " + index + " é nulo ou vazio. Item ignorado.");
                    index++;
                    continue;
                }

                int quantidade = 0;
                if (quantidadeArray != null && quantidadeArray.length > 0 && quantidadeArray[0] != null && !quantidadeArray[0].isEmpty()) {
                    quantidade = Integer.parseInt(quantidadeArray[0]);
                } else {
                    System.err.println("ERRO: quantidade para o item do carrinho na posição " + index + " é nulo ou vazio. Item ignorado.");
                    index++;
                    continue;
                }

                double valorUnitario = 0.0;
                if (valorUnitarioArray != null && valorUnitarioArray.length > 0 && valorUnitarioArray[0] != null && !valorUnitarioArray[0].isEmpty()) {
                    valorUnitario = Double.parseDouble(valorUnitarioArray[0]);
                } else {
                    System.err.println("ERRO: valorUnitario para o item do carrinho na posição " + index + " é nulo ou vazio. Item ignorado.");
                    index++;
                    continue;
                }

                ItemCompra item = new ItemCompra();
                item.setIdLivro(idLivro);
                item.setQuantidade(quantidade);
                item.setValorUnitarioNaCompra(valorUnitario);
                itensCompra.add(item);
                index++;
            }

            if (itensCompra.isEmpty()) {
                System.err.println("ERRO: Nenhum item válido encontrado no carrinho para finalizar a compra.");
                request.getSession().setAttribute("mensagemErro", "Não foi possível finalizar a compra: carrinho vazio ou com itens inválidos.");
                response.sendRedirect("carrinho.jsp");
                return;
            }

            conn = Conexao.createConnectionToMySQL();
            conn.setAutoCommit(false);
            System.out.println("DEBUG: Conexão com banco estabelecida e transação iniciada");

            CompraDAO compraDAO = new CompraDAO();
            String numeroPedidoGerado = compraDAO.gerarNumeroPedidoAleatorio();

            Compra compraParaSalvar = new Compra(clienteId, idEnderecoEntrega, idEnderecoCobranca, valorSubtotal, valorFreteEnum, valorTotalDesconto, valorTotalFinalComDescontosEFrete, 1, numeroPedidoGerado);
            int idCompraGerado = compraDAO.salvar(compraParaSalvar, conn);
            System.out.println("DEBUG: Compra salva com ID = " + idCompraGerado + " e Número do Pedido = " + compraParaSalvar.getNumeroPedido());

            ItensCompraDAO itensCompraDAO = new ItensCompraDAO();
            itensCompraDAO.salvar(idCompraGerado, itensCompra, conn);
            System.out.println("DEBUG: Itens da compra salvos");

            EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
            EstoqueSaidaDAO estoqueSaidaDAO = new EstoqueSaidaDAO();

            for (ItemCompra itemCompra : itensCompra) {
                int idLivro = itemCompra.getIdLivro();
                int quantidadeVendida = itemCompra.getQuantidade();

                dominio.EstoqueSaida saida = new dominio.EstoqueSaida();
                saida.setIdLivro(idLivro);
                saida.setQuantidade(quantidadeVendida);
                saida.setMotivoSaida(TipoSaida.VENDA);
                saida.setDataSaida(LocalDate.now());
                estoqueSaidaDAO.salvar(saida, conn);
                System.out.println("DEBUG: Saída de " + quantidadeVendida + " unidades do livro " + idLivro + " registrada em saida_estoque.");

                EstoqueAtual estoqueAtual = estoqueAtualDAO.consultarPorLivroId(idLivro, conn);
                if (estoqueAtual != null && estoqueAtual.getQuantidadeAtual() >= quantidadeVendida) {
                    estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() - quantidadeVendida);
                    estoqueAtualDAO.atualizar(estoqueAtual, conn);
                    System.out.println("DEBUG: Estoque atual do livro " + idLivro + " decrementado em " + quantidadeVendida + " unidades.");
                } else {
                    conn.rollback();
                    String mensagemErroEstoque = "Erro: Estoque insuficiente para o livro " + idLivro + ". A compra foi cancelada.";
                    System.err.println("ERRO: " + mensagemErroEstoque);
                    request.getSession().setAttribute("mensagemErro", mensagemErroEstoque);
                    response.sendRedirect("carrinho.jsp?erroEstoque=true");
                    return;
                }
            }

            PagamentoCompraDAO pagamentoCompraDAO = new PagamentoCompraDAO();
            pagamentoCompraDAO.salvarPagamentosComCartao(idCompraGerado, valoresPagosPorCartao, idCupomPromocional, valorTotalDesconto, valorFreteParaPagamento, conn);
            System.out.println("DEBUG: Pagamentos individuais dos cartões e cupom salvos.");

            CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
            carrinhoDAO.excluir(clienteId, conn);
            session.removeAttribute("carrinho");
            session.removeAttribute("previaPedidoItens");
            session.removeAttribute("previaPedidoSubtotal");
            session.removeAttribute("previaPedidoTipoFreteSelecionadoStr");
            session.removeAttribute("previaPedidoValorFrete");
            session.removeAttribute("previaPedidoCupomPromocional");
            session.removeAttribute("previaPedidoCupomTroca");
            session.removeAttribute("previaPedidoDescontoPromocional");
            session.removeAttribute("previaPedidoDescontoTroca");
            session.removeAttribute("previaPedidoTotal");
            session.removeAttribute("cupomSelecionado");
            session.removeAttribute("cupomTrocaSelecionado");
            session.removeAttribute("previaPedidoTrocoGerado"); // Limpa o troco da sessão
            System.out.println("DEBUG: Carrinho e dados de sessão do pedido limpos");

            conn.commit(); // Commit da transação principal (compra, estoque, pagamento)
            System.out.println("DEBUG: Transação commitada");


            if (idCupomPromocional != null && valorDescontoPromocional > 0) {
                CupomDAO cupomDAO = new CupomDAO();

                boolean cupomDesassociado = cupomDAO.excluirCupomDoCliente(clienteId, idCupomPromocional, conn);
                if (cupomDesassociado) {
                    System.out.println("DEBUG: Cupom Promocional ID " + idCupomPromocional + " desassociado para o cliente ID " + clienteId);
                } else {
                    System.out.println("DEBUG: Falha ao desassociar o Cupom Promocional ID " + idCupomPromocional + " para o cliente ID " + clienteId);
                }
            }

            if (idCupomTroca != null && valorDescontoTroca > 0) {
                CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
                boolean cupomExcluido = cupomTrocaDAO.excluir(idCupomTroca, conn);
                if (cupomExcluido) {
                    System.out.println("DEBUG: Cupom de Troca ID " + idCupomTroca + " excluído.");
                } else {
                    System.err.println("DEBUG: Falha ao excluir o Cupom de Troca ID " + idCupomTroca + ".");
                }
            }

            String valorTrocoGeradoStr = request.getParameter("valorTrocoGerado");
            double valorTrocoGerado = 0.0;
            if (valorTrocoGeradoStr != null && !valorTrocoGeradoStr.isEmpty()) {
                valorTrocoGerado = Double.parseDouble(valorTrocoGeradoStr);
            } else {
                System.err.println("AVISO: valorTrocoGerado está nulo ou vazio. Nenhum cupom de troco será gerado por excesso.");
            }

            if (valorTrocoGerado > 0.0) {
                System.out.println("DEBUG: Valor a ser gerado como NOVO cupom de troca: R$" + valorTrocoGerado);

                CupomTroca novoCupomTroca = new CupomTroca();
                novoCupomTroca.setIdCliente(clienteId);
                novoCupomTroca.setValorCupom(valorTrocoGerado);
                novoCupomTroca.setDataCriacao(LocalDate.now());

                String novoCodigoCupom = compraDAO.gerarNumeroPedidoAleatorio(); // Reutilizando gerador de número de pedido
                novoCupomTroca.setCodigoCupom(novoCodigoCupom);

                CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
                try {
                    cupomTrocaDAO.salvar(novoCupomTroca);
                    session.setAttribute("mensagemSucessoCupom", "Um cupom de troca de R$ " + String.format("%.2f", valorTrocoGerado) + " foi gerado com o código: " + novoCodigoCupom);
                    System.out.println("DEBUG: Novo cupom de troca gerado com sucesso! Código: " + novoCodigoCupom);

                } catch (SQLException e) {
                    System.err.println("ERRO SQL ao gerar novo cupom de troca: " + e.getMessage());
                    e.printStackTrace();
                    session.setAttribute("mensagemErroCupom", "Houve um problema ao gerar seu cupom de troca. Por favor, contate o suporte.");
                }
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
            // Define uma mensagem de erro genérica para o usuário
            request.getSession().setAttribute("mensagemErro", "Ocorreu um erro no banco de dados ao finalizar a compra. Por favor, tente novamente.");
            response.sendRedirect("finalizarCompra.jsp"); // Redireciona para a mesma página com erro
        } catch (Exception e) {
            System.out.println("DEBUG: Exceção capturada - " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("DEBUG: Transação rollback realizada devido a erro");
                } catch (SQLException ex) {
                    System.out.println("DEBUG: Erro no rollback - " + ex.getMessage());
                }
            }

            request.getSession().setAttribute("mensagemErro", "Ocorreu um erro inesperado ao finalizar a compra. Por favor, tente novamente.");
            response.sendRedirect("finalizarCompra.jsp"); // Redireciona para a mesma página com erro
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


                String statusDescricao = obterDescricaoStatus(pedido.getStatusIdCompra());
                pedido.setStatusDescricao(statusDescricao); // Defina o novo atributo

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
        } catch (SQLException e) {
            System.err.println("[ERROR - CtrlServlet.atualizarStatusCompra] Erro de SQL ao atualizar o status: " + e.getMessage());
            e.printStackTrace();
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
                conn.setAutoCommit(false);
                CompraDAO compraDAO = new CompraDAO();
                ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
                EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
                EstoqueDAO estoqueDAO = new EstoqueDAO();


                List<ItemPedido> itensDoPedido = itemPedidoDAO.consultarPorCompraId(idCompra, conn);


                for (ItemPedido itemPedido : itensDoPedido) {
                    int idLivro = itemPedido.getLivro().getId();
                    int quantidadeDevolvida = itemPedido.getQuantidade();

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


                    BigDecimal valorCusto = estoqueDAO.buscarValorCustoMaisRecente(idLivro, conn);

                    if (valorCusto != null) {
                        dominio.Estoque entrada = new dominio.Estoque();
                        entrada.setIdLivro(idLivro);
                        entrada.setTipoEntrada(TipoEntrada.TROCA);
                        entrada.setQuantidadeEstoque(quantidadeDevolvida);
                        entrada.setValorCusto(valorCusto);
                        entrada.setDataEntrada(LocalDate.now());
                        estoqueDAO.salvar(entrada, conn);
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Entrada de troca registrada para o livro " + idLivro + ", quantidade " + quantidadeDevolvida + ", valor custo " + valorCusto);
                    } else {
                        System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Aviso: Não foi possível encontrar o valor de custo para registrar a entrada de troca do livro " + idLivro + ".");
                        request.setAttribute("mensagemAviso", "Aviso: Não foi possível encontrar o valor de custo para registrar a entrada de troca.");
                    }
                }

                compraDAO.atualizarStatus(idCompra, StatusCompra.TROCA_AUTORIZADA.getId());
                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Status do pedido " + idCompra + " atualizado para TROCA AUTORIZADA.");


                String codigoCupom = gerarCodigoCupomUnico();
                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Código de cupom gerado: " + codigoCupom);

                CupomTroca cupomDevolucao = new CupomTroca();
                cupomDevolucao.setCodigoCupom(codigoCupom);
                cupomDevolucao.setValorCupom(valorCompra);
                cupomDevolucao.setIdCliente(idCliente);
                cupomDevolucao.setIdCompra(idCompra);
                cupomDevolucao.setDataCriacao(LocalDate.now());


                CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();

                cupomTrocaDAO.salvar(cupomDevolucao);

                System.out.println("[DEBUG - CtrlServlet.autorizarTroca] Cupom de troca gerado para o pedido " + idCompra +
                        ", código: " + codigoCupom + ", valor: " + valorCompra + ", cliente: " + idCliente);

                conn.commit();
                response.sendRedirect("servlet?action=exibirPedidosADM&mensagemSucesso=Troca autorizada e cupom gerado com sucesso para o pedido " + idCompra + ", código: " + codigoCupom);
                return;

            } catch (NumberFormatException e) {
                if (conn != null) conn.rollback();
                response.sendRedirect("servlet?action=exibirPedidosADM&erro=Dados inválidos para autorizar a troca.");
                e.printStackTrace();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                response.sendRedirect("servlet?action=exibirPedidosADM&erro=Erro ao autorizar a troca ou gerar o cupom. Detalhes: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                if (conn != null) conn.rollback();
                throw new RuntimeException("Erro inesperado ao autorizar troca: " + e.getMessage(), e);
            } finally {
                if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        } else {
            response.sendRedirect("servlet?action=exibirPedidosADM&erro=Dados incompletos para autorizar a troca.");
        }
    }

    private String gerarCodigoCupomUnico() {

        return "TROCA-" + System.currentTimeMillis();
    }

    private void cartaoCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("exibirCartoesCompra.jsp").forward(request, response);
    }

    private void enderecoCobrancaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("exibirEndCobrCompra.jsp").forward(request, response);
    }

    private void enderecoEntregaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        if (clienteId == null) {

            response.sendRedirect("login.jsp?mensagemErro=Sessão de cliente inválida. Faça login novamente.");
            return;
        }

        try {

            EnderecoDAO enderecoDAO = new EnderecoDAO();
            List<Endereco> todosEnderecosDoCliente = enderecoDAO.consultarPorCliente(clienteId);
            List<Endereco> enderecosEntrega = new ArrayList<>();

            if (todosEnderecosDoCliente != null) {
                for (Endereco endereco : todosEnderecosDoCliente) {

                    if (endereco.getTipoEndereco() != null && endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Entrega")) {
                        enderecosEntrega.add(endereco);
                    }
                }
            }

            request.setAttribute("enderecosEntrega", enderecosEntrega);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao carregar endereços de entrega para seleção: " + e.getMessage());
        }

        request.getRequestDispatcher("exibirEndEntCompra.jsp").forward(request, response);
    }

    private void salvarEnderecoEntregaCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método salvarEnderecoEntregaCompra");

        try {
            HttpSession session = request.getSession();
            Integer clienteId = (Integer) session.getAttribute("clienteId");

            if (clienteId == null) {
                System.out.println("DEBUG: Cliente não logado ou sessão expirada.");
                response.sendRedirect("login.jsp");
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


    private void aplicarCupom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Entrou no método aplicarCupom");
        HttpSession session = request.getSession();
        CupomDAO cupomDAO = new CupomDAO();
        CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
        Cupom cupomPromocionalSelecionado = null;
        CupomTroca cupomTrocaSelecionado = null;

        String idCupomPromocionalStr = request.getParameter("idCupomPromocional");
        String idCupomTrocaStr = request.getParameter("idCupomTroca");


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

                compraDAO.atualizarStatus(idCompra, 6);
                response.sendRedirect("mensagemTrocaSolicitada.jsp?id_compra=" + idCompra);
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter id_compra para inteiro: " + e.getMessage());

            } catch (SQLException e) {
                System.err.println("Erro ao atualizar o status da compra: " + e.getMessage());
                e.printStackTrace();

            }
        } else {

        }
    }
}



