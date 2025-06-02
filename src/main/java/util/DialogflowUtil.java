package util;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class DialogflowUtil {
    private static final String PROJECT_ID = "western-emitter-424712-v8";
    private static final String PATH_TO_CREDENTIALS = "C:\\Users\\thgmo\\OneDrive\\Documentos\\Faculdade\\LES\\Key_Cloud\\western-emitter-424712-v8-18259b09a327.json";

    // *** MUDANÇA CRÍTICA AQUI: Agora aceita sessionId como parâmetro ***
    public static String detectIntentText(String text, String sessionId) throws Exception {
        // O sessionId agora vem do ChatApiServlet (da sessão HTTP)
        System.out.println("[DialogflowUtil] Usando SESSION_ID: " + sessionId);

        System.out.println("[DialogflowUtil] Iniciando autenticação e geração do token...");
        String accessToken = null;
        try {
            GoogleCredentials credentials = ServiceAccountCredentials
                    .fromStream(new FileInputStream(PATH_TO_CREDENTIALS))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            credentials.refresh();
            AccessToken token = credentials.getAccessToken();

            if (token == null || token.getTokenValue() == null || token.getTokenValue().isEmpty()) {
                throw new IOException("Token de acesso nulo ou vazio após a autenticação.");
            }
            accessToken = token.getTokenValue();

            System.out.println("[DialogflowUtil] Token obtido com sucesso.");
            System.out.println("[DialogflowUtil] Access Token (primeiros 10 chars): " + accessToken.substring(0, Math.min(accessToken.length(), 10)) + "...");
        } catch (IOException e) {
            System.err.println("[DialogflowUtil] ERRO na autenticação/leitura da chave: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha na autenticação com Google Cloud: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("[DialogflowUtil] ERRO inesperado na autenticação: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erro durante a autenticação: " + e.getMessage(), e);
        }

        if (accessToken == null || accessToken.isEmpty()) {
            throw new Exception("Access Token não pôde ser obtido. Não é possível prosseguir com a requisição.");
        }

        String urlString = String.format(
                "https://dialogflow.googleapis.com/v2/projects/%s/agent/sessions/%s:detectIntent",
                PROJECT_ID, sessionId); // <<< Usando o sessionId passado como parâmetro
        System.out.println("[DialogflowUtil] URL da requisição: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        System.out.println("[DialogflowUtil] Configurando conexão HTTP...");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JSONObject textObject = new JSONObject();
        textObject.put("text", text);
        textObject.put("languageCode", "pt-BR");

        JSONObject queryInput = new JSONObject();
        queryInput.put("text", textObject);

        JSONObject queryParams = new JSONObject();
        queryParams.put("timeZone", "America/Sao_Paulo");

        JSONObject jsonRequestObj = new JSONObject();
        jsonRequestObj.put("queryInput", queryInput);
        jsonRequestObj.put("queryParams", queryParams);

        String jsonRequest = jsonRequestObj.toString();
        System.out.println("[DialogflowUtil] JSON da requisição (gerado por JSONObject): " + jsonRequest);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonRequest.getBytes("utf-8");
            os.write(input, 0, input.length);
            System.out.println("[DialogflowUtil] JSON enviado no corpo da requisição.");
        } catch (IOException e) {
            System.err.println("[DialogflowUtil] ERRO ao enviar JSON no corpo da requisição: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao enviar JSON para a API: " + e.getMessage(), e);
        }

        int code = -1;
        try {
            code = conn.getResponseCode();
            System.out.println("[DialogflowUtil] Código de resposta HTTP: " + code);
        } catch (IOException e) {
            System.err.println("[DialogflowUtil] ERRO ao obter código de resposta HTTP: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao conectar com a API Dialogflow: " + e.getMessage(), e);
        }

        InputStream is = null;
        try {
            is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
        } catch (IOException e) {
            System.err.println("[DialogflowUtil] ERRO ao obter stream de resposta (mesmo para erro): " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao obter stream de resposta da API: " + e.getMessage(), e);
        }

        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseContent.append(responseLine.trim());
            }
            System.out.println("[DialogflowUtil] Resposta RAW recebida da API Dialogflow: " + responseContent.toString());
        } catch (IOException e) {
            System.err.println("[DialogflowUtil] ERRO ao ler resposta da API: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao ler resposta da API Dialogflow: " + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("[DialogflowUtil] ERRO ao fechar InputStream: " + e.getMessage());
                }
            }
        }

        if (code != 200) {
            System.err.println("[DialogflowUtil] Resposta de erro da API: " + responseContent.toString());
            if (responseContent.toString().contains("PERMISSION_DENIED") || responseContent.toString().contains("Permission denied")) {
                throw new Exception("Erro de permissão com a API Dialogflow. Verifique as credenciais da conta de serviço e as permissões no GCP.");
            }
            throw new Exception("Falha na chamada da API Dialogflow com código: " + code + ". Resposta: " + responseContent.toString());
        }

        return responseContent.toString();
    }

    /**
     * Método utilitário para extrair string de um parâmetro do Dialogflow, que pode vir como String ou JSONObject.
     */
    public static String extrairStringDeParametro(Object parametro) {
        if (parametro == null) {
            return null;
        }

        if (parametro instanceof String) {
            return (String) parametro;
        } else if (parametro instanceof JSONObject) {
            JSONObject paramJson = (JSONObject) parametro;
            // Adapte conforme os nomes reais dos campos que você espera dentro do JSONObject
            // Por exemplo, se for uma entidade @sys.book-title, pode vir como {"original": "Harry Potter", "resolved": "Harry Potter"}
            // Se for um item de lista, pode ter "name" ou "value"
            if (paramJson.has("name")) { // Ex: para entidades mapeadas para "name"
                return paramJson.getString("name");
            } else if (paramJson.has("id")) { // Ex: para IDs que podem vir como JSONObject
                return paramJson.getString("id");
            } else if (paramJson.has("value")) { // Ex: para entidades customizadas com campo "value"
                return paramJson.getString("value");
            } else if (paramJson.has("title")) { // Adicionei para caso o titulo esteja nesse formato
                return paramJson.getString("title");
            } else if (paramJson.has("resolved")) { // Ex: para @sys.book-title
                return paramJson.getString("resolved");
            } else {
                // Se nenhum campo conhecido for encontrado, retorne a representação JSON
                return paramJson.toString();
            }
        }
        return null;
    }
}
