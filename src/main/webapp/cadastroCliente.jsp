<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Cadastro - E-commerce de Livros</title>
    <link rel="stylesheet" href="./css/login.css">
</head>
<body>
<%@page import="dao.*, dominio.*, controle.*"%>

<%
		TipoEnderecoDAO tipoEnderecoDAO = new TipoEnderecoDAO();
		TipoLogradouroDAO tipoLogradouroDAO = new TipoLogradouroDAO();
		TipoResidenciaDAO tipoResidenciaDAO = new TipoResidenciaDAO();
		TipoTelefoneDAO tipoTelefoneDAO = new TipoTelefoneDAO();

		PaisDAO paisDAO = new PaisDAO();
		EstadoDAO estadoDAO = new EstadoDAO();
		CidadeDAO cidadeDAO = new CidadeDAO();

		List<TipoEndereco> listaTiposEnd = tipoEnderecoDAO.consultar();
		List<TipoLogradouro> listaTiposLog = tipoLogradouroDAO.consultar();
		List<TipoResidencia> listaTiposRes = tipoResidenciaDAO.consultar();
		List<TipoTelefone> listaTiposTel = tipoTelefoneDAO.consultar();
		List<Pais> listaPaises = paisDAO.consultar();
		List<Estado> listaEstados = estadoDAO.consultar();
		List<Cidade> listaCidades = cidadeDAO.consultar();

		request.setAttribute("listaTiposEnd", listaTiposEnd);
		request.setAttribute("listaTiposLog", listaTiposLog);
		request.setAttribute("listaTiposRes", listaTiposRes);
		request.setAttribute("listaTiposTel", listaTiposTel);
		request.setAttribute("listaPaises", listaPaises);
		request.setAttribute("listaEstados", listaEstados);
		request.setAttribute("listaCidades", listaCidades);

		List<Estado> estados = (List<Estado>) request.getAttribute("listaEstados");
		List<Cidade> cidades = (List<Cidade>) request.getAttribute("listaCidades");
		List<Pais> paises = (List<Pais>) request.getAttribute("listaPaises");
		List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) request.getAttribute("listaTiposEnd");
		List<TipoLogradouro> tipoLogradouros = (List<TipoLogradouro>) request.getAttribute("listaTiposLog");
		List<TipoResidencia> tipoResidencias = (List<TipoResidencia>) request.getAttribute("listaTiposRes");
		List<TipoTelefone> tipoTelefones = (List<TipoTelefone>) request.getAttribute("listaTiposTel");
	%>
    <header>
        <div class="logo-link">
            <h1>PageStation</h1>
            <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
        </div>
    </header>

    <div class="container">
        <h1 class="title">CADASTRO DE CLIENTE</h1>
        <form action="controller" method="post">
            <input type="hidden" name="action" value="cadastro">

            <div class="input-container">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" placeholder="Insira o nome" required>
            </div>

            <div class="input-container">
                <label for="email">E-mail:</label>
                <input type="email" id="email" name="email" required>
            </div>

            <h2 class="title2">Endereco</h2>
                <h3 class="title3">Endereco Residencial</h3>
                <div class="container-input">
                    <label for="pais">Pais:*</label>
                    <select name="nomePaisRes" id="nomePaisRes">
                        <option value="" disabled selected>Selecione um Pais</option>
                        <%
                            for (Pais pais : paises) {
                        %>
                            <option value="<%= pais.getNome() %>"><%= pais.getNome() %></option>
                        <%
                            }
                        %>
                    </select>
                </div>
                <div class="container-input">
                    <label for="estado">Estado:*</label>
                    <select name="nomeEstadoRes" id="nomeEstadoRes">
                    <option value="" disabled selected>Selecione um Estado</option>
                        <%
                            for (Estado estado : estados) {
                        %>
                            <option value="<%= estado.getNome() %>"><%= estado.getNome() %></option>
                        <%
                            }
                        %>
                    </select>
                </div>

            <div class="input-container">
                <label for="senha">Senha:</label>
                <input type="password" id="senha" name="senha" required>
            </div>

            <div class="buttons-container">
                <button type="submit">Cadastrar</button>
            </div>
        </form>

        <div class="links-container">
            <a href="index.jsp">Ja tem uma conta? Faca login</a>
        </div>
    </div>
</body>
</html>