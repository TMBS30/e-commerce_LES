<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Estado" %>
<%@ page import="dominio.Cidade" %>
<%@ page import="dominio.TipoEndereco" %>
<%@ page import="dao.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adicionar Novo Endereco | PageStation</title>
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

<div class="container">
    <div class="form_box">
        <h1 class="title">Adicionar Novo Endereco</h1>
        <form class="form" action="servlet" method="POST">
            <input type="hidden" name="idCliente" value="<%= session.getAttribute("clienteId") %>">
            <input type="hidden" name="action" value="salvarNovoEndereco">

            <h3 class="title3">Endereco</h3>
            <div class="container-input">
                <label for="pais">Pais:*</label>
                <select name="nomePais_NovoEnd" id="nomePaisNovoEnd">
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
                <select name="nomeEstado_NovoEnd" id="nomeEstadoNovoEnd">
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
                <select name="nomeCidade_NovoEnd" id="nomeCidadeNovoEnd">
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
                <label for="tipoEndereco">Tipo de Endereco:*</label>
                <select name="tipoEndereco_NovoEnd" id="tipoEnderecoNovoEnd">
                    <option value="" disabled selected>Selecione o Tipo de Endereco</option>
                    <%
                        for (TipoEndereco tipoEndereco : tipoEnderecos) {
                    %>
                        <option value="<%= tipoEndereco.getDescricao() %>"><%= tipoEndereco.getDescricao() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="container-input">
                <label for="tipoLogradouro">Tipo de Logradouro:*</label>
                <select name="tipoLogradouro_NovoEnd" id="tipoLogradouroNovoEnd">
                    <option value="" disabled selected>Selecione o Tipo de Logradouro</option>
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
                <label for="tipoResidencia">Tipo de Residencia:*</label>
                <select name="tipoResidencia_NovoEnd" id="tipoResidenciaNovoEnd">
                    <option value="" disabled selected>Selecione o Tipo de Residencia</option>
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
                <input type="text" name="cep_NovoEnd" id="cepNovoEnd" placeholder="Insira o CEP" required />
            </div>
            <div class="container-input">
                <label for="logradouro">Logradouro:*</label>
                <input type="text" name="logradouro_NovoEnd" id="logradouroNovoEnd" placeholder="Insira o Logradouro" required />
            </div>
            <div class="container-input">
                <label for="numero">Numero:*</label>
                <input type="text" name="numero_NovoEnd" id="numeroNovoEnd" placeholder="Insira o Numero" required />
            </div>
            <div class="container-input">
                <label for="bairro">Bairro:*</label>
                <input type="text" name="bairro_NovoEnd" id="bairroNovoEnd" placeholder="Insira o Bairro" required />
            </div>
            <div class="container-input">
                <label for="observacao">Observacao:</label>
                <input type="text" name="observacao_NovoEnd" id="observacaoNovoEnd" placeholder="Insira uma Observacao"/>
            </div>

            <div class="buttons-container">
                <button class="botao-voltar" type="button" onclick="window.location.href='consultarEndereco.jsp'">Voltar</button>
                <button type="submit" class="botao-proximo-link">Adicionar Endereco</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>