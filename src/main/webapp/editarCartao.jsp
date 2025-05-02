<%@ page import="java.util.*" %>
<%@ page import="dominio.Cartao" %>
<%@ page import="dao.CartaoDAO" %>
<%@ page import="dao.BandeiraCartaoDAO" %>
<%@ page import="dominio.BandeiraCartao" %>

<head>
    <title>Editar Cartao | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/adicionarNovoCartao.css">
</head>
<body>

<header>
    <a href="home.jsp" class="logo-link">
        <h1>PageStation</h1>
        <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
    </a>
</header>

<main>
    <%
        // Obter o ID do cartão a ser editado
        String cartaoId = request.getParameter("id");
        CartaoDAO cartaoDAO = new CartaoDAO();
        Cartao cartao = cartaoDAO.consultarCartaoPorId(Integer.parseInt(cartaoId));

        // Carregar as bandeiras disponíveis para o select
        BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
        List<BandeiraCartao> listaBandeiras = bandeiraCartaoDAO.consultar();
        request.setAttribute("listaBandeiras", listaBandeiras);
    %>

    <h2>Editar Cartao</h2> <!-- Título da tela -->

    <form class="form" action="servlet" method="POST">
        <input type="hidden" name="action" value="editarCartao">
        <input type="hidden" name="idCartao" value="<%= cartao.getId() %>">

        <div class="input-container_nomeCartao">
            <label for="nomeTitular_editarCartao">*Nome do Titular</label>
            <input type="text" name="nomeTitular_editarCartao" id="nomeTitular_editarCartao" value="<%= cartao.getNomeTitular() %>" required />
        </div>

        <div class="input-container_numeroCartao">
            <label for="numeroCartao_editarCartao">*Numero do Cartao</label>
            <input type="text" name="numeroCartao_editarCartao" id="numeroCartao_editarCartao" value="<%= cartao.getNumero() %>" required />
        </div>

        <div class="input-container_bandeira">
            <label for="bandeira_editarCartao">*Bandeira</label>
            <select name="bandeira_editarCartao" id="bandeira_editarCartao">
                <option value="" disabled>Selecione a Bandeira do Cartao</option>
                <%
                    for (BandeiraCartao bandeiraCartao : listaBandeiras) {
                        // Verificar se a bandeira é a mesma do cartão
                        String selected = bandeiraCartao.getDescricao().equals(cartao.getBandeiraCartao().getDescricao()) ? "selected" : "";
                %>
                    <option value="<%= bandeiraCartao.getDescricao() %>" <%= selected %>><%= bandeiraCartao.getDescricao() %></option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="container-lado-a-lado">
            <div class="input-container_codCartao">
                <label for="codSeguranca_editarCartao">*CVV</label>
                <input type="text" name="codSeguranca_editarCartao" id="codSeguranca_editarCartao" value="<%= cartao.getCodSeguranca() %>" required />
            </div>

            <div class="input-container_dataVencimento">
                <label for="dataVencimento_editarCartao">*Data Vencimento</label>
                <input type="text" name="dataVencimento_editarCartao" id="dataVencimento_editarCartao" value="<%= cartao.getDataVencimento() %>" required />
            </div>

            <div class="input-container">
                <label for="preferencialCartao_editarCartao">Preferencial?</label>
                <input type="checkbox" name="preferencialCartao_editarCartao" id="preferencialCartao_editarCartao" value="true" <%= cartao.isPreferencial() ? "checked" : "" %> />
            </div>

            <div class="buttons-container">
                <button type="submit">Salvar Alteracoes</button>
            </div>
            <div class="buttons-container">
                <button class="voltar-button" onclick="window.location.href='consultarCartao.jsp'">Voltar</button>
            </div>
        </div>

    </form>

    <script>
        // Máscara para a Data de Vencimento
        document.getElementById('dataVencimento_editarCartao').addEventListener('input', function (e) {
            var value = e.target.value;
            value = value.replace(/\D/g, '');
            if (value.length <= 2) {
                e.target.value = value.replace(/(\d{2})(\d{0,2})/, '$1/$2');
            } else {
                e.target.value = value.replace(/(\d{2})(\d{2})(\d{0,4})/, '$1/$2');
            }
        });

        // Formatação de envio da data
        document.querySelector('form').addEventListener('submit', function (e) {
            var dataVencimentoInput = document.getElementById('dataVencimento_editarCartao');
            var dataVencimentoValue = dataVencimentoInput.value;
            dataVencimentoInput.value = dataVencimentoValue.replace('/', '');
        });
    </script>
</main>

</body>
</html>