<%@ page import="java.util.*" %>
<%@ page import="dominio.Cupom" %>
<%@ page import="dao.CupomDAO" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.sql.SQLException" %>

<head>
    <title>Cupons | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/cupons.css?v=<%= System.currentTimeMillis()%>">
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
            <h2>Cupons</h2>

            <form action="servlet" method="post">
                <input type="hidden" name="action" value="aplicarCupom">

                <div class="cupom-section">
                    <h3>Cupons Promocionais</h3>
                    <div class="checkbox-container">
                        <input type="checkbox" id="habilitarPromocionais" name="habilitarPromocionais">
                        <label for="habilitarPromocionais">Habilitar cupons promocionais</label>
                    </div>
                    <%
                        CupomDAO cupomDAO = new CupomDAO();
                        Integer clienteId = (Integer) session.getAttribute("clienteId");
                        List<Cupom> cuponsPromocionais = new ArrayList<>();
                        if (clienteId != null) {
                            cuponsPromocionais = cupomDAO.listarCuponsPorTipoParaCliente("PROMOCIONAL", clienteId);
                        }
                        if (cuponsPromocionais.isEmpty()) {
                    %>
                        <p class="no-cupons" style="padding-left: 20px;">Nenhum cupom promocional disponivel!</p>
                    <%
                        } else {
                            Cupom cupomPromocionalSelecionado = (Cupom) session.getAttribute("cupomSelecionado");
                            int cupomPromocionalSelecionadoId = (cupomPromocionalSelecionado != null && cupomPromocionalSelecionado.getTipoCupom().getDescricao().equalsIgnoreCase("PROMOCIONAL")) ? cupomPromocionalSelecionado.getId() : -1;
                            for (Cupom cupom : cuponsPromocionais) {
                    %>
                        <div class="radio-container">
                            <input type="radio" name="idCupomPromocional" value="<%= cupom.getId() %>" id="cupom_<%= cupom.getId() %>_promo" <%= (cupom.getId() == cupomPromocionalSelecionadoId ? "checked" : "") %> onclick="document.getElementById('habilitarPromocionais').checked = true;">
                            <label for="cupom_<%= cupom.getId() %>_promo">Valor: R$ <%= new DecimalFormat("#0.00").format(cupom.getValor()) %></label>
                        </div>
                    <%
                            }
                        }
                    %>
                    <button type="button" class="limpar-cupom-button" onclick="limparSelecao('idCupomPromocional')">Limpar</button>
                </div>

                <div class="cupom-section">
                    <h3>Cupons de Troca</h3>
                    <div class="checkbox-container">
                        <input type="checkbox" id="habilitarTroca" name="habilitarTroca">
                        <label for="habilitarTroca">Habilitar cupons de troca</label>
                    </div>
                    <%
                        CupomTrocaDAO cupomTrocaDAO = new CupomTrocaDAO();
                        List<CupomTroca> cuponsTrocaList = new ArrayList<>();
                        //Integer clienteId = (Integer) session.getAttribute("clienteId");
                        if (clienteId != null) {
                            try {
                                cuponsTrocaList = cupomTrocaDAO.listarTodosPorCliente(clienteId);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Lide com o erro de banco de dados aqui (ex: exibir uma mensagem para o usuário)
                            }
                        }
                        if (cuponsTrocaList.isEmpty() && clienteId != null) {
                    %>
                        <p class="no-cupons" style="padding-left: 20px;">Nenhum cupom de troca disponivel!</p>
                    <%
                        } else {
                            // Para manter a seleção do cupom de troca, você precisará armazenar o ID do cupom selecionado na sessão
                            CupomTroca cupomTrocaSelecionado = (CupomTroca) session.getAttribute("cupomTrocaSelecionado");
                            int cupomTrocaSelecionadoId = (cupomTrocaSelecionado != null) ? cupomTrocaSelecionado.getIdCupomTroca() : -1;
                            for (CupomTroca cupomTroca : cuponsTrocaList) {
                    %>
                        <div class="radio-container">
                            <input type="radio" name="idCupomTroca" value="<%= cupomTroca.getIdCupomTroca() %>" id="cupom_<%= cupomTroca.getIdCupomTroca() %>_troca" <%= (cupomTroca.getIdCupomTroca() == cupomTrocaSelecionadoId ? "checked" : "") %> onclick="document.getElementById('habilitarTroca').checked = true;">
                            <label for="cupom_<%= cupomTroca.getIdCupomTroca() %>_troca">Valor: R$ <%= new DecimalFormat("#0.00", new java.text.DecimalFormatSymbols(new Locale("pt", "BR"))).format(cupomTroca.getValorCupom()) %></label>
                        </div>
                    <%
                            }
                        }
                    %>
                    <button type="button" class="limpar-cupom-button" onclick="limparSelecao('idCupomTroca')">Limpar</button>
                </div>
                <div class="buttons-container">
                    <button type="submit" class="aplicar-button">Salvar</button>
                    <button type="button" class="voltar-button" onclick="window.location.href='carrinho.jsp'">Voltar ao Carrinho</button>
                </div>
            </form>
        </div>
    </main>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const checkboxes = document.querySelectorAll('input[type="checkbox"]');
            const radiosPromocionais = document.querySelectorAll('input[name="idCupomPromocional"]');
            const radiosTroca = document.querySelectorAll('input[name="idCupomTroca"]');
            const checkboxPromocionais = document.getElementById('habilitarPromocionais');
            const checkboxTroca = document.getElementById('habilitarTroca');

            function updateRadioStates() {
                radiosPromocionais.forEach(radio => {
                    radio.disabled = !checkboxPromocionais.checked;
                });
                radiosTroca.forEach(radio => {
                    radio.disabled = !checkboxTroca.checked;
                });
            }

            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', updateRadioStates);
            });

            updateRadioStates(); // Initial state
        });

        function limparSelecao(radioGroupName) {
            const radios = document.querySelectorAll('input[name="' + radioGroupName + '"]');
            radios.forEach(radio => {
                radio.checked = false;
            });
            // Opcional: Desmarcar o checkbox correspondente também
            if (radioGroupName === 'idCupomPromocional') {
                document.getElementById('habilitarPromocionais').checked = false;
            } else if (radioGroupName === 'idCupomTroca') {
                document.getElementById('habilitarTroca').checked = false;
            }
        }
    </script>
</body>