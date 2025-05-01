<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Estado" %>
<%@ page import="dominio.Cidade" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang = "pt-br">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Cadastro de Cliente - Endereço</title>
	<link rel="stylesheet" href="./css/clienteForm.css">
</head>
<body>

<%@page import="dao.*, dominio.*, controle.*"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
		<h2 class="title2">Endereco</h2>
		<form class="form" action="servlet" method="POST">
			<input type="hidden" name="action" value="salvarEndereco">

			<input type="hidden" name="idCliente" value="${sessionScope.idCliente != null ? sessionScope.idCliente : ''}">

			<h3 class="title3">Endereco Residencial</h3>
            <input type="hidden" name="tipoEnderecoRes" value="Residencial">
            <div class="container-input">
                <label for="pais">Pais:*</label>
                <select name="nomePaisRes_cadastro" id="nomePaisRes">
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

                <select name="nomeEstadoRes_cadastro" id="nomeEstadoRes">
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
            <div class="container-input">
                <label for="cidade">Cidade:*</label>
                <select name="nomeCidadeRes_cadastro" id="nomeCidadeRes">
                    <option value="" disabled selected>Selecione uma Cidade</option>
                        <%
                            for (Cidade cidade : cidades) {
                        %>
                            <option value="<%= cidade.getNome() %>"><%= cidade.getNome() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_logradouro">Tipo Logradouro:*</label>
                <select name="tipoLogradouroRes_cadastro" id="tipoLogradouroRes">
                     <option value="" disabled selected>Selecione um Tipo Logradouro</option>
                        <%
                            for (TipoLogradouro tipoLogradouro : tipoLogradouros) {
                        %>
                            <option value="<%= tipoLogradouro.getDescricao() %>"><%= tipoLogradouro.getDescricao() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_residencia">Tipo Residencia:*</label>
               <select name="tipoResidenciaRes_cadastro" id="tipoResidenciaRes" required>
                   <option value="" disabled selected>Selecione um tipo Residencia</option>
                   <%
                       for (TipoResidencia tipoResidencia : tipoResidencias) {
                   %>
                       <option value="<%= tipoResidencia.getDescricao() %>"><%= tipoResidencia.getDescricao() %></option>
                   <%
                       }
                   %>
               </select>
            </div>
            <div class="container-input">
                <label for="cep">CEP:*</label>
                <input type="text" name="cepRes_cadastro" id="cepRes" placeholder="Insira o CEP" required />
            </div>
            <div class="container-input">
                <label for="logradouro">Logradouro:*</label>
                <input type="text" name="logradouroRes_cadastro" id="logradouroRes" placeholder="Insira o Logradouro" required />
            </div>
            <div class="container-input">
                <label for="numero">Numero:*</label>
                <input type="text" name="numeroRes_cadastro" id="numeroRes" placeholder="Insira o Numero" required />
            </div>
            <div class="container-input">
                <label for="bairro">Bairro:*</label>
                <input type="text" name="bairroRes_cadastro" id="bairroRes" placeholder="Insira o Bairro" required />
            </div>
            <div class="container-input">
                <label for="observacao">Observacao:</label>
                <input type="text" name="observacaoRes_cadastro" id="observacaoRes" placeholder="Insira uma Observacao"/>
            </div>

            <h3 class="title3">Endereco Cobranca</h3>
            <input type="hidden" name="tipoEnderecoCob" value="Cobranca">
            <div class="container-input">
                <label for="pais">Pais:*</label>
                    <select name="nomePaisCob_cadastro" id="nomePaisCob">
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
                <select name="nomeEstadoCob_cadastro" id="nomeEstadoCob">
                    <option value="" disabled selected>Selecione um estado</option>
                    <%
                        for (Estado estado : estados) {
                    %>
                        <option value="<%= estado.getNome() %>"><%= estado.getNome() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="container-input">
                <label for="cidade">Cidade:*</label>
                <select name="nomeCidadeCob_cadastro" id="nomeCidadeCob">
                    <option value="" disabled selected>Selecione uma Cidade</option>
                    <%
                        for (Cidade cidade : cidades) {
                    %>
                        <option value="<%= cidade.getNome() %>"><%= cidade.getNome() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_logradouro">Tipo Logradouro:*</label>
                <select name="tipoLogradouroCob_cadastro" id="tipoLogradouroCob">
                    <option value="" disabled selected>Selecione um Tipo Logradouro</option>
                        <%
                            for (TipoLogradouro tipoLogradouro : tipoLogradouros) {
                        %>
                            <option value="<%= tipoLogradouro.getDescricao() %>"><%= tipoLogradouro.getDescricao() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_residencia">Tipo Residencia:*</label>
                <select name="tipoResidenciaCob_cadastro" id="tipoResidenciaCob">
                    <option value="" disabled selected>Selecione um tipo Residencia</option>
                        <%
                            for (TipoResidencia tipoResidencia : tipoResidencias) {
                        %>
                            <option value="<%= tipoResidencia.getDescricao() %>"><%= tipoResidencia.getDescricao() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="cep">CEP:*</label>
                <input type="text" name="cepCob_cadastro" id="cepCob" placeholder="Insira o CEP" required />
            </div>
            <div class="container-input">
                <label for="logradouro">Logradouro:*</label>
                <input type="text" name="logradouroCob_cadastro" id="logradouroCob" placeholder="Insira o Logradouro" required />
            </div>
            <div class="container-input">
                <label for="numero">Numero:*</label>
                <input type="text" name="numeroCob_cadastro" id="numeroCob" placeholder="Insira o Numero" required />
            </div>
            <div class="container-input">
                <label for="bairro">Bairro:*</label>
                <input type="text" name="bairroCob_cadastro" id="bairroCob" placeholder="Insira o Bairro" required />
            </div>
            <div class="container-input">
                <label for="observacao">Observacao:</label>
                <input type="text" name="observacaoCob_cadastro" id="observacaoCob" placeholder="Insira uma Observacao"/>
            </div>

            <h3 class="title3">Endereco Entrega</h3>
            <input type="hidden" name="tipoEnderecoEnt" value="Entrega">
            <div class="container-input">
                <label for="pais">Pais:*</label>
                    <select name="nomePaisEnt_cadastro" id="nomePaisEnt">
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
                <select name="nomeEstadoEnt_cadastro" id="nomeEstadoEnt">
                    <option value="" disabled selected>Selecione um estado</option>
                    <%
                        for (Estado estado : estados) {
                    %>
                        <option value="<%= estado.getNome() %>"><%= estado.getNome() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="container-input">
                <label for="cidade">Cidade:*</label>
                <select name="nomeCidadeEnt_cadastro" id="nomeCidadeEnt">
                    <option value="" disabled selected>Selecione uma Cidade</option>
                    <%
                        for (Cidade cidade : cidades) {
                    %>
                        <option value="<%= cidade.getNome() %>"><%= cidade.getNome() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_logradouro">Tipo Logradouro:*</label>
                <select name="tipoLogradouroEnt_cadastro" id="tipoLogradouroEnt">
                    <option value="" disabled selected>Selecione um Tipo Logradouro</option>
                        <%
                            for (TipoLogradouro tipoLogradouro : tipoLogradouros) {
                        %>
                            <option value="<%= tipoLogradouro.getDescricao() %>"><%= tipoLogradouro.getDescricao() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipo_residencia">Tipo Residencia:*</label>
                <select name="tipoResidenciaEnt_cadastro" id="tipoResidenciaEnt">
                    <option value="" disabled selected>Selecione um tipo Residencia</option>
                        <%
                            for (TipoResidencia tipoResidencia : tipoResidencias) {
                        %>
                            <option value="<%= tipoResidencia.getDescricao() %>"><%= tipoResidencia.getDescricao() %></option>
                        <%
                            }
                        %>
                </select>
            </div>
            <div class="container-input">
                <label for="cep">CEP:*</label>
                <input type="text" name="cepEnt_cadastro" id="cepEnt" placeholder="Insira o CEP" required />
            </div>
            <div class="container-input">
                <label for="logradouro">Logradouro:*</label>
                <input type="text" name="logradouroEnt_cadastro" id="logradouroEnt" placeholder="Insira o Logradouro" required />
            </div>
            <div class="container-input">
                <label for="numero">Numero:*</label>
                <input type="text" name="numeroEnt_cadastro" id="numeroEnt" placeholder="Insira o Numero" required />
            </div>
            <div class="container-input">
                <label for="bairro">Bairro:*</label>
                <input type="text" name="bairroEnt_cadastro" id="bairroEnt" placeholder="Insira o Bairro" required />
            </div>
            <div class="container-input">
                <label for="observacao">Observacao:</label>
                <input type="text" name="observacaoEnt_cadastro" id="observacaoEnt" placeholder="Insira uma Observacao"/>
            </div>

			<div class="buttons-container">
                <button class="botao-voltar" type="button" onclick="window.location.href='clienteForm.jsp'">Voltar</button>
                <button type="submit" class="botao-proximo-link">Proximo</button>
            </div>
		</form>
	</div>
</div>

</body>
</html>