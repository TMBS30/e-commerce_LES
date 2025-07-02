<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    Integer clienteId = (Integer) session.getAttribute("clienteId");
    List<CarrinhoItem> itensCarrinho = (List<CarrinhoItem>) session.getAttribute("previaPedidoItens");
    Double subtotal = (Double) session.getAttribute("previaPedidoSubtotal");
    Double valorFreteSessao = (Double) session.getAttribute("previaPedidoValorFrete");
    String tipoFreteSelecionadoStr = (String) session.getAttribute("previaPedidoTipoFreteSelecionadoStr");

    // Recupera os novos atributos de cupom da sessão
    Cupom cupomPromocional = (Cupom) session.getAttribute("previaPedidoCupomPromocional");
    CupomTroca cupomTroca = (CupomTroca) session.getAttribute("previaPedidoCupomTroca");
    Double descontoPromocional = (Double) session.getAttribute("previaPedidoDescontoPromocional");
    Double descontoTroca = (Double) session.getAttribute("previaPedidoDescontoTroca");
    Double totalFinal = (Double) session.getAttribute("previaPedidoTotal");
    // **NOVO: Recupera o troco gerado do servlet**
    Double valorTrocoGerado = (Double) session.getAttribute("previaPedidoTrocoGerado");


    // Garante que os valores não sejam nulos para evitar NullPointerExceptions
    if (subtotal == null) subtotal = 0.0;
    if (valorFreteSessao == null) valorFreteSessao = 0.0;
    if (descontoPromocional == null) descontoPromocional = 0.0;
    if (descontoTroca == null) descontoTroca = 0.0;
    if (totalFinal == null) totalFinal = 0.0;
    if (valorTrocoGerado == null) valorTrocoGerado = 0.0; // Inicializa como 0.0 se não estiver na sessão


    DecimalFormat df = new DecimalFormat("#0.00");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Finalizar Compra | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/finalizarCompra.css?v=<%= System.currentTimeMillis()%>">
    <style>
        .link-adicionar {
            display: block;
            margin-top: 10px;
            color: blue;
            text-decoration: underline;
            cursor: pointer;
        }
        .link-adicionar:hover {
            color: darkblue;
        }
        /* Estilo para a informação do troco */
        .troco-info {
            font-weight: bold;
            color: green;
            margin-top: 10px;
            border-top: 1px solid #eee; /* Opcional: para separar visualmente */
            padding-top: 10px;
        }
        .troco-info small {
            display: block;
            font-size: 0.8em;
            color: #666;
            font-weight: normal;
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
            <h2>Finalizar Compra</h2>

            <form action="servlet" method="post">
                <input type="hidden" name="action" value="confirmarCompra">
                <input type="hidden" name="idCliente" value="<%= clienteId %>">
                <div class="itens">
                    <h3>Itens do Carrinho</h3>
                    <%
                        int index = 0;
                        if (itensCarrinho != null && !itensCarrinho.isEmpty()) {
                            for (CarrinhoItem item : itensCarrinho) {
                                Livro livro = item.getItem().getLivro();
                                double precoUnitario = item.getItem().getValorVenda();
                                double precoTotalItem = precoUnitario * item.getQuantidade();
                    %>
                    <div class="item-finalizacao">
                        <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                        <div>
                            <p><strong><%= livro.getTitulo() %></strong></p>
                            <p>Editora: <%= livro.getEditora().getNome() %></p>
                            <p>Quantidade: <%= item.getQuantidade() %></p>
                            <p>Total: R$ <%= df.format(precoTotalItem) %></p>
                        </div>
                    </div>

                    <input type="hidden" name="itens[<%= index %>].idLivro" value="<%= livro.getId() %>">
                    <input type="hidden" name="itens[<%= index %>].quantidade" value="<%= item.getQuantidade() %>">
                    <input type="hidden" name="itens[<%= index %>].valorUnitario" value="<%= precoUnitario %>">
                    <input type="hidden" name="itens[<%= index %>].valorTotal" value="<%= precoTotalItem %>">
                    <%
                                index++;
                            }
                        } else {
                    %>
                        <p>Nenhum item no carrinho para finalizar a compra.</p>
                    <%
                        }
                    %>
                </div>

                <div class="resumo-compra">
                    <h3>Endereco de Entrega</h3>
                    <%
                        Endereco enderecoEntrega = (Endereco) session.getAttribute("enderecoSelecionado");
                        if (enderecoEntrega != null) {
                    %>
                        <p><%= enderecoEntrega.getLogradouro() %>, <%= enderecoEntrega.getNumero() %> - <%= enderecoEntrega.getCidade() %></p>
                        <input type="hidden" name="idEnderecoEntrega" value="<%= enderecoEntrega.getId() %>">
                    <%
                        } else {
                    %>
                        <p style="color:red;">Nenhum endereco de entrega selecionado.</p>
                    <%
                        }
                    %>
                    <a href="servlet?action=enderecoEntregaCompra" class="link-adicionar">Adicionar/Trocar Endereco de Entrega</a>

                    <h3>Endereco de Cobranca</h3>
                    <%
                        Endereco enderecoCobranca = (Endereco) session.getAttribute("enderecoCobrancaSelecionado");
                        if (enderecoCobranca != null) {
                    %>
                        <p><%= enderecoCobranca.getLogradouro() %>, <%= enderecoCobranca.getNumero() %> - <%= enderecoCobranca.getCidade() %></p>
                        <input type="hidden" name="idEnderecoCobranca" value="<%= enderecoCobranca.getId() %>">
                    <%
                        } else {
                    %>
                        <p style="color:red;">Nenhum endereco de cobranca selecionado.</p>
                    <%
                        }
                    %>
                    <a href="servlet?action=enderecoCobrancaCompra" class="link-adicionar">Adicionar/Trocar Endereco de Cobranca</a>

                    <h3>Forma de Pagamento</h3>
                    <%
                        List<Cartao> cartoesSelecionados = (List<Cartao>) session.getAttribute("cartoesSelecionados");
                        if (cartoesSelecionados != null && !cartoesSelecionados.isEmpty()) {
                            for (Cartao cartao : cartoesSelecionados) {
                    %>
                        <p>Cartão final <%= cartao.getNumero().substring(cartao.getNumero().length() - 4) %> - <%= cartao.getBandeiraCartao().getDescricao() %></p>
                    <%
                            }
                        } else {
                    %>
                        <p style="color:red;">Nenhum cartao selecionado.</p>
                    <%
                        }
                    %>
                    <a href="servlet?action=cartaoCompra" class="link-adicionar">Adicionar/Trocar Cartao</a>

                    <h3>Resumo</h3>
                    <p>Subtotal: R$ <%= df.format(subtotal) %></p>
                    <p>Frete: R$ <%= df.format(valorFreteSessao) %></p>

                    <%-- --- MODIFICAÇÕES AQUI: Exibe cupons com base nos métodos existentes --- --%>
                    <% if (cupomPromocional != null) { %>
                        <p>Cupom Promocional (ID: <%= cupomPromocional.getId() %>): - R$ <%= df.format(descontoPromocional) %></p>
                    <% } else { %>
                        <p>Cupom Promocional: - R$ <%= df.format(0.00) %></p>
                    <% } %>

                    <% if (cupomTroca != null) { %>
                        <p>Cupom de Troca (Cód: <%= cupomTroca.getCodigoCupom() %>): - R$ <%= df.format(descontoTroca) %></p>
                    <% } else { %>
                        <p>Cupom de Troca: - R$ <%= df.format(0.00) %></p>
                    <% } %>
                    <%-- --- FIM MODIFICAÇÕES --- --%>

                    <p><strong>Total: R$ <%= df.format(totalFinal) %></strong></p>

                    <%-- **NOVO: Exibição do Troco** --%>
                    <% if (valorTrocoGerado > 0.0) { %>
                        <p class="troco-info">
                            Troco em Cupom: R$ <%= df.format(valorTrocoGerado) %>
                            <br><small>Este valor sera gerado como um novo cupom de troca apos a finalizacao da compra.</small>
                        </p>
                    <% } %>
                    <%-- **FIM NOVO** --%>

                    <input type="hidden" name="valorSubtotal" value="<%= subtotal %>">
                    <input type="hidden" name="valorFrete" value="<%= valorFreteSessao %>">
                    <input type="hidden" name="valorDescontoPromocional" value="<%= descontoPromocional %>">
                    <input type="hidden" name="valorDescontoTroca" value="<%= descontoTroca %>">
                    <input type="hidden" name="valorTotal" value="<%= totalFinal %>">
                    <%-- **NOVO: Passa o valor do troco também no hidden input, se necessário no backend** --%>
                    <input type="hidden" name="valorTrocoGerado" value="<%= valorTrocoGerado %>">
                    <%-- **FIM NOVO** --%>


                    <%
                        if (cupomPromocional != null) {
                    %>
                        <input type="hidden" name="idCupomPromocional" value="<%= cupomPromocional.getId() %>">
                    <%
                        }
                        if (cupomTroca != null) {
                    %>
                        <input type="hidden" name="idCupomTroca" value="<%= cupomTroca.getIdCupomTroca() %>">
                    <%
                        }
                    %>

                    <%
                        if (tipoFreteSelecionadoStr != null && !tipoFreteSelecionadoStr.isEmpty()) {
                    %>
                        <input type="hidden" name="tipoFreteSelecionado" value="<%= tipoFreteSelecionadoStr %>">
                    <%
                        }
                    %>

                    <button type="submit">Confirmar Compra</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>