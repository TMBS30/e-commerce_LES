<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    Integer clienteId = (Integer) session.getAttribute("clienteId");
    List<CarrinhoItem> itensCarrinho = (List<CarrinhoItem>) session.getAttribute("previaPedidoItens");
    Double subtotal = (Double) session.getAttribute("previaPedidoSubtotal");
    Double valorFreteSessao = (Double) session.getAttribute("previaPedidoValorFrete");
    Double descontoCupom = (session.getAttribute("cupomSelecionado") != null) ? ((Cupom) session.getAttribute("cupomSelecionado")).getValor() : 0.00;
    String tipoFreteSelecionadoStr = (String) session.getAttribute("previaPedidoTipoFreteSelecionadoStr");
    DecimalFormat df = new DecimalFormat("#0.00");
    Cupom cupomSelecionado = (Cupom) session.getAttribute("cupomSelecionado");
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
                <%
                    int index = 0;
                    for (CarrinhoItem item : itensCarrinho) {
                        Livro livro = item.getItem().getLivro();
                        double precoUnitario = item.getItem().getValorVenda();
                        double precoTotalItem = precoUnitario * item.getQuantidade();
                %>
                <div class="itens">
                    <h3>Itens do Carrinho</h3>
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
                %>
                </div>

                <div class="resumo-compra"></div>
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
                    <%
                        double valorTotal = subtotal + (valorFreteSessao != null ? valorFreteSessao : 0.00) - descontoCupom;
                    %>
                    <p>Subtotal: R$ <%= df.format(subtotal) %></p>
                    <p>Frete: R$ <%= df.format(valorFreteSessao != null ? valorFreteSessao : 0.00) %></p>
                    <p>Cupom: - R$ <%= df.format(descontoCupom) %></p>
                    <p><strong>Total: R$ <%= df.format(valorTotal) %></strong></p>

                    <input type="hidden" name="valorSubtotal" value="<%= subtotal %>">
                    <input type="hidden" name="valorFrete" value="<%= valorFreteSessao != null ? valorFreteSessao : 0.00 %>">
                    <input type="hidden" name="valorDesconto" value="<%= descontoCupom %>">
                    <input type="hidden" name="valorTotal" value="<%= valorTotal %>">
                    <%
                        if (cupomSelecionado != null) {
                    %>
                        <input type="hidden" name="idCupom" value="<%= cupomSelecionado.getId() %>">
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