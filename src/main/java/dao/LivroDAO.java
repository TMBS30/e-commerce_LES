package dao;

import dominio.Editora;
import dominio.EntidadeDominio;
import dominio.Livro;
import util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Livro> consultarPorFiltros(Map<String, Object> filtros) {
        List<Livro> livros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados");
            }

            StringBuilder sql = new StringBuilder("SELECT DISTINCT l.id_livro, l.titulo, l.ano, l.edicao, l.isbn, l.numero_paginas, l.sinopse, l.codigo_barras, l.status, l.id_edit, l.id_dimensao, l.caminho_imagem ");
            sql.append("FROM livro l ");
            sql.append("LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro ");
            sql.append("LEFT JOIN categoria c ON lc.id_categoria = c.id_categoria ");
            // --- AQUI ESTÁ A CORREÇÃO ---
            sql.append("LEFT JOIN dimensoes d ON l.id_dimensao = d.id_dimensao "); // Corrigido para 'dimensoes'
            // --- FIM DA CORREÇÃO ---
            sql.append("WHERE 1=1");

            List<Object> parametros = new ArrayList<>();

            // Filtro por título
            if (filtros.containsKey("titulo")) {
                sql.append(" AND l.titulo LIKE ?");
                parametros.add("%" + filtros.get("titulo") + "%");
            }

            // Filtro por ano
            if (filtros.containsKey("ano")) {
                sql.append(" AND l.ano = ?");
                parametros.add(filtros.get("ano"));
            }

            // Filtro por editora
            if (filtros.containsKey("editora")) {
                String nomeEditoraFiltro = (String) filtros.get("editora");
                EditoraDAO editoraDAO = new EditoraDAO();
                try {
                    Editora editoraEncontrada = editoraDAO.buscarPorNome(nomeEditoraFiltro, conn);
                    if (editoraEncontrada != null) {
                        sql.append(" AND l.id_edit = ?");
                        parametros.add(editoraEncontrada.getId());
                    } else {
                        System.out.println("Editora '" + nomeEditoraFiltro + "' não encontrada no banco de dados. Nenhum livro será filtrado por esta editora.");
                        sql.append(" AND 1=0");
                    }
                } catch (SQLException e) {
                    System.err.println("Erro SQL ao buscar ID da editora por nome: " + e.getMessage());
                    sql.append(" AND 1=0");
                }
            }

            // Filtro por categoria
            if (filtros.containsKey("categoria")) {
                sql.append(" AND l.id_livro IN (SELECT lc.id_livro FROM livro_categoria lc " +
                        "JOIN categoria cat ON lc.id_categoria = cat.id_categoria " +
                        "WHERE cat.nome_categoria LIKE ?)");
                parametros.add("%" + filtros.get("categoria") + "%");
            }

            // Filtro por Dimensão
            if (filtros.containsKey("dimensao_tipo") && filtros.containsKey("valor_dimensao")) {
                String dimensaoTipo = (String) filtros.get("dimensao_tipo");
                Double valorDimensao = ((Number) filtros.get("valor_dimensao")).doubleValue();

                switch (dimensaoTipo.toLowerCase()) {
                    case "peso":
                        sql.append(" AND d.peso <= ?");
                        parametros.add(valorDimensao);
                        break;
                    case "altura":
                        sql.append(" AND d.altura <= ?");
                        parametros.add(valorDimensao);
                        break;
                    case "largura":
                        sql.append(" AND d.largura <= ?");
                        parametros.add(valorDimensao);
                        break;
                    case "profundidade": // Adicionei profundidade, se quiser usar
                        sql.append(" AND d.profundidade <= ?");
                        parametros.add(valorDimensao);
                        break;
                }
            }

            // Filtro por Preço
            if (filtros.containsKey("preco_max")) {
                sql.append(" AND l.id_livro IN (SELECT i.id_livro FROM item i WHERE i.preco <= ? GROUP BY i.id_livro)");
                parametros.add(filtros.get("preco_max"));
            }
            if (filtros.containsKey("preco_min")) {
                sql.append(" AND l.id_livro IN (SELECT i.id_livro FROM item i WHERE i.preco >= ? GROUP BY i.id_livro)");
                parametros.add(filtros.get("preco_min"));
            }

            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

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

                AutorDAO autorDAO = new AutorDAO();
                livro.setAutores(autorDAO.consultarPorLivro(livro.getId(), conn));

                EditoraDAO editoraDAO = new EditoraDAO();
                livro.setEditora(editoraDAO.consultarPorLivro(rs.getInt("id_edit"), conn));

                CategoriaDAO categoriaDAO = new CategoriaDAO();
                livro.setCategorias(categoriaDAO.consultarPorLivro(livro.getId(), conn));

                DimensoesDAO dimensoesDAO = new DimensoesDAO();
                livro.setDimensoes(dimensoesDAO.consultarPorLivro(rs.getInt("id_dimensao"), conn));

                livros.add(livro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar livros com filtros: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar livros com filtros: " + e.getMessage(), e);
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

    public Livro buscarPorTitulo(String titulo) {
        Map<String, Object> filtros = new HashMap<>();
        filtros.put("titulo", titulo);
        List<Livro> livros = consultarPorFiltros(filtros);
        if (!livros.isEmpty()) {
            return livros.get(0); // Retorna o primeiro livro encontrado com esse título
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

