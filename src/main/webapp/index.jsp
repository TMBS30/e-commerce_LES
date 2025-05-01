<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="ISO-8859-1">
        <title>Login</title>
        <link rel="stylesheet" href="css/login.css?v=<%= System.currentTimeMillis() %>">
    </head>
    <body>
        <header>
            <div class="logo-link">
                <h1>PageStation</h1>
                <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
            </div>
        </header>

        <main>
            <div class="container">
                <h2 class="title">PageStation</h1>
                <h3 class="subtitulo">A parada para sua leitura</h3>
                <p class="texto-login">Faca seu login</p>
                <form action="servlet" method="post">
                    <div class="input-container">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email_login" required>
                    </div>
                    <div class="input-container">
                        <label for="senha">Senha</label>
                        <input type="password" id="senha" name="senha_login" required>
                    </div>
                    <div class="buttons-container">
                        <button type="submit" name="action" value="login">Login</button>
                    </div>
                </form>
                <div class="links-container">
                    <a href="recuperarSenha.jsp">Esqueci minha senha</a> |
                    <a href="servlet?action=cadastro">Cadastrar</a>
                </div>
                <div class="mensagem-erro">
                    <%= request.getAttribute("mensagemErro") != null ? request.getAttribute("mensagemErro") : "" %>
                </div>
            </div>
        </main>
    </body>
    </html>
