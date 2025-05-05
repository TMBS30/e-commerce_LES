<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %>

<!DOCTYPE html>
<html>
<head>
    <title>Carrinho | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/carrinho.css?v=<%= System.currentTimeMillis() %>"">
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
        <div class="container-master">
            <%
                Integer clienteId = (Integer) session.getAttribute("clienteId");
                CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                Carrinho carrinho = carrinhoDAO.consultarCarrinhoPorCliente(clienteId);
                List<CarrinhoItem> itensCarrinho = (carrinho != null) ? carrinho.getItens() : new ArrayList<>();
            %>

            <% if (!itensCarrinho.isEmpty()) { %>
                <div id="temporizador-inatividade" class="temporizador-inatividade">
                    Seu carrinho sera automaticamente esvaziado em <span id="tempo-restante">4:00</span> minutos devido a inatividade.
                </div>
            <% } %>
            <div class="container">
                <div class="itens-carrinho">
                    <h2 class="carrrinho">Meu Carrinho</h2>
                    <div class="lista-de-itens-carrinho">
                        <%
                        double subtotal = 0;
                        double frete = 0.0;

                        if (itensCarrinho.isEmpty()) {
                        %>
                            <p>Seu carrinho esta vazio!</p>
                        <%
                        } else {
                            frete = 20.0;
                            for (CarrinhoItem item : itensCarrinho) {
                                Livro livro = item.getItem().getLivro();
                                double precoUnitario = item.getItem().getValorVenda();
                                double precoTotalItem = precoUnitario * item.getQuantidade();
                                subtotal += precoTotalItem;
                        %>

                                <div class="item-carrinho">
                                    <div class="info-livro">
                                        <div class="imagem-livro">
                                            <img src="<%= livro.getCaminhoImagem() %>" alt="<%= livro.getTitulo() %>">
                                        </div>
                                        <div>
                                            <p style="color: red;">ID do item: <%= item.getId() %></p>
                                            <div class="titulo-com-botao">
                                                <h3 class="titulo"><%= livro.getTitulo() %></h3>
                                                <form action="servlet" method="post" onsubmit="return confirmarExclusao('<%= item.getId() %>');">
                                                    <input type="hidden" name="action" value="removerItemCarrinho">
                                                    <input type="hidden" name="id" value="<%= item.getId() %>">
                                                    <button type="submit" class="excluir-btn">Excluir Item</button>
                                                </form>
                                            </div>
                                            <p class="editora">Editora: <%= livro.getEditora().getNome() %></p>
                                            <div class="quantidade-container">
                                                <button type="button" class="botao-quantidade" onclick="alterarQuantidade(this, -1, '<%= item.getId() %>')">-</button>
                                                <input type="text" class="visor-quantidade" value="<%= item.getQuantidade() %>" min="1" readonly>
                                                <button type="button" class="botao-quantidade" onclick="alterarQuantidade(this, 1, '<%= item.getId() %>')">+</button>
                                            </div>
                                            <p class="preco-total">Total: R$ <%= String.format("%.2f", precoTotalItem) %></p>
                                        </div>
                                    </div>
                                </div>
                        <%
                            }
                        }
                        %>
                    </div>
                </div>

                <div class="resumo-compra">
                    <h2 class="txt-resumo-compra">Resumo da Compra</h2>
                    <p>Subtotal: R$ <%= String.format("%.2f", subtotal) %></p>
                    <div class="frete-container">
                        <h2 class="txt-frete">Escolha o Frete</h2>
                        <select name="tipoFrete" onchange="atualizarTotalComFrete(this.value)">
                            <option value="<%= TipoFrete.EXPRESS.getDescricao() %>"><%= TipoFrete.EXPRESS %></option>
                            <option value="<%= TipoFrete.PADRAO.getDescricao() %>"><%= TipoFrete.PADRAO %></option>
                            <option value="<%= TipoFrete.ECONOMICO.getDescricao() %>"><%= TipoFrete.ECONOMICO %></option>
                        </select>
                        <p id="valorFrete">
                            <% if (session.getAttribute("previaPedidoValorFrete") != null) { %>
                                Frete: R$ <%= String.format("%.2f", session.getAttribute("previaPedidoValorFrete")) %>
                            <% } else { %>
                                Frete: Selecione o tipo de frete
                            <% } %>
                        </p>
                    </div>

                    <div class="cupom-container">
                        <p>Cupom Promocional: - R$ <%= String.format("%.2f", (session.getAttribute("cupomSelecionado") != null ? ((Cupom)session.getAttribute("cupomSelecionado")).getValor() : 0.00)) %></p>
                        <% if (session.getAttribute("cupomTrocaSelecionado") != null) { %>
                            <p>Cupom de Troca: - R$ <%= String.format("%.2f", ((CupomTroca)session.getAttribute("cupomTrocaSelecionado")).getValorCupom()) %> (<%= ((CupomTroca)session.getAttribute("cupomTrocaSelecionado")).getCodigoCupom() %>)</p>
                        <% } else { %>
                            <p>Cupom de Troca: - R$ 0.00</p>
                        <% } %>
                        <a href="servlet?action=exibirCupons">Ver meus cupons</a>
                    </div>

                    <%
                        double descontoPromocional = (session.getAttribute("cupomSelecionado") != null) ? ((Cupom)session.getAttribute("cupomSelecionado")).getValor() : 0.00;
                        double descontoTroca = (session.getAttribute("cupomTrocaSelecionado") != null) ? ((CupomTroca)session.getAttribute("cupomTrocaSelecionado")).getValorCupom() : 0.00;
                        double valorFreteSessao = (session.getAttribute("previaPedidoValorFrete") != null) ? (Double)session.getAttribute("previaPedidoValorFrete") : 0.00;
                        double valorTotalCompra = subtotal + valorFreteSessao - descontoPromocional - descontoTroca;
                    %>
                    <p><strong>Total: R$ <span id="valorTotalCompra"><%= String.format("%.2f", valorTotalCompra) %></span></strong></p>
                    <form action="servlet" method="post">
                        <input type="hidden" name="action" value="salvarPreviaPedido">
                        <input type="hidden" id="tipoFreteSelecionado" name="tipoFreteSelecionado" value="">
                        <button type="submit" class="finalizar-compra-btn">Finalizar Compra</button>
                    </form>
                </div>
            </div>
        </div>
    </main>

    <script>
        const carrinhoTemItens = <%= !carrinho.getItens().isEmpty() %>; // true ou false
    </script>

    <script>
        const tempoLimiteInatividade = 4 * 60 * 1000;
        let tempoRestanteAtual = tempoLimiteInatividade;
        let temporizadorInterval;

        const elementoTempoRestante = document.getElementById('tempo-restante');

        function formatarTempo(ms) {
            const minutos = Math.floor(ms / (60 * 1000));
            const segundos = Math.floor((ms % (60 * 1000)) / 1000);
            return `${String(minutos).padStart(1, '0')}:${String(segundos).padStart(2, '0')}`;
        }

        function atualizarTemporizador() {
            tempoRestanteAtual -= 1000;
            elementoTempoRestante.textContent = formatarTempo(tempoRestanteAtual);

            if (tempoRestanteAtual <= 0) {
                clearInterval(temporizadorInterval);
                if (carrinhoTemItens) {
                    window.location.href = 'servlet?action=esvaziarCarrinhoInativo';
                    alert('Seu carrinho foi esvaziado devido a inatividade.');
                }
            }
        }

        function resetarTemporizador() {
            clearInterval(temporizadorInterval);
            tempoRestanteAtual = tempoLimiteInatividade;
            elementoTempoRestante.textContent = formatarTempo(tempoRestanteAtual);
            iniciarTemporizador();
            // Enviar um ping de atividade para o servidor (opcional)
            enviarPingAtividade();
        }

        function iniciarTemporizador() {
            temporizadorInterval = setInterval(atualizarTemporizador, 1000);
        }

        function enviarPingAtividade() {
            fetch('servlet?action=pingAtividadeCarrinho', { method: 'POST' }) // Ajuste a URL
                .then(response => {
                    if (!response.ok) {
                        console.error('Erro ao enviar ping de atividade.');
                    }
                })
                .catch(error => {
                    console.error('Erro de rede ao enviar ping de atividade:', error);
                });
        }

        //document.addEventListener('mousemove', resetarTemporizador);
        //document.addEventListener('keypress', resetarTemporizador);
        //document.addEventListener('click', resetarTemporizador);

        function alterarQuantidade(botao, delta, itemId) {
            const inputQuantidade = botao.parentNode.querySelector('.visor-quantidade');
            let quantidadeAtual = parseInt(inputQuantidade.value);
            let novaQuantidade = quantidadeAtual + delta;

            if (novaQuantidade >= 1) {
                fetch(`servlet?action=alterarQuantidadeCarrinho&itemId=${itemId}&quantidade=${novaQuantidade}`)
                    .then(response => response.json())
                    .then(data => {
                        inputQuantidade.value = novaQuantidade;
                        const precoTotalItemElement = botao.parentNode.parentNode.querySelector('.preco-total');
                        precoTotalItemElement.textContent = `Total: R$ ${data.precoTotalItem}`;
                        document.querySelector('.resumo-compra > p:first-child').textContent = `Subtotal: R$ ${data.subtotal}`;
                        document.getElementById('valorTotalCompra').textContent = `${data.totalCompra}`;
                        resetarTemporizador();
                    })
                    .catch(error => {
                        console.error('Erro ao alterar a quantidade:', error);
                        alert('Erro ao atualizar a quantidade do item.');
                    });
            }
        }

        function atualizarTotalComFrete(tipoFreteDescricao) {
            let valorFrete = 0.0;
            if (tipoFreteDescricao === '<%= TipoFrete.EXPRESS.getDescricao() %>') {
                valorFrete = <%= TipoFrete.EXPRESS.getValor() %>;
            } else if (tipoFreteDescricao === '<%= TipoFrete.PADRAO.getDescricao() %>') {
                valorFrete = <%= TipoFrete.PADRAO.getValor() %>;
            } else if (tipoFreteDescricao === '<%= TipoFrete.ECONOMICO.getDescricao() %>') {
                valorFrete = <%= TipoFrete.ECONOMICO.getValor() %>;
            }

            document.getElementById('valorFrete').textContent = `Frete: R$ ${valorFrete.toFixed(2)}`;

            const subtotal = <%= subtotal %>;
            const descontoPromocional = <%= session.getAttribute("cupomSelecionado") != null ? ((Cupom)session.getAttribute("cupomSelecionado")).getValor() : 0.00 %>;
            const descontoTroca = <%= session.getAttribute("cupomTrocaSelecionado") != null ? ((CupomTroca)session.getAttribute("cupomTrocaSelecionado")).getValorCupom() : 0.00 %>;
            const total = subtotal + valorFrete - descontoPromocional - descontoTroca;

            document.getElementById('valorTotalCompra').textContent = `${total.toFixed(2)}`;
            document.getElementById('tipoFreteSelecionado').value = tipoFreteDescricao;
            resetarTemporizador(); // Resetar o temporizador após a interação
        }

        window.onload = function() {
            const selectFrete = document.querySelector('select[name="tipoFrete"]');
            if (selectFrete && selectFrete.value) {
                atualizarTotalComFrete(selectFrete.value);
            }

            if (carrinhoTemItens) {
                iniciarTemporizador();
            }
        };

        function confirmarExclusao(itemId) {
            return confirm("Tem certeza que deseja excluir este item do carrinho?");
        }
    </script>
</body>
</html>