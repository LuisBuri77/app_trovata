package com.example.applicationtrovata.activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationtrovata.EmpresaListAdapter
import com.example.applicationtrovata.R
import com.example.applicationtrovata.database.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmpresasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmpresaListAdapter
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresas)

        dbHelper = DatabaseHelper(this)
        setupRecyclerView()
        setupFab()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewEmpresas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadEmpresas()
    }

    private fun setupFab() {
        fab = findViewById(R.id.fabAddEmpresa)
        fab.setOnClickListener {
            startActivityForResult(
                Intent(this, AddEmpresaActivity::class.java),
                REQUEST_ADD_EMPRESA
            )
        }
    }

    private fun loadEmpresas() {
        val cursor = dbHelper.getAllEmpresas()
        adapter = EmpresaListAdapter(
            cursor = cursor,
            onItemClick = { empresaId ->
                val intent = Intent(this, ProdutosActivity::class.java)
                intent.putExtra("EMPRESA_ID", empresaId)
                startActivity(intent)
            },
            onEditClick = { empresaId ->
                val intent = Intent(this, AddEmpresaActivity::class.java)
                intent.putExtra("EMPRESA_ID", empresaId)
                startActivityForResult(intent, REQUEST_EDIT_EMPRESA)
            },
            onDeleteClick = { empresaId ->
                showDeleteConfirmationDialog(empresaId)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun showDeleteConfirmationDialog(empresaId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Tem certeza que deseja excluir esta empresa? Todos os produtos relacionados também serão excluídos.")
            .setPositiveButton("Sim") { _, _ ->
                deleteEmpresa(empresaId)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deleteEmpresa(empresaId: Int) {
        dbHelper.deleteEmpresa(empresaId)
        loadEmpresas() // Recarrega a lista
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            (requestCode == REQUEST_ADD_EMPRESA || requestCode == REQUEST_EDIT_EMPRESA)
        ) {
            loadEmpresas()
        }
    }

    override fun onResume() {
        super.onResume()
        loadEmpresas() // Recarrega a lista quando a atividade volta ao primeiro plano
    }

    override fun onDestroy() {
        adapter.closeCursor()
        dbHelper.close()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_ADD_EMPRESA = 1
        private const val REQUEST_EDIT_EMPRESA = 2
    }
    }