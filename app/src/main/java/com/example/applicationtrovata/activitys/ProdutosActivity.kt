package com.example.applicationtrovata.activitys


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationtrovata.ProdutoAdapter
import com.example.applicationtrovata.R
import com.example.applicationtrovata.database.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProdutosActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter
    private lateinit var fab: FloatingActionButton
    private var empresaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)

        dbHelper = DatabaseHelper(this)
        empresaId = intent.getIntExtra("EMPRESA_ID", -1)

        if (empresaId == -1) {
            Toast.makeText(this, "Empresa inválida.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configurar o título com "<" como botão de voltar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilita o botão de voltar
            title = " Produtos" // Adiciona o símbolo "<" no título
        }

        setupRecyclerView()
        setupFab()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView) // Associa o RecyclerView do layout.
        recyclerView.layoutManager = LinearLayoutManager(this) // Define o LayoutManager como linear.
        recyclerView.setHasFixedSize(true) // Otimiza o RecyclerView se o tamanho for fixo.
        loadProdutos() // Carrega os produtos e configura o adaptador.
    }

    private fun setupFab() {
        fab = findViewById(R.id.fab) // Associa o FloatingActionButton do layout.
        fab.setOnClickListener {
            val intent = Intent(this, AddProdutoActivity::class.java)
            intent.putExtra("EMPRESA_ID", empresaId) // Passa o ID da empresa para a tela de cadastro.
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.produtos_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // ID do botão de voltar
                finish() // Finaliza a Activity e volta para a anterior
                true
            }
            R.id.sort_by_description -> {
                adapter.sortByDescription()
                true
            }
            R.id.sort_by_code -> {
                adapter.sortByCode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadProdutos() {
        try {
            val cursor = dbHelper.getProdutosByEmpresa(empresaId)
            adapter = ProdutoAdapter(
                cursor = cursor,
                onDelete = { produtoId -> deleteProduto(produtoId) },
                onEdit = { produtoId ->
                    val intent = Intent(this, AddProdutoActivity::class.java)
                    intent.putExtra("EMPRESA_ID", empresaId)
                    intent.putExtra("PRODUTO_ID", produtoId)
                    startActivity(intent)
                }
            )
            recyclerView.adapter = adapter
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao carregar produtos: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProduto(produtoId: String) {
        // Exibe o AlertDialog de confirmação
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmação de Exclusão")
        builder.setMessage("Você tem certeza de que deseja excluir este produto?")
        builder.setPositiveButton("Sim") { _, _ ->
            // Executa a exclusão se o usuário confirmar
            val rowsDeleted = dbHelper.deleteProduto(empresaId, produtoId)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao excluir o produto.", Toast.LENGTH_SHORT).show()
            }
            loadProdutos() // Atualiza a lista após a exclusão
        }
        builder.setNegativeButton("Não") { dialog, _ ->
            // Cancela a ação e fecha o diálogo
            dialog.dismiss()
        }
        // Exibe o diálogo na tela
        builder.create().show()
    }

    override fun onResume() {
        super.onResume()
        loadProdutos()
    }

    override fun onDestroy() {
        adapter.closeCursor()
        dbHelper.close()
        super.onDestroy()
    }
}