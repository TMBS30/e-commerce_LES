<%@ page import="dao.*, dominio.*, java.util.*, controle.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="cli" class="dominio.Cliente"></jsp:useBean>
<jsp:setProperty property="*" name="cli" />


<%
	Controller controller = new Controller();

	String dddTelefone = request.getParameter("ddd");
	String numeroTelefone = request.getParameter("numero");
	String tipoTelefone = request.getParameter("tipo");
	Telefone telefone = new Telefone();
	telefone.setDdd(dddTelefone);
	telefone.setNumero(numeroTelefone);
	telefone.setTipo(TipoTelefone.valueOf(tipoTelefone));
	((Cliente) cli).setTelefone(telefone);

	if (cli instanceof EntidadeDominio){
		String cliente = controller.salvar((EntidadeDominio) cli);

		if(cliente.contains("Erro:")){
			out.println(cliente);
		}else{

		String cepRes = request.getParameter("cepRes");
		String logradouroRes = request.getParameter("logradouroRes");
		String numeroRes = request.getParameter("numeroRes");
		String bairroRes = request.getParameter("bairroRes");
		String tipoLogRes = request.getParameter("tipoLogradouroRes");
		String tipoResRes = request.getParameter("tipoResidenciaRes");
		String paisNomeRes = request.getParameter("nomePaisRes");
		String estadoNomeRes = request.getParameter("nomeEstadoRes");
		String cidadeNomeRes = request.getParameter("nomeCidadeRes");
		String observacaoRes = request.getParameter("observacaoRes");

		String cepCob = request.getParameter("cepCob");
		String logradouroCob = request.getParameter("logradouroCob");
		String numeroCob = request.getParameter("numeroCob");
		String bairroCob = request.getParameter("bairroCob");
		String tipoLogCob = request.getParameter("tipoLogradouroCob");
		String tipoResCob = request.getParameter("tipoResidenciaCob");
		String paisNomeCob = request.getParameter("nomePaisCob");
		String estadoNomeCob = request.getParameter("nomeEstadoCob");
		String cidadeNomeCob = request.getParameter("nomeCidadeCob");
		String observacaoCob = request.getParameter("observacaoCob");

		String cepEnt = request.getParameter("cepEnt");
		String logradouroEnt = request.getParameter("logradouroEnt");
		String numeroEnt = request.getParameter("numeroEnt");
		String bairroEnt = request.getParameter("bairroEnt");
		String tipoLogEnt = request.getParameter("tipoLogradouroEnt");
		String tipoResEnt = request.getParameter("tipoResidenciaEnt");
		String paisNomeEnt = request.getParameter("nomePaisEnt");
		String estadoNomeEnt = request.getParameter("nomeEstadoEnt");
		String cidadeNomeEnt = request.getParameter("nomeCidadeEnt");
		String observacaoEnt = request.getParameter("observacaoEnt");

		Cidade cidadeRes = new Cidade();
		Estado estadoRes = new Estado();
		Pais paisRes = new Pais();

		Cidade cidadeCob = new Cidade();
		Estado estadoCob = new Estado();
		Pais paisCob = new Pais();

		Cidade cidadeEnt = new Cidade();
		Estado estadoEnt = new Estado();
		Pais paisEnt = new Pais();

		Endereco enderecoRes = new Endereco();
		enderecoRes.setCep(cepRes);
		enderecoRes.setLogradouro(logradouroRes);
		enderecoRes.setNumero(1);
		enderecoRes.setBairro(bairroRes);
		enderecoRes.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		enderecoRes.setTipoLogradouro(TipoLogradouro.valueOf(tipoLogRes));
		enderecoRes.setTipoResidencia(TipoResidencia.valueOf(tipoResRes));
		enderecoRes.setObservacao(observacaoRes);
		paisRes.setNome(paisNomeRes);
		estadoRes.setNome(estadoNomeRes);
		estadoRes.setPais(paisRes);
		cidadeRes.setEstado(estadoRes);
		cidadeRes.setNome(cidadeNomeRes);
		enderecoRes.setCidade(cidadeRes);
		enderecoRes.setIdCliente(Integer.parseInt(cliente));

		Endereco enderecoCob = new Endereco();
		enderecoCob.setCep(cepCob);
		enderecoCob.setLogradouro(logradouroCob);
		enderecoCob.setNumero(1);
		enderecoCob.setBairro(bairroCob);
		enderecoCob.setTipoEndereco(TipoEndereco.COBRANÇA);
		enderecoCob.setTipoLogradouro(TipoLogradouro.valueOf(tipoLogCob));
		enderecoCob.setTipoResidencia(TipoResidencia.valueOf(tipoResCob));
		paisCob.setNome(paisNomeCob);
		estadoCob.setNome(estadoNomeCob);
		estadoCob.setPais(paisCob);
		cidadeCob.setEstado(estadoCob);
		cidadeCob.setNome(cidadeNomeCob);
		enderecoCob.setCidade(cidadeCob);
		enderecoCob.setIdCliente(Integer.parseInt(cliente));
		enderecoCob.setObservacao(observacaoCob);


		Endereco enderecoEnt = new Endereco();
		enderecoEnt.setCep(cepEnt);
		enderecoEnt.setLogradouro(logradouroEnt);
		enderecoEnt.setNumero(1);
		enderecoEnt.setBairro(bairroEnt);
		enderecoEnt.setTipoEndereco(TipoEndereco.ENTREGA);
		enderecoEnt.setTipoLogradouro(TipoLogradouro.valueOf(tipoLogEnt));
		enderecoEnt.setTipoResidencia(TipoResidencia.valueOf(tipoResEnt));
		enderecoEnt.setObservacao(observacaoEnt);
		paisEnt.setNome(paisNomeEnt);
		estadoEnt.setNome(estadoNomeEnt);
		estadoEnt.setPais(paisEnt);
		cidadeEnt.setEstado(estadoEnt);
		cidadeEnt.setNome(cidadeNomeEnt);
		enderecoEnt.setCidade(cidadeEnt);
		enderecoEnt.setIdCliente(Integer.parseInt(cliente));

		clienteController.salvar(enderecoRes);
		clienteController.salvar(enderecoCob);
		clienteController.salvar(enderecoEnt);

		}

		response.sendRedirect("index.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);

	}else{
		out.println("O objeto nao e uma instancia de entidade dominio");
	}