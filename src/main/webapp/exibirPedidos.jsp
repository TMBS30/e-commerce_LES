<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, dao.*, dominio.*, controle.*, java.util.Map, dominio.StatusCompra"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>Pedidos | PageStation</title>
        <link rel="stylesheet" href="css/exibirPedidos.css?v=<%= System.currentTimeMillis()%>">
        <script>
            function solicitarTroca(idCompra) {
                console.log("Solicitando troca para o pedido ID: " + idCompra);
                window.location.href = 'servlet?action=solicitarTroca&id_compra=' + idCompra;
            }
        </script>
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
                <h2 class="meus-pedidos">Meus Pedidos</h2>
                <%
                    List<Compra> listaDePedidos = (List<Compra>) request.getAttribute("listaDePedidos");
                    if (listaDePedidos == null || listaDePedidos.isEmpty()) {
                %>
                    <p class="mensagem-nenhum-pedido">Voce ainda nao fez nenhum pedido.</p>
                <%
                    } else {
                        for (Compra pedido : listaDePedidos) {
                %>
                    <div class="pedido-container">
                        <h3 class="numero-pedido">Pedido: <%= pedido.getNumeroPedido() %></h3>
                        <div class="datas-controle">
                            <p class="data-criacao">Data da Compra: <%= pedido.getDataCriacao() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(pedido.getDataCriacao()) : "" %></p>
                            <p class="data-modificacao">Ultima Atualizacao: <%= pedido.getDataModificacao() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(pedido.getDataModificacao()) : "N/A" %></p>
                        </div>
                        <div class="itens-pedido">
                            <%
                                if (pedido.getItensPedido() != null) {
                                   for (dominio.ItemPedido itemPedido : pedido.getItensPedido()) {
                                        Livro livro = itemPedido.getLivro();
                                        if (livro != null) {
                            %>
                                <div class="item-pedido">
                                    <div class="imagem-container">
                                        <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                                    </div>
                                    <div class="info-container">
                                        <p class="titulo"><%= livro.getTitulo() %></p>
                                        <p class="editora">Editora: <%= livro.getEditora().getNome() %></p>
                                        <p class="valor-unitario">Und: R$ <%= String.format("%.2f", itemPedido.getValorUnitario()) %></p>
                                    </div>
                                    <div class="detalhes-container">
                                        <p class="quantidade">Qtde: <%= itemPedido.getQuantidade() %></p>
                                        <p class="valor-total">Total: R$ <%= String.format("%.2f", itemPedido.getQuantidade() * itemPedido.getValorUnitario()) %></p>
                                    </div>
                                    <div class="acompanhamento-container">
                                        <div class="status-container">
                                             <label>Status da Compra</label>
                                             <input type="text" value="<%= pedido.getStatusDescricao() %>" readonly>
                                        </div>
                                        <%
                                            if (pedido.isTroca()) {
                                                System.out.println("[DEBUG - exibirPedidos.jsp] Pedido " + pedido.getIdCompra() + " isTroca: true");
                                        %>
                                            <button class="btn-trocar" onclick="solicitarTroca(<%= pedido.getIdCompra() %>)">Trocar</button>
                                        <%
                                            } else {
                                                System.out.println("[DEBUG - exibirPedidos.jsp] Pedido " + pedido.getIdCompra() + " isTroca: false");
                                        %>
                                            <button class="btn-trocar" disabled>Trocar</button>
                                        <%
                                            }
                                        %>
                                    </div>
                                </div>
                            <%
                                        }
                                    }
                                }
                            %>
                        </div>
                        <% if (pedido.getStatusDescricao().equals("TROCA AUTORIZADA")) { %>
                            <p class="msg-troca-cupom" >Sua troca foi autorizada. Um cupom de troca no valor da compra foi gerado e esta disponivel na pagina de <a href="cupons.jsp">Cupons</a>.</p>
                        <% } %>
                    </div>
                <%
                        }
                    }
                %>
            </div>
        </main>
    </body>
</html>