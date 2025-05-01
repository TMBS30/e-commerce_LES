<%@ page import="java.util.*" %>
<%@ page import="dominio.Cartao" %>
<%@ page import="dao.CartaoDAO" %>
<%@ page import="dao.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dominio.BandeiraCartao" %>

<head>
    <title>Cartoes | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/consultarCartao.css">
</head>
<body>

    <header>
        <a href="home.jsp" class="logo-link">
            <h1>PageStation</h1>
            <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
        </a>
    </header>    
    <main>

    <h2>Cartoes do Cliente</h2> <!-- Título da tela -->

<!-- Botão Adicionar Cartao no topo -->
        <div class="top-buttons">
            <form action="servlet" method="post">
                <input type="hidden" name="action" value="salvarNovoCartaoForm" />
                <button type="submit" class="add-button">Adicionar Cartao  +</button>
            </form>
        </div>

        <%
            Integer clienteId = (Integer) session.getAttribute("clienteId");
            List<Cartao> cartoes = null;
            if (clienteId != null) {
                CartaoDAO cartaoDAO = new CartaoDAO();
                cartoes = cartaoDAO.consultarCartoesPorCliente(clienteId);
            }

            if (cartoes == null || cartoes.isEmpty()) {
        %>
            <p>Nao ha cartoes cadastrados!</p>
        <%
            } else {
                for (Cartao cartao : cartoes) {
                    // Verificar se o cartão é preferencial e definir o título
                    String tituloCartao = cartao.isPreferencial() ? "Cartao Preferencial" : "Cartao Comum";
        %>

        <div class="container">
            <div class="cartao-container">
                <h3><%= tituloCartao %></h3> <!-- Exibe o título do tipo de cartão -->

                <div class="input-numero-nomeTitular">
                    <div class="input-container">
                        <label>Numero do Cartao</label>
                        <input type="text" value="<%= cartao.getNumero() %>" disabled>
                    </div>
                    <div class="input-container">
                        <label>Nome do Titular</label>
                        <input type="text" value="<%= cartao.getNomeTitular() %>" disabled>
                    </div>
                    <div class="input-container">
                        <label>Bandeira</label>
                        <input type="text" value="<%= cartao.getBandeiraCartao().getDescricao() %>" disabled>
                    </div>
                </div>

                <div class="input-cod-data-bandeira">
                    <div class="input-container-cvv">
                        <label>CVV</label>
                        <input type="text" value="<%= cartao.getCodSeguranca() %>" disabled>
                    </div>
                    <div class="input-container-dtVencimento">
                        <label>Vencimento</label>
                        <input type="text" value="<%= cartao.getDataVencimento() %>" disabled>
                    </div>
                    <div class="buttons-container">
                         <form action="servlet" method="get">
                             <input type="hidden" name="action" value="editarCartaoForm">
                             <input type="hidden" name="id" value="<%= cartao.getId() %>">
                             <button type="submit" class="editar-btn">Editar Cartao</button>
                         </form>

                         <!-- Formulário para exclusão -->
                            <form action="servlet" method="post" style="display:inline;" onsubmit="return confirmarExclusao();">
                                <input type="hidden" name="action" value="excluirCartao">
                                <input type="hidden" name="id" value="<%= cartao.getId() %>">
                                <button type="submit" class="excluir-btn">Excluir Cartao</button>
                            </form>
                    </div>
                
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
        <div class="buttons-container">
            <button class="voltar-button" onclick="window.location.href='servlet?action=consultarInfoPessoais'">Voltar</button>
        </div>
    </main>

    <script>
        function confirmarExclusao() {
            return confirm("Tem certeza que deseja excluir este cartao?");
        }
    </script>
</body>