package dao;

import dominio.TipoTelefone;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoTelefoneDAO {
    public List<TipoTelefone> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT nome_tip_tel FROM tipos_telefone";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            List<TipoTelefone> listaTiposTel = new ArrayList<>();


            while (rs.next()) {
                String tipoTel = rs.getString("nome_tip_tel");
                try {
                    TipoTelefone tipoTelefone = TipoTelefone.fromDescricao(tipoTel);
                    listaTiposTel.add(tipoTelefone);
                } catch (IllegalArgumentException e) {
                    //System.err.println("TipoEndereco não encontrado: " + tipoEnd);
                }
            }

            System.out.println("Número de tipos Telefone encontrados: " + listaTiposTel.size()); // Log de quantos estados foram encontrados
            return listaTiposTel;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar  tipos Telefone: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}