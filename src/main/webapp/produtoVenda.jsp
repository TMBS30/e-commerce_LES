<%@ page import="java.util.*" %>
<%@ page import="dominio.Livro" %>
<%@ page import="dominio.Autor" %>
<%@ page import="dominio.Categoria" %>
<%@ page import="dominio.Editora" %>
<%@ page import="dominio.Dimensoes" %>
<%@ page import="dao.LivroDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.*, dominio.*, controle.*" %>

<head>
    <title>Informacoes do Livro | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/produtoVenda.css?v=<%= System.currentTimeMillis()%>">
</head>
<body>
<%
    String idLivroStr = request.getParameter("id_livro");


    Livro livro = null;
    Item item = (Item) request.getAttribute("item");

    if (idLivroStr != null) {
        try {
            int idLivro = Integer.parseInt(idLivroStr);
            LivroDAO livroDAO = new LivroDAO();
            livro = livroDAO.consultarPorId(idLivro);


        } catch (Exception e) {

        }
    }

    if (livro == null) {
%>
    <p>Erro: Livro nao encontrado.</p>
<%
    } else {
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

        <div class="conteudo">
            <div class="buttons-container">
                <div class="produto-info-compra">
                    <div class="imagem-livro">
                        <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                    </div>
                    <div class="detalhes-compra">
                        <p class="titulo"><%= livro.getTitulo() %></p>
                        <% if (item != null) { %>
                            <p class="preco">R$<%= item.getValorVenda() %></p>
                        <% } else { %>
                            <p class="preco">Preço não disponível</p>
                        <% } %>
                        <div class="controle-comprar">
                            <div class="quantidade-container">
                                <button type="button" class="botao-quantidade" onclick="alterarQuantidade(-1)">-</button>
                                <input type="text" id="quantidade" value="1" readonly class="visor-quantidade">
                                <button type="button" class="botao-quantidade" onclick="alterarQuantidade(1)">+</button>
                            </div>
                            <button type="button" class="botao-comprar" onclick="comprar()">COMPRAR</button>
                        </div>
                    </div>
                </div>

                <div class="acoes-carrinho">
                    <div class="frete-container">
                        <div class="frete-label-input">  
                            <label for="cep" class="label-cep">Calcular frete</label>
                            <input type="text" id="cep" placeholder="Digite seu CEP" class="input-cep">
                        </div>
                        <button type="button" class="botao-calcular" onclick="calcularFrete()">CALCULAR</button>
                    </div>

                    <div class="adicionar-container">
                        <form action="servlet" method="POST">
                            <input type="hidden" name="action" value="adicionarCarrinho">
                            <input type="hidden" name="idLivro" value="<%= livro.getId() %>">
                            <input type="hidden" name="quantidade" id="quantidadeForm" value="1">
                            <button type="submit" class="botao-adicionar">ADICIONAR AO CARRINHO</button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="container">
                <div class="input-container-sinopse">
                    <label class="label-sinopse">Sinopse:</label>
                    <textarea disabled><%= livro.getSinopse() %></textarea>
                </div>
                <h2 class="title-carac">Caracteristicas do Livro</h2>
                <div class="input-container">
                    <label>Titulo</label>
                    <input type="text" value="<%= livro.getTitulo() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Ano</label>
                    <input type="text" value="<%= livro.getAno() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Edicao</label>
                    <input type="text" value="<%= livro.getEdicao() %>" disabled>
                </div>
                <div class="input-container">
                    <label>ISBN</label>
                    <input type="text" value="<%= livro.getIsbn() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Numero de Paginas</label>
                    <input type="text" value="<%= livro.getNumeroPaginas() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Codigo de Barras</label>
                    <input type="text" value="<%= livro.getCodigoBarras() %>" disabled>
                </div>
                <div class="input-container">
                    <label>Status</label>
                    <input type="text" value="<%= livro.isStatus() ? "Disponivel" : "Indisponivel" %>" disabled>
                </div>

                <div class="input-container">
                    <label>Autores:</label>
                    <textarea class="output-autores" disabled><%
                        if (livro.getAutores() != null && !livro.getAutores().isEmpty()) {
                            for (int i = 0; i < livro.getAutores().size(); i++) {
                                Autor autor = livro.getAutores().get(i);
                                out.print(autor.getNome());
                                if (i < livro.getAutores().size() - 1) {
                                    out.print("; ");
                                }
                            }
                        } else {
                            out.print("Nenhum autor informado.");
                        }
                    %></textarea>
                </div>

                <div class="input-container">
                    <label>Editora</label>
                    <input type="text" value="<%= livro.getEditora() != null ? livro.getEditora().getNome() : "Nao informada" %>" disabled>
                </div>

                <div class="input-container">
                    <label>Categorias</label>
                    <textarea class="output-categorias" disabled><%
                        if (livro.getCategorias() != null && !livro.getCategorias().isEmpty()) {
                            for (int i = 0; i < livro.getCategorias().size(); i++) {
                                Categoria categoria = livro.getCategorias().get(i);
                                out.print(categoria.getNome());
                                if (i < livro.getCategorias().size() - 1) {
                                    out.print("; "); // Adiciona um separador (ponto e vírgula com espaço)
                                }
                            }
                        } else {
                            out.print("Nenhuma categoria informada.");
                        }
                    %></textarea>
                </div>

                <div class="input-container-dimensoes">
                    <% if (livro.getDimensoes() != null) { %>
                        <div class="dimensao-item">
                            <label>Altura</label>
                            <input type="text" value="<%= livro.getDimensoes().getAltura() %> cm" disabled>
                        </div>
                        <div class="dimensao-item">
                            <label>Largura</label>
                            <input type="text" value="<%= livro.getDimensoes().getLargura() %> cm" disabled>
                        </div>
                        <div class="dimensao-item">
                            <label>Profundidade</label>
                            <input type="text" value="<%= livro.getDimensoes().getProfundidade() %> cm" disabled>
                        </div>
                        <div class="dimensao-item">
                            <label>Peso</label>
                            <input type="text" value="<%= livro.getDimensoes().getPeso() %> g" disabled>
                        </div>
                    <% } else { %>
                        <div class="dimensao-item">
                            <label>Dimensoes</label>
                            <input type="text" value="Nao informadas" disabled>
                        </div>
                    <% } %>
                </div>

                <div class="buttons-container">
                    <button onclick="window.location.href='servlet?action=voltarHomePage'">Voltar</button>
                </div>
            </div>
        </div>
    </main>

    <script>
        let quantidade = 1;

        function alterarQuantidade(delta) {
            quantidade += delta;
            if (quantidade < 1) quantidade = 1;
            document.getElementById("quantidade").value = quantidade;
            document.getElementById("quantidadeForm").value = quantidade;
        }

        function calcularFrete() {
            const cep = document.getElementById("cep").value;
            if (!cep) {
                alert("Digite um CEP válido.");
                return;
            }
            alert("Frete calculado para o CEP: " + cep);
            // Aqui poderá ser feita chamada para API de frete
        }

        function comprar() {
            alert("Compra iniciada com " + quantidade + " unidade(s).\nO item foi adicionado ao seu carrinho");
            const formAdicionarCarrinho = document.querySelector('.adicionar-container form');
            formAdicionarCarrinho.submit();
        }

        function adicionarCarrinho() {
            alert(quantidade + " unidade(s) adicionada(s) ao carrinho.");
            // Aqui você poderá chamar a servlet de carrinho
        }
    </script>

    <%
        }
    %>
</body>