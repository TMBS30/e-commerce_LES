package controle;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.ServiceAccountCredentials;

public class DialogflowRestClient {

    private static final String PROJECT_ID = "western-emitter-424712-v8";
    private static final String SESSION_ID = "300902";

    public static void main(String[] args) throws Exception {
        String question = "me indique um livro de drama";
        String response = detectIntentText(question);
        System.out.println("Resposta Dialogflow: " + response);
    }

    private static String detectIntentText(String text) throws Exception {
        // 1. Autenticar e gerar token
        String pathToCredentials = "C:\\Users\\thgmo\\OneDrive\\Documentos\\Faculdade\\LES\\western-emitter-424712-v8-1212979a62e6.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(pathToCredentials))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        String accessToken = token.getTokenValue();

        // 2. Montar URL
        String urlString = String.format(
                "https://dialogflow.googleapis.com/v2/projects/%s/agent/sessions/%s:detectIntent",
                PROJECT_ID, SESSION_ID);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 3. Configurar conexão POST e headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        // 4. Construir JSON da requisição
        String jsonRequest = "{"
                + "\"queryInput\": {"
                + "    \"text\": {"
                + "        \"text\": \"" + text + "\","
                + "        \"languageCode\": \"pt-BR\""
                + "    }"
                + "}"
                + "}";

        // 5. Enviar JSON no corpo da requisição
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonRequest.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 6. Ler resposta
        int code = conn.getResponseCode();
        InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        }
    }
}