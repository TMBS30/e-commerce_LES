package controle;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dao.*;
import dominio.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DialogflowUtil;

@WebServlet("/fulfillment")
public class FulfillmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        String acao = request.getParameter("acao");

        if ("filtro".equalsIgnoreCase(acao)) {
            try {
                consultarLivrosComFiltros(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                homePage(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();
        System.out.println("JSON recebido do Dialogflow: " + jsonString);

        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            System.err.println("Erro ao parsear JSON de entrada do Dialogflow: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"fulfillmentText\": \"Erro interno ao processar sua solicitação.\"}");
            return;
        }

        String sessionPath = json.getString("session");

        JSONObject queryResult = json.getJSONObject("queryResult");
        JSONObject parametros = queryResult.getJSONObject("parameters");

        String intentDisplayName = queryResult.getJSONObject("intent").getString("displayName");
        System.out.println("Intent Detectada no Webhook: " + intentDisplayName);

        String respostaBot = "";
        JSONArray outputContextsParaDialogflow = new JSONArray(); // Array para os contextos de saída do webhook

        // Instancie seus DAOs
        LivroDAO livroDAO = new LivroDAO();
        ItemDAO itemDAO = new ItemDAO();

        try {
            switch (intentDisplayName) {
                case "BuscarSinopseLivro":
                    String nomeDoLivroParaSinopse = null;
                    // Tenta pegar o título dos parâmetros da Intent
                    if (parametros.has("titulo") && !parametros.isNull("titulo")) {
                        nomeDoLivroParaSinopse = extrairStringDeParametro(parametros.get("titulo"));
                    }

                    // Se não veio dos parâmetros, tenta pegar do contexto 'livro_mencionado_context'
                    if ((nomeDoLivroParaSinopse == null || nomeDoLivroParaSinopse.isEmpty()) && queryResult.has("outputContexts")) {
                        JSONArray inputContextsForWebhook = queryResult.getJSONArray("outputContexts");
                        for (int i = 0; i < inputContextsForWebhook.length(); i++) {
                            JSONObject context = inputContextsForWebhook.getJSONObject(i);
                            if (context.getString("name").contains("livro_mencionado_context")) {
                                JSONObject contextParameters = context.getJSONObject("parameters");
                                if (contextParameters.has("titulo") && !contextParameters.isNull("titulo")) {
                                    nomeDoLivroParaSinopse = extrairStringDeParametro(contextParameters.get("titulo"));
                                    System.out.println("DEBUG: Título para sinopse recuperado do contexto: " + nomeDoLivroParaSinopse);
                                }
                                break;
                            }
                        }
                    }

                    if (nomeDoLivroParaSinopse != null && !nomeDoLivroParaSinopse.isEmpty()) {
                        Livro livroSinopse = livroDAO.buscarPorTitulo(nomeDoLivroParaSinopse);
                        if (livroSinopse != null && livroSinopse.getSinopse() != null && !livroSinopse.getSinopse().isEmpty()) {
                            respostaBot = "A sinopse de \"" + livroSinopse.getTitulo() + "\" é: " + livroSinopse.getSinopse();

                            // Sempre que um livro é identificado e respondido, crie/atualize o contexto
                            JSONObject livroMencionadoContext = new JSONObject();
                            livroMencionadoContext.put("name", sessionPath + "/contexts/livro_mencionado_context");
                            livroMencionadoContext.put("lifespanCount", 3); // Vida útil do contexto

                            JSONObject paramsParaContexto = new JSONObject();
                            paramsParaContexto.put("titulo", livroSinopse.getTitulo());
                            paramsParaContexto.put("titulo.original", nomeDoLivroParaSinopse);
                            paramsParaContexto.put("idLivro", livroSinopse.getId()); // Adiciona o ID do livro
                            livroMencionadoContext.put("parameters", paramsParaContexto);
                            outputContextsParaDialogflow.put(livroMencionadoContext);
                            System.out.println("DEBUG: Contexto 'livro_mencionado_context' criado/atualizado com título: " + livroSinopse.getTitulo());

                        } else if (livroSinopse != null) {
                            respostaBot = "Desculpe, não encontrei a sinopse para o livro \"" + livroSinopse.getTitulo() + "\".";
                        } else {
                            respostaBot = "Desculpe, não consegui encontrar o livro \"" + nomeDoLivroParaSinopse + "\" em nosso catálogo. Tem certeza do nome?";
                        }
                    } else {
                        respostaBot = "De qual livro você gostaria de saber a sinopse?";
                    }
                    break;

                case "ElogioBot":
                    respostaBot = RespostasChatBot.gerarRespostaElogio();
                    System.out.println("DEBUG: Resposta gerada para ElogioBot: " + respostaBot);

                    // Re-propaga o contexto 'livro_mencionado_context' se ele existe
                    if (queryResult.has("outputContexts")) {
                        JSONArray inputContextsForWebhook = queryResult.getJSONArray("outputContexts");
                        for (int i = 0; i < inputContextsForWebhook.length(); i++) {
                            JSONObject context = inputContextsForWebhook.getJSONObject(i);
                            if (context.getString("name").contains("livro_mencionado_context")) {
                                JSONObject paramsOriginals = context.getJSONObject("parameters");
                                if (paramsOriginals.has("titulo") && !paramsOriginals.isNull("titulo")) {
                                    JSONObject newContext = new JSONObject();
                                    newContext.put("name", sessionPath + "/contexts/livro_mencionado_context");
                                    newContext.put("lifespanCount", 3); // Mantém o lifespan
                                    newContext.put("parameters", paramsOriginals); // Copia TODOS os parâmetros
                                    outputContextsParaDialogflow.put(newContext);
                                    System.out.println("DEBUG: Contexto 'livro_mencionado_context' propagado da ElogioBot com título: " + paramsOriginals.getString("titulo"));
                                }
                                break;
                            }
                        }
                    }
                    break;

                case "ComprarLivro":
                    String tituloDoLivroParaComprar = null;
                    String idDoLivroParaComprar = null;

                    // 1. Tentar pegar o título/ID diretamente dos parâmetros da Intent atual
                    if (parametros.has("titulo") && !parametros.isNull("titulo")) {
                        tituloDoLivroParaComprar = extrairStringDeParametro(parametros.get("titulo"));
                    }
                    if (parametros.has("idLivro") && !parametros.isNull("idLivro")) {
                        idDoLivroParaComprar = extrairStringDeParametro(parametros.get("idLivro"));
                    }

                    // 2. Se o título/ID não veio diretamente, tentar buscar no contexto
                    if ((tituloDoLivroParaComprar == null || tituloDoLivroParaComprar.isEmpty()) && queryResult.has("outputContexts")) {
                        JSONArray inputContextsForWebhook = queryResult.getJSONArray("outputContexts");
                        for (int i = 0; i < inputContextsForWebhook.length(); i++) {
                            JSONObject context = inputContextsForWebhook.getJSONObject(i);
                            if (context.getString("name").contains("livro_mencionado_context")) {
                                JSONObject contextParameters = context.getJSONObject("parameters");
                                if (contextParameters.has("titulo") && !contextParameters.isNull("titulo")) {
                                    tituloDoLivroParaComprar = extrairStringDeParametro(contextParameters.get("titulo"));
                                }
                                if (contextParameters.has("idLivro") && !contextParameters.isNull("idLivro")) {
                                    idDoLivroParaComprar = extrairStringDeParametro(contextParameters.get("idLivro"));
                                }

                                // Propaga o contexto 'livro_mencionado_context' para manter a memória ativa após a compra
                                JSONObject livroMencionadoContext = new JSONObject();
                                livroMencionadoContext.put("name", sessionPath + "/contexts/livro_mencionado_context");
                                livroMencionadoContext.put("lifespanCount", 3);
                                livroMencionadoContext.put("parameters", contextParameters);
                                outputContextsParaDialogflow.put(livroMencionadoContext);

                                System.out.println("DEBUG: Título '" + tituloDoLivroParaComprar + "' e ID '" + idDoLivroParaComprar + "' recuperados do contexto para compra.");
                                break;
                            }
                        }
                    }

                    if (tituloDoLivroParaComprar != null && !tituloDoLivroParaComprar.isEmpty()) {
                        // Lógica para adicionar ao carrinho ou redirecionar para a página de compra
                        respostaBot = "Certo! Prosseguindo com a compra do livro \"" + tituloDoLivroParaComprar + "\".";
                        // Exemplo de como você poderia usar o ID do livro:
                        // if (idDoLivroParaComprar != null) {
                        //     respostaBot += " Acesse: http://localhost:8080/e-commerce_LES/servlet?action=adicionarAoCarrinho&id_livro=" + idDoLivroParaComprar;
                        // }
                    } else {
                        respostaBot = "Desculpe, não identifiquei qual livro você gostaria de comprar. Poderia me dizer o título?";
                    }
                    break;

                case "BuscarLivroPorTitulo":
                case "BuscarLivroPorCategoria":
                case "BuscarLivroPorEditora":
                case "BuscarLivroPorAno":
                case "BuscarLivroPorDimensao":
                case "BuscarLivroPorPreco":
                    Map<String, Object> filtros = new HashMap<>();
                    String tituloParaBusca = null;

                    // 1. Tentar pegar o título diretamente dos parâmetros da Intent atual
                    if (parametros.has("titulo") && !parametros.isNull("titulo")) {
                        tituloParaBusca = extrairStringDeParametro(parametros.get("titulo"));
                        if (tituloParaBusca != null && !tituloParaBusca.isEmpty()) {
                            filtros.put("titulo", tituloParaBusca);
                            System.out.println("DEBUG: Título encontrado diretamente nos parâmetros da Intent: " + tituloParaBusca);
                        }
                    }

                    // 2. Se o título não veio diretamente, tentar buscar nos outputContexts (que são os inputContexts para o WEBHOOK)
                    // Este bloco é para *reutilizar* o contexto anterior, não para criar um novo primariamente
                    if ((tituloParaBusca == null || tituloParaBusca.isEmpty()) && queryResult.has("outputContexts")) {
                        JSONArray inputContextsForWebhook = queryResult.getJSONArray("outputContexts");
                        for (int i = 0; i < inputContextsForWebhook.length(); i++) {
                            JSONObject context = inputContextsForWebhook.getJSONObject(i);
                            if (context.getString("name").contains("livro_mencionado_context")) {
                                JSONObject contextParameters = context.getJSONObject("parameters");
                                if (contextParameters.has("titulo") && !contextParameters.isNull("titulo")) {
                                    // Se o usuário não especificou um novo título, usamos o do contexto
                                    if (tituloParaBusca == null || tituloParaBusca.isEmpty()) {
                                        tituloParaBusca = extrairStringDeParametro(contextParameters.get("titulo"));
                                        if (tituloParaBusca != null && !tituloParaBusca.isEmpty()) {
                                            filtros.put("titulo", tituloParaBusca);
                                            System.out.println("DEBUG: Título recuperado do contexto 'livro_mencionado_context': " + tituloParaBusca);
                                        }
                                    }
                                    // O contexto será criado/atualizado *após* a busca, caso um livro seja encontrado.
                                }
                                break;
                            }
                        }
                    }

                    // --- O restante da extração de filtros permanece como estava ---
                    // Categoria
                    if (parametros.has("categoria") && !parametros.isNull("categoria")) {
                        String categoriaStr = extrairStringDeParametro(parametros.get("categoria"));
                        if (categoriaStr != null && !categoriaStr.isEmpty()) {
                            filtros.put("categoria", categoriaStr);
                        }
                    }

                    // Ano
                    if (parametros.has("ano") && !parametros.isNull("ano")) {
                        try {
                            Object anoObj = parametros.get("ano");
                            if (anoObj instanceof Integer) {
                                filtros.put("ano", (Integer) anoObj);
                            } else if (anoObj instanceof Double) {
                                filtros.put("ano", ((Double) anoObj).intValue());
                            } else if (anoObj instanceof String && !((String) anoObj).isEmpty()) {
                                filtros.put("ano", Integer.parseInt((String) anoObj));
                            }
                        } catch (Exception e) {
                            System.out.println("Erro ao extrair ano: " + e.getMessage());
                        }
                    }

                    // Editora
                    if (parametros.has("editora") && !parametros.isNull("editora")) {
                        String editoraStr = extrairStringDeParametro(parametros.get("editora"));
                        if (editoraStr != null && !editoraStr.isEmpty()) {
                            filtros.put("editora", editoraStr);
                        }
                    }

                    // Preço
                    if (parametros.has("preco") && !parametros.isNull("preco")) {
                        Object precoObj = parametros.get("preco");
                        if (precoObj instanceof Integer) {
                            filtros.put("preco_max", ((Integer) precoObj).doubleValue());
                        } else if (precoObj instanceof Double) {
                            filtros.put("preco_max", (Double) precoObj);
                        } else if (precoObj instanceof JSONObject) {
                            JSONObject precoRange = (JSONObject) precoObj;
                            if (precoRange.has("amount")) {
                                filtros.put("preco_max", precoRange.getDouble("amount"));
                            }
                            if (precoRange.has("start")) {
                                filtros.put("preco_min", precoRange.getDouble("start"));
                            }
                            if (precoRange.has("end")) {
                                filtros.put("preco_max", precoRange.getDouble("end"));
                            }
                        }
                    }
                    if (parametros.has("preco_min") && !parametros.isNull("preco_min")) {
                        filtros.put("preco_min", ((Number) parametros.get("preco_min")).doubleValue());
                    }
                    if (parametros.has("preco_max") && !parametros.isNull("preco_max")) {
                        filtros.put("preco_max", ((Number) parametros.get("preco_max")).doubleValue());
                    }

                    // Dimensão
                    if (parametros.has("dimensao_tipo") && !parametros.isNull("dimensao_tipo")) {
                        String dimensaoTipo = extrairStringDeParametro(parametros.get("dimensao_tipo"));
                        if (dimensaoTipo != null && !dimensaoTipo.isEmpty()) {
                            filtros.put("dimensao_tipo", dimensaoTipo);

                            if (parametros.has("valor_dimensao") && !parametros.isNull("valor_dimensao")) {
                                Object valorDimensaoObj = parametros.get("valor_dimensao");
                                if (valorDimensaoObj instanceof Integer) {
                                    filtros.put("valor_dimensao", ((Integer) valorDimensaoObj).doubleValue());
                                } else if (valorDimensaoObj instanceof Double) {
                                    filtros.put("valor_dimensao", (Double) valorDimensaoObj);
                                } else if (valorDimensaoObj instanceof JSONObject) {
                                    JSONObject dimensaoUnitObj = (JSONObject) valorDimensaoObj;
                                    if (dimensaoUnitObj.has("amount")) {
                                        filtros.put("valor_dimensao", dimensaoUnitObj.getDouble("amount"));
                                        if (dimensaoUnitObj.has("unit")) {
                                            filtros.put("unidade_dimensao", dimensaoUnitObj.getString("unit"));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("Filtros utilizados para busca: " + filtros);

                    List<Livro> livrosEncontrados = livroDAO.consultarPorFiltros(filtros);

                    if (!livrosEncontrados.isEmpty()) {
                        Livro livroPrincipal = livrosEncontrados.get(0);
                        Double precoMenor = itemDAO.buscarMenorPrecoPorLivroId(livroPrincipal.getId());

                        // --- AJUSTE CRÍTICO: Sempre que um livro é encontrado e respondido, CRIE/ATUALIZE o contexto
                        JSONObject livroMencionadoContext = new JSONObject();
                        livroMencionadoContext.put("name", sessionPath + "/contexts/livro_mencionado_context");
                        livroMencionadoContext.put("lifespanCount", 3); // Define o lifespan

                        JSONObject paramsParaContexto = new JSONObject();
                        paramsParaContexto.put("titulo", livroPrincipal.getTitulo());
                        paramsParaContexto.put("idLivro", livroPrincipal.getId()); // Adicione o ID para uso futuro
                        livroMencionadoContext.put("parameters", paramsParaContexto);
                        outputContextsParaDialogflow.put(livroMencionadoContext);
                        System.out.println("DEBUG: Contexto 'livro_mencionado_context' criado/atualizado com título: " + livroPrincipal.getTitulo() + " (ID: " + livroPrincipal.getId() + ")");
                        // --- FIM DO AJUSTE CRÍTICO ---

                        if ("BuscarLivroPorTitulo".equals(intentDisplayName)) { // ComprarLivro já foi tratada separadamente
                            respostaBot = RespostasChatBot.gerarRespostaLivro("Livro", livroPrincipal.getTitulo(), precoMenor, livroPrincipal.getId());
                        } else {
                            StringBuilder sbResposta = new StringBuilder("Encontrei alguns livros que combinam com sua busca");

                            if (filtros.containsKey("categoria") && !filtros.get("categoria").toString().isEmpty()) {
                                sbResposta.append(" na categoria de ").append(filtros.get("categoria"));
                            }
                            if (filtros.containsKey("editora") && !filtros.get("editora").toString().isEmpty()) {
                                sbResposta.append(" da editora ").append(filtros.get("editora"));
                            }
                            if (filtros.containsKey("ano") && filtros.get("ano") != null) {
                                sbResposta.append(" do ano ").append(filtros.get("ano"));
                            }
                            if (filtros.containsKey("dimensao_tipo") && filtros.get("dimensao_tipo") != null && filtros.containsKey("valor_dimensao") && filtros.get("valor_dimensao") != null) {
                                sbResposta.append(" com ").append(filtros.get("dimensao_tipo")).append(" de até ").append(filtros.get("valor_dimensao"));
                                if (filtros.containsKey("unidade_dimensao")) sbResposta.append(filtros.get("unidade_dimensao"));
                            }
                            if (filtros.containsKey("preco_min") && !filtros.get("preco_min").toString().isEmpty()) {
                                sbResposta.append(" a partir de R$ ").append(String.format("%.2f", (Double) filtros.get("preco_min")));
                            }
                            if (filtros.containsKey("preco_max") && !filtros.get("preco_max").toString().isEmpty()) {
                                if (filtros.containsKey("preco_min")) sbResposta.append(" e ");
                                else sbResposta.append(" por ");
                                sbResposta.append("até R$ ").append(String.format("%.2f", (Double) filtros.get("preco_max")));
                            }

                            sbResposta.append(": Um exemplo é \"").append(livroPrincipal.getTitulo()).append("\". Confira: ")
                                    .append("http://localhost:8080/e-commerce_LES/servlet?action=consultarProduto&id_livro=").append(livroPrincipal.getId());
                            respostaBot = sbResposta.toString();
                        }

                    } else {
                        respostaBot = "Desculpe, não consegui encontrar livros com os filtros informados. Que tal tentar outra busca? 😊";
                    }
                    break;

                default:
                    respostaBot = "Desculpe, não entendi sua solicitação. Poderia tentar de outra forma?";
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro no FulfillmentServlet: " + e.getMessage());
            e.printStackTrace();
            respostaBot = "Ocorreu um erro interno. Por favor, tente novamente mais tarde.";
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("fulfillmentText", respostaBot);

        // Adiciona os contextos de saída ao JSON de resposta, se houver
        if (!outputContextsParaDialogflow.isEmpty()) {
            jsonResponse.put("outputContexts", outputContextsParaDialogflow);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }


    private void homePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, jakarta.servlet.ServletException {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.consultarTodos();

        ItemDAO itemDAO = new ItemDAO();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        for (Livro livro : livros) {
            Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
            if (menorPreco != null) {
                precoPorLivro.put(livro.getId(), menorPreco);
            }
        }

        request.setAttribute("livros", livros);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private void consultarLivrosComFiltros(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, jakarta.servlet.ServletException {
        Map<String, Object> filtros = new HashMap<>();

        String titulo = request.getParameter("titulo");
        String ano = request.getParameter("ano");
        String editora = request.getParameter("editora");

        if (titulo != null && !titulo.isEmpty()) {
            filtros.put("titulo", titulo);
        }
        if (ano != null && !ano.isEmpty()) {
            filtros.put("ano", Integer.parseInt(ano));
        }
        if (editora != null && !editora.isEmpty()) {
            filtros.put("editora", Integer.parseInt(editora));
        }

        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.consultarPorFiltros(filtros);

        // Buscar menor preço
        ItemDAO itemDAO = new ItemDAO();
        Map<Integer, Double> precoPorLivro = new HashMap<>();

        for (Livro livro : livros) {
            Double menorPreco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
            if (menorPreco != null) {
                precoPorLivro.put(livro.getId(), menorPreco);
            }
        }

        request.setAttribute("livros", livros);
        request.setAttribute("precos", precoPorLivro);
        request.getRequestDispatcher("chatBot.jsp").forward(request, response);
    }

    private String extrairStringDeParametro(Object param) {
        if (param == null) {
            return null;
        }
        if (param instanceof String) {
            return (String) param;
        }
        if (param instanceof Integer) { // Se for um número inteiro, converte para string
            return String.valueOf(param);
        }
        if (param instanceof Double) { // Se for um número decimal, converte para string
            return String.valueOf(param);
        }
        if (param instanceof JSONObject) {
            JSONObject jsonParam = (JSONObject) param;
            if (jsonParam.has("resolved")) { // Pode ser o valor resolvido de uma entidade
                return jsonParam.getString("resolved");
            } else if (jsonParam.has("original")) { // Ou o valor original
                return jsonParam.getString("original");
            } else if (jsonParam.has("amount")) { // Para unidades (ex: preço, dimensão)
                // Você pode querer retornar apenas o amount, ou concatenar com a unidade
                return String.valueOf(jsonParam.getDouble("amount"));
            }
            // Se for um JSON, mas não tem as chaves esperadas, retornar a representação String do JSON
            return jsonParam.toString();
        }
        if (param instanceof JSONArray) { // Para múltiplos valores de uma entidade (se permitido)
            JSONArray arr = (JSONArray) param;
            if (arr.length() > 0) {
                // Retorna o primeiro elemento como string. Você pode ajustar para concatenar todos.
                return extrairStringDeParametro(arr.get(0)); // Chama recursivamente para tratar o tipo do elemento
            } else {
                return null;
            }
        }
        return param.toString(); // Fallback genérico
    }
}
