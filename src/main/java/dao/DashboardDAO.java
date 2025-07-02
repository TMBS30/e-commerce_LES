package dao;

import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DashboardDAO {
    private static final Logger LOGGER = Logger.getLogger(DashboardDAO.class.getName());

    public List<Map<String, Object>> getDailySalesMetrics(String startDate, String endDate) {
        List<Map<String, Object>> dailyMetrics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    DATE(data_criacao) AS report_date, ");
        sql.append("    SUM(valor_unitario_na_compra * quantidade) AS total_sales, ");
        sql.append("    COUNT(DISTINCT id_compra) AS total_orders, ");
        sql.append("    IFNULL(SUM(valor_unitario_na_compra * quantidade) / COUNT(DISTINCT id_compra), 0) AS avg_order_value ");
        sql.append("FROM ");
        sql.append("    itens_da_compra ");
        sql.append("WHERE 1=1 ");

        List<String> params = new ArrayList<>();

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND DATE(data_criacao) >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND DATE(data_criacao) <= ?");
            params.add(endDate);
        }

        sql.append("GROUP BY ");
        sql.append("    report_date ");
        sql.append("ORDER BY ");
        sql.append("    report_date ASC;");

        try (Connection conn = Conexao.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("date", rs.getString("report_date"));
                    dayData.put("totalSales", rs.getDouble("total_sales"));
                    dayData.put("orders", rs.getLong("total_orders"));
                    dayData.put("avgOrderValue", rs.getDouble("avg_order_value"));
                    dailyMetrics.add(dayData);
                }
            }
            LOGGER.info("Métricas de vendas diárias obtidas com sucesso. Total de dias: " + dailyMetrics.size() +
                    " (Filtro: StartDate=" + startDate + ", EndDate=" + endDate + ")");

        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao obter métricas de vendas diárias: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar métricas de vendas diárias.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao obter métricas de vendas diárias: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar métricas de vendas diárias.", e);
        }
        return dailyMetrics;
    }

    /**
     * Retorna as top N categorias mais vendidas por receita.
     * Pode ser filtrado por um período específico.
     *
     * @param limit O número máximo de categorias a serem retornadas (ex: 3 para Top 3).
     * @param startDate String no formato 'YYYY-MM-DD' para a data de início (opcional).
     * @param endDate String no formato 'YYYY-MM-DD' para a data de fim (opcional).
     * @return Uma lista de mapas, onde cada mapa representa uma categoria e sua receita total.
     */
    public List<Map<String, Object>> getTopCategoriesByRevenue(int limit, String startDate, String endDate) {
        List<Map<String, Object>> topCategories = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    c.nome_categoria AS category, ");
        sql.append("    SUM(ic.valor_unitario_na_compra * ic.quantidade) AS total_revenue ");
        sql.append("FROM ");
        sql.append("    itens_da_compra ic ");
        sql.append("JOIN ");
        sql.append("    livro l ON ic.id_livro = l.id_livro ");
        sql.append("JOIN ");
        sql.append("    livro_categoria lc ON l.id_livro = lc.id_livro ");
        sql.append("JOIN ");
        sql.append("    categoria c ON lc.id_categoria = c.id_categoria ");
        sql.append("WHERE 1=1 ");
        List<String> params = new ArrayList<>();

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND DATE(ic.data_criacao) >= ?");
            params.add(startDate);
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND DATE(ic.data_criacao) <= ?");
            params.add(endDate);
        }

        sql.append("GROUP BY ");
        sql.append("    c.nome_categoria ");
        sql.append("ORDER BY ");
        sql.append("    total_revenue DESC ");
        sql.append("LIMIT ?;");

        try (Connection conn = Conexao.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            for (String param : params) {
                stmt.setString(paramIndex++, param);
            }
            stmt.setInt(paramIndex, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> categoryData = new HashMap<>();
                    categoryData.put("category", rs.getString("category"));
                    categoryData.put("totalRevenue", rs.getDouble("total_revenue"));
                    topCategories.add(categoryData);
                }
            }
            LOGGER.info("Top " + limit + " categorias por receita obtidas com sucesso. Total de categorias: " + topCategories.size() +
                    " (Filtro: StartDate=" + startDate + ", EndDate=" + endDate + ")");

        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao obter top categorias por receita: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar top categorias por receita.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao obter top categorias por receita: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar top categorias por receita.", e);
        }
        return topCategories;
    }
}
