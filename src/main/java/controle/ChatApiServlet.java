package controle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import dao.*;
import dominio.*;
import util.*;

@WebServlet("/chatApi") // Esta será a URL que o frontend chamará
public class ChatApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userMessage = request.getParameter("mensagemUsuario");
        System.out.println("[ChatApiServlet] Mensagem do usuário recebida: " + userMessage);

        JSONObject jsonResponseForFrontend = new JSONObject();

        if (userMessage != null && !userMessage.trim().isEmpty()) {
            try {
                // *** MUDANÇA CRÍTICA AQUI: Obter o ID da sessão HTTP ***
                // Este ID será consistente para todas as requisições do mesmo usuário nesta sessão web.
                String dialogflowSessionId = request.getSession().getId();
                System.out.println("[ChatApiServlet] Usando SESSION_ID da sessão HTTP: " + dialogflowSessionId);

                // CHAMA O DIALOGFLOWUTIL PARA ENVIAR A MENSAGEM AO DIALOGFLOW, PASSANDO O SESSION_ID
                String dialogflowApiResponseJson = DialogflowUtil.detectIntentText(userMessage, dialogflowSessionId);
                System.out.println("[ChatApiServlet] Resposta bruta da API Dialogflow: " + dialogflowApiResponseJson);

                JSONObject dialogflowResponse = new JSONObject(dialogflowApiResponseJson);
                JSONObject queryResult = dialogflowResponse.getJSONObject("queryResult");

                String fulfillmentText = "";

                if (dialogflowResponse.has("webhookStatus") && !dialogflowResponse.isNull("webhookStatus")) {
                    if (queryResult.has("fulfillmentText") && !queryResult.isNull("fulfillmentText")) {
                        fulfillmentText = queryResult.getString("fulfillmentText");
                    } else {
                        System.err.println("Webhook acionado mas não retornou fulfillmentText.");
                        fulfillmentText = "Desculpe, o serviço está com problemas. Tente novamente mais tarde.";
                    }
                } else {
                    if (queryResult.has("fulfillmentText") && !queryResult.isNull("fulfillmentText")) {
                        fulfillmentText = queryResult.getString("fulfillmentText");
                    } else if (queryResult.has("fulfillmentMessages")) {
                        JSONArray messages = queryResult.getJSONArray("fulfillmentMessages");
                        if (messages.length() > 0) {
                            JSONObject firstMessage = messages.getJSONObject(0);
                            if (firstMessage.has("text")) {
                                JSONObject textObject = firstMessage.getJSONObject("text");
                                if (textObject.has("text") && textObject.getJSONArray("text").length() > 0) {
                                    fulfillmentText = textObject.getJSONArray("text").getString(0);
                                }
                            }
                        }
                    } else {
                        System.err.println("Dialogflow não retornou fulfillmentText nem fulfillmentMessages.");
                        fulfillmentText = "Desculpe, não consegui entender sua solicitação.";
                    }
                }

                jsonResponseForFrontend.put("botMessage", fulfillmentText);

            } catch (Exception e) {
                System.err.println("[ChatApiServlet] ERRO ao comunicar com Dialogflow API ou processar resposta: " + e.getMessage());
                e.printStackTrace();
                jsonResponseForFrontend.put("botMessage", "Desculpe, ocorreu um erro interno ao processar sua mensagem. Tente novamente mais tarde.");
            }
        } else {
            jsonResponseForFrontend.put("botMessage", "Por favor, digite algo para eu poder te ajudar.");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResponseForFrontend.toString());
        out.flush();
    }
}