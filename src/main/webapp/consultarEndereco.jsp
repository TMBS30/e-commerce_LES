<%@ page import="java.util.*" %>
<%@ page import="dominio.Endereco" %>
<%@ page import="dao.EnderecoDAO" %>
<head>
    <title>Enderecos Cadastrados | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/consultaEnderecos.css">
</head>
<body>
    <h2>Enderecos do Cliente</h2> <!-- Titulo da tela -->

    <!-- Botão Adicionar Endereço no topo -->
    <div class="top-buttons">
        <form action="servlet" method="post">
            <input type="hidden" name="action" value="salvarNovoEnderecoForm" />
            <button type="submit" class="add-button">Adicionar Endereco +</button>
        </form>
    </div>

    <%
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        List<Endereco> enderecos = null;
        if (clienteId != null) {
            EnderecoDAO enderecoDAO = new EnderecoDAO();
            enderecos = enderecoDAO.consultarPorCliente(clienteId);
        }

        if (enderecos == null || enderecos.isEmpty()) {
    %>
        <p>Erro: Nenhum endereço encontrado.</p>
    <%
        } else {
            for (Endereco endereco : enderecos) {
                // Verificar o tipo de endereço e definir o título
                String tituloEndereco = "";
                if (endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Cobranca")) {
                    tituloEndereco = "Endereco de Cobranca";
                } else if (endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Entrega")) {
                    tituloEndereco = "Endereco de Entrega";
                } else if (endereco.getTipoEndereco().getDescricao().equalsIgnoreCase("Residencial")) {
                    tituloEndereco = "Endereco Residencial";
                }
    %>

    <div class="container">
        <div class="endereco-container">
            <h3><%= tituloEndereco %></h3> <!-- Exibe o título do tipo de endereço -->

            <div class="input-container">
                <label>CEP:</label>
                <input type="text" value="<%= endereco.getCep() %>" disabled>
            </div>
            <div class="input-container">
                <label>Logradouro:</label>
                <input type="text" value="<%= endereco.getLogradouro() %>" disabled>
            </div>
            <div class="input-container">
                <label>Tipo Logradouro:</label>
                <input type="text" value="<%= endereco.getTipoLogradouro() %>" disabled> <!-- Tipo Logradouro -->
            </div>
            <div class="input-container">
                <label>Numero:</label>
                <input type="text" value="<%= endereco.getNumero() %>" disabled>
            </div>
            <div class="input-container">
                <label>Bairro:</label>
                <input type="text" value="<%= endereco.getBairro() %>" disabled>
            </div>
            <div class="input-container">
                <label>Cidade:</label>
                <input type="text" value="<%= endereco.getCidade().getNome() %>" disabled>
            </div>
            <div class="input-container">
                <label>Estado:</label>
                <input type="text" value="<%= endereco.getCidade().getEstado().getNome() %>" disabled>
            </div>
            <div class="input-container">
                <label>Tipo Residencia:</label>
                <input type="text" value="<%= endereco.getTipoResidencia() %>" disabled> <!-- Tipo Residência -->
            </div>
            <div class="input-container">
                <label>Observacao:</label>
                <input type="text" value="<%= endereco.getObservacao() %>" disabled>
            </div>

            <div class="buttons-container">
                <!-- Botão Editar -->
                <button class="editar-btn" onclick="window.location.href='editEndereco.jsp?id=<%= endereco.getId() %>'">Editar Endereco</button>

                <!-- Botão Excluir -->
                <form action="excluirEndereco.jsp" method="post" style="display:inline;">
                    <input type="hidden" name="enderecoId" value="<%= endereco.getId() %>">
                    <button type="submit" class="delete-btn" onclick="return confirm('Tem certeza que deseja excluir este endereço?');">Excluir</button>
                </form>
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

</body>
</html>