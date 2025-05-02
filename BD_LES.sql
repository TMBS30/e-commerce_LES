CREATE DATABASE cadastroCliente;
USE cadastroCliente;

CREATE TABLE tipos_telefone (
    id_tip_tel INT PRIMARY KEY AUTO_INCREMENT,
    nome_tip_tel VARCHAR(255) NOT NULL UNIQUE
);

SELECT telefone_id FROM cliente;
SELECT * FROM telefones WHERE id IN (6, 7, 8);

SELECT * FROM endereco WHERE cliente_id = 1;
SELECT * FROM tipos_endereco;
SELECT * FROM tipos_logradouro;
SELECT * FROM tipos_residencia;
SELECT * FROM cidades;
SELECT * FROM estados;
SELECT * FROM paises;

SHOW TABLES LIKE 'telefones';

INSERT INTO telefones (ddd, numero, tipo_telefone_id) 
VALUES ('11', '987654321', 1);

INSERT INTO cliente (nome, cpf, email, senha, genero, telefone_id) 
VALUES ('Elias Silva', '12345678900', 'elias@exemplo.com', 'senha000', 'MASCULINO', 11);

SELECT * FROM endereco WHERE cliente_id = 1 AND (tipo_endereco_id IS NULL OR tipo_logradouro_id IS NULL OR tipo_residencia_id IS NULL OR cidade_id IS NULL);


INSERT INTO tipos_telefone (nome_tip_tel) VALUES
('CELULAR'),
('RESIDENCIAL'),
('COMERCIAL'),
('OUTRO');
SELECT * FROM tipos_telefone

-- TELEFONES
CREATE TABLE telefones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ddd CHAR(3) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    tipo_telefone_id INT,
    FOREIGN KEY (tipo_telefone_id) REFERENCES tipos_telefone(id_tip_tel)
);
INSERT INTO telefones (ddd, numero, tipo_telefone_id) VALUES
('011', '99876-5432', 1),  
('011', '2121-2121', 2),   
('011', '97013-3543', 1);   
SELECT * FROM telefones

-- CLIENTES
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,                       
    nome VARCHAR(100) NOT NULL,                          
    cpf VARCHAR(14) NOT NULL UNIQUE,                      
    email VARCHAR(100) NOT NULL,                         
    senha VARCHAR(64) NOT NULL,                                
    genero ENUM('MASCULINO', 'FEMININO', 'OUTRO') NOT NULL,    
    dataNascimento DATE,                                      
    telefone_id INT,                                          
    status_ativo BOOLEAN DEFAULT TRUE,                        
    FOREIGN KEY (telefone_id) REFERENCES telefones(id)         
);

SELECT * FROM cliente
DELETE FROM cliente WHERE id = 6;

INSERT INTO cliente (nome, cpf, email, senha, genero, dataNascimento, telefone_id, status_ativo) VALUES
('João Silva', '123.456.789-00', 'joao.silva@example.com', 'senha123', 'MASCULINO', '1990-05-15', 6, TRUE),
('Maria Oliveira', '987.654.321-00', 'maria.oliveira@example.com', 'senha456', 'FEMININO', '1985-08-20', 7, TRUE),
('Carlos Souza', '456.789.123-00', 'carlos.souza@example.com', 'senha789', 'MASCULINO', '1992-03-10', 8, TRUE);

CREATE TABLE logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao TEXT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TELEFONE EDITORAS
INSERT INTO telefones (ddd, numero, tipo_telefone_id) VALUES
('11', '987654321', 3),  -- Comercial
('21', '212345678', 3),  -- Comercial
('31', '313234567', 3),  -- Comercial
('41', '419876543', 3),  -- Comercial
('51', '519876543', 3);  -- Comercial

SELECT * FROM tipos_endereco WHERE id_tip_end = 1;
SELECT * FROM tipos_endereco WHERE id_tip_end = 2;
SELECT * FROM tipos_endereco WHERE id_tip_end = 3;

-- Tabela de países
CREATE TABLE paises (
    id_pais INT PRIMARY KEY AUTO_INCREMENT,
    nome_pais VARCHAR(255) NOT NULL
);

INSERT INTO paises (nome_pais) VALUES
('Brasil'),
('Argentina'),
('Alemanha');

SELECT * FROM paises

CREATE TABLE estados (
    id_estado INT PRIMARY KEY AUTO_INCREMENT,
    nome_estado VARCHAR(100) NOT NULL,
    id_pais INT,
    FOREIGN KEY (id_pais) REFERENCES paises(id_pais)
);
INSERT INTO estados (nome_estado) VALUES
('Sao Paulo'),
('Buenos Aires'),
('Munique');

SELECT * FROM estados

CREATE TABLE cidades (
    id_cidade INT AUTO_INCREMENT PRIMARY KEY,    -- Identificador único da cidade
    nome_cidade VARCHAR(100) NOT NULL,           -- Nome da cidade
    id_estado INT,                               -- Chave estrangeira para o estado
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado)
);

INSERT INTO cidades (nome_cidade) VALUES
('Mogi das Cruzes'),
('Palermo'),
('Grasbrunn');

SELECT * FROM cidades

-- Tabela de tipos de endereço
CREATE TABLE tipos_endereco (
    id_tip_end INT PRIMARY KEY AUTO_INCREMENT,
    descricao_tip_end VARCHAR(50)
);
INSERT INTO tipos_endereco (descricao_tip_end) VALUES
('Residencial'),
('Cobranca'),
('Entrega');

