INSERT INTO EMPRESA (EMPRESA, NOME_FANTASIA, RAZAO_SOCIAL, ENDERECO, BAIRRO, CEP, CIDADE, TELEFONE, CNPJ, IE) VALUES (1,'ROMA VENDAS ONLINE', 'ROMA VENDAS LTDA', 'RUA NELSON CALIXTO 142', 'PARQUE SAO VICENTE', '16200-320', 1,'(18)3644-7333','88.060.431/0001-94','ISENTO');
INSERT INTO EMPRESA (EMPRESA, NOME_FANTASIA, RAZAO_SOCIAL, ENDERECO, BAIRRO, CEP, CIDADE, TELEFONE, CNPJ, IE) VALUES (2,'MILANO VENDAS OFFLINE', 'MILANO VENDAS OFFLINE LTDA', 'RUA BELMONTE, 334', 'VILA MARIANA', '16334-532', 45,'(19)3523-5232','26.523.811/0001-60','ISENTO');

INSERT INTO PRODUTO (
    EMPRESA,
    PRODUTO,
    DESCRICAO_PRODUTO,
    APELIDO_PRODUTO,
    GRUPO_PRODUTO,
    SUBGRUPO_PRODUTO,
    SITUACAO,
    PESO_LIQUIDO,
    CLASSIFICACAO_FISCAL,
    CODIGO_BARRAS,
    COLECAO
) VALUES
    (1, '4', 'ALMOFADA VISCO PESCOÇO 28X30CM ROSA CX:', '10060305', 355, 0, 'A', 0.224, '4', NULL, 1),
    (2, '6', 'BANDEJA PARA COLO CARIMBOS MICROPEROLAS 50X40CM CARIMBOS CX:8', '10020025', 357, 0, 'A', 0.71, '6', NULL, 1),
    (2, '8', 'BANDEJA PARA COLO CARIMBOS MICROPEROLAS 50X40CM HQ CX:8', '10020026', 357, 0, 'A', 0.71, '8', NULL, 1),
    (2, '25', 'KIT 2 XICARAS C/ PIRES 160ML BONE CHINA ETERNO AMOR CX:24', '10020027', 357, 0, 'A', 1.337, '25', NULL, 1);

INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,1,'COR');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,2,'TAMANHO');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,3,'ESTAMPA');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,4,'MATERIAL');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,1,'COR');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,22,'TAMANHO');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,3,'ESTAMPA');
INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,4,'MATERIAL');


INSERT INTO GRUPO_PRODUTO (
    EMPRESA,
    GRUPO_PRODUTO,
    DESCRICAO_GRUPO_PRODUTO,
    PERC_DESCONTO,
    TIPO_COMPLEMENTO
) VALUES
    (1, 2, 'TECIDO', NULL, '1'),
    (1, 3, 'EMBALAGENS', NULL, '4'),
    (1, 4, 'FIBRA', NULL, '3'),
    (1, 51, 'FRONHA', NULL, '1'),
    (1, 355, 'ALMOFADA', NULL, '3'),
    (1, 359, 'DECORAÇAO', NULL, '4'),
    (1, 361, 'ELETRONICOS', NULL, '4'),
    (1, 363, 'PESSOAL', NULL, '4'),
    (1, 383, 'JOGOS', NULL, '3'),
    (1, 389, 'TRAVESSEIROS', NULL, '2'),
    (2, 40130843, 'ACESSÓRIOS PARA CELULAR', NULL, '12'),
    (1, 40130849, 'CARTÃO CD', NULL, '12'),
    (2, 40130859, 'MP DIVERSAS', NULL, '2'),
    (1, 90294395, 'VESTE CRIATIVA', NULL, '2'),
    (1, 90294397, 'ESCRITORIO', NULL, '1'),
    (1, 110359819, 'MASTER COMFORT', NULL, '3'),
    (1, 120392521, 'PRODUTOS DE 2ª QUALIDADE', NULL, '4'),
    (1, 150490651, 'KIT VIAGEM', NULL, '3'),
    (1, 150490653, 'KIT', NULL, '1'),
    (1, 150490655, 'COLCHA', NULL, '4'),
    (1, 150490657, 'JG DE CAMA SOLTEIRO', NULL, '2'),
    (1, 150490661, 'DOCES', NULL, '3'),
    (1, 511190670, 'KIT PRESENTE', NULL, '4'),
    (1, 511190672, 'CORTE MESA', NULL, '2'),
    (1, 511190690, 'REFIL', NULL, '1'),
    (1, 404, 'CANECAS PERSONALIZADAS', NULL, '2'),
    (2, 357, 'CASA', NULL, '3');

