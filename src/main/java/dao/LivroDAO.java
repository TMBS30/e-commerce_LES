package dao;

import dominio.EntidadeDominio;
import dominio.Livro;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements IDAO{
    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        return null;
    }

    /*public Livro consultarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_livro, titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, status " +
                    "FROM livro WHERE id_livro = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAno(rs.getInt("ano"));
                livro.setEdicao(rs.getInt("edicao"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                livro.setSinopse(rs.getString("sinopse"));
                livro.setCodigoBarras(rs.getString("codigo_barras"));
                livro.setStatus(rs.getBoolean("status"));
                return livro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o livro por ID", e);
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

    public Livro consultarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_livro, titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, status, id_edit, id_dimensao, caminho_imagem  " +
                    "FROM livro WHERE id_livro = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAno(rs.getInt("ano"));
                livro.setEdicao(rs.getInt("edicao"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                livro.setSinopse(rs.getString("sinopse"));
                livro.setCodigoBarras(rs.getString("codigo_barras"));
                livro.setStatus(rs.getBoolean("status"));
                livro.setCaminhoImagem(rs.getString("caminho_imagem"));

                // Consultando autores
                AutorDAO autorDAO = new AutorDAO();
                livro.setAutores(autorDAO.consultarPorLivro(id, conn));

                // Consultando editora
                EditoraDAO editoraDAO = new EditoraDAO();
                livro.setEditora(editoraDAO.consultarPorLivro(rs.getInt("id_edit"), conn));

                // Consultando categorias
                CategoriaDAO categoriaDAO = new CategoriaDAO();
                livro.setCategorias(categoriaDAO.consultarPorLivro(id, conn));

                // Consultando dimensões
                DimensoesDAO dimensoesDAO = new DimensoesDAO();
                livro.setDimensoes(dimensoesDAO.consultarPorLivro(rs.getInt("id_dimensao"), conn));

                return livro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o livro por ID", e);
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
    }

    public List<Livro> consultarTodos() {
        List<Livro> livros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            String sql = "SELECT id_livro, titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, status, id_edit, id_dimensao, caminho_imagem  FROM livro";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAno(rs.getInt("ano"));
                livro.setEdicao(rs.getInt("edicao"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                livro.setSinopse(rs.getString("sinopse"));
                livro.setCodigoBarras(rs.getString("codigo_barras"));
                livro.setStatus(rs.getBoolean("status"));
                livro.setCaminhoImagem(rs.getString("caminho_imagem"));


                // Consultando autores
                AutorDAO autorDAO = new AutorDAO();
                livro.setAutores(autorDAO.consultarPorLivro(livro.getId(), conn));

                // Consultando editora
                EditoraDAO editoraDAO = new EditoraDAO();
                livro.setEditora(editoraDAO.consultarPorLivro(rs.getInt("id_edit"), conn));

                // Consultando categorias
                CategoriaDAO categoriaDAO = new CategoriaDAO();
                livro.setCategorias(categoriaDAO.consultarPorLivro(livro.getId(), conn));

                // Consultando dimensões
                DimensoesDAO dimensoesDAO = new DimensoesDAO();
                livro.setDimensoes(dimensoesDAO.consultarPorLivro(rs.getInt("id_dimensao"), conn));

                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar todos os livros", e);
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
        return livros;
    }


    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Estabelecendo a conexão
            conn = Conexao.createConnectionToMySQL(); // Verifique se esta linha retorna a conexão corretamente

            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return null;
    }
}

