
/*import com.google.api.gax.rpc.ApiException; // Importação para a exceção da API
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerationConfig;
import com.google.genai.types.HarmCategory;
import com.google.genai.types.SafetySetting;
// IMPORTANTE: Adicione as classes Builder se elas existirem no seu SDK para essas classes
// Exemplo: import com.google.genai.types.GenerationConfig.Builder;
// import com.google.genai.types.SafetySetting.Builder;
// Importação para a classe de nível de bloqueio, se existir
import com.google.genai.types.SafetySetting.HarmBlockThreshold;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class GeminiApiClient {

    private static final Logger LOGGER = Logger.getLogger(GeminiApiClient.class.getName());
    private static final String MODEL_NAME = "gemini-1.5-flash";
    private static Client client;

    static {
        try {
            // Tenta o construtor padrão. Se ainda falhar, a classe Client pode ter um Builder
            // Ou o erro é na falta da variável de ambiente GOOGLE_API_KEY.
            client = new Client();
            LOGGER.info("Cliente Gemini API inicializado com sucesso.");
        } catch (IOException e) {
            LOGGER.severe("Erro ao inicializar o cliente Gemini: " + e.getMessage());
            throw new RuntimeException("Falha crítica ao inicializar o cliente Gemini API. Verifique a variável de ambiente GOOGLE_API_KEY.", e);
        }
    }

    public static String gerarRespostaGemini(String promptUsuario) throws IOException {
        String fullPrompt = """
            Você é um assistente especializado em livros para a PageStation. Sua função principal é ajudar o usuário a encontrar livros no catálogo, aceitando múltiplos filtros. Se a pergunta não for sobre livros, responda com a frase EXATA: "Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta."

            Para perguntas sobre livros, siga estas instruções RIGOROSAMENTE:
            1.   **Priorize a extração de filtros.** Identifique e extraia todos os filtros possíveis da solicitação do usuário. Os filtros são:     `titulo` (String), `autor` (String), `ano_publicacao` (Integer, ex: 2007), `preco_max` (Double), `categoria` (String),     `editora` (String), `dimensao_altura` (Double), `dimensao_largura` (Double), `dimensao_peso` (Double),     `edicao` (String), `numero_paginas` (Integer), `isbn` (String).
                 Se um filtro for identificado, inclua-o no objeto JSON de filtros. Se um filtro não for especificado, OMITE-O.     Por exemplo, para 'livro de fantasia': `{"tipo":"filtros_livro", "filtros":{"categoria":"fantasia"}}`
                 Para 'livro do autor X publicado em 2007': `{"tipo":"filtros_livro", "filtros":{"autor":"Autor X", "ano_publicacao":2007}}`

            2.   **Formato de Resposta (JSON):** Sua resposta **DEVE** ser um objeto JSON. Existem dois tipos principais:
                 a.   `{"tipo":"filtros_livro", "filtros":{...}, "justificativa_gemini":"Breve justificativa para a recomendação/filtros."}`
                      - Use este tipo se a pergunta do usuário permitir extrair um ou mais filtros de livro. O objeto `filtros` deve conter as chaves de filtro relevantes.          Mesmo que não encontre filtros, mas a pergunta seja claramente sobre um livro, use este tipo com um objeto `filtros` vazio `{}`.
                 b.   `{"tipo":"fora_de_escopo", "mensagem":"Desculpe, meu conhecimento é focado exclusivamente em livros e não posso ajudar com essa pergunta."}`

                 **É CRÍTICO:** Se a pergunta for sobre livros mas não houver filtros claros (ex: 'me recomende um livro bom'), ainda use o tipo `filtros_livro` com `{"filtros":{}}`. Não tente gerar uma recomendação genérica direta aqui, pois o sistema tentará o DB primeiro.

            Solicitação do usuário: '%s'
            """.formatted(promptUsuario);

        LOGGER.info("DEBUG GeminiApiClient: Enviando prompt ao Gemini: " + fullPrompt);

        long startTime = System.currentTimeMillis();

        try {
            // Tenta criar GenerationConfig via builder, se 'newBuilder' não for estático ou existir
            // podemos ter que usar uma abordagem diferente.
            GenerationConfig generationConfig = GenerationConfig.newBuilder()
                    .setTemperature(0.7f)
                    .build();

            // Tenta criar SafetySetting via builder, com os enums de HarmCategory e HarmBlockThreshold
            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(HarmBlockThreshold.BLOCK_NONE) // Use HarmBlockThreshold
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(HarmBlockThreshold.BLOCK_NONE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(HarmBlockThreshold.BLOCK_NONE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(HarmBlockThreshold.BLOCK_NONE)
                            .build()
            );

            // A chamada principal ao modelo
            GenerateContentResponse response = client.models.generateContent(
                    MODEL_NAME,
                    fullPrompt,
                    generationConfig,
                    safetySettings
            );

            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("Chamada ao Gemini finalizada em " + duration + " ms.");

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                String responseText = response.getCandidates().get(0).getContent().getParts().get(0).getText();
                LOGGER.info("Resposta bruta recebida do Gemini -> " + responseText);
                return responseText;
            } else {
                LOGGER.warning("Nenhuma resposta válida recebida do Gemini.");
                return "Desculpe, não consegui obter uma resposta do assistente no momento.";
            }
        } catch (ApiException e) {
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.severe("Erro específico da API Gemini (tempo: " + duration + " ms): " + e.getMessage());
            throw new IOException("Erro na comunicação com a API Gemini.", e);
        } catch (IOException e) {
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.severe("Erro de E/S ao usar a API Gemini (tempo: " + duration + " ms): " + e.getMessage());
            throw e;
        } catch (Exception e) { // Catch-all para qualquer outro problema inesperado
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.severe("Erro genérico ao usar a API Gemini (tempo: " + duration + " ms): " + e.getMessage());
            throw new IOException("Erro genérico na comunicação com a API Gemini.", e);
        }
    }
}*/

package util;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.io.IOException;
import java.util.logging.Logger;

// Início da classe GeminiApiClient
public class GeminiApiClient {

    private static final Logger LOGGER = Logger.getLogger(GeminiApiClient.class.getName());
    private static final String MODEL_NAME = "gemini-1.5-flash";
    private static Client client;

    static {
        client = new Client();
        LOGGER.info("Cliente Gemini API inicializado com sucesso.");
    }

    public static String gerarRespostaGemini(String promptUsuario) throws IOException {
        String fullPrompt = "Solicitação do usuário: '" + promptUsuario + "'";

        LOGGER.info("DEBUG GeminiApiClient: Enviando prompt ao Gemini: " + fullPrompt);

        long startTime = System.currentTimeMillis();

        try {
            GenerateContentResponse response = client.models.generateContent(
                    MODEL_NAME,
                    fullPrompt,
                    null
            );

            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("Chamada ao Gemini finalizada em " + duration + " ms.");

            if (response != null && response.text() != null && !response.text().isEmpty()) {
                String geminiTextResponse = response.text();
                LOGGER.info("Resposta bruta recebida do Gemini -> " + geminiTextResponse);

                String cleanedJsonResponse = geminiTextResponse.trim();

                if (cleanedJsonResponse.startsWith("```json")) {
                    cleanedJsonResponse = cleanedJsonResponse.substring("```json".length());
                }
                if (cleanedJsonResponse.endsWith("```")) {
                    cleanedJsonResponse = cleanedJsonResponse.substring(0, cleanedJsonResponse.length() - "```".length());
                }
                cleanedJsonResponse = cleanedJsonResponse.trim();
                return cleanedJsonResponse;
            } else {
                LOGGER.warning("Nenhuma resposta válida recebida do Gemini.");
                return "Desculpe, não consegui obter uma resposta do assistente no momento.";
            }
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.severe("Erro ao usar a API Gemini (tempo: " + duration + " ms): " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Erro na comunicação com a API Gemini.", e);
        }
    }
}
