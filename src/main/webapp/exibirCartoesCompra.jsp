<%@ page import="java.util.*" %>
<%@ page import="dominio.Cartao" %>
<%@ page import="dao.CartaoDAO" %>
<%@ page import="dominio.BandeiraCartao" %>

<head>
    <title>Cartoes Compra | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/selecionarCartoesCompra.css?v=<%= System.currentTimeMillis()%>">
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
            <h2>Selecionar Cartoes para Pagamento</h2>

            <form action="servlet" method="post">
                <input type="hidden" name="action" value="salvarCartoesCompra">

                <%
                    Integer clienteId = (Integer) session.getAttribute("clienteId");
                    List<Cartao> cartoesCliente = new ArrayList<>();

                    if (clienteId != null) {
                        CartaoDAO cartaoDAO = new CartaoDAO();
                        cartoesCliente = cartaoDAO.consultarCartoesPorCliente(clienteId);
                    }

                    if (cartoesCliente.isEmpty()) {
                %>
                    <p class="no-cartoes">Nenhum cartao cadastrado.</p>
                    <div class="buttons-container">
                        <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                        <a href="cartaoCadastro.jsp?redirect=finalizarCompra" class="link-adicionar">Adicionar Novo Cartao</a>
                    </div>
                <%
                    } else {
                        List<Integer> cartoesSelecionadosSessao = (List<Integer>) session.getAttribute("cartoesSelecionados");
                        if (cartoesSelecionadosSessao == null) {
                            cartoesSelecionadosSessao = new ArrayList<>();
                        }

                        for (Cartao cartao : cartoesCliente) {
                %>
                    <div class="cartao-container">
                        <h3>Cartao</h3>
                        <div class="checkbox-container">
                            <input type="checkbox" id="cartao_<%= cartao.getId() %>" name="idsCartoes" value="<%= cartao.getId() %>" <%= (cartoesSelecionadosSessao.contains(cartao.getId()) ? "checked" : "") %>>
                            <label for="cartao_<%= cartao.getId() %>">Final <%= cartao.getNumero().substring(cartao.getNumero().length() - 4) %> - <%= cartao.getBandeiraCartao().getDescricao() %></label>
                        </div>
                        <div class="info-cartao">
                            <div class="input-container">
                                <label>Numero Cartao</label>
                                <input type="text" value="<%= cartao.getNumero() %>" disabled>
                            </div>
                            <div class="input-container">
                                <label>Nome do Titular</label>
                                <input type="text" value="<%= cartao.getNomeTitular() %>" disabled>
                            </div>
                            <div class="input-container">
                                <label>Bandeira</label>
                                <input type="text" value="<%= cartao.getBandeiraCartao().getDescricao() %>" disabled>
                            </div>
                            <div class="input-container">
                                <label>Vencimento</label>
                                <input type="text" value="<%= cartao.getDataVencimento() %>" disabled>
                            </div>
                        </div>
                    </div>
                <%
                        }
                %>
                    <div class="buttons-container">
                        <button type="submit" class="salvar-button">Salvar Cartoes Selecionados</button>
                        <button type="button" class="voltar-button" onclick="window.location.href='finalizarCompra.jsp'">Voltar</button>
                        <a href="cartaoCadastro.jsp?redirect=finalizarCompra" class="link-adicionar">Adicionar Novo Cartao</a>
                    </div>
                <%
                    }
                %>
            </form>
        </div>
    </main>
</body>
</html>