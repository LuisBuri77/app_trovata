package com.example.applicationtrovata

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationtrovata.database.DatabaseHelper

class EmpresaListAdapter(
    private var cursor: Cursor,
    private val onItemClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<EmpresaListAdapter.EmpresaViewHolder>() {

    inner class EmpresaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val razaoSocialTextView: TextView = view.findViewById(R.id.textViewRazaoSocial)
        val cidadeTextView: TextView = view.findViewById(R.id.textViewCidade)
        val editButton: ImageButton = view.findViewById(R.id.buttonEdit)
        val deleteButton: ImageButton = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empresa, parent, false)
        return EmpresaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        if (cursor.moveToPosition(position)) {
            val empresaId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMPRESA))
            val razaoSocial = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RAZAO_SOCIAL))
            val cidade = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CIDADE))

            holder.razaoSocialTextView.text = razaoSocial
            holder.cidadeTextView.text = cidade

            holder.itemView.setOnClickListener { onItemClick(empresaId) }
            holder.editButton.setOnClickListener { onEditClick(empresaId) }
            holder.deleteButton.setOnClickListener { onDeleteClick(empresaId) }
        }
    }

    override fun getItemCount(): Int = cursor.count

    fun updateCursor(newCursor: Cursor) {
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }

    fun closeCursor() {
        cursor.close()
    }
}