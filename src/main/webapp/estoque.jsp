<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Locale" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Controle de Estoque | PageStation</title>
    <link rel="stylesheet" href="css/estoque.css?v=<%= System.currentTimeMillis()%>">
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
            <h2 class="titulo-pagina">Controle de Estoque</h2>
            <div class="lista-de-livros">
                <%
                    LivroDAO livroDAO = new LivroDAO();
                    List<Livro> listaDeLivros = livroDAO.consultarTodos();

                    if (listaDeLivros == null || listaDeLivros.isEmpty()) {
                %>
                    <p class="mensagem-nenhum-livro">Nenhum livro cadastrado no sistema.</p>
                <%
                    } else {
                        Map<Integer, Item> estoquePorLivro = (Map<Integer, Item>) request.getAttribute("estoquePorLivro");
                        for (Livro livro : listaDeLivros) {
                            Item itemEstoque = estoquePorLivro.get(livro.getId());
                            int quantidadeAtual = (itemEstoque != null) ? itemEstoque.getQuantidade() : 0;
                %>
                    <div class="livro-container">
                        <div class="info-livro">
                            <div class="imagem-container">
                                <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                            </div>
                            <div class="detalhes-livro">
                                <h3 class="titulo"><%= livro.getTitulo() %></h3>
                                <p class="editora">Editora: <%= livro.getEditora().getNome() %></p>
                                <p class="quantidade-atual">Qtde Atual: <%= quantidadeAtual %></p>
                            </div>
                        </div>

                        <div class="controle-estoque">
                            <h4 class="subtitulo-controle">Entrada de Estoque</h4>
                            <form action="servlet" method="post" class="form-entrada">
                                <input type="hidden" name="action" value="registrarEntradaEstoque">
                                <input type="hidden" name="id_livro" value="<%= livro.getId() %>">

                                <div class="form-group">
                                    <label for="qtde_entrada_<%= livro.getId() %>">Qtde Entrada:</label>
                                    <input type="number" id="qtde_entrada_<%= livro.getId() %>" name="qtde_entrada" min="1" value="1">
                                </div>

                                <div class="form-group">
                                    <label for="data_entrada_<%= livro.getId() %>">Data de Entrada:</label>
                                    <input type="date" id="data_entrada_<%= livro.getId() %>" name="data_entrada" required>
                                </div>

                                <div class="form-group">
                                    <label for="valor_custo_<%= livro.getId() %>">Valor Custo Un.:</label>
                                    <input type="number" id="valor_custo_<%= livro.getId() %>" name="valor_custo" step="0.01" min="0.01" value="0.01" required>
                                </div>

                                <div class="fornec-container">
                                    <label for="fornecedor_<%= livro.getId() %>">Fornecedor:</label>
                                    <select id="fornecedor_<%= livro.getId() %>" name="id_fornec" required>
                                        <option value="">Selecione</option>
                                        <%
                                            List<Fornecedor> listaDeFornecedores = (List<Fornecedor>) request.getAttribute("listaDeFornecedores");
                                            if (listaDeFornecedores != null) {
                                                for (Fornecedor fornecedor : listaDeFornecedores) {
                                        %>
                                            <option value="<%= fornecedor.getId() %>"><%= fornecedor.getDescricao() %></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>

                                <button type="submit" class="btn-registrar">REGISTRAR</button>
                                <button type="button" class="btn-reentrada">REENTRADA</button>
                            </form>
                        </div>
                    </div>
                <%
                        }
                    }
                %>
            </div>
        </div>
    </main>
</body>
</html>