SELECT id_tip_end FROM tipos_endereco;
SELECT * FROM tipos_endereco

-- Tabela de tipos de logradouro
CREATE TABLE tipos_logradouro (
    id_tip_log INT PRIMARY KEY AUTO_INCREMENT,
    descricao_tip_log VARCHAR(50)
);
INSERT INTO tipos_logradouro (descricao_tip_log) VALUES
('Rua'),
('Avenida'),
('Travessa'),
('Alameda'),
('Estrada'),
('Outro');

SELECT * FROM tipos_logradouro

-- Tabela de tipos de residência
CREATE TABLE tipos_residencia (
    id_tip_res INT PRIMARY KEY AUTO_INCREMENT,
    descricao_tip_res VARCHAR(50)
);

INSERT INTO tipos_residencia (descricao_tip_res) VALUES
('Casa'),
('Apartamento'),
('Chacara'),
('Condominio'),
('Outro');

SELECT * FROM tipos_residencia

CREATE TABLE endereco (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cep VARCHAR(10),
    logradouro VARCHAR(100),
    numero INT,
    bairro VARCHAR(50),
    cliente_id INT,
    tipo_endereco_id INT,
    tipo_logradouro_id INT,
    tipo_residencia_id INT,
    cidade_id INT,
    observacao TEXT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (tipo_endereco_id) REFERENCES tipos_endereco(id_tip_end),
    FOREIGN KEY (tipo_logradouro_id) REFERENCES tipos_logradouro(id_tip_log),
    FOREIGN KEY (tipo_residencia_id) REFERENCES tipos_residencia(id_tip_res),
    FOREIGN KEY (cidade_id) REFERENCES cidades(id_cidade)
);

INSERT INTO endereco (cep, logradouro, numero, bairro, cliente_id, tipo_endereco_id, tipo_logradouro_id, tipo_residencia_id, cidade_id, observacao) VALUES
('08730-010', 'Rua dos Pinheiros', 123, 'Centro', 1, 1, 1, 1, 1, 'Endereço Residencial'),  -- Residencial
('08730-010', 'Rua dos Pinheiros', 123, 'Centro', 1, 2, 1, 1, 1, 'Endereço de Cobrança'),  -- Cobranca
('08730-010', 'Rua dos Pinheiros', 123, 'Centro', 1, 3, 1, 1, 1, 'Endereço de Entrega'),   -- Entrega
('14120-000', 'Avenida das Palmeiras', 456, 'Jardim', 2, 1, 2, 2, 2, 'Endereço Residencial'),  -- Residencial
('14120-000', 'Avenida das Palmeiras', 456, 'Jardim', 2, 2, 2, 2, 2, 'Endereço de Cobrança'),  -- Cobranca
('14120-000', 'Avenida das Palmeiras', 456, 'Jardim', 2, 3, 2, 2, 2, 'Endereço de Entrega'),   -- Entrega
('85660-000', 'Estrada do Campo', 789, 'Zona Rural', 3, 1, 5, 3, 3, 'Endereço Residencial'),  -- Residencial
('85660-000', 'Estrada do Campo', 789, 'Zona Rural', 3, 2, 5, 3, 3, 'Endereço de Cobrança'),  -- Cobranca
('85660-000', 'Estrada do Campo', 789, 'Zona Rural', 3, 3, 5, 3, 3, 'Endereço de Entrega');   -- Entrega

SELECT * FROM endereco;

SELECT * FROM paises;
SELECT * FROM estados;
SELECT * FROM cidades;

SET SQL_SAFE_UPDATES = 0;
SET SQL_SAFE_UPDATES = 1;

UPDATE estados SET id_pais = 1 WHERE nome_estado = 'Sao Paulo';
UPDATE estados SET id_pais = 2 WHERE nome_estado = 'Buenos Aires';
UPDATE estados SET id_pais = 3 WHERE nome_estado = 'Munique';

UPDATE cidades SET id_estado = (SELECT id_estado FROM estados WHERE nome_estado = 'Sao Paulo') WHERE nome_cidade = 'Mogi das Cruzes';
UPDATE cidades SET id_estado = (SELECT id_estado FROM estados WHERE nome_estado = 'Buenos Aires') WHERE nome_cidade = 'Palermo';
UPDATE cidades SET id_estado = (SELECT id_estado FROM estados WHERE nome_estado = 'Munique') WHERE nome_cidade = 'Grasbrunn';

SELECT c.id_cidade, c.nome_cidade, e.nome_estado, p.nome_pais 
FROM cidades c
JOIN estados e ON c.id_estado = e.id_estado
JOIN paises p ON e.id_pais = p.id_pais;

SELECT 
    e.id,
    e.cep,
    tlog.descricao_tip_log AS tipo_logradouro,
    e.logradouro,
    e.numero,
    e.bairro,
    tres.descricao_tip_res AS tipo_residencia,
    c.nome_cidade,
    est.nome_estado,
    p.nome_pais,
    tend.descricao_tip_end AS tipo_endereco,
    e.observacao
FROM 
    endereco e
    INNER JOIN cidades c ON e.cidade_id = c.id_cidade
    INNER JOIN estados est ON c.id_estado = est.id_estado
    INNER JOIN paises p ON est.id_pais = p.id_pais
    LEFT JOIN tipos_endereco tend ON e.tipo_endereco_id = tend.id_tip_end
    LEFT JOIN tipos_logradouro tlog ON e.tipo_logradouro_id = tlog.id_tip_log
    LEFT JOIN tipos_residencia tres ON e.tipo_residencia_id = tres.id_tip_res
