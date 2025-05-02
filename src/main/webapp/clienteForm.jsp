<%@ page import="java.util.List" %>
<%@ page import="dominio.Estado" %>
<%@ page import="dominio.Cidade" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang = "pt-br">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<title>Cadastro</title>
	<link rel="stylesheet" href="./css/clienteForm.css">
</head>
<body>

<%@page import="dao.*, dominio.*, controle.*"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<jsp:useBean id="cli" class="dominio.Cliente"></jsp:useBean>
	<jsp:setProperty property="*" name="cli" />

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

	<div class="container">
		<div class="form_box">
			<h1 class="title">Cadastro de Cliente</h1>
			<h2 class="title2">Dados pessoais</h2>
			<form class="form" action="servlet" method="POST">
			<input type="hidden" name="idCliente" value="${cli.getId()}">
			<input type="hidden" name="action" value="salvarCliente">
				<div class="container-input">
					<label for="nome">Nome:*</label>
					<input type="text" name="nome_cadastro" id="nome" placeholder="Insira o nome" required />
				</div>
				<div class="container-input">
					<label for="cpf">CPF:*</label>
					<input type="text" name="cpf_cadastro" id= "cpf" placeholder="Insira o CPF" required />
				</div>
				<div class="container-input">
					<label for="email">E-mail:*</label>
					<input type="email" name="email_cadastro" id= "email" placeholder="Insira o E-mail" required />
				</div>
				<div class="container-input">
					<label for="password">Senha:*</label>
					<input type="password" name="senha_cadastro" id= "senha" placeholder="Insira a Senha" required />
				</div>
				<div class="container-input">
					<label for="password">Confirmar Senha:*</label>
					<input type="password" name="confirmarSenha_cadastro" id= "confirmarSenha" placeholder="Confirme a Senha" required />
				</div>
				<div class="container-input">
					<label for="genero">Genero:*</label>
					<div>
						<div class="radio-options">
							<input type="radio" id="feminino" name="genero_cadastro" value="FEMININO">
							<label for="feminino">Feminino</label>
						</div>
						<div class="radio-options">
							<input type="radio" id="masculino" name="genero_cadastro" value="MASCULINO">
							<label for="masculino">Masculino</label>
						</div>
						<div class="radio-options">
							<input type="radio" id="outro" name="genero_cadastro" value="OUTRO">
							<label for="outro">Outro</label>
						</div>
					</div>
				</div>
				<div class="container-input">
					<label for="dataNascimento">Data Nascimento:*</label>
					<input type="date" name="dataNascimento_cadastro" id="dataNascimento" required />
				</div>

				<h3 class="title3">Telefone</h3>
				<div class="container-input">
				    <label for="telefone">Tipo Telefone:*</label>
                    <select name="tipoTel_cadastro" id="tipoTel">
                        <option value="" disabled selected>Selecione um tipo Telefone</option>
                        <%
                            for (TipoTelefone tipoTelefone : tipoTelefones) {
                        %>
                            <option value="<%= tipoTelefone.getDescricao() %>"><%= tipoTelefone.getDescricao() %></option>
                        <%
                            }
                        %>
                    </select>
				</div>

				<div class="container-input">
					<label for="ddd">DDD:* </label>
					<input type="text" name="ddd_cadastro" id="ddd" placeholder="Insira o DDD" required />
				</div>
				<div class="container-input">
					<label for="numero">Numero Telefone:*</label>
					<input type="text" name="numero_cadastro" id="numero" placeholder="Insira o Numero de Telefone" required />
				</div>

				<div class="buttons-container">
					<button class="botao-voltar" type="button" onclick="window.location.href='index.jsp'">Voltar</button>
					<button type="submit" class="botao-proximo-link">Proximo</button>
				</div>
			</form>
		</div>
	</div>
</body>