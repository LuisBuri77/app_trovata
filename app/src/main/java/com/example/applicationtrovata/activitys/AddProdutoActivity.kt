package com.example.applicationtrovata.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.ContentValues
import android.view.MenuItem
import android.widget.*
import com.example.applicationtrovata.R
import com.example.applicationtrovata.database.DatabaseHelper

class AddProdutoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var edtEmpresaId: EditText
    private lateinit var edtProdutoId: EditText
    private lateinit var edtDescricaoProduto: EditText
    private lateinit var edtApelidoProduto: EditText
    private lateinit var spinnerGrupoProduto: Spinner
    private lateinit var edtSubgrupoProduto: EditText
    private lateinit var edtSituacao: EditText
    private lateinit var edtPesoLiquido: EditText
    private lateinit var edtClassificacaoFiscal: EditText
    private lateinit var edtCodigoBarras: EditText
    private lateinit var edtColecao: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_registration)

        // Inicializa o DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Vincula os componentes do layout
        edtEmpresaId = findViewById(R.id.edtEmpresaId)
        edtProdutoId = findViewById(R.id.edtProdutoId)
        edtDescricaoProduto = findViewById(R.id.edtDescricaoProduto)
        edtApelidoProduto = findViewById(R.id.edtApelidoProduto)
        spinnerGrupoProduto = findViewById(R.id.spinnerGrupoProduto)
        edtSubgrupoProduto = findViewById(R.id.edtSubgrupoProduto)
        edtSituacao = findViewById(R.id.edtSituacao)
        edtPesoLiquido = findViewById(R.id.edtPesoLiquido)
        edtClassificacaoFiscal = findViewById(R.id.edtClassificacaoFiscal)
        edtCodigoBarras = findViewById(R.id.edtCodigoBarras)
        edtColecao = findViewById(R.id.edtColecao)
        btnSave = findViewById(R.id.btnSave)

        // Carrega os grupos de produto no Spinner
        loadGruposProduto()

        // Configura o botão salvar
        btnSave.setOnClickListener { saveProduct() }

        // Configurar o título com "<" como botão de voltar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilita o botão de voltar
            title = " Produtos" // Adiciona o símbolo "<" no título
        }
    }


    private fun loadGruposProduto() {
        // Obtenha a lista de grupos de produto do banco de dados
        val cursor = dbHelper.getAllGruposProduto()

        // Crie uma lista para armazenar os grupos de produto
        val grupoProdutos = mutableListOf<String>()

        // Itere sobre o cursor e adicione os grupos de produto à lista
        if (cursor.moveToFirst()) {
            do {
                val grupoProduto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRUPO_PRODUTO))
                grupoProdutos.add(grupoProduto)
            } while (cursor.moveToNext())
        }

        // Crie um ArrayAdapter para o Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grupoProdutos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Defina o adapter no Spinner
        spinnerGrupoProduto.adapter = adapter
    }

    private fun saveProduct() {
        // Obtém os valores dos campos
        val empresaId = edtEmpresaId.text.toString().toIntOrNull()
        val produtoId = edtProdutoId.text.toString()
        val descricaoProduto = edtDescricaoProduto.text.toString()
        val apelidoProduto = edtApelidoProduto.text.toString()
        val grupoProduto = spinnerGrupoProduto.selectedItem.toString()
        val subgrupoProduto = edtSubgrupoProduto.text.toString().toIntOrNull()
        val situacao = edtSituacao.text.toString()
        val pesoLiquido = edtPesoLiquido.text.toString().toDoubleOrNull()
        val classificacaoFiscal = edtClassificacaoFiscal.text.toString()
        val codigoBarras = edtCodigoBarras.text.toString()
        val colecao = edtColecao.text.toString()

        // Valida os campos obrigatórios
        if (empresaId == null || produtoId.isBlank() || grupoProduto.isBlank()) {
            Toast.makeText(this, "Preencha os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            return
        }

        // Insere os dados no banco
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.COL_EMPRESA, empresaId)
            put(DatabaseHelper.COL_PRODUTO, produtoId)
            put(DatabaseHelper.COL_DESCRICAO_PRODUTO, descricaoProduto)
            put(DatabaseHelper.COL_APELIDO_PRODUTO, apelidoProduto)
            put(DatabaseHelper.COL_GRUPO_PRODUTO, grupoProduto)
            put(DatabaseHelper.COL_SUBGRUPO_PRODUTO, subgrupoProduto)
            put(DatabaseHelper.COL_SITUACAO, situacao)
            put(DatabaseHelper.COL_PESO_LIQUIDO, pesoLiquido)
            put(DatabaseHelper.COL_CLASSIFICACAO_FISCAL, classificacaoFiscal)
            put(DatabaseHelper.COL_CODIGO_BARRAS, codigoBarras)
            put(DatabaseHelper.COL_COLECAO, colecao)
        }

        val result = dbHelper.insertProduto(contentValues)

        if (result != -1L) {
            Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            clearFields()
        } else {
            Toast.makeText(this, "Erro ao cadastrar o produto.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        edtEmpresaId.text.clear()
        edtProdutoId.text.clear()
        edtDescricaoProduto.text.clear()
        edtApelidoProduto.text.clear()
        edtSubgrupoProduto.text.clear()
        edtSituacao.text.clear()
        edtPesoLiquido.text.clear()
        edtClassificacaoFiscal.text.clear()
        edtCodigoBarras.text.clear()
        edtColecao.text.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // ID do botão de voltar
                finish() // Finaliza a Activity e retorna à anterior
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}