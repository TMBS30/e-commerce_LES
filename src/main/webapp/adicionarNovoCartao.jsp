<%@ page import="java.util.*" %>
<%@ page import="dominio.Cartao" %>
<%@ page import="dao.CartaoDAO" %>
<%@ page import="dominio.BandeiraCartao" %>
<%@ page import="dao.ClienteDAO" %>
<%@ page import="dominio.Cliente" %>
<%@ page import="dao.*" %>
<%@ page import="dominio.*" %>

<head>
    <title>Adicionar Cartao | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/adicionarNovoCartao.css">
</head>
<body>

<header>
     <a href="servlet?action=voltarHomePage" class="logo-link">
         <h1>PageStation</h1>
         <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
     </a>
</header>
<main>
    <%
        BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
        List<BandeiraCartao> listaBandeiras = bandeiraCartaoDAO.consultar();
        request.setAttribute("listaBandeiras", listaBandeiras);
        List<BandeiraCartao> bandeiraCartaoLista = (List<BandeiraCartao>) request.getAttribute("listaBandeiras");
    %>

    <%
         Integer clienteId = (Integer) session.getAttribute("clienteId");
         Cliente cliente = null;
         if (clienteId != null) {
             ClienteDAO clienteDAO = new ClienteDAO();
             cliente = clienteDAO.consultarPorId(clienteId);
         }
     System.out.println("ID do Cliente na sessao: " + clienteId); // Valide se o ID aparece
    %>

    <%
        if (clienteId != null) {
    %>
        <input type="hidden" name="idCliente" value="<%= clienteId %>">
    <%
        } else {
    %>
        <p>Erro: Cliente nao encontrado.</p>
    <%
        }
    %>


    <h2>Adicionar Novo Cartao</h2> <!-- Título da tela -->

    <form class="form" action="servlet" method="POST">
        <input type="hidden" name="idCliente" value="<%= session.getAttribute("clienteId") %>">
        <input type="hidden" name="action" value="salvarNovoCartao">

        <div class="input-container_nomeCartao">
            <label for="nomeTitular_novoCartao">*Nome do Titular</label>
            <input type="text" name="nomeTitular_novoCartao" id="nomeTitular_novoCartao" required />
        </div>

        <div class="input-container_numeroCartao">
            <label for="numeroCartao_novoCartao">*Numero do Cartao</label>
            <input type="text" name="numeroCartao_novoCartao" id="numeroCartao_novoCartao" required />
        </div>

        <div class="input-container_bandeira">
            <label for="bandeira_novoCartao">*Bandeira</label>
            <select name="bandeira_novoCartao" id="bandeira_novoCartao">
                <option value="" disabled selected>Selecione a Bandeira do Cartao</option>
                <%
                    for (BandeiraCartao bandeiraCartao : bandeiraCartaoLista) {
                %>
                    <option value="<%= bandeiraCartao.getDescricao() %>"><%= bandeiraCartao.getDescricao() %></option>
                <%
                    }
                %>
            </select>
        </div>

        
        <div class="container-lado-a-lado">
            <div class="input-container_codCartao">
                <label for="codSeguranca_novoCartao">*CVV</label>
                <input type="text" name="codSeguranca_novoCartao" id="codSeguranca_novoCartao" required />
            </div>
    
            <div class="input-container_dataVencimento">
                <label for="dataVencimento_novoCartao">*Data Vencimento</label>
                <input type="text" name="dataVencimento_novoCartao" id="dataVencimento_novoCartao" required />
            </div>
            <div class="input-container">
                <label for="preferencialCartao_novoCartao">Preferencial?</label>
                <input type="checkbox" name="preferencialCartao_novoCartao" id="preferencialCartao_novoCartao" value="true"/>
            </div>
    
            <div class="buttons-container">
                <button type="submit">Salvar</button>
            </div>
            <div class="buttons-container">
                <button class="voltar-button" onclick="window.location.href='consultarCartao.jsp'">Voltar</button>
            </div>
        </div>


        <script>
    document.getElementById('dataVencimento_novoCartao').addEventListener('input', function (e) {
        var value = e.target.value;
        value = value.replace(/\D/g, '');
        if (value.length <= 2) {
            e.target.value = value.replace(/(\d{2})(\d{0,2})/, '$1/$2');
        } else {
            e.target.value = value.replace(/(\d{2})(\d{2})(\d{0,4})/, '$1/$2');
        }
    });
    document.querySelector('form').addEventListener('submit', function (e) {
        var dataVencimentoInput = document.getElementById('dataVencimento_novoCartao');
        var dataVencimentoValue = dataVencimentoInput.value;
        dataVencimentoInput.value = dataVencimentoValue.replace('/', '');
    });
        </script>
    </form>
</main>
</body>
</html>