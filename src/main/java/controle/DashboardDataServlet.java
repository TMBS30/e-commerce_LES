package controle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import dao.DashboardDAO;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/dashboard-data")
public class DashboardDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DashboardDataServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        LOGGER.info("DashboardDataServlet: Acesso recebido para buscar dados do dashboard (autenticação ignorada para projeto).");
        LOGGER.info("DashboardDataServlet: Request URI: " + request.getRequestURI());
        LOGGER.info("DashboardDataServlet: Request URL: " + request.getRequestURL());
        LOGGER.info("DashboardDataServlet: Referer: " + request.getHeader("referer"));

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        LOGGER.info("DashboardDataServlet: Parâmetros de filtro de data recebidos: StartDate=" + startDate + ", EndDate=" + endDate);

        DashboardDAO dashboardDAO = new DashboardDAO();
        JSONObject responseData = new JSONObject();

        try {
            List<Map<String, Object>> dailySales = dashboardDAO.getDailySalesMetrics(startDate, endDate);
            responseData.put("dailySales", new JSONArray(dailySales));
            LOGGER.info("DashboardDataServlet: Métricas diárias de vendas obtidas do DAO. Total de dias: " + dailySales.size());

            // Busca as top 3 categorias mais vendidas, também passando os filtros de data.
            List<Map<String, Object>> topCategories = dashboardDAO.getTopCategoriesByRevenue(3, startDate, endDate);
            responseData.put("topCategories", new JSONArray(topCategories));
            LOGGER.info("DashboardDataServlet: Top categorias por receita obtidas do DAO. Total de categorias: " + topCategories.size());

            response.setStatus(HttpServletResponse.SC_OK); // Responde com status HTTP 200 (OK)
            out.print(responseData.toString()); // Envia o JSON como resposta

        } catch (RuntimeException e) { // Captura exceções lançadas pelo DAO (erros no DB, etc.)
            LOGGER.severe("DashboardDataServlet: Erro ao buscar dados do dashboard: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração detalhada
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Responde com status HTTP 500 (Erro Interno do Servidor)
            out.print("{\"error\": \"Erro interno ao buscar dados do dashboard.\"}"); // Envia mensagem de erro em JSON
        } finally {
            out.flush(); // Garante que todos os dados sejam enviados antes de fechar o fluxo de saída
        }
    }
}