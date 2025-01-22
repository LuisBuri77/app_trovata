# App Trovata 🚀

O **App Trovata** é um aplicativo Android desenvolvido em **Java** e **Kotlin**. Este projeto foi criado com fins de estudo, sendo uma tentativa inicial de explorar o uso do Kotlin em aplicações Android. O objetivo principal é a gestão de dados empresariais e de produtos, com suporte para inicialização de dados utilizando um arquivo SQL.

## ✨ Funcionalidades

- 🏢 **Cadastro de Empresas**: Incluindo dados como nome fantasia, razão social, endereço, telefone, e CNPJ.
- 📦 **Cadastro de Produtos**: Com informações detalhadas como descrição, apelido, grupo, subgrupo, peso líquido, e classificação fiscal.
- 📍 **Autocompletar Endereço com o CEP**: Ao inserir um CEP válido, o aplicativo preenche automaticamente os campos de endereço correspondentes, otimizando o cadastro.
- 💾 **Persistência de Dados**: Carregamento inicial de dados usando SQL.

## 🛠 Melhorias Planejadas

- 🖼️ **Adicionar Imagem para Cada Produto**: Permitir que cada produto tenha uma imagem associada para facilitar a identificação visual.
- 📄 **Paginação de Produtos**: Implementar paginação para melhorar a navegação quando houver uma grande quantidade de produtos.
- 🔍 **Filtro de Produtos**: Criar um sistema de busca e filtros para facilitar a localização de produtos por categoria, nome ou outros atributos.
- 🎨 **Melhoria no Design da Interface (UI/UX)**: Atualizar o design usando melhores práticas no código XML para proporcionar uma experiência mais moderna e intuitiva.

## 📂 Dados de Inicialização

O aplicativo utiliza o arquivo `initial_data.sql` para popular o banco de dados com informações iniciais. Abaixo está o conteúdo do arquivo, que mostra os tipos de dados gerenciados:

```sql
INSERT INTO EMPRESA (EMPRESA, NOME_FANTASIA, RAZAO_SOCIAL, ENDERECO, BAIRRO, CEP, CIDADE, TELEFONE, CNPJ, IE) VALUES
(1, 'ROMA VENDAS ONLINE', 'ROMA VENDAS LTDA', 'RUA NELSON CALIXTO 142', 'PARQUE SAO VICENTE', '16200-320', 1, '(18)3644-7333', '88.060.431/0001-94', 'ISENTO'),
(2, 'MILANO VENDAS OFFLINE', 'MILANO VENDAS OFFLINE LTDA', 'RUA BELMONTE, 334', 'VILA MARIANA', '16334-532', 45, '(19)3523-5232', '26.523.811/0001-60', 'ISENTO');

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
  (2, '6', 'ALMOFADA VISCO PESCOÇO 28X30CM AZUL CX:', '10060306', 355, 0, 'A', 0.224, '6', NULL, 1);
```

### 🗂 Estrutura das Tabelas

#### 🏢 EMPRESA
- **EMPRESA**: Identificador único da empresa.
- **NOME_FANTASIA**: Nome de uso comercial da empresa.
- **RAZAO_SOCIAL**: Razão social registrada.
- **ENDERECO**: Endereço completo.
- **BAIRRO**: Bairro de localização.
- **CEP**: Código postal.
- **CIDADE**: Identificador da cidade.
- **TELEFONE**: Contato telefônico.
- **CNPJ**: Cadastro Nacional de Pessoa Jurídica.
- **IE**: Inscrição Estadual.

#### 📦 PRODUTO
- **EMPRESA**: Identificador da empresa associada ao produto.
- **PRODUTO**: Código único do produto.
- **DESCRICAO_PRODUTO**: Descrição detalhada.
- **APELIDO_PRODUTO**: Nome curto ou código.
- **GRUPO_PRODUTO**: Categoria principal.
- **SUBGRUPO_PRODUTO**: Subcategoria.
- **SITUACAO**: Estado do produto (ex.: ativo/inativo).
- **PESO_LIQUIDO**: Peso líquido em quilogramas.
- **CLASSIFICACAO_FISCAL**: Código fiscal.
- **CODIGO_BARRAS**: Código de barras.
- **COLECAO**: Identificador da coleção.

## 🛠 Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone https://github.com/LuisBuri77/app_trovata.git
   ```
2. Abra o Android Studio e selecione "Open an existing project".
3. Navegue até o diretório clonado e selecione a pasta do projeto.
4. Aguarde a sincronização do Gradle e a construção do projeto.
5. Execute o aplicativo em um dispositivo ou emulador Android.

## 📜 Licença

Este projeto está licenciado sob a [licença que você escolher].

---

💡 Sinta-se à vontade para contribuir, relatar problemas ou sugerir melhorias! 😊

