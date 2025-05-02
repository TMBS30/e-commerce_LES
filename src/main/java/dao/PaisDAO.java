package dao;

import dominio.Pais;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PaisDAO {
    public List<Pais> consultar() {
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
            String sql = "SELECT nome_pais FROM paises";
            stmt = conn.prepareStatement(sql);

            // Executando a consulta
            rs = stmt.executeQuery();

            List<Pais> listaPaises = new ArrayList<>();

            while (rs.next()) {
                Pais pais = new Pais();
                pais.setNome(rs.getString("nome_pais"));
                listaPaises.add(pais);
            }

            System.out.println("Número de paises encontrados: " + listaPaises.size()); // Log de quantos estados foram encontrados
            return listaPaises;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar paises: " + e.getMessage());
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

    public int consultarIdPaises(String nomePais, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_pais FROM paises WHERE nome_pais = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomePais); // Passa o nome do país para o parâmetro da consulta
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_pais"); // Retorna o ID do país encontrado
            } else {
                return -1; // Caso o país não seja encontrado, retorna -1
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar ID Pais: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Retorna -1 caso não encontre o país ou haja erro
    }

    /*public List<Pais> consultarIdPaises() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_pais, nome_pais FROM paises";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Pais> listaPaises = new ArrayList<>();
            while (rs.next()) {
                Pais pais = new Pais();
                pais.setId(rs.getInt("id_pais"));
                pais.setNome(rs.getString("nome_pais"));
                listaPaises.add(pais);
            }
            return listaPaises;
        } catch (SQLException e) {
            System.err.println("Erro ao consultar paises: " + e.getMessage());
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

    /*public Pais buscarPorNome(String nomePais) {
        // Exemplo de implementação da consulta ao banco
        String sql = "SELECT * FROM pais WHERE nome = ?";
        // Código de acesso ao banco, execução da consulta e mapeamento do resultado para um objeto Pais
        Pais pais = new Pais();
        // Supondo que o resultado tenha sido mapeado, setamos os valores
        pais.setId(rs.getInt("id"));
        pais.setNome(rs.getString("nome"));
        return pais;
    }*/

}