WHERE 
    e.cliente_id = 68;
    
    SELECT * FROM cliente WHERE id;

SELECT * FROM endereco WHERE cliente_id = 1;

SELECT * FROM tipos_endereco WHERE id_tip_end = 1;
SELECT * FROM tipos_logradouro WHERE id_tip_log = 1;
SELECT * FROM tipos_residencia WHERE id_tip_res = 1;
SELECT * FROM cidades WHERE id_cidade = 1;

SELECT * FROM endereco WHERE cliente_id = 1 AND (tipo_endereco_id IS NULL OR tipo_logradouro_id IS NULL OR tipo_residencia_id IS NULL OR cidade_id IS NULL);

-- CONSULTA RETORNA CORRETAMENTE
SELECT
    e.*,
    te.descricao_tip_end,
    tl.descricao_tip_log,
    tr.descricao_tip_res,
    c.nome_cidade,
    es.nome_estado,
    p.nome_pais
FROM
    endereco e
JOIN
    tipos_endereco te ON e.tipo_endereco_id = te.id_tip_end
JOIN
    tipos_logradouro tl ON e.tipo_logradouro_id = tl.id_tip_log
JOIN
    tipos_residencia tr ON e.tipo_residencia_id = tr.id_tip_res
JOIN
    cidades c ON e.cidade_id = c.id_cidade
JOIN
    estados es ON c.id_estado = es.id_estado
JOIN
    paises p ON es.id_pais = p.id_pais
WHERE
    e.cliente_id = 69;

-- BANDEIRA CARTÃO
CREATE TABLE bandeiras_cartao (
    id_band INT AUTO_INCREMENT PRIMARY KEY,
    descricao_band VARCHAR(50) NOT NULL
);

INSERT INTO bandeiras_cartao (descricao_band) VALUES
('VISA'),
('MASTERCARD'),
('AMERICAN_EXPRESS'),
('DISCOVER'),
('DINERS_CLUB'),
('JCB'),
('Outras');

SELECT * FROM bandeiras_cartao

-- CARTÃO DE CREDITO
CREATE TABLE cartao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(19) NOT NULL,               
    nomeTitular VARCHAR(100) NOT NULL,        
    codSeguranca VARCHAR(4) NOT NULL,         
    preferencial BOOLEAN DEFAULT FALSE,        
    cliente_id INT NOT NULL,                 
    bandeiraCartao_id INT,                     -
    FOREIGN KEY (bandeiraCartao_id) REFERENCES bandeiras_cartao(id_band),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Remover a coluna data_vencimento existente
ALTER TABLE cartao
DROP COLUMN data_vencimento;

-- Adicionar a coluna data_vencimento com o tipo VARCHAR(5)
ALTER TABLE cartao
ADD data_vencimento VARCHAR(5);

INSERT INTO cartao (numero, nomeTitular, codSeguranca, preferencial, cliente_id, bandeiraCartao_id) VALUES
('4111111111111111', 'Jorge Silva', '123', TRUE, 1, 1),       -- VISA, preferencial
('5500000000000004', 'Jorge Silva', '456', FALSE, 1, 2),       -- MASTERCARD
('3400000000000005', 'Ana Costa', '789', TRUE, 2, 3),          -- AMERICAN_EXPRESS, preferencial
('6011000000000012', 'Ana Costa', '321', FALSE, 2, 4),         -- DISCOVER
('3600000000000014', 'Carlos Souza', '654', TRUE, 3, 5),       -- DINERS_CLUB, preferencial
('3528000000000008', 'Carlos Souza', '987', FALSE, 3, 6);      -- JCB

SELECT * FROM cartao;

UPDATE cartao SET data_vencimento = '2025-08-01' WHERE id = 1;
UPDATE cartao SET data_vencimento = '2026-03-01' WHERE id = 2;
UPDATE cartao SET data_vencimento = '2024-11-01' WHERE id = 3;
UPDATE cartao SET data_vencimento = '2027-05-01' WHERE id = 4;
UPDATE cartao SET data_vencimento = '2025-09-01' WHERE id = 5;
UPDATE cartao SET data_vencimento = '2026-12-01' WHERE id = 6;

SELECT 
    c.id,
    c.numero,
    c.nomeTitular,
    c.codSeguranca,
    c.data_vencimento,
    c.preferencial,
    b.descricao_band AS bandeira
FROM 
    cartao c
JOIN 
    bandeiras_cartao b ON c.bandeiraCartao_id = b.id_band
WHERE 
    c.cliente_id = 1;

SELECT * FROM livro WHERE id_livro = 7;

CREATE TABLE editora (
    id_edit INT PRIMARY KEY AUTO_INCREMENT,
    nome_edit VARCHAR(20) NOT NULL,
    telefone_id INT,
    FOREIGN KEY (telefone_id) REFERENCES telefones(id)
);

INSERT INTO editora (nome_edit, telefone_id) VALUES
('Pearson', 1),
('Companhia das Letras', 2),
('LTC', 3),  
('Senac', 4),  
('Novatec', 5); 

SET SQL_SAFE_UPDATES = 0;

DELETE FROM editora WHERE nome_edit IN (
    'Pearson',
    'Companhia das Letras',
    'LTC',
    'Senac',
    'Novatec'
);

INSERT INTO editora (nome_edit, telefone_id) VALUES 
('Rocco', 1),
('Galera', 2),
('Arqueiro', 3),
('Cenage', 4);

SELECT * FROM editora

CREATE TABLE dimensoes (
    id_dimensao INT AUTO_INCREMENT PRIMARY KEY,
    altura DOUBLE NOT NULL,
    largura DOUBLE NOT NULL,
    profundidade DOUBLE NOT NULL,
    peso DOUBLE NOT NULL
);

INSERT INTO dimensoes (altura, largura, profundidade, peso) VALUES
(24, 17, 3, 1),  -- A Arte da Programação
(25, 18, 4, 1.2),  -- História do Brasil
(24, 17, 3.5, 1),  -- Física Moderna
(27, 21, 2.5, 1.3),  -- Cozinha Internacional
(25, 18, 3.5, 1.1);  -- Inteligência Artificial

INSERT INTO dimensoes (altura, largura, profundidade, peso) VALUES
(23.5, 15.5, 4, 0.5),   -- Harry Potter
(21, 14, 2.5, 0.4),     -- Algoritmo e Lógica de Programação
(23, 16, 4.5, 0.6),     -- É Assim que Acaba
(21, 14, 3, 0.45);      -- A Cabana

SELECT * FROM dimensoes

CREATE TABLE autor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nacionalidade VARCHAR(50) NOT NULL
);

INSERT INTO autor (nome, nacionalidade) VALUES
('Donald Knuth', 'Americano'),
('Boris Fausto', 'Brasileiro'),
('David Bohm', 'Britânico'),
('Claude Troisgros', 'Francês'),
('Stuart Russell', 'Britânico'),
('Peter Norvig', 'Americano');

INSERT INTO autor (nome, nacionalidade) VALUES
('Ricardo de Oliveira', 'Brasileira'),
('Colleen Hoover', 'Americana'),
('William P. Young', 'Canadense');

INSERT INTO autor (nome, nacionalidade) VALUES
('J.K. Rowling', 'Britanica');
SELECT * FROM autor

 -- FORNECEDORES
CREATE TABLE fornecedor (
    id_fornec INT AUTO_INCREMENT PRIMARY KEY,
    descricao_fornec VARCHAR(30) NOT NULL
);

INSERT INTO fornecedor (descricao_fornec) VALUES
('INGRAM_CONTENT_GROUP'),
('AMAZON_KDP_E_DISTRIBUICAO'),
('SARAIVA_DISTRIBUIDORA'),
('BOOKWIRE_BRASIL'),
('CATAVENTO_DISTRIBUIDORA'),
('OUTROS');

SELECT * FROM fornecedor

 -- ATIVAÇÃO CATEGORIA 
CREATE TABLE categoria_ativacao (
    id_cat_ativacao INT AUTO_INCREMENT PRIMARY KEY,
    descricao_cat_ativacao VARCHAR(30) NOT NULL
);

INSERT INTO categoria_ativacao (descricao_cat_ativacao) VALUES
('DISPONIVEL'),
('NOVA_EDICAO'),
('ALTA_PROCURA'),
('OUTROS');

SELECT * FROM categoria_ativacao

 -- INATIVAÇÃO CATEGORIA
CREATE TABLE categoria_inativacao (
    id_cat_inativacao INT AUTO_INCREMENT PRIMARY KEY,
    descricao_cat_inativacao VARCHAR(30) NOT NULL
);

INSERT INTO categoria_inativacao (descricao_cat_inativacao) VALUES
('ESGOTADO'),
('CONTEUDO_DESATUALIZADO'),
('BAIXA_PROCURA'),
('OUTROS');

SELECT * 
FROM endereco 
WHERE cliente_id = 2;

SELECT * FROM categoria_inativacao

-- ADMINISTRADOR
CREATE TABLE administrador (
    id_adm INT AUTO_INCREMENT PRIMARY KEY,
    nome_adm VARCHAR(100) NOT NULL,
    email_adm VARCHAR(100) NOT NULL,
    senha_adm VARCHAR(64) NOT NULL
);

SELECT * FROM administrador

-- ITEM
CREATE TABLE item (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    quantidade_item INT NOT NULL,
    valor_custo_item DECIMAL(10, 2) NOT NULL,
    data_entrada_item DATE NOT NULL,
    id_fornec INT,
    FOREIGN KEY (id_fornec) REFERENCES fornecedor(id_fornec)
);

ALTER TABLE item
ADD COLUMN valorVenda DECIMAL(10,2) NOT NULL;

ALTER TABLE item
ADD COLUMN id_livro INT,
ADD CONSTRAINT fk_item_livro
FOREIGN KEY (id_livro) REFERENCES livro(id_livro);

INSERT INTO item (quantidade_item, valor_custo_item, data_entrada_item, id_fornec, valorVenda, id_livro) VALUES
(30, 29.61, '2025-03-30', 5, 34.20, 7),
(15, 54.56, '2024-09-29', 2, 65.47, 8),
(45, 26.11, '2025-01-05', 1, 28.72, 9),
(10, 27.67, '2024-11-07', 3, 31.98, 10);

SELECT * FROM item

-- CARRINHO
CREATE TABLE carrinho (
    id_carrinho INT AUTO_INCREMENT PRIMARY KEY,
    bloqueado_carrinho BOOLEAN NOT NULL DEFAULT FALSE,
    data_bloqueio_carrinho DATE DEFAULT NULL,
    valor_carrinho DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    id_item INT, -- id_item foi removido
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

SHOW CREATE TABLE carrinho;
ALTER TABLE carrinho DROP FOREIGN KEY carrinho_ibfk_1;
ALTER TABLE carrinho DROP COLUMN id_item;

ALTER TABLE carrinho ADD COLUMN cliente_id INT;
ALTER TABLE carrinho ADD CONSTRAINT fk_carrinho_cliente
FOREIGN KEY (cliente_id) REFERENCES cliente(id);

SELECT * FROM carrinho;
INSERT INTO carrinho (cliente_id) VALUES (70);
SELECT * FROM carrinho WHERE cliente_id = 70;

-- CATEGORIA
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(100) NOT NULL,
    descricao_categoria TEXT
);

INSERT INTO categoria (nome_categoria, descricao_categoria) VALUES
('Tecnologia', 'Livros sobre inovação tecnológica e desenvolvimento');
SET SQL_SAFE_UPDATES = 1;

INSERT INTO categoria (nome_categoria, descricao_categoria) VALUES 
('Fantasia', 'Livros com temas magicos, criaturas sobrenaturais e mundos imaginarios.'),
('Ficcao', 'Livros que exploram cenarios e eventos imaginarios, muitas vezes com elementos futuristas ou fantasticos.'),
('Aventura', 'Livros com tramas empolgantes, personagens corajosos e jornadas emocionantes.'),
('Romance', 'Livros focados em relacionamentos amorosos e dramas emocionais.'),
('Drama', 'Livros que exploram situacoes emocionantes e realistas, muitas vezes com temas profundos e complexos.'),
('Religiao', 'Livros que abordam temas espirituais, doutrinas religiosas e praticas de fe.'),
('Suspense', 'Livros com narrativas cheias de misterio, tensao e reviravoltas inesperadas.'),
('Computacao', 'Livros sobre tecnologia, programacao, desenvolvimento de software e inovacao tecnologica.'),
('Educacao', 'Livros focados em ensino, aprendizado e aprimoramento pessoal e profissional.');

SELECT * FROM categoria

-- TIPO CUPOM
CREATE TABLE tipo_cupom (
    id_tipo_cupom INT AUTO_INCREMENT PRIMARY KEY,
    descricao_tipo_cupom VARCHAR(50) NOT NULL
);

INSERT INTO tipo_cupom (descricao_tipo_cupom) VALUES
('PROMOCIONAL'),
('TROCA');

SELECT * FROM tipo_cupom

-- CUPOM
CREATE TABLE cupom (
    id_cupom INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_cupom INT NOT NULL,
    valor_cupom DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_tipo_cupom) REFERENCES tipo_cupom(id_tipo_cupom)
);

SELECT d.id_dimensao, d.altura, d.largura, d.profundidade, d.peso
FROM dimensoes d
INNER JOIN livro l ON d.id_dimensao = l.id_dimensao
WHERE l.id_livro = 8;

SELECT * FROM cupom

-- ESTOQUE
CREATE TABLE estoque (
    id_estoque INT AUTO_INCREMENT PRIMARY KEY,
    id_item INT NOT NULL,
    quantidade_estoque INT NOT NULL,
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

INSERT INTO estoque (id_item, quantidade_estoque) VALUES
(1, 50),  -- A Arte da Programação (DISPONIVEL)
(2, 30),  -- História do Brasil (NOVA_EDICAO)
(3, 40),  -- Física Moderna (ALTA_PROCURA)
(4, 15),  -- Cozinha Internacional (CONTEUDO_DESATUALIZADO)
(5, 20);  -- Inteligência Artificial (BAIXA_PROCURA)

SELECT * FROM estoque

-- FRETE
CREATE TABLE frete (
    id_frete INT AUTO_INCREMENT PRIMARY KEY,
    valor_frete DECIMAL(10, 2) NOT NULL,
    id_endereco INT NOT NULL,
    FOREIGN KEY (id_endereco) REFERENCES endereco(id)
);

SELECT * FROM frete

-- GRUPO PRECIFICAÇÃO
CREATE TABLE grupo_precificacao (
    id_grupo_precificacao INT AUTO_INCREMENT PRIMARY KEY,
    nome_grupo_precificacao VARCHAR(100) NOT NULL,
    margem_lucro_minima_precificacao DECIMAL(10, 2) NOT NULL,
    desconto_maximo_precificacao DECIMAL(10, 2) NOT NULL
);

INSERT INTO grupo_precificacao (nome_grupo_precificacao, margem_lucro_minima_precificacao, desconto_maximo_precificacao) VALUES
('Grupo 1', 15.50, 10.00), -- Literatura Clássica
('Grupo 2', 20.00, 15.00), -- Best-sellers
('Grupo 3', 10.00, 5.00); -- Novidades

SELECT * FROM grupo_precificacao

-- LIVRO
CREATE TABLE livro (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    ano INT NOT NULL,
    edicao INT NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    numero_paginas INT NOT NULL,
    sinopse TEXT,
    codigo_barras VARCHAR(20) NOT NULL,
    id_grupo_precificacao INT,
    status BOOLEAN NOT NULL,
    id_edit INT,
    id_dimensao INT,
    justificativa_inativacao TEXT,
    justificativa_ativacao TEXT,
    categoria_inativacao INT,
    categoria_ativacao INT,
    FOREIGN KEY (id_grupo_precificacao) REFERENCES grupo_precificacao(id_grupo_precificacao),
    FOREIGN KEY (id_edit) REFERENCES editora(id_edit),
    FOREIGN KEY (id_dimensao) REFERENCES dimensoes(id_dimensao),
    FOREIGN KEY (categoria_inativacao) REFERENCES categoria_inativacao(id_cat_inativacao),
    FOREIGN KEY (categoria_ativacao) REFERENCES categoria_ativacao(id_cat_ativacao)
);
 
INSERT INTO livro (titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, id_grupo_precificacao, status, id_edit, id_dimensao, justificativa_inativacao, justificativa_ativacao, categoria_inativacao, categoria_ativacao) VALUES
('A Arte da Programação', 2020, 1, '9780134685991', 500, 'Este livro aborda os fundamentos da programação.', '1234567890123', 2, TRUE, 1, 1, NULL, 'DISPONIVEL', NULL, 1),
('História do Brasil', 2018, 2, '9788535908559', 400, 'Estudo sobre a história do Brasil desde a colonização.', '2345678901234', 1, TRUE, 2, 2, NULL, 'NOVA_EDICAO', NULL, 2),
('Física Moderna', 2021, 1, '9788575221410', 350, 'Exploração dos princípios da física moderna.', '3456789012345', 3, TRUE, 3, 3, NULL, 'ALTA_PROCURA', NULL, 3),
('Cozinha Internacional', 2019, 3, '9788587896781', 250, 'Receitas e técnicas da culinária mundial.', '4567890123456', 2, TRUE, 4, 4, NULL, 'CONTEUDO_DESATUALIZADO', 2, NULL),
('Inteligência Artificial', 2022, 1, '9788535908849', 450, 'Fundamentos e aplicações de IA.', '5678901234567', 3, TRUE, 5, 5, NULL, 'BAIXA_PROCURA', 3, NULL);
 
 ALTER TABLE livro
ADD COLUMN id_item INT,
ADD FOREIGN KEY (id_item) REFERENCES item(id_item);

SELECT CONSTRAINT_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'livro' AND COLUMN_NAME = 'id_item';

ALTER TABLE livro DROP FOREIGN KEY livro_ibfk_6;
ALTER TABLE livro DROP COLUMN id_item;

UPDATE livro SET id_item = 1 WHERE id_livro = 7;  -- HP
UPDATE livro SET id_item = 2 WHERE id_livro = 8;  -- LP
UPDATE livro SET id_item = 3 WHERE id_livro = 9;  -- É Assim...
UPDATE livro SET id_item = 4 WHERE id_livro = 10; -- A cabana


UPDATE livro SET justificativa_inativacao = 'CONTEUDO_DESATUALIZADO' WHERE id_livro = 4;
UPDATE livro SET justificativa_inativacao = 'BAIXA_PROCURA' WHERE id_livro = 5;

UPDATE livro SET justificativa_ativacao = NULL WHERE id_livro = 4;
UPDATE livro SET justificativa_ativacao = NULL WHERE id_livro = 5;

UPDATE livro SET status = FALSE WHERE id_livro = 4;
UPDATE livro SET status = FALSE WHERE id_livro = 5;

ALTER TABLE livro ADD COLUMN caminho_imagem VARCHAR(255);

UPDATE livro
SET caminho_imagem = 'Imagens/Imagem - Harry Potter.png'
WHERE id_livro = 7;
 
 SELECT * FROM livro;
 SELECT * FROM item;
 
 SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM livro WHERE id_livro IN (1);
SET FOREIGN_KEY_CHECKS = 1;
 
 
 -- LIVRO RELACIONAMENTO
 CREATE TABLE livro_autor (
    id_livro INT,
    id_autor INT,
    PRIMARY KEY (id_livro, id_autor),
    FOREIGN KEY (id_livro) REFERENCES livro(id_livro),
    FOREIGN KEY (id_autor) REFERENCES autor(id)
);

SELECT a.nome AS nome_autor
FROM autor a
JOIN livro_autor la ON a.id = la.id_autor
JOIN livro l ON l.id_livro = la.id_livro
WHERE l.id_livro = 7;

CREATE TABLE livro_categoria (
    id_livro INT,
    id_categoria INT,
    PRIMARY KEY (id_livro, id_categoria),
    FOREIGN KEY (id_livro) REFERENCES livro(id_livro),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

SELECT e.id_edit, e.nome_edit
FROM editora e
INNER JOIN livro l ON e.id_edit = l.id_edit
WHERE l.id_livro = 7;


-- TIPO PAGAMENTO
CREATE TABLE tipo_pagamento (
    id_tipo_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    descricao_tipo_pagamento VARCHAR(50) NOT NULL
);

INSERT INTO tipo_pagamento (descricao_tipo_pagamento) VALUES
('CARTAO'),
('CUPOM');

 SELECT * FROM tipo_pagamento


-- PAGAMENTO
CREATE TABLE pagamento (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_pagamento INT,
    valor DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_tipo_pagamento) REFERENCES tipo_pagamento(id_tipo_pagamento)
);

 SELECT * FROM pagamento
 
 -- STATUS DA COMPRA
 CREATE TABLE status_compra (
    id_status_compra INT AUTO_INCREMENT PRIMARY KEY,
    descricao_status_compra VARCHAR(15) NOT NULL
);

INSERT INTO status_compra (descricao_status_compra) VALUES
('ABERTA'),
('APROVADA'),
('REPROVADA'),
('EM_TRANSPORTE'),
('ENTREGUE'),
('EM_TROCA'),
('TROCADO');

SELECT * FROM status_compra

-- TROCA
CREATE TABLE troca (
    id_troca INT AUTO_INCREMENT PRIMARY KEY,
    data_troca DATE NOT NULL
);

-- RELACIONAMENTO ITEM/TROCA
CREATE TABLE troca_item (
    id_troca INT,
    id_item INT,
    PRIMARY KEY (id_troca, id_item),
    FOREIGN KEY (id_troca) REFERENCES troca(id_troca),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

-- VENDA
CREATE TABLE venda (
    id_venda INT AUTO_INCREMENT PRIMARY KEY,
    valor_total DECIMAL(10, 2) NOT NULL,
    cupom_promocional BOOLEAN DEFAULT FALSE
);

SELECT * FROM venda

-- RELACIONAMENTO VENDA/PAGAMENTO
CREATE TABLE venda_pagamento (
    id_venda INT,
    id_pagamento INT,
    PRIMARY KEY (id_venda, id_pagamento),
    FOREIGN KEY (id_venda) REFERENCES venda(id_venda),
    FOREIGN KEY (id_pagamento) REFERENCES pagamento(id_pagamento)
);

SELECT * FROM venda_pagamento

-- PAGAMENTO/CARTAO
CREATE TABLE pagamento_cartao (
    id_pagamento INT,
    id_cartao INT,
    PRIMARY KEY (id_pagamento, id_cartao),
    FOREIGN KEY (id_pagamento) REFERENCES pagamento(id_pagamento),
    FOREIGN KEY (id_cartao) REFERENCES cartao(id)
);

-- PAGAMENTO/CUPOM
CREATE TABLE pagamento_cupom (
    id_pagamento INT,
    id_cupom INT,
    PRIMARY KEY (id_pagamento, id_cupom),
    FOREIGN KEY (id_pagamento) REFERENCES pagamento(id_pagamento),
    FOREIGN KEY (id_cupom) REFERENCES cupom(id_cupom)
);

SELECT * FROM pagamento_cupom


SET SQL_SAFE_UPDATES = 0;

-- Inserção do livro 'Harry Potter e a Pedra Filosofal'
INSERT INTO livro (titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, id_grupo_precificacao, status, id_edit, id_dimensao, justificativa_inativacao, justificativa_ativacao, categoria_inativacao, categoria_ativacao)
VALUES ('Harry Potter e a Pedra Filosofal', 1997, 1, '9780747532699', 223, 'Sinopse do livro...', '9780747532699', 1, TRUE, 6, 6, NULL, 'Novo lançamento', NULL, 1);
UPDATE livro
SET sinopse = 'Harry Potter descobre que é um bruxo e vai estudar na Escola de Magia e Bruxaria de Hogwarts.'
WHERE id_livro = 7;


INSERT INTO livro (titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, id_grupo_precificacao, status, id_edit, id_dimensao, justificativa_inativacao, justificativa_ativacao, categoria_inativacao, categoria_ativacao)
VALUES ('Algoritmo e Lógica de Programação', 2008, 1, '9788534613328', 150, 'Livro que ensina os conceitos básicos de lógica de programação e algoritmos.', '9788534613328', 3, TRUE, 7, 7, NULL, 'Disponível para estudantes de TI', NULL, 1);

INSERT INTO livro (titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, id_grupo_precificacao, status, id_edit, id_dimensao, justificativa_inativacao, justificativa_ativacao, categoria_inativacao, categoria_ativacao)
VALUES ('É Assim que Acaba', 2016, 1, '9788580416351', 320, 'História emocional que explora relacionamentos e superação pessoal.', '9788580416351', 2, TRUE, 8, 8, NULL, 'Disponível para novos leitores', NULL, 1);

INSERT INTO livro (titulo, ano, edicao, isbn, numero_paginas, sinopse, codigo_barras, id_grupo_precificacao, status, id_edit, id_dimensao, justificativa_inativacao, justificativa_ativacao, categoria_inativacao, categoria_ativacao)
VALUES ('A Cabana', 2007, 1, '9788581302589', 256, 'Uma jornada espiritual profunda e transformadora através de uma tragédia familiar.', '9788581302589', 1, TRUE, 9, 9, NULL, 'Disponível para novos leitores', NULL, 1);

SELECT * FROM livro;

INSERT INTO livro_autor (id_livro, id_autor) VALUES
(7, 16);  -- J.K. Rowling

-- Inserir categorias para o livro 'Harry Potter e a Pedra Filosofal'
INSERT INTO livro_categoria (id_livro, id_categoria) VALUES
(7, 6),  -- Fantasia
(7, 8),  -- Aventura
(7, 7); -- Ficcao

-- Inserir autores para o livro 'Algoritmo e Lógica de Programação'
INSERT INTO livro_autor (id_livro, id_autor) VALUES
(8, 13);  -- Ricardo de Oliveira

-- Inserir categorias para o livro 'Algoritmo e Lógica de Programação'
INSERT INTO livro_categoria (id_livro, id_categoria) VALUES
(8, 1),  -- Tecnologia
(8, 14), -- Educacao
(8, 13);  -- Computacao

-- Inserir autores para o livro 'É Assim que Acaba'
INSERT INTO livro_autor (id_livro, id_autor) VALUES
(9, 14);  -- Colleen Hoover

-- Inserir categorias para o livro 'É Assim que Acaba'
INSERT INTO livro_categoria (id_livro, id_categoria) VALUES
(9, 9),  -- Romance
(9, 10);  -- Drama

-- Inserir autores para o livro 'A Cabana'
INSERT INTO livro_autor (id_livro, id_autor) VALUES
(10, 15);  -- William P. Young

-- Inserir categorias para o livro 'A Cabana'
INSERT INTO livro_categoria (id_livro, id_categoria) VALUES
(10, 11),  -- Religiao
(10, 10),  -- Drama
(10, 12);  -- Suspense

CREATE TABLE carrinho_item (
    id_carrinho INT,
    id_item INT,
    quantidade INT NOT NULL,
    PRIMARY KEY (id_carrinho, id_item),
    FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

ALTER TABLE carrinho_item DROP FOREIGN KEY carrinho_item_ibfk_1;
ALTER TABLE carrinho_item DROP PRIMARY KEY;
ALTER TABLE carrinho_item ADD PRIMARY KEY (id_carrinho, id_livro);
ALTER TABLE carrinho_item 
ADD CONSTRAINT fk_carrinho FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho);
ALTER TABLE carrinho_item DROP PRIMARY KEY;

-- adicionar id_livro

SELECT * FROM carrinho_item;
DESCRIBE carrinho_item;
DELETE FROM carrinho_item;
SET SQL_SAFE_UPDATES = 0;

SELECT * FROM item;
SELECT * FROM carrinho;
SELECT * FROM livro;
SELECT * FROM endereco;

ALTER TABLE carrinho_item DROP FOREIGN KEY fk_carrinho;
ALTER TABLE carrinho_item DROP FOREIGN KEY fk_carrinho_livro;
ALTER TABLE carrinho_item DROP PRIMARY KEY;
ALTER TABLE carrinho_item ADD COLUMN id INT AUTO_INCREMENT PRIMARY KEY FIRST;
ALTER TABLE carrinho_item
  ADD CONSTRAINT fk_carrinho FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho),
  ADD CONSTRAINT fk_carrinho_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro);
  
  CREATE TABLE previa_pedido (
    id_previa_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_endereco_entrega INT NOT NULL,
    id_frete INT NOT NULL,
    id_cupom INT, -- opcional
    valor_subtotal DECIMAL(10, 2) NOT NULL,
    valor_frete DECIMAL(10, 2) NOT NULL,
    valor_desconto DECIMAL(10, 2) DEFAULT 0,
    valor_total DECIMAL(10, 2) NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    FOREIGN KEY (id_endereco_entrega) REFERENCES endereco(id),
    FOREIGN KEY (id_frete) REFERENCES frete(id_frete),
    FOREIGN KEY (id_cupom) REFERENCES cupom(id_cupom)
);

CREATE TABLE previa_pedido_item (
    id_previa_pedido INT,
    id_livro_ppi INT,
    quantidade_ppi INT NOT NULL,
    valor_unitario_ppi DECIMAL(10, 2) NOT NULL,
    valor_total_ppi DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_previa_pedido, id_livro_ppi),
    FOREIGN KEY (id_previa_pedido) REFERENCES previa_pedido(id_previa_pedido),
    FOREIGN KEY (id_livro_ppi) REFERENCES livro(id_livro)
);

-- Tabela de Compra (singular)
CREATE TABLE compra (
    id_compra INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT UNSIGNED NOT NULL,
    id_endereco_entrega INT UNSIGNED NOT NULL,
    id_endereco_cobranca INT UNSIGNED NOT NULL,
    valor_subtotal DECIMAL(10, 2) NOT NULL,
    valor_frete DECIMAL(10, 2) NOT NULL,
    valor_desconto_total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    valor_total_compra DECIMAL(10, 2) NOT NULL,
    data_hora_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_id_compra INT UNSIGNED NOT NULL,
    observacoes TEXT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (id_endereco_entrega) REFERENCES endereco(id),
    FOREIGN KEY (id_endereco_cobranca) REFERENCES endereco(id),
    FOREIGN KEY (status_id_compra) REFERENCES status_compra(id_status_compra)
);

-- Tabela de Itens da Compra
CREATE TABLE itens_da_compra (
    id_item_compra INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_compra INT UNSIGNED NOT NULL,
    id_livro INT UNSIGNED NOT NULL,
    quantidade INT UNSIGNED NOT NULL,
    valor_unitario_na_compra DECIMAL(10, 2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra),
    FOREIGN KEY (id_livro) REFERENCES livro(id)
);

-- Tabela de Pagamentos da Compra
CREATE TABLE pagamento_da_compra (
    id_pagamento_compra INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_compra INT UNSIGNED NOT NULL,
    id_cartao INT UNSIGNED NULL,
    id_cupom_usado INT UNSIGNED NULL,
    valor_pago DECIMAL(10, 2) NOT NULL,
    tipo_pagamento VARCHAR(50) NOT NULL, -- 'cartao' ou 'cupom'
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra),
    FOREIGN KEY (id_cartao) REFERENCES cartao(id),
    FOREIGN KEY (id_cupom_usado) REFERENCES cupom(id_cupom)
);