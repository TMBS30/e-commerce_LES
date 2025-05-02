<%@ page import="java.util.*" %>
<%@ page import="dominio.Endereco" %>
<%@ page import="dao.EnderecoDAO" %>
<head>
    <title>Endereco de Cobrança | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/selecionarEnderecoCobranca.css?v=<%= System.currentTimeMillis()%>">
    <style>
        .endereco-container {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .endereco-container h3 {
            margin-top: 0;
            margin-bottom: 10px;
        }

        .input-container {
            margin-bottom: 8px;
        }

        .input-container label {
            display: inline-block;
            width: 120px;
            font-weight: bold;
        }

        .input-container input[type="text"] {
            width: calc(100% - 130px);
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 3px;
            box-sizing: border-box;
            margin-bottom: 5px;
        }

        .radio-container {
            margin-bottom: 10px;
        }

        .radio-container input[type="radio"] {
            margin-right: 5px;
        }

        .buttons-container {
            margin-top: 20px;
        }

        .salvar-button, .voltar-button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .salvar-button {
            background-color: #007bff; /* Azul para cobrança */
            color: white;
        }

        .salvar-button:hover {
            background-color: #0056b3;
        }

        .voltar-button {
            background-color: #f44336;
            color: white;
            margin-left: 10px;
        }

        .voltar-button:hover {
            background-color: #d32f2f;
        }

        .no-enderecos {
            color: red;
            font-style: italic;
        }
    </style>
</head>
<body>
<header>
    <a href="servlet?action=voltarHomePage" class="logo-link">
        <h1>PageStation</h1>
        <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
    </a>
</header>

<main>
    <div class="menu-fixo">
        <a href="#" class="chatbot">
            <p class="texto-chatbot">ChatBot</p>
            <img class="icone-chatbot"src="Imagens/Icone - ChatBot.svg" alt="Icone ChatBot">
        </a>

        <div class="input-pesquisa_livro">
            <input class="input-pesquisa"type="text" name="pesquisa_livro" id="pesquisa_livro" placeholder="Procurar livro...">
        </div>

        <a href="#" class="link-categorias">
            <p class="categorias">Categorias</p>
        </a>

        <a href="servlet?action=exibirPedidos" class="link-pedidos">
            <p class="pedidos">Pedidos</p>
        </a>

        <a href="carrinho.jsp" class="link-carrinho">
            <img class="icone-carrinho" src="Imagens/Icone - Carrinho.svg" alt="Icone Carrinho de Compras">
        </a>

        <a href="servlet?action=consultarInfoPessoais" class="link-user">
            <img class="icone-user" src="Imagens/Icone - User.svg" alt="Icone Consultar Informacoes Cadastrais">
        </a>
    </div>
    <div class="container">
    <h2>Selecionar Endereco de Cobranca</h2>

    <form action="servlet" method="post">
        <input type="hidden" name="action" value="salvarEnderecoCobrancaCompra">

        <%
            Integer clienteId = (Integer) session.getAttribute("clienteId");
            List<Endereco> enderecosCobranca = new ArrayList<>();

            if (clienteId != null) {
                EnderecoDAO enderecoDAO = new EnderecoDAO();
                List<Endereco> todosEnderecos = enderecoDAO.consultarPorCliente(clienteId);
                if (todosEnderecos != null) {
                    for (Endereco endereco : todosEnderecos) {
                        if (endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Cobranca")) {
                            enderecosCobranca.add(endereco);
                        }
                    }
                }
            }

            if (enderecosCobranca.isEmpty()) {
        %>
            <p class="no-enderecos">Nenhum endereco de cobranca cadastrado.</p>
            <div class="buttons-container">
                <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                <button type="button" class="add-end-button" onclick="window.location.href='enderecoCadastro.jsp?tipo=cobranca&redirect=finalizarCompra'">Adicionar Endereco de Cobranca</button>
            </div>
        <%
            } else {
                Endereco enderecoCobrancaSelecionadoSessao = (Endereco) session.getAttribute("enderecoCobrancaSelecionado");
                int enderecoCobrancaSelecionadoId = (enderecoCobrancaSelecionadoSessao != null) ? enderecoCobrancaSelecionadoSessao.getId() : -1;

                for (Endereco endereco : enderecosCobranca) {
        %>
            <div class="endereco-container">
                <h3>Endereco de Cobranca</h3>
                <div class="radio-container">
                    <input type="radio" id="endereco_<%= endereco.getId() %>" name="idEnderecoCobrancaSelecionado" value="<%= endereco.getId() %>" <%= (endereco.getId() == enderecoCobrancaSelecionadoId ? "checked" : "") %>>
                    <label for="endereco_<%= endereco.getId() %>">Selecionar este endereco de cobranca</label>
                </div>
                <div class="input-container">
                    <label>CEP:</label>
                    <input type="text" value="<%= endereco.getCep() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Logradouro:</label>
                    <input type="text" value="<%= endereco.getLogradouro() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Tipo Logradouro:</label>
                    <input type="text" value="<%= endereco.getTipoLogradouro() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Numero:</label>
                    <input type="text" value="<%= endereco.getNumero() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Bairro:</label>
                    <input type="text" value="<%= endereco.getBairro() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Cidade:</label>
                    <input type="text" value="<%= endereco.getCidade().getNome() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Estado:</label>
                    <input type="text" value="<%= endereco.getCidade().getEstado().getNome() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Tipo Residencia:</label>
                    <input type="text" value="<%= endereco.getTipoResidencia() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Observacao:</label>
                    <input type="text" value="<%= endereco.getObservacao() %>" disabled>
                </div>
            </div>
        <%
                }
        %>
            <div class="buttons-container">
                <button type="submit" class="salvar-button">Salvar Endereco de Cobranca</button>
                <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                <button type="button" class="link-adicionar" onclick="window.location.href='enderecoCadastro.jsp?tipo=cobranca&redirect=finalizarCompra'">Adicionar Novo Endereco de Cobranca</button>
            </div>
        <%
            }
        %>
    </form>
    </div>
</body>
</html>