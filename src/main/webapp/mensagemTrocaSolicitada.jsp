<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>Solicitação de Troca | PageStation</title>
        <link rel="stylesheet" href="css/mensagemCompraFinalizada.css?v=<%= System.currentTimeMillis()%>">
    </head>
    <body>
    <%@page import="java.util.List, dao.*, dominio.*, controle.*, java.util.Map"%>

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
                <div class="titulo">
                    <h2 class="subtitulo">PageStation</h2>
                    <h3 class="slogan">A parada para sua leitura</h3>
                </div>
                <p class="msg-compra-finalizada">SOLICITACAO DE TROCA<br>REALIZADA!</p>
                <p class="msg-pedidos">A solicitacao de troca para o seu pedido foi enviada.<br>
                Acompanhe o processo em PEDIDOS</p>
                <div class="buttons-container">
                    <form action="servlet" method="get">
                        <input type="hidden" name="action" value="exibirPedidos">
                        <button type="submit" class="btn-pedidos">Pedidos</button>
                    </form>
                </div>
            </div>
        </main>
    </body>
</html>