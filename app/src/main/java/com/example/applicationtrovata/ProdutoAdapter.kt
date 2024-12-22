package com.example.applicationtrovata

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationtrovata.database.DatabaseHelper

class ProdutoAdapter(
    private var cursor: Cursor,
    private val onDelete: (String) -> Unit,
    private val onEdit: (String) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {


    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descricaoTextView: TextView = view.findViewById(R.id.textViewDescricao)
        val codigoTextView: TextView = view.findViewById(R.id.textViewCodigo)
        val deleteButton: View = view.findViewById(R.id.btnDelete)
        val editButton: View = view.findViewById(R.id.btnEdit)
    }

    private var sortedCursor: Cursor = cursor

    fun sortByDescription() {
        val produtos = mutableListOf<Triple<String, String, Cursor>>()

        if (cursor.moveToFirst()) {
            do {
                val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao_produto"))
                val codigo = cursor.getString(cursor.getColumnIndexOrThrow("produto"))
                produtos.add(Triple(descricao, codigo, cursor))
            } while (cursor.moveToNext())
        }

        produtos.sortBy { it.first.lowercase() } // Ordena por descrição
        updateSortedCursor(produtos)
    }

    fun sortByCode() {
        val produtos = mutableListOf<Triple<String, String, Cursor>>()

        if (cursor.moveToFirst()) {
            do {
                val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao_produto"))
                val codigo = cursor.getString(cursor.getColumnIndexOrThrow("produto"))
                produtos.add(Triple(descricao, codigo, cursor))
            } while (cursor.moveToNext())
        }

        produtos.sortBy { it.second } // Ordena por código
        updateSortedCursor(produtos)
    }

    private fun updateSortedCursor(sortedList: List<Triple<String, String, Cursor>>) {
        // Notifica o RecyclerView que os dados mudaram
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        cursor.moveToPosition(position)

        val produtoId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUTO))
        val descricao = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRICAO_PRODUTO))

        holder.codigoTextView.text = produtoId
        holder.descricaoTextView.text = descricao

        holder.deleteButton.setOnClickListener { onDelete(produtoId) }
        holder.editButton.setOnClickListener { onEdit(produtoId) }
    }

    override fun getItemCount(): Int = cursor.count

    fun closeCursor() {
        cursor.close()
    }
}
