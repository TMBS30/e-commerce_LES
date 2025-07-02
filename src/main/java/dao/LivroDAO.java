package dao;

import dominio.Editora;
import dominio.EntidadeDominio;
import dominio.Livro;
import util.Conexao;
import dominio.*;
import dao.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LivroDAO implements IDAO {

    private static final Logger LOGGER = Logger.getLogger(LivroDAO.class.getName()); // Declaração única do Logger

    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        // Implementação do método salvar (se necessário para seu projeto)
        return null;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        // Implementação do método alterar (se necessário para seu projeto)
        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return List.of();
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        // Implementação do método excluir (se necessário para seu projeto)
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        // Implementação do método selecionar (se necessário para seu projeto)
        return null;
    }

    /**
     * Consulta um livro específico pelo seu ID.
     * Recupera autores, editora, categorias e dimensões através de DAOs separados.
     * @param id O ID do livro a ser consultado.
     * @return O objeto Livro correspondente, ou null se não for encontrado.
     */
    public Livro consultarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            // Consulta principal do livro
            String sql = "SELECT id_livro, titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, status, id_edit, id_dimensao, caminho_imagem " +
                    "FROM livro WHERE id_livro = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            Livro livro = null;
            if (rs.next()) {
                livro = new Livro();
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

                // Consultando e setando autores, editora, categorias e dimensões via DAOs separados
                // Estes DAOs devem ter métodos como 'consultarPorLivro' ou 'consultarPorId'
                livro.setAutores(new AutorDAO().consultarPorLivro(id, conn));
                livro.setEditora(new EditoraDAO().consultarPorLivro(rs.getInt("id_edit"), conn));
                livro.setCategorias(new CategoriaDAO().consultarPorLivro(id, conn));
                livro.setDimensoes(new DimensoesDAO().consultarPorLivro(rs.getInt("id_dimensao"), conn));
            }
            return livro;
        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao consultar o livro por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar o livro por ID.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao consultar o livro por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar o livro por ID.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em consultarPorId: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Retorna uma lista de todos os livros no catálogo.
     * Recupera autores, editora, categorias e dimensões através de DAOs separados.
     * @return Uma lista de objetos Livro.
     */
    public List<Livro> consultarTodos() {
        List<Livro> livros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            String sql = "SELECT id_livro, titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, status, id_edit, id_dimensao, caminho_imagem FROM livro";
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

                // Consultando e setando autores, editora, categorias e dimensões via DAOs separados
                livro.setAutores(new AutorDAO().consultarPorLivro(livro.getId(), conn));
                livro.setEditora(new EditoraDAO().consultarPorLivro(rs.getInt("id_edit"), conn));
                livro.setCategorias(new CategoriaDAO().consultarPorLivro(livro.getId(), conn));
                livro.setDimensoes(new DimensoesDAO().consultarPorLivro(rs.getInt("id_dimensao"), conn));

                livros.add(livro);
            }
        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao consultar todos os livros: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar todos os livros.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao consultar todos os livros: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar todos os livros.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em consultarTodos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return livros;
    }

    /**
     * Consulta livros no banco de dados com base em um mapa de filtros fornecido.
     * Este método é utilizado para buscas específicas com múltiplos critérios.
     * Otimizado para preencher Autor, Categoria, Editora e Dimensões
     * diretamente do ResultSet principal, utilizando seus construtores parametrizados.
     *
     * @param filtros Um mapa contendo os nomes dos filtros (chaves) e seus respectivos valores.
     * Exemplos de chaves: "titulo", "autor", "categoria", "preco_max", "preco_min", etc.
     * @return Uma lista de livros que correspondem a todos os filtros aplicados.
     */
    public List<Livro> consultarPorFiltros(Map<String, Object> filtros) {
        List<Livro> livros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            // Seleciona todos os campos necessários para montar o objeto Livro e seus objetos relacionados
            // Inclui campos de Editora, Dimensões, Autor e Categoria para popular diretamente do ResultSet
            StringBuilder sql = new StringBuilder("SELECT DISTINCT l.id_livro, l.titulo, l.ano, l.edicao, l.isbn, l.numero_paginas, l.sinopse, l.codigo_barras, l.status, l.caminho_imagem, ");
            // Usando 'e.nome_edit' conforme o feedback do usuário e o esquema da tabela
            sql.append("e.id_edit, e.nome_edit, ");
            sql.append("d.id_dimensao, d.altura, d.largura, d.peso, d.profundidade, ");
            // Usando 'a.id' e 'a.nome' para Autor, baseado na classe de domínio Autor e no esquema da tabela
            sql.append("a.id, a.nome, a.nacionalidade, ");
            sql.append("c.id_categoria, c.nome_categoria, c.descricao_categoria ");
            sql.append("FROM livro l ");
            sql.append("LEFT JOIN editora e ON l.id_edit = e.id_edit ");
            sql.append("LEFT JOIN dimensoes d ON l.id_dimensao = d.id_dimensao ");
            sql.append("LEFT JOIN livro_autor la ON l.id_livro = la.id_livro ");
            sql.append("LEFT JOIN autor a ON la.id_autor = a.id "); // Usando 'a.id' no JOIN conforme o esquema
            sql.append("LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro ");
            sql.append("LEFT JOIN categoria c ON lc.id_categoria = c.id_categoria ");

            sql.append("WHERE l.status = 1"); // Apenas livros ativos
            List<Object> parametros = new ArrayList<>();

            // Adição de filtros dinamicamente
            if (filtros.containsKey("titulo") && filtros.get("titulo") != null) {
                sql.append(" AND l.titulo LIKE ?");
                parametros.add("%" + filtros.get("titulo") + "%");
            }
            if (filtros.containsKey("autor") && filtros.get("autor") != null) {
                String nomeAutor = (String) filtros.get("autor");
                sql.append(" AND a.nome LIKE ?"); // Usando 'a.nome' para o filtro conforme o esquema
                parametros.add("%" + nomeAutor + "%");
            }
            if (filtros.containsKey("nacionalidade_autor") && filtros.get("nacionalidade_autor") != null) {
                String nacionalidade = (String) filtros.get("nacionalidade_autor");
                sql.append(" AND a.nacionalidade LIKE ?");
                parametros.add("%" + nacionalidade + "%");
            }
            if (filtros.containsKey("ano_publicacao") && filtros.get("ano_publicacao") != null) {
                sql.append(" AND l.ano = ?");
                parametros.add(filtros.get("ano_publicacao"));
            }
            if (filtros.containsKey("edicao") && filtros.get("edicao") != null) {
                sql.append(" AND l.edicao = ?");
                parametros.add(filtros.get("edicao"));
            }
            if (filtros.containsKey("isbn") && filtros.get("isbn") != null) {
                String isbn = (String) filtros.get("isbn");
                sql.append(" AND l.isbn = ?");
                parametros.add(isbn.replace("-", "").trim());
            }
            if (filtros.containsKey("numero_paginas") && filtros.get("numero_paginas") != null) {
                Integer numeroPaginas = (Integer) filtros.get("numero_paginas");
                if (numeroPaginas != null) {
                    if (numeroPaginas <= 200) { // Inferência para "poucas páginas"
                        sql.append(" AND l.numero_paginas <= ?");
                    } else if (numeroPaginas >= 500) { // Inferência para "muitas páginas"
                        sql.append(" AND l.numero_paginas >= ?");
                    } else { // Para valores específicos ou intermediários
                        sql.append(" AND l.numero_paginas = ?");
                    }
                    parametros.add(numeroPaginas);
                }
            }
            if (filtros.containsKey("editora") && filtros.get("editora") != null) {
                sql.append(" AND e.nome_edit LIKE ?"); // Usando 'e.nome_edit'
                parametros.add("%" + filtros.get("editora") + "%");
            }
            if (filtros.containsKey("categoria") && filtros.get("categoria") != null) {
                sql.append(" AND c.nome_categoria LIKE ?");
                parametros.add("%" + filtros.get("categoria") + "%");
            }
            if (filtros.containsKey("dimensao_peso") && filtros.get("dimensao_peso") != null) {
                sql.append(" AND d.peso <= ?");
                parametros.add(((Number) filtros.get("dimensao_peso")).doubleValue());
            }
            if (filtros.containsKey("dimensao_altura") && filtros.get("dimensao_altura") != null) {
                sql.append(" AND d.altura <= ?");
                parametros.add(((Number) filtros.get("dimensao_altura")).doubleValue());
            }
            if (filtros.containsKey("dimensao_largura") && filtros.get("dimensao_largura") != null) {
                sql.append(" AND d.largura <= ?");
                parametros.add(((Number) filtros.get("dimensao_largura")).doubleValue());
            }
            if (filtros.containsKey("dimensao_profundidade") && filtros.get("dimensao_profundidade") != null) {
                sql.append(" AND d.profundidade <= ?");
                parametros.add(((Number) filtros.get("dimensao_profundidade")).doubleValue());
            }
            if (filtros.containsKey("preco_max") && filtros.get("preco_max") != null) {
                sql.append(" AND EXISTS (SELECT 1 FROM item i WHERE i.id_livro = l.id_livro AND i.status_item = 'Ativo' GROUP BY i.id_livro HAVING MIN(i.valorVenda) <= ?)");
                parametros.add(((Number) filtros.get("preco_max")).doubleValue());
            }
            if (filtros.containsKey("preco_min") && filtros.get("preco_min") != null) {
                sql.append(" AND EXISTS (SELECT 1 FROM item i WHERE i.id_livro = l.id_livro AND i.status_item = 'Ativo' GROUP BY i.id_livro HAVING MIN(i.valorVenda) >= ?)");
                parametros.add(((Number) filtros.get("preco_min")).doubleValue());
            }

            sql.append(" ORDER BY l.titulo ASC"); // Ordena para resultados consistentes

            LOGGER.info("SQL gerada para consultarPorFiltros: " + sql.toString());
            LOGGER.info("Parâmetros da consulta: " + parametros.toString());

            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            rs = stmt.executeQuery();

            Map<Integer, Livro> livroMap = new HashMap<>(); // Usar mapa para agregar autores/categorias/dimensoes/editora

            while (rs.next()) {
                int livroId = rs.getInt("id_livro");
                Livro livro = livroMap.get(livroId);

                if (livro == null) {
                    livro = new Livro();
                    livro.setId(livroId);
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAno(rs.getInt("ano"));
                    livro.setEdicao(rs.getInt("edicao"));
                    livro.setIsbn(rs.getString("isbn"));
                    livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                    livro.setSinopse(rs.getString("sinopse"));
                    livro.setCodigoBarras(rs.getString("codigo_barras"));
                    livro.setStatus(rs.getBoolean("status"));
                    livro.setCaminhoImagem(rs.getString("caminho_imagem"));

                    // Editora: Usando o construtor parametrizado
                    Editora editora = new Editora(rs.getInt("id_edit"), rs.getString("nome_edit"));
                    livro.setEditora(editora);

                    // Dimensões: Usando o construtor parametrizado
                    Dimensoes dimensoes = new Dimensoes(
                            rs.getInt("id_dimensao"),
                            rs.getDouble("altura"),
                            rs.getDouble("largura"),
                            rs.getDouble("profundidade"),
                            rs.getDouble("peso")
                    );
                    livro.setDimensoes(dimensoes);

                    livro.setAutores(new ArrayList<>());
                    livro.setCategorias(new ArrayList<>());
                    livroMap.put(livroId, livro);
                }

                // Autor: Usando o construtor parametrizado (apenas se houver autor para evitar NullPointer)
                int autorId = rs.getInt("id"); // Usando 'id' conforme o esquema da tabela Autor
                String nomeAutor = rs.getString("nome"); // Usando 'nome' conforme o esquema da tabela Autor
                String nacionalidadeAutor = rs.getString("nacionalidade");
                if (autorId != 0 && nomeAutor != null) { // Garante que há um autor válido
                    Autor autor = new Autor(autorId, nomeAutor, nacionalidadeAutor);
                    if (!livro.getAutores().contains(autor)) {
                        livro.getAutores().add(autor);
                    }
                }

                // Categoria: Usando o construtor parametrizado (apenas se houver categoria)
                int categoriaId = rs.getInt("id_categoria");
                String nomeCategoria = rs.getString("nome_categoria");
                String descricaoCategoria = rs.getString("descricao_categoria");
                if (categoriaId != 0 && nomeCategoria != null) { // Garante que há uma categoria válida
                    Categoria categoria = new Categoria(categoriaId, nomeCategoria, descricaoCategoria);
                    if (!livro.getCategorias().contains(categoria)) {
                        livro.getCategorias().add(categoria);
                    }
                }
            }
            livros.addAll(livroMap.values());

        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao consultar livros com filtros: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro SQL ao consultar livros com filtros: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao consultar livros com filtros: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar livros com filtros: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em consultarPorFiltros: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return livros;
    }

    /**
     * Consulta livros no banco de dados por palavras-chave no título ou sinopse.
     * Otimizado para preencher Autor, Categoria, Editora e Dimensões
     * diretamente do ResultSet principal, utilizando seus construtores parametrizados.
     * @param keywords A string de palavras-chave a ser pesquisada.
     * @return Uma lista de livros que contêm as palavras-chave no título ou sinopse.
     */
    public List<Livro> consultarPorKeywords(String keywords) {
        List<Livro> livros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }

            // Seleciona todos os campos necessários para montar o objeto Livro e seus objetos relacionados
            StringBuilder sql = new StringBuilder("SELECT DISTINCT l.id_livro, l.titulo, l.ano, l.edicao, l.isbn, l.numero_paginas, l.sinopse, l.codigo_barras, l.caminho_imagem, l.status, ");
            // Usando 'e.nome_edit' conforme o feedback do usuário e o esquema da tabela
            sql.append("e.id_edit, e.nome_edit, ");
            sql.append("d.id_dimensao, d.altura, d.largura, d.peso, d.profundidade, ");
            // Usando 'a.id' e 'a.nome' para Autor, baseado na classe de domínio Autor e no esquema da tabela
            sql.append("a.id, a.nome, a.nacionalidade, ");
            sql.append("c.id_categoria, c.nome_categoria, c.descricao_categoria ");
            sql.append("FROM livro l ");
            sql.append("LEFT JOIN editora e ON l.id_edit = e.id_edit ");
            sql.append("LEFT JOIN dimensoes d ON l.id_dimensao = d.id_dimensao ");
            sql.append("LEFT JOIN livro_autor la ON l.id_livro = la.id_livro ");
            sql.append("LEFT JOIN autor a ON la.id_autor = a.id "); // Usando 'a.id' no JOIN conforme o esquema
            sql.append("LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro ");
            sql.append("LEFT JOIN categoria c ON lc.id_categoria = c.id_categoria ");
            sql.append("WHERE l.status = 1 "); // Apenas livros ativos
            sql.append("AND (l.titulo LIKE ? OR l.sinopse LIKE ?) "); // Busca no título OU sinopse
            sql.append("ORDER BY l.titulo ASC;");

            LOGGER.info("Consulta SQL por palavras-chave: " + sql);
            LOGGER.info("Palavra-chave: " + keywords);

            stmt = conn.prepareStatement(sql.toString());

            String searchKeyword = "%" + keywords + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);

            rs = stmt.executeQuery();

            Map<Integer, Livro> livroMap = new HashMap<>(); // Usar mapa para agregar autores/categorias/dimensoes/editora
            while (rs.next()) {
                int livroId = rs.getInt("id_livro");
                Livro livro = livroMap.get(livroId);

                if (livro == null) {
                    livro = new Livro();
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

                    // Editora: Usando o construtor parametrizado
                    Editora editora = new Editora(rs.getInt("id_edit"), rs.getString("nome_edit"));
                    livro.setEditora(editora);

                    // Dimensões: Usando o construtor parametrizado
                    Dimensoes dimensoes = new Dimensoes(
                            rs.getInt("id_dimensao"),
                            rs.getDouble("altura"),
                            rs.getDouble("largura"),
                            rs.getDouble("profundidade"),
                            rs.getDouble("peso")
                    );
                    livro.setDimensoes(dimensoes);

                    livro.setAutores(new ArrayList<>());
                    livro.setCategorias(new ArrayList<>());
                    livroMap.put(livroId, livro);
                }

                // Autor: Usando o construtor parametrizado (apenas se houver autor para evitar NullPointer)
                int autorId = rs.getInt("id"); // Usando 'id' conforme o esquema da tabela Autor
                String nomeAutor = rs.getString("nome"); // Usando 'nome' conforme o esquema da tabela Autor
                String nacionalidadeAutor = rs.getString("nacionalidade");
                if (autorId != 0 && nomeAutor != null) { // Garante que há um autor válido
                    Autor autor = new Autor(autorId, nomeAutor, nacionalidadeAutor);
                    if (!livro.getAutores().contains(autor)) {
                        livro.getAutores().add(autor);
                    }
                }

                // Categoria: Usando o construtor parametrizado (apenas se houver categoria)
                int categoriaId = rs.getInt("id_categoria");
                String nomeCategoria = rs.getString("nome_categoria");
                String descricaoCategoria = rs.getString("descricao_categoria");
                if (categoriaId != 0 && nomeCategoria != null) { // Garante que há uma categoria válida
                    Categoria categoria = new Categoria(categoriaId, nomeCategoria, descricaoCategoria);
                    if (!livro.getCategorias().contains(categoria)) {
                        livro.getCategorias().add(categoria);
                    }
                }
            }
            livros.addAll(livroMap.values());

        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao consultar livros por palavras-chave: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar livros por palavras-chave.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao consultar livros por palavras-chave: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao consultar livros por palavras-chave.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em consultarPorKeywords: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return livros;
    }

    /**
     * Retorna uma lista de todos os nomes de categorias distintas no banco de dados.
     * @return Uma lista de Strings contendo os nomes das categorias.
     */
    public List<String> getAllCategoriesNames() {
        List<String> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT nome_categoria FROM categoria ORDER BY nome_categoria ASC;";

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("nome_categoria"));
            }
            LOGGER.info("Nomes de categorias obtidos com sucesso. Total: " + categories.size());
        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao obter nomes de categorias: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter nomes de categorias.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao obter nomes de categorias: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao obter nomes de categorias.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em getAllCategoriesNames: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return categories;
    }

    /**
     * Retorna uma amostra de nomes de autores distintos, limitando a quantidade.
     * @param limit O número máximo de autores a retornar.
     * @return Uma lista de Strings contendo nomes de autores.
     */
    public List<String> getSomeAuthorsNames(int limit) {
        List<String> authors = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // CORREÇÃO: Usando 'nome' ao invés de 'nome_autor' conforme o esquema da tabela Autor
        String sql = "SELECT DISTINCT nome FROM autor ORDER BY nome ASC LIMIT ?;";

        try {
            conn = Conexao.createConnectionToMySQL();
            if (conn == null) {
                throw new SQLException("Erro ao conectar ao banco de dados: Conexão nula.");
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                authors.add(rs.getString("nome")); // CORREÇÃO: Usando 'nome' ao invés de 'nome_autor'
            }
            LOGGER.info("Nomes de autores obtidos com sucesso. Total: " + authors.size());
        } catch (SQLException e) {
            LOGGER.severe("Erro SQL ao obter nomes de autores: " + e.getMessage());
            e.printStackTrace(); // Chama printStackTrace separadamente
            throw new RuntimeException("Erro ao obter nomes de autores.", e);
        } catch (Exception e) {
            LOGGER.severe("Erro inesperado ao obter nomes de autores: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao obter nomes de autores.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.severe("Erro ao fechar recursos do banco de dados em getSomeAuthorsNames: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return authors;
    }

    /**
     * Busca um livro pelo seu título exato.
     * Este método é funcional, mas a lógica de chamada via ChatBotServlet
     * agora prioriza consultarPorFiltros ou consultarPorKeywords para maior flexibilidade.
     * @param titulo O título do livro a ser buscado.
     * @return O primeiro livro encontrado com o título, ou null se nenhum for encontrado.
     */
    public Livro buscarPorTitulo(String titulo) {
        Map<String, Object> filtros = new HashMap<>();
        filtros.put("titulo", titulo);
        List<Livro> livros = consultarPorFiltros(filtros);
        if (!livros.isEmpty()) {
            return livros.get(0); // Retorna o primeiro livro encontrado com esse título
        }
        return null;
    }
}