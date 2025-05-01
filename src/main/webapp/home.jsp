<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Home | PageStation</title>
    <link rel="stylesheet" href="css/home.css?v=<%= System.currentTimeMillis()%>">
</head>
<body>
<%@page import="java.util.List, dao.*, dominio.*, controle.*, java.util.Map"%>
<%
    List<Livro> livros = (List<Livro>) request.getAttribute("livros");
    Map<Integer, Double> precos = (Map<Integer, Double>) request.getAttribute("precos");
%>

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


        <h2 class="subtitulo">PageStation</h2>
        <h3 class="slogan">A parada para sua leitura</h3>

        <%
            String mensagemBoasVindas = (String) session.getAttribute("mensagemBoasVindas");
            Boolean primeiroAcesso = (Boolean) session.getAttribute("primeiroAcesso");

            if (primeiroAcesso != null && primeiroAcesso && mensagemBoasVindas != null && !mensagemBoasVindas.isEmpty()) {
        %>
            <div class="mensagem-boas-vindas">
                <%= mensagemBoasVindas %>
            </div>
        <%
                session.removeAttribute("mensagemBoasVindas"); // Limpa a mensagem
                session.removeAttribute("primeiroAcesso"); // Indica que já foi exibido
            }
        %>

        <div class="produtos">
        <% if (livros != null && !livros.isEmpty()) { %>
            <% for (Livro livro : livros) { %>
                <a href="servlet?action=consultarProduto&id_livro=<%= livro.getId() %>" class="livro-item">
                    <img class="imagem-livro" src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                    <p class="titulo"><%= livro.getTitulo() %></p>
                    <% if (precos != null && precos.containsKey(livro.getId())) { %>
                        <p class="preco">R$<%= String.format("%.2f", precos.get(livro.getId())) %></p>
                    <% } else { %>
                        <p class="preco">Preço não disponível</p>
                    <% } %>
                </a>
            <% } %>
        <% } else { %>
            <p>Nenhum livro encontrado.</p>
        <% } %>
        </div>
        <a class="link-logout" href="servlet?action=logout">Sair</a>
    </div>
</main>
</body>
</html>