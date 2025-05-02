package dao;

import dominio.BandeiraCartao;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BandeiraCartaoDAO {
    public List<BandeiraCartao> consultar() {
        List<BandeiraCartao> listaBandeiras= new ArrayList<>();
        String sql = "SELECT id_band,descricao_band FROM bandeiras_cartao";

        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement mysql = conn.prepareStatement(sql);
            ResultSet rs = mysql.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String descricao = rs.getString(2);
                BandeiraCartao tipo = BandeiraCartao.valueOf(descricao.toUpperCase());
                tipo.setId(id);
                tipo.setDescricao(descricao);
                listaBandeiras.add(tipo);
            }
            conn.close();

        }catch(Exception e) {
            System.out.println();
        }

        return listaBandeiras;
    }

    public int consultarIdBandeira(String bandeiraCartao, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // SQL para buscar o id da cidade pelo nome
            String sql = "SELECT id_band FROM bandeiras_cartao WHERE descricao_band = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, bandeiraCartao); // Substituindo pelo nome da cidade que você quer buscar
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_band"); // Retorna o id da cidade
            } else {
                return -1; // Se a cidade não for encontrada, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID da bandeira: " + e.getMessage());
            throw e; // Re-lança a exceção para ser tratada na camada superior
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}