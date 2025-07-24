package controle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.lang.Math;

import dao.LivroDAO;
import dao.ItemDAO;
import dao.EstoqueAtualDAO;
import dominio.Livro;
import dominio.Autor;
import dominio.Categoria;
import dominio.EstoqueAtual;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.GeminiApiClient;
import util.Conexao;

@WebServlet("/chatBot")
public class ChatBotServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.sendRedirect(request.getContextPath() + "/chatBot.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String userMessage = "";
        try {
            JSONObject requestBody = new JSONObject(request.getReader().readLine());
            userMessage = requestBody.getString("message");
        } catch (JSONException | IOException e) {
            System.err.println("Erro ao ler ou parsear a mensagem do usuário: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"response\": \"Desculpe, não consegui entender sua mensagem. Por favor, tente novamente.\"}");
            return;
        }

        if (userMessage == null || userMessage.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"response\": \"Mensagem vazia recebida. Por favor, digite algo.\"}");
            return;
        }

        System.out.println("[ChatBotServlet] Mensagem do usuário recebida: " + userMessage);

        LivroDAO livroDAO = new LivroDAO();
        ItemDAO itemDAO = new ItemDAO();
        EstoqueAtualDAO estoqueAtualDAO = new EstoqueAtualDAO();
        String botResponse = "";

        Connection conn = null;
        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                System.err.println("Erro: Conexão nula ao tentar conectar ao banco de dados no ChatBotServlet.");
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            String promptToGemini =
                    "Você é um assistente especializado em livros para a PageStation, o PageBot. " +
                            "Sua função principal é ajudar o usuário a encontrar livros no catálogo, aceitando múltiplos filtros. " +
                            "Sua comunicação deve ser **amigável, atenciosa e prestativa**, sempre com o objetivo de guiar o usuário na busca por livros. " +
                            "Sempre que possível, adicione uma frase inicial amigável ou um breve comentário antes ou depois do bloco JSON. " +
                            "Exemplos: 'Que interessante! Encontrei alguns filtros para a sua busca:', ou 'Certo! Com base na sua solicitação, analisei os seguintes critérios:'.\n\n" +
                            // Instrução mais forte para 'fora_de_escopo'
                            "Se a pergunta **NÃO** for sobre livros (ex: uma saudação como 'Olá', 'Oi', 'Bom dia', uma pergunta pessoal, algo não relacionado a um catálogo de livros) ou não for relevante para uma busca em catálogo, responda com o tipo \"fora_de_escopo\" e a frase EXATA: \"Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.\"\n\n" +
                            "Para perguntas sobre livros, siga estas instruções RIGOROSAMENTE para a **PARTE DO JSON** da sua resposta:\n" +
                            "1.  **Priorize a extração de filtros.** Identifique e extraia todos os filtros possíveis da solicitação do usuário. Os filtros são: " +
                            "    `titulo` (String), " +
                            "    `autor` (String), use o nome completo se possível; o sistema fará uma busca flexível que pode encontrar variações), " +
                            "    `nacionalidade_autor` (String, se o usuário mencionar nacionalidade de autor, extraia aqui. O sistema de busca tentará associar), " +
                            "    `ano_publicacao` (Integer, ex: 2007), " +
                            "    `preco_max` (Double), " +
                            "    `preco_min` (Double), " +
                            "    `categoria` (String), " +
                            "    `editora` (String), " +
                            "    `dimensao_altura` (Double), `dimensao_largura` (Double), `dimensao_peso` (Double), " +
                            "    `edicao` (String), " +
                            "    `numero_paginas` (Integer, para termos como 'poucas páginas' ou 'muitas páginas', infira um número apropriado, ex: 'poucas' -> 200, 'muitas' -> 500), " +
                            "    `isbn` (String, formato 10 ou 13 dígitos, sem hífens). \n" +
                            "    `tema_buscado` (String, se o usuário pedir algo como 'aprender a programar', 'desenvolver um site', 'história do Brasil', 'ficção científica', 'religião', 'infantil', 'presente para sobrinho', 'livro para neto', extraia o TEMA GERAL que ele está buscando. Se não for um filtro, mas um tema, use esta chave).**\n" +
                            "    Se um filtro ou tema for identificado, inclua-o no objeto JSON de filtros. Se um filtro não for especificado, OMITE-O. " +
                            "    **Para categorias:** Se a categoria solicitada pelo usuário não for uma categoria comum ou pode ser interpretada de forma mais ampla (ex: 'juvenil' para 'fantasia' ou 'ficção'), tente mapear para uma categoria mais abrangente ou deixe a categoria como veio se for uma categoria plausível. Se não houver uma categoria exata, não tem problema, o sistema de busca vai tentar encontrar livros que se *encaixem* na descrição.\n" +
                            "    Por exemplo, para 'livro de fantasia': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"categoria\":\"fantasia\"}}`\n" +
                            "    Para 'livro do autor X publicado em 2007': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"autor\":\"Autor X\", \"ano_publicacao\":2007}}`\n" +
                            "    Para 'livro para programar': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"tema_buscado\":\"programação\"}}`\n" +
                            "    Para 'livro para desenvolver um site': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"tema_buscado\":\"desenvolvimento web\"}}`\n" +
                            "    Para 'livro sobre igreja' ou 'livro religioso': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"tema_buscado\":\"religião\"}}`\n" +
                            "    Para 'livro para presentear meu sobrinho' ou 'livro para criança': `{\"tipo\":\"filtros_livro\", \"filtros\":{\"tema_buscado\":\"infantil\"}}`\n\n" +
                            "2.  **Formato de Resposta (JSON):** Sua resposta **DEVE** conter um objeto JSON. Existem dois tipos principais para este JSON:\n" +
                            "    a.  `{\"tipo\":\"filtros_livro\", \"filtros\":{...}, \"justificativa_gemini\":\"Breve justificativa para a recomendação/filtros.\"}`\n" +
                            "        - Use este tipo se a pergunta do usuário permitir extrair um ou mais filtros de livro. O objeto `filtros` deve conter as chaves de filtro relevantes. " +
                            "          Mesmo que não encontre filtros, mas a pergunta seja claramente sobre um livro, use este tipo com um objeto `filtros` vazio `{}`.\n" +
                            "    b.  `{\"tipo\":\"fora_de_escopo\", \"mensagem\":\"Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.\"}`\n" +
                            "        - Use este tipo se a pergunta não for sobre livros.\n\n" +
                            "    **É CRÍTICO:** Se a pergunta for sobre livros mas não houver filtros claros (ex: 'me recomende um livro bom'), ainda use o tipo `filtros_livro` com `\"filtros\":{}`. Não tente gerar uma recomendação genérica direta aqui, pois o sistema tentará o DB primeiro.\n\n" +
                            "Solicitação do usuário: '" + userMessage + "'";

            System.out.println("DEBUG: Enviando prompt ao Gemini: " + promptToGemini);

            String geminiResponseRaw = GeminiApiClient.gerarRespostaGemini(promptToGemini);
            System.out.println("DEBUG: Resposta RAW do Gemini: " + geminiResponseRaw);

            // --- PROCESSAMENTO DA RESPOSTA DO GEMINI ---
            String jsonPart = "";
            JSONObject geminiJsonResponse;
            String tipoResposta;
            JSONObject filtrosGemini = null;
            String justificativaGemini = "";
            String temaBuscado = null;

            try {
                int startIndex = geminiResponseRaw.indexOf('{');
                int endIndex = geminiResponseRaw.lastIndexOf('}');

                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                    jsonPart = geminiResponseRaw.substring(startIndex, endIndex + 1);
                    jsonPart = jsonPart.replace("```json", "").replace("```", "").trim();
                    System.out.println("DEBUG: JSON Extraído: " + jsonPart);
                } else {
                    String generalFallbackMessage = "Desculpe, o PageBot não conseguiu interpretar a resposta do Gemini. Tente reformular sua pergunta sobre livros.";
                    if (geminiResponseRaw.trim().equals("Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.")) {
                        botResponse = geminiResponseRaw.trim();
                    } else {
                        botResponse = generalFallbackMessage;
                    }
                    out.print(new JSONObject().put("response", botResponse).toString());
                    out.flush();
                    return;
                }

                geminiJsonResponse = new JSONObject(jsonPart);
                tipoResposta = geminiJsonResponse.optString("tipo");

                if ("fora_de_escopo".equals(tipoResposta)) {
                    botResponse = geminiJsonResponse.optString("mensagem", "Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.");

                } else if ("filtros_livro".equals(tipoResposta)) {
                    filtrosGemini = geminiJsonResponse.optJSONObject("filtros");
                    justificativaGemini = geminiJsonResponse.optString("justificativa_gemini", "Uma ótima escolha!");
                    temaBuscado = filtrosGemini != null ? filtrosGemini.optString("tema_buscado", null) : null;

                    Map<String, Object> filtrosParaDAO = new HashMap<>();

                    if (filtrosGemini != null) {
                        if (filtrosGemini.has("titulo")) filtrosParaDAO.put("titulo", filtrosGemini.getString("titulo"));
                        if (filtrosGemini.has("autor")) filtrosParaDAO.put("autor", filtrosGemini.getString("autor"));
                        if (filtrosGemini.has("nacionalidade_autor")) filtrosParaDAO.put("nacionalidade_autor", filtrosGemini.getString("nacionalidade_autor"));
                        if (filtrosGemini.has("ano_publicacao")) filtrosParaDAO.put("ano_publicacao", filtrosGemini.getInt("ano_publicacao"));
                        if (filtrosGemini.has("preco_max")) filtrosParaDAO.put("preco_max", filtrosGemini.getDouble("preco_max"));
                        if (filtrosGemini.has("preco_min")) filtrosParaDAO.put("preco_min", filtrosGemini.getDouble("preco_min"));
                        if (filtrosGemini.has("categoria")) filtrosParaDAO.put("categoria", filtrosGemini.getString("categoria"));
                        if (filtrosGemini.has("editora")) filtrosParaDAO.put("editora", filtrosGemini.getString("editora"));
                        if (filtrosGemini.has("dimensao_altura")) filtrosParaDAO.put("dimensao_altura", filtrosGemini.getDouble("dimensao_altura"));
                        if (filtrosGemini.has("dimensao_largura")) filtrosParaDAO.put("dimensao_largura", filtrosGemini.getDouble("dimensao_largura"));
                        if (filtrosGemini.has("dimensao_peso")) filtrosParaDAO.put("dimensao_peso", filtrosGemini.getDouble("dimensao_peso"));
                        if (filtrosGemini.has("edicao")) filtrosParaDAO.put("edicao", filtrosGemini.getString("edicao"));
                        if (filtrosGemini.has("numero_paginas")) filtrosParaDAO.put("numero_paginas", filtrosGemini.getInt("numero_paginas"));
                        if (filtrosGemini.has("isbn")) filtrosParaDAO.put("isbn", filtrosGemini.getString("isbn"));
                        if (filtrosGemini.has("tema_buscado")) temaBuscado = filtrosGemini.getString("tema_buscado");
                    }

                    List<Livro> livrosEncontrados = new ArrayList<>();


                    if (filtrosParaDAO != null && !filtrosParaDAO.isEmpty()) {
                        System.out.println("DEBUG: Tentando buscar livros no banco com filtros extraídos: " + filtrosParaDAO);
                        livrosEncontrados = livroDAO.consultarPorFiltros(filtrosParaDAO);
                    }

                    if (livrosEncontrados.isEmpty() && temaBuscado != null && !temaBuscado.trim().isEmpty()) {
                        System.out.println("DEBUG: Busca por filtros falhou. Tentando buscar por tema_buscado: '" + temaBuscado + "' no título/sinopse.");
                        livrosEncontrados = livroDAO.consultarPorKeywords(temaBuscado);
                    }

                    if (livrosEncontrados.isEmpty() && (filtrosParaDAO == null || filtrosParaDAO.isEmpty()) && (temaBuscado == null || temaBuscado.trim().isEmpty())) {
                        System.out.println("DEBUG: Nenhuma busca específica retornou resultados. Tentando buscar por palavras-chave da mensagem original do usuário: '" + userMessage + "'");
                        livrosEncontrados = livroDAO.consultarPorKeywords(userMessage);
                    }


                    List<Livro> livrosComEstoqueDisponivel = new ArrayList<>();
                    for (Livro livro : livrosEncontrados) {
                        EstoqueAtual estoque = estoqueAtualDAO.consultarPorLivroId(livro.getId(), conn);
                        if (estoque != null && estoque.getQuantidadeAtual() > 0) {
                            livrosComEstoqueDisponivel.add(livro);
                        }
                    }
                    livrosEncontrados = livrosComEstoqueDisponivel;

                    if (!livrosEncontrados.isEmpty()) {
                        StringBuilder sb = new StringBuilder("Que ótima escolha! Encontrei o(s) seguinte(s) livro(s) no nosso catálogo que correspondem aos seus critérios:\n");
                        for (Livro livro : livrosEncontrados) {

                            Double preco = itemDAO.buscarMenorPrecoPorLivroId(livro.getId());
                            String autoresStr = livro.getAutores().stream()
                                    .map(Autor::getNome)
                                    .collect(Collectors.joining(", "));

                            sb.append("- **").append(livro.getTitulo()).append("** de ").append(autoresStr);
                            if (livro.getAno() > 0) {
                                sb.append(" (Ano: ").append(livro.getAno()).append(")");
                            }
                            sb.append(".\n");
                            if (preco != null) {
                                sb.append("   Preço a partir de R$ ").append(String.format("%.2f", preco)).append(".\n");
                            }
                            sb.append("   Confira aqui: http://localhost:8080/e-commerce_LES/servlet?action=consultarProduto&id_livro=").append(livro.getId()).append("\n");
                        }
                        sb.append("\nEspero que encontre o que procura!");
                        botResponse = justificativaGemini + "\n" + sb.toString();

                    } else {

                        System.out.println("DEBUG: Nenhuma busca direta retornou resultados. Tentando busca semântica com Gemini.");

                        List<Livro> allActiveBooks = livroDAO.consultarTodos();

                        List<Livro> allActiveBooksWithStock = new ArrayList<>();
                        for (Livro livro : allActiveBooks) {
                            EstoqueAtual estoque = estoqueAtualDAO.consultarPorLivroId(livro.getId(), conn);
                            if (estoque != null && estoque.getQuantidadeAtual() > 0) {
                                allActiveBooksWithStock.add(livro);
                            }
                        }
                        allActiveBooks = allActiveBooksWithStock;

                        if (!allActiveBooks.isEmpty()) {
                            StringBuilder booksForGeminiPrompt = new StringBuilder();
                            for (Livro livro : allActiveBooks) {
                                booksForGeminiPrompt.append(livro.getId()).append(" | ")
                                        .append(livro.getTitulo()).append(" | ")
                                        .append(livro.getSinopse() != null ? livro.getSinopse().substring(0, Math.min(livro.getSinopse().length(), 250)) : "Sem sinopse").append(" | ")
                                        .append(livro.getCategorias().stream().map(Categoria::getNome).collect(Collectors.joining(", "))).append("\n");
                            }

                            String semanticPrompt = "O usuário busca um livro com o seguinte tema/requerimento: '" +
                                    (temaBuscado != null && !temaBuscado.trim().isEmpty() ? temaBuscado : userMessage) +
                                    "'.\n" +
                                    "**Instruções de Relevância Cruciais:**\n" +
                                    "1. Para buscas por 'estudar' ou 'aprender': Priorize livros que são explicitamente projetados para fins acadêmicos, técnicos ou de aprendizado de habilidades (como manuais, guias, livros didáticos). **Evite recomendar ficção, drama ou romance, a menos que a justificativa para estudo seja *extremamente* clara e explícita no conteúdo do livro.**\n" +
                                    "2. Para buscas por 'igreja', 'religião', 'espiritualidade': Priorize livros de temática religiosa, espiritual ou de valores morais/filosóficos. **Evite categoricamente recomendar ficção (como 'Harry Potter') ou outros gêneros não relacionados, a menos que a conexão seja *extremamente* óbvia e explicitamente mencionada no sinopse/título para o contexto religioso.**\n" +
                                    "3. Para buscas por 'infantil', 'criança', 'presente para sobrinho/filho': Priorize livros com temáticas e linguagem adequadas para crianças e jovens. **É altamente recomendável considerar ficção popular (como fantasia ou aventura) que seja amplamente reconhecida e consumida por este público (ex: 'Harry Potter') se o título/sinopse e as categorias do livro indicarem claramente que ele é apropriado e amplamente consumido por essa faixa etária. Não se restrinja apenas à categoria 'infantil' se o livro se encaixa no perfil do público alvo.** Evite recomendar livros com temas adultos ou que não sejam adequados para crianças. \n" +
                                    "4. A relevância deve ser **alta e específica** para a intenção do usuário. Se a conexão for fraca ou tangencial (i.e., não uma \"melhor correspondência\"), retorne uma lista vazia de recomendações.\n\n" +
                                    "Eu tenho a seguinte lista de livros no meu catálogo (ID | Título | Sinopse | Categorias). Por favor, identifique e liste os 3 livros MAIS RELEVANTES desta lista que melhor se encaixam na busca do usuário. Para cada livro, forneça o ID do livro, o título e uma breve justificativa de por que ele é relevante. Se nenhum livro for relevante, retorne uma lista vazia.\n\n" +
                                    "Formato esperado (JSON):\n" +
                                    "```json\n" +
                                    "{\n" +
                                    "  \"recomendacoes\": [\n" +
                                    "    {\"id_livro\": 123, \"titulo\":\"Título do Livro 1\", \"justificativa\":\"Breve justificativa de relevância.\"}\n" +
                                    "  ]\n" +
                                    "}\n" +
                                    "```\n" +
                                    "Lista de livros disponíveis:\n" +
                                    booksForGeminiPrompt.toString();

                            System.out.println("DEBUG: Enviando prompt semântico ao Gemini: " + semanticPrompt);
                            String geminiSemanticResponseRaw = GeminiApiClient.gerarRespostaGemini(semanticPrompt);
                            System.out.println("DEBUG: Resposta RAW do Gemini (semântica): " + geminiSemanticResponseRaw);

                            try {
                                int semanticStartIndex = geminiSemanticResponseRaw.indexOf('{');
                                int semanticEndIndex = geminiSemanticResponseRaw.lastIndexOf('}');
                                String semanticJsonPart = "";
                                if (semanticStartIndex != -1 && semanticEndIndex != -1 && semanticEndIndex > semanticStartIndex) {
                                    semanticJsonPart = geminiSemanticResponseRaw.substring(semanticStartIndex, semanticEndIndex + 1);
                                    semanticJsonPart = semanticJsonPart.replace("```json", "").replace("```", "").trim();
                                }

                                JSONObject semanticJsonResponse = new JSONObject(semanticJsonPart);
                                JSONArray recomendacoes = semanticJsonResponse.optJSONArray("recomendacoes");

                                if (recomendacoes != null && recomendacoes.length() > 0) {
                                    StringBuilder sb = new StringBuilder("Que interessante! Com base na sua busca, encontrei algumas recomendações que podem te interessar:\n");

                                    List<JSONObject> recomendacoesComEstoque = new ArrayList<>();
                                    for (int i = 0; i < recomendacoes.length(); i++) {
                                        JSONObject recomendacao = recomendacoes.getJSONObject(i);
                                        int livroId = recomendacao.getInt("id_livro");
                                        EstoqueAtual estoque = estoqueAtualDAO.consultarPorLivroId(livroId, conn);
                                        if (estoque != null && estoque.getQuantidadeAtual() > 0) {
                                            recomendacoesComEstoque.add(recomendacao);
                                        }
                                    }

                                    if (!recomendacoesComEstoque.isEmpty()) {
                                        for (JSONObject recomendacao : recomendacoesComEstoque) {
                                            int livroId = recomendacao.getInt("id_livro");
                                            String tituloRecomendado = recomendacao.getString("titulo");
                                            String justificativa = recomendacao.getString("justificativa");

                                            Livro livroRecomendado = livroDAO.consultarPorId(livroId);
                                            if (livroRecomendado != null) {

                                                Double preco = itemDAO.buscarMenorPrecoPorLivroId(livroRecomendado.getId());
                                                String autoresStr = livroRecomendado.getAutores().stream()
                                                        .map(Autor::getNome)
                                                        .collect(Collectors.joining(", "));

                                                sb.append("- **").append(tituloRecomendado).append("** de ").append(autoresStr);
                                                if (livroRecomendado.getAno() > 0) {
                                                    sb.append(" (Ano: ").append(livroRecomendado.getAno()).append(")");
                                                }
                                                sb.append(".\n");
                                                sb.append("   *Justificativa*: ").append(justificativa).append("\n");
                                                if (preco != null) {
                                                    sb.append("   Preço a partir de R$ ").append(String.format("%.2f", preco)).append(".\n");
                                                }
                                                sb.append("   Confira aqui: http://localhost:8080/e-commerce_LES/servlet?action=consultarProduto&id_livro=").append(livroRecomendado.getId()).append("\n");
                                            }
                                        }
                                        sb.append("\nEspero que alguma destas opções seja o que você procura!");
                                        botResponse = sb.toString();
                                    } else {

                                        System.out.println("DEBUG: As recomendações semânticas não tinham estoque. Iniciando bate-papo guiado por categorias (fallback).");
                                        List<String> categories = livroDAO.getAllCategoriesNames();
                                        String searchTopic = (temaBuscado != null && !temaBuscado.trim().isEmpty() ? temaBuscado : userMessage);

                                        String[] fallbackTemplates = {
                                                "Entendi! Pelo que percebi, você está buscando algo relacionado a '{0}'. Infelizmente, não encontrei um livro em estoque que se encaixe perfeitamente agora. Mas não desanime! Nossas categorias podem ter o que você precisa! Por exemplo: {1}. Que tal explorar por aí ou me dar mais detalhes sobre o que busca?",
                                                "Hmm, para a sua pesquisa sobre '{0}', não localizamos um título direto em estoque no momento. Mas o PageBot está aqui para ajudar! Você se interessaria por alguma de nossas categorias, como: {1}? Ou podemos tentar outra palavra-chave ou uma descrição mais detalhada?",
                                                "A sua solicitação sobre '{0}' é muito interessante! Infelizmente, não encontrei um resultado exato em estoque agora. Para te ajudar, posso sugerir algumas categorias populares, como: {1}. O que você acha de darmos uma olhada por lá ou me contar mais sobre o livro ideal?",
                                                "Não encontrei um livro em estoque que corresponda exatamente a '{0}' no momento, peço desculpas por isso. Mas que tal explorar outras possibilidades? Nossas categorias incluem: {1}. Estou à disposição para refinar sua busca ou tentar uma nova abordagem! 😊",
                                                "Para ' {0} ', o PageBot não localizou um título específico em estoque. Parece que precisamos de um pouco mais de informação! Você gostaria de explorar por gênero? Nossas categorias mais populares são: {1}. Ou se tiver algo mais em mente, me conte!"
                                        };

                                        String categorySuggestions = "";
                                        if (!categories.isEmpty()) {
                                            categorySuggestions = "**" + String.join("**, **", categories.subList(0, Math.min(categories.size(), 3))) + "**";
                                            if (categories.size() > 3) {
                                                categorySuggestions += " e várias outras!";
                                            }
                                        } else {
                                            categorySuggestions = "parece que não temos categorias disponíveis no momento.";
                                        }
                                        String chosenTemplate = fallbackTemplates[(int) (Math.random() * fallbackTemplates.length)];
                                        botResponse = chosenTemplate.replace("{0}", searchTopic).replace("{1}", categorySuggestions);
                                    }
                                } else {
                                    // Busca semântica também não retornou recomendações. Iniciar bate-papo guiado por categorias mais fluidamente.
                                    System.out.println("DEBUG: Busca semântica não retornou recomendações. Iniciando bate-papo guiado por categorias.");
                                    List<String> categories = livroDAO.getAllCategoriesNames();

                                    String searchTopic = (temaBuscado != null && !temaBuscado.trim().isEmpty() ? temaBuscado : userMessage);

                                    String[] fallbackTemplates = {
                                            "Entendi! Pelo que percebi, você está buscando algo relacionado a '{0}'. Infelizmente, não encontrei um livro específico que se encaixe perfeitamente agora. Mas não desanime! Nossas categorias podem ter o que você precisa! Por exemplo: {1}. Que tal explorar por aí ou me dar mais detalhes sobre o que busca?",
                                            "Hmm, para a sua pesquisa sobre '{0}', não localizamos um título direto em nosso catálogo no momento. Mas o PageBot está aqui para ajudar! Você se interessaria por alguma de nossas categorias, como: {1}? Ou podemos tentar outra palavra-chave ou uma descrição mais detalhada?",
                                            "A sua solicitação sobre '{0}' é muito interessante! Infelizmente, não encontrei um resultado exato agora. Para te ajudar, posso sugerir algumas categorias populares, como: {1}. O que você acha de darmos uma olhada por lá ou me contar mais sobre o livro ideal?",
                                            "Não encontrei um livro que corresponda exatamente a '{0}' no momento, peço desculpas por isso. Mas que tal explorar outras possibilidades? Nossas categorias incluem: {1}. Estou à disposição para refinar sua busca ou tentar uma nova abordagem! 😊",
                                            "Para ' {0} ', o PageBot não localizou um título específico. Parece que precisamos de um pouco mais de informação! Você gostaria de explorar por gênero? Nossas categorias mais populares são: {1}. Ou se tiver algo mais em mente, me conte!"
                                    };

                                    String categorySuggestions = "";
                                    if (!categories.isEmpty()) {
                                        categorySuggestions = "**" + String.join("**, **", categories.subList(0, Math.min(categories.size(), 3))) + "**";
                                        if (categories.size() > 3) {
                                            categorySuggestions += " e várias outras!";
                                        }
                                    } else {
                                        categorySuggestions = "parece que não temos categorias disponíveis no momento.";
                                    }

                                    String chosenTemplate = fallbackTemplates[(int) (Math.random() * fallbackTemplates.length)];
                                    botResponse = chosenTemplate.replace("{0}", searchTopic).replace("{1}", categorySuggestions);

                                }
                            } catch (JSONException semanticJsonE) {
                                System.err.println("Erro ao parsear JSON da resposta semântica do Gemini: " + semanticJsonE.getMessage());
                                System.err.println("Resposta bruta do Gemini (semântica) que causou o erro: " + geminiSemanticResponseRaw);
                                System.out.println("DEBUG: Erro de parsing JSON da resposta semântica. Iniciando bate-papo guiado por categorias.");
                                List<String> categories = livroDAO.getAllCategoriesNames();

                                String searchTopic = (temaBuscado != null && !temaBuscado.trim().isEmpty() ? temaBuscado : userMessage);

                                String[] errorFallbackTemplates = {
                                        "Ops! Encontrei uma pequena dificuldade ao processar a busca para '{0}'. Peço desculpas por isso. Que tal explorarmos algumas de nossas categorias? Temos opções como: {1}. Ou se preferir, pode me dar outra palavra-chave ou uma descrição mais detalhada? 😊",
                                        "Houve um probleminha técnico ao tentar entender melhor o que você procura para '{0}'. Mas não se preocupe, podemos tentar de outra forma! Que tal dar uma olhada nas categorias: {1}? Ou me diga o que mais posso fazer para ajudar!",
                                        "Para ' {0} ', tive uma falha momentânea. Mas ainda podemos encontrar algo! Nossas categorias incluem: {1}. Me diga se há algo mais que posso tentar!"
                                };
                                String chosenErrorTemplate = errorFallbackTemplates[(int) (Math.random() * errorFallbackTemplates.length)];

                                String categorySuggestions = "";
                                if (!categories.isEmpty()) {
                                    categorySuggestions = "**" + String.join("**, **", categories.subList(0, Math.min(categories.size(), 3))) + "**";
                                    if (categories.size() > 3) {
                                        categorySuggestions += " e várias outras!";
                                    }
                                } else {
                                    categorySuggestions = "parece que não temos categorias disponíveis no momento.";
                                }
                                botResponse = chosenErrorTemplate.replace("{0}", searchTopic).replace("{1}", categorySuggestions);
                            }
                        } else {
                            System.out.println("DEBUG: Nenhum livro ativo no catálogo com estoque. Iniciando bate-papo guiado por categorias.");
                            List<String> categories = livroDAO.getAllCategoriesNames();

                            StringBuilder suggestions = new StringBuilder("Parece que nosso catálogo está um pouco vazio no momento para sua busca. ");
                            suggestions.append("Que tal explorar algumas categorias que temos disponíveis? ");
                            if (!categories.isEmpty()) {
                                suggestions.append("Por exemplo: **");
                                suggestions.append(String.join("**, **", categories.subList(0, Math.min(categories.size(), 3))));
                                suggestions.append("**");
                                if (categories.size() > 3) {
                                    suggestions.append(" e muitas outras!");
                                }
                                suggestions.append("\n\nOu você gostaria de tentar outra palavra-chave? 😊");
                            } else {
                                suggestions.append("Atualmente não temos categorias para sugerir. Tente outra palavra-chave ou descreva o livro que procura de outra forma. 😊");
                            }
                            botResponse = suggestions.toString();
                        }
                    }
                } else {
                    botResponse = geminiJsonResponse.optString("mensagem", "Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.");
                    System.err.println("DEBUG: Tipo de resposta desconhecido ou fora de escopo do Gemini: " + tipoResposta + ". Resposta bruta: " + geminiResponseRaw);
                }

            } catch (JSONException jsonE) {
                System.err.println("Erro ao parsear JSON da resposta principal do Gemini: " + jsonE.getMessage());
                System.err.println("Resposta bruta do Gemini que causou o erro: " + geminiResponseRaw);

                String geminiPlainTextResponse = geminiResponseRaw.trim();
                if (geminiPlainTextResponse.equals("Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta.")) {
                    botResponse = geminiPlainTextResponse;
                } else {
                    botResponse = "Desculpe, o PageBot teve dificuldades para entender a resposta do Gemini. Por favor, tente reformular sua pergunta sobre livros.";
                }
            } catch (SQLException e) {
                System.err.println("Erro de banco de dados no ChatBotServlet: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                botResponse = "Desculpe, estou com problemas para consultar nosso catálogo de livros no momento. Por favor, tente novamente mais tarde.";
            } catch (IOException e) {
                System.err.println("Erro de comunicação com a API Gemini: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                botResponse = "Desculpe, estou com problemas para consultar o PageBot no momento. Por favor, tente novamente mais tarde.";
            } catch (Exception e) {
                System.err.println("Erro inesperado no ChatBotServlet: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                botResponse = "Ocorreu um erro interno. Por favor, tente novamente mais tarde.";
            } finally {

                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão do banco de dados no ChatBotServlet: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            JSONObject finalResponse = new JSONObject();
            finalResponse.put("response", botResponse);

            out.print(finalResponse.toString());
            out.flush();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}