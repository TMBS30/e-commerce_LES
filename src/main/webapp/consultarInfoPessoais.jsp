<%@ page import="java.util.*" %>
 <%@ page import="dominio.Cliente" %>
 <%@ page import="dao.ClienteDAO" %>
<head>
    <title>Informacoes Pessoais | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/infoPessoais.css">
</head>
<body>

 <%
     Integer clienteId = (Integer) session.getAttribute("clienteId");
     Cliente cliente = null;
     if (clienteId != null) {
         ClienteDAO clienteDAO = new ClienteDAO();
         cliente = clienteDAO.consultarPorId(clienteId);
     }

     if (cliente == null) {
 %>
     <p>Erro: Cliente não encontrado.</p>
 <%
     } else {
 %>

 <header>
    <a href="servlet?action=voltarHomePage" class="logo-link">
        <h1>PageStation</h1>
        <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
    </a>
</header>

     <div class="container">
         <h2 class="title">Informacoes Pessoais</h2>
         <div class="input-container">
             <label>Nome:</label>
             <input type="text" value="<%= cliente.getNome() %>" disabled>
         </div>
         <div class="input-container">
             <label>CPF:</label>
             <input type="text" value="<%= cliente.getCpf() %>" disabled>
         </div>
         <div class="input-container">
             <label>Email:</label>
             <input type="text" value="<%= cliente.getEmail() %>" disabled>
         </div>
         <div class="input-container">
             <label>Genero:</label>
             <input type="text" value="<%= cliente.getGenero().getDescricao() %>" disabled>
         </div>
         <div class="input-container">
             <label>Data de Nascimento:</label>
             <input type="text" value="<%= cliente.getDataNascimento() %>" disabled>
         </div>
         <div class="input-container">
             <label>Telefone:</label>
             <input type="text" value="<%= cliente.getTelefoneFormatado() != null ? cliente.getTelefoneFormatado() : "Nao informado" %>" disabled>
         </div>
         <div class="input-container">
             <label>Status:</label>
             <input type="text" class="<%= cliente.isStatus() ? "status-ativo" : "status-inativo" %>"
                    value="<%= cliente.isStatus() ? "Ativo" : "Inativo" %>" disabled>
         </div>

         <!-- Link para consultar cartões -->
         <div class="input-container">
             <label>Cartoes:</label>
             <a href="servlet?action=consultarCartao&id=<%= cliente.getId() %>">Ver Cartoes</a>
         </div>

         <!-- Link para consultar endereços -->
         <div class="input-container">
             <label>Enderecos:</label>
             <a href="servlet?action=consultarEndereco&id=<%= cliente.getId() %>">Ver Enderecos</a>
         </div>

         <!-- Botões para editar informações e voltar -->
         <div class="buttons-container">
             <button class="editar-btn" onclick="window.location.href='editform.jsp?id=<%= cliente.getId() %>'">Editar Informacoes</button>
             <button class="voltar-button" onclick="window.location.href='servlet?action=voltarHomePage'">Voltar</button>
         </div>
     </div>
 <%
     }
 %>

</main>
</body>