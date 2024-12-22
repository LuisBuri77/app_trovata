package com.example.applicationtrovata.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val context: Context = context.applicationContext

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys=ON;")

        // Criar tabelas na ordem correta devido às foreign keys
        db.execSQL(CREATE_EMPRESA_TABLE)
        db.execSQL(CREATE_TIPO_COMPLEMENTO_TABLE)
        db.execSQL(CREATE_GRUPO_PRODUTO_TABLE)
        db.execSQL(CREATE_PRODUTO_TABLE)

        // Executar arquivo SQL inicial
        executeInitialSQL(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Remover tabelas na ordem inversa devido às foreign keys
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUTO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GRUPO_PRODUTO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TIPO_COMPLEMENTO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPRESA")
        onCreate(db)
    }

    private fun executeInitialSQL(db: SQLiteDatabase) {
        try {
            val inputStream = context.assets.open("initial_data.sql")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()

            val sql = String(buffer, Charsets.UTF_8)
            val statements = sql.split(";")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            db.beginTransaction()
            try {
                statements.forEach { statement ->
                    if (statement.isNotEmpty()) {
                        db.execSQL(statement)
                    }
                }
                db.setTransactionSuccessful()
                Log.d("DatabaseHelper", "Initial SQL executed successfully")
            } catch (e: Exception) {
                Log.e("DatabaseHelper", "Error executing initial SQL: ${e.message}")
            } finally {
                db.endTransaction()
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error reading SQL file: ${e.message}")
        }
    }

    // EMPRESA CRUD Operations
    fun insertEmpresa(empresa: ContentValues): Long {
        val db = this.writableDatabase
        return db.insert(TABLE_EMPRESA, null, empresa)
    }

    fun updateEmpresa(empresa: ContentValues, id: Int): Int {
        val db = this.writableDatabase
        return db.update(TABLE_EMPRESA, empresa, "$COL_EMPRESA = ?", arrayOf(id.toString()))
    }

    fun deleteEmpresa(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_EMPRESA, "$COL_EMPRESA = ?", arrayOf(id.toString()))
    }

    fun getEmpresa(id: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_EMPRESA, null,
            "$COL_EMPRESA = ?", arrayOf(id.toString()),
            null, null, null
        )
    }

    fun getAllEmpresas(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_EMPRESA, null, null, null, null, null, null)
    }

    // TIPO_COMPLEMENTO CRUD Operations
    fun insertTipoComplemento(tipoComplemento: ContentValues): Long {
        val db = this.writableDatabase
        return db.insert(TABLE_TIPO_COMPLEMENTO, null, tipoComplemento)
    }

    fun updateTipoComplemento(tipoComplemento: ContentValues, empresaId: Int, tipo: String): Int {
        val db = this.writableDatabase
        return db.update(
            TABLE_TIPO_COMPLEMENTO,
            tipoComplemento,
            "$COL_EMPRESA = ? AND $COL_TIPO_COMPLEMENTO = ?",
            arrayOf(empresaId.toString(), tipo)
        )
    }

    fun deleteTipoComplemento(empresaId: Int, tipo: String): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_TIPO_COMPLEMENTO,
            "$COL_EMPRESA = ? AND $COL_TIPO_COMPLEMENTO = ?",
            arrayOf(empresaId.toString(), tipo)
        )
    }

    fun getTipoComplemento(empresaId: Int, tipo: String): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_TIPO_COMPLEMENTO, null,
            "$COL_EMPRESA = ? AND $COL_TIPO_COMPLEMENTO = ?",
            arrayOf(empresaId.toString(), tipo),
            null, null, null
        )
    }

    fun getTiposComplementoByEmpresa(empresaId: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_TIPO_COMPLEMENTO, null,
            "$COL_EMPRESA = ?", arrayOf(empresaId.toString()),
            null, null, null
        )
    }

    // GRUPO_PRODUTO CRUD Operations
    fun insertGrupoProduto(grupoProduto: ContentValues): Long {
        val db = this.writableDatabase
        return db.insert(TABLE_GRUPO_PRODUTO, null, grupoProduto)
    }

    fun updateGrupoProduto(grupoProduto: ContentValues, empresaId: Int, grupoId: Int): Int {
        val db = this.writableDatabase
        return db.update(
            TABLE_GRUPO_PRODUTO,
            grupoProduto,
            "$COL_EMPRESA = ? AND $COL_GRUPO_PRODUTO = ?",
            arrayOf(empresaId.toString(), grupoId.toString())
        )
    }

    fun deleteGrupoProduto(empresaId: Int, grupoId: Int): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_GRUPO_PRODUTO,
            "$COL_EMPRESA = ? AND $COL_GRUPO_PRODUTO = ?",
            arrayOf(empresaId.toString(), grupoId.toString())
        )
    }

    fun getGrupoProduto(empresaId: Int, grupoId: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_GRUPO_PRODUTO, null,
            "$COL_EMPRESA = ? AND $COL_GRUPO_PRODUTO = ?",
            arrayOf(empresaId.toString(), grupoId.toString()),
            null, null, null
        )
    }

    fun getGruposProdutoByEmpresa(empresaId: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_GRUPO_PRODUTO, null,
            "$COL_EMPRESA = ?", arrayOf(empresaId.toString()),
            null, null, null
        )
    }

    // PRODUTO CRUD Operations
    fun insertProduto(produto: ContentValues): Long {
        val db = this.writableDatabase
        return db.insert(TABLE_PRODUTO, null, produto)
    }

    fun updateProduto(produto: ContentValues, empresaId: Int, produtoId: String): Int {
        val db = this.writableDatabase
        return db.update(
            TABLE_PRODUTO,
            produto,
            "$COL_EMPRESA = ? AND $COL_PRODUTO = ?",
            arrayOf(empresaId.toString(), produtoId)
        )
    }

    fun deleteProduto(empresaId: Int, produtoId: String): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_PRODUTO,
            "$COL_EMPRESA = ? AND $COL_PRODUTO = ?",
            arrayOf(empresaId.toString(), produtoId)
        )
    }

    fun getAllGruposProduto(): Cursor {
        val db = this.readableDatabase
        return db.query(
            true, // Garante que os resultados sejam distintos
            TABLE_PRODUTO, // Nome da tabela
            arrayOf(COL_GRUPO_PRODUTO), // Colunas que queremos (somente o GRUPO_PRODUTO)
            null, // Sem cláusula WHERE
            null, // Sem argumentos para WHERE
            null, // Sem GROUP BY
            null, // Sem HAVING
            "$COL_GRUPO_PRODUTO ASC", // Ordenar por GRUPO_PRODUTO em ordem crescente
            null // Sem limite
        )
    }

    fun getProduto(empresaId: Int, produtoId: String): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_PRODUTO, null,
            "$COL_EMPRESA = ? AND $COL_PRODUTO = ?",
            arrayOf(empresaId.toString(), produtoId),
            null, null, null
        )
    }

    fun getProdutosByEmpresa(empresaId: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_PRODUTO, null,
            "$COL_EMPRESA = ?", arrayOf(empresaId.toString()),
            null, null, null
        )
    }

    fun getProdutosByGrupo(empresaId: Int, grupoId: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_PRODUTO, null,
            "$COL_EMPRESA = ? AND $COL_GRUPO_PRODUTO = ?",
            arrayOf(empresaId.toString(), grupoId.toString()),
            null, null, null
        )
    }

    companion object {
        private const val DATABASE_NAME = "empresas.db"
        private const val DATABASE_VERSION = 1

        // Nomes das tabelas e colunas
        const val TABLE_EMPRESA = "empresa"
        const val COL_EMPRESA = "empresa"
        const val COL_NOME_FANTASIA = "nome_fantasia"
        const val COL_RAZAO_SOCIAL = "razao_social"
        const val COL_ENDERECO = "endereco"
        const val COL_BAIRRO = "bairro"
        const val COL_CEP = "cep"
        const val COL_CIDADE = "cidade"
        const val COL_TELEFONE = "telefone"
        const val COL_FAX = "fax"
        const val COL_CNPJ = "cnpj"
        const val COL_IE = "ie"

        const val TABLE_TIPO_COMPLEMENTO = "tipo_complemento"
        const val COL_TIPO_COMPLEMENTO = "tipo_complemento"
        const val COL_DESCRICAO_TIPO_COMPLEMENTO = "descricao_tipo_complemento"

        const val TABLE_GRUPO_PRODUTO = "grupo_produto"
        const val COL_GRUPO_PRODUTO = "grupo_produto"
        const val COL_DESCRICAO_GRUPO_PRODUTO = "descricao_grupo_produto"
        const val COL_PERC_DESCONTO = "perc_desconto"

        const val TABLE_PRODUTO = "produto"
        const val COL_PRODUTO = "produto"
        const val COL_DESCRICAO_PRODUTO = "descricao_produto"
        const val COL_APELIDO_PRODUTO = "apelido_produto"
        const val COL_SUBGRUPO_PRODUTO = "subgrupo_produto"
        const val COL_SITUACAO = "situacao"
        const val COL_PESO_LIQUIDO = "peso_liquido"
        const val COL_CLASSIFICACAO_FISCAL = "classificacao_fiscal"
        const val COL_CODIGO_BARRAS = "codigo_barras"
        const val COL_COLECAO = "colecao"

        // Criar tabelas
        private const val CREATE_EMPRESA_TABLE = """
            CREATE TABLE $TABLE_EMPRESA (
                $COL_EMPRESA INTEGER NOT NULL PRIMARY KEY,
                $COL_NOME_FANTASIA VARCHAR(30),
                $COL_RAZAO_SOCIAL VARCHAR(50),
                $COL_ENDERECO VARCHAR(50),
                $COL_BAIRRO VARCHAR(30),
                $COL_CEP VARCHAR(10),
                $COL_CIDADE VARCHAR(250),
                $COL_TELEFONE VARCHAR(15),
                $COL_FAX VARCHAR(15),
                $COL_CNPJ VARCHAR(18),
                $COL_IE VARCHAR(18)
            )
        """

        private const val CREATE_TIPO_COMPLEMENTO_TABLE = """
            CREATE TABLE $TABLE_TIPO_COMPLEMENTO (
                $COL_EMPRESA INTEGER NOT NULL,
                $COL_TIPO_COMPLEMENTO VARCHAR(3) NOT NULL,
                $COL_DESCRICAO_TIPO_COMPLEMENTO VARCHAR(30),
                PRIMARY KEY ($COL_EMPRESA, $COL_TIPO_COMPLEMENTO),
                FOREIGN KEY ($COL_EMPRESA) REFERENCES $TABLE_EMPRESA($COL_EMPRESA)
            )
        """

        private const val CREATE_GRUPO_PRODUTO_TABLE = """
            CREATE TABLE $TABLE_GRUPO_PRODUTO (
                $COL_EMPRESA INTEGER NOT NULL,
                $COL_GRUPO_PRODUTO INTEGER NOT NULL,
                $COL_DESCRICAO_GRUPO_PRODUTO VARCHAR(50),
                $COL_PERC_DESCONTO NUMERIC(5,2),
                $COL_TIPO_COMPLEMENTO VARCHAR(3),
                PRIMARY KEY ($COL_EMPRESA, $COL_GRUPO_PRODUTO),
                FOREIGN KEY ($COL_EMPRESA) REFERENCES $TABLE_EMPRESA($COL_EMPRESA),
                FOREIGN KEY ($COL_EMPRESA, $COL_TIPO_COMPLEMENTO) 
                REFERENCES $TABLE_TIPO_COMPLEMENTO($COL_EMPRESA, $COL_TIPO_COMPLEMENTO)
            )
        """

        private const val CREATE_PRODUTO_TABLE = """
            CREATE TABLE $TABLE_PRODUTO (
                $COL_EMPRESA INTEGER NOT NULL,
                $COL_PRODUTO VARCHAR(15) NOT NULL,
                $COL_DESCRICAO_PRODUTO VARCHAR(50),
                $COL_APELIDO_PRODUTO VARCHAR(30),
                $COL_GRUPO_PRODUTO INTEGER,
                $COL_SUBGRUPO_PRODUTO INTEGER,
                $COL_SITUACAO VARCHAR(1),
                $COL_PESO_LIQUIDO NUMERIC(11,3),
                $COL_CLASSIFICACAO_FISCAL VARCHAR(10),
                $COL_CODIGO_BARRAS VARCHAR(50),
                $COL_COLECAO VARCHAR(100),
                PRIMARY KEY ($COL_EMPRESA, $COL_PRODUTO),
                FOREIGN KEY ($COL_EMPRESA) REFERENCES $TABLE_EMPRESA($COL_EMPRESA),
                FOREIGN KEY ($COL_EMPRESA, $COL_GRUPO_PRODUTO) 
                REFERENCES $TABLE_GRUPO_PRODUTO($COL_EMPRESA, $COL_GRUPO_PRODUTO)
            )
        """
    }
}