package dao;

import dominio.Cidade;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {
    public List<Cidade> consultar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // Criando o PreparedStatement
            String sql = "SELECT nome_cidade FROM cidades";
            stmt = conn.prepareStatement(sql);

            // Executando a consulta
            rs = stmt.executeQuery();

            List<Cidade> listaCidades = new ArrayList<>();

            while (rs.next()) {
                Cidade cidade = new Cidade();
                //estado.setId(rs.getInt("id_estado"));
                cidade.setNome(rs.getString("nome_cidade"));
                listaCidades.add(cidade);
            }
            System.out.println("Número de cidades encontrados: " + listaCidades.size()); // Log de quantos estados foram encontrados
            return listaCidades;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar cidades: " + e.getMessage());
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
    public List<Cidade> obterCidadesPorEstado(String idEstado) {
        List<Cidade> cidades = new ArrayList<>();
        String sql = "SELECT id_cidade, nome_cidade FROM cidades WHERE id_estados = ?";

        try {
            Connection conn = Conexao.createConnectionToMySQL();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idEstado);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("id_cidade"));
                cidade.setNome(rs.getString("nome_cidade"));

                cidades.add(cidade);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // Trate adequadamente em um ambiente de produção
        }

        return cidades;
    }

    /*public List<Cidade> consultarIdCidades() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_cidade, nome_cidade FROM cidades";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Cidade> listaCidades = new ArrayList<>();
            while (rs.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("id_cidade"));
                cidade.setNome(rs.getString("nome_cidade"));
                listaCidades.add(cidade);
            }
            return listaCidades;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar cidades: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    }*/

    public int consultarIdCidades(String nomeCidade, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            // SQL para buscar o id da cidade pelo nome
            String sql = "SELECT id_cidade FROM cidades WHERE nome_cidade = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeCidade); // Substituindo pelo nome da cidade que você quer buscar
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_cidade"); // Retorna o id da cidade
            } else {
                return -1; // Se a cidade não for encontrada, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID da cidade: " + e.getMessage());
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