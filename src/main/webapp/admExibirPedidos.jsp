<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List, dao.*, dominio.*, controle.*, java.util.Map, dominio.StatusCompra"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Pedidos - Admin | PageStation</title>
    <link rel="stylesheet" href="css/admExibirPedidos.css?v=<%= System.currentTimeMillis()%>">
</head>
<body>
    <header>
        <a href="servlet?action=voltarHomePageADM" class="logo-link">
            <h1>PageStation</h1>
            <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
        </a>
    </header>

    <main>
        <div class="menu-fixo">
            <div class="texto-modo-adm">
                 <p class="texto-adm">Modo: ADM</p>
            </div>
            <a href="#" class="chatbot">
                <p class="texto-chatbot">ChatBot</p>
                <img class="icone-chatbot"src="Imagens/Icone - ChatBot.svg" alt="Icone ChatBot">
            </a>
            <div class="input-pesquisa_livro">
                <input class="input-pesquisa"type="text" name="pesquisa_livro" id="pesquisa_livro" placeholder="Procurar livro...">
            </div>
            <a href="servlet?action=exibirEstoque" class="link-estoque">
                <p class="estoque">Estoque</p>
            </a>
            <a href="servlet?action=exibirPedidosADM" class="link-pedidos">
                <p class="pedidos">Pedidos</p>
            </a>
            <a href="servlet?action=consultarInfoPessoaisADM" class="link-user">
                <img class="icone-user" src="Imagens/Icone - User.svg" alt="Icone Consultar Informacoes Cadastrais">
            </a>
        </div>

        <div class="container">
            <h2 class="meus-pedidos">Pedidos</h2>
            <%
                List<Compra> listaDePedidos = (List<Compra>) request.getAttribute("listaDePedidos");

                if (listaDePedidos == null || listaDePedidos.isEmpty()) {
            %>
                <p class="mensagem-nenhum-pedido">Nenhum pedido encontrado.</p>
            <%
                } else {
                    DecimalFormat df = new DecimalFormat("#.00", new java.text.DecimalFormatSymbols(Locale.US));
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
                            if (pedido.getItensCompra() != null) {
                               for (dominio.ItemCompra itemCompra : pedido.getItensCompra()) {
                                    Livro livro = itemCompra.getLivro();
                                    if (livro != null) {
                        %>
                            <div class="item-pedido">
                                <div class="imagem-container">
                                    <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                                </div>
                                <div class="info-container">
                                    <p class="titulo"><%= livro.getTitulo() %></p>
                                    <p class="editora">Editora: <%= livro.getEditora().getNome() %></p>
                                    <p class="valor-unitario">Und:<br>R$<%= String.format("%.2f", itemCompra.getValorUnitarioNaCompra()) %></p>
                                </div>
                                <div class="detalhes-container">
                                    <p class="quantidade">Qtde: <%= itemCompra.getQuantidade() %></p>
                                    <p class="valor-total">Total:<br>R$<%= String.format("%.2f", itemCompra.getQuantidade() * itemCompra.getValorUnitarioNaCompra()) %></p>
                                </div>
                                <div class="acompanhamento-container">
                                    <div class="status-container">
                                         <label>Status da Compra</label>
                                         <select name="status_<%= pedido.getIdCompra() %>">
                                            <% for (StatusCompra status : StatusCompra.values()) { %>
                                                <option value="<%= status.getId() %>" <%= pedido.getStatusIdCompra() == status.getId() ? "selected" : "" %>>
                                                    <%= status.getDescricao() %>
                                                </option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <button class="btn-retornar-estoque">Retornar Estoque</button>
                                </div>
                            </div>
                        <%
                                    }
                                }
                            }
                        %>
                    </div>
                    <div class="acao-container">
                    <% if (pedido.getStatusIdCompra() == StatusCompra.EM_TROCA.getId()) { %>
                        <form action="servlet?action=autorizarTroca&id_compra=<%= pedido.getIdCompra() %>&idCliente=<%= pedido.getClienteId() %>&valorCompra=<%= df.format(pedido.getValorTotalCompra()) %>" method="post">
                            <button type="submit" class="btn-autorizar-troca">Autorizar Troca</button>
                        </form>
                    <% } %>
                        <form action="servlet" method="post">
                            <input type="hidden" name="action" value="atualizarStatusCompra">
                            <input type="hidden" name="id_compra" value="<%= pedido.getIdCompra() %>">
                            <input type="hidden" name="status_id" id="status_<%= pedido.getIdCompra() %>" value="">
                            <button type="submit" class="btn-salvar-status" onclick="atualizarValorStatus(<%= pedido.getIdCompra() %>)">Salvar Status</button>
                        </form>
                    </div>
                </div>
            <%
                    }
                }
            %>
        </div>
    </main>

    <script>
        function atualizarValorStatus(idCompra) {
            console.log("Função atualizarValorStatus chamada para o pedido ID:", idCompra);
            var selectStatus = document.querySelector(`select[name="status_${idCompra}"]`);
            var novoStatusId = selectStatus.value;
            console.log("Novo Status ID selecionado:", novoStatusId);
            document.getElementById('status_' + idCompra).value = novoStatusId;
            console.log("Valor do campo oculto status_" + idCompra + " atualizado para:", document.getElementById('status_' + idCompra).value);
        }
    </script>
</body>
</html>