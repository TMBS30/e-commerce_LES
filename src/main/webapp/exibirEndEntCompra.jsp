<%@ page import="java.util.*" %>
<%@ page import="dominio.Endereco" %>
<%@ page import="dao.EnderecoDAO" %>
<head>
    <title>Endereço de Entrega | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/selecionarEnderecoEntrega.css?v=<%= System.currentTimeMillis()%>">
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
    <h2>Selecionar Endereco de Entrega</h2>

    <form action="servlet" method="post">
        <input type="hidden" name="action" value="salvarEnderecoEntregaCompra">

        <%
            Integer clienteId = (Integer) session.getAttribute("clienteId");
            if (clienteId == null) {
                response.sendRedirect("login.jsp?mensagemErro=Sessão de cliente inválida. Faça login novamente.");
                return;
            }

            List<Endereco> enderecosEntrega = (List<Endereco>) request.getAttribute("enderecosEntrega");

            // Se a lista não veio da requisição (ex: acesso direto ao JSP sem passar pelo Servlet ou erro anterior),
            // então a carregue do DAO como fallback.
            if (enderecosEntrega == null) {
                enderecosEntrega = new ArrayList<>(); // Inicializa para evitar NullPointerException
                try {
                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    List<Endereco> todosEnderecos = enderecoDAO.consultarPorCliente(clienteId);
                    if (todosEnderecos != null) {
                        for (Endereco endereco : todosEnderecos) {
                            if (endereco.getTipoEndereco() != null && endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Entrega")) {
                                enderecosEntrega.add(endereco);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar endereços do cliente como fallback: " + e.getMessage());
                    // Opcional: request.setAttribute("mensagemErro", "Erro ao carregar endereços: " + e.getMessage());
                }
            }

            // --- DEPURACAO NO JSP: Verifique o tamanho da lista e os IDs dos endereços aqui ---
            System.out.println("DEBUG JSP: Total de endereços de ENTREGA na lista: " + enderecosEntrega.size());
            for (Endereco debugEndereco : enderecosEntrega) {
                System.out.println("DEBUG JSP: Endereço na lista - ID: " + debugEndereco.getId() +
                                   ", Tipo: " + (debugEndereco.getTipoEndereco() != null ? debugEndereco.getTipoEndereco().getDescricao() : "NULL_TIPO") +
                                   ", Logradouro: " + debugEndereco.getLogradouro());
            }
            // --- FIM DA DEPURACAO ---

            if (enderecosEntrega.isEmpty()) {
        %>
            <p class="no-enderecos">Nenhum endereco de entrega cadastrado.</p>
            <div class="buttons-container">
                <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                <button type="button" class="link-adicionar" onclick="window.location.href='adicionarNovoEndereco.jsp?redirect=finalizarCompra&tipoEnderecoPredefinido=Entrega&origemFluxo=entrega'">Adicionar Endereco de Entrega</button>
            </div>
        <%
            } else {
                // Simplificação: Initialize enderecoSelecionadoId com um valor padrão seguro
                Endereco enderecoSelecionadoSessao = (Endereco) session.getAttribute("enderecoSelecionado");
                int enderecoSelecionadoId = (enderecoSelecionadoSessao != null) ? enderecoSelecionadoSessao.getId() : -1;

                // Percorre e exibe CADA endereço na lista
                for (Endereco endereco : enderecosEntrega) {
        %>
            <div class="endereco-container">
                <h3>Endereco de Entrega</h3>
                <div class="radio-container">
                    <%-- Certifique-se de que o ID do endereço é único para cada radio button --%>
                    <input type="radio" id="endereco_<%= endereco.getId() %>" name="idEnderecoEntregaSelecionado" value="<%= endereco.getId() %>" <%= (endereco.getId() == enderecoSelecionadoId ? "checked" : "") %>>
                    <label for="endereco_<%= endereco.getId() %>">Selecionar este endereco</label>
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
                    <input type="text" value="<%= endereco.getTipoLogradouro().getDescricao() %>" disabled>
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
                    <input type="text" value="<%= endereco.getTipoResidencia().getDescricao() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Observacao:</label>
                    <input type="text" value="<%= endereco.getObservacao() %>" disabled>
                </div>
            </div>
        <%
                } // Fim do loop for
        %>
            <div class="buttons-container">
                <button type="submit" class="salvar-button">Salvar Endereco de Entrega</button>
                <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                <button type="button" class="link-adicionar" onclick="window.location.href='adicionarNovoEndereco.jsp?redirect=finalizarCompra&tipoEnderecoPredefinido=Entrega&origemFluxo=entrega'">Adicionar Endereco de Entrega</button>
            </div>
        <%
            } // Fim do if (enderecosEntrega.isEmpty())
        %>
    </form>
    </div>
</body>
</html>