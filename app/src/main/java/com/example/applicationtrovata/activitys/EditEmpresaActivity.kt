package com.example.applicationtrovata.activitys

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.applicationtrovata.R
import com.example.applicationtrovata.database.DatabaseHelper
import com.example.applicationtrovata.utils.CnpjValidator
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditEmpresaActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var empresaId: Int = -1

    private lateinit var editTextNomeFantasia: TextInputEditText
    private lateinit var editTextEndereco: TextInputEditText
    private lateinit var editTextBairro: TextInputEditText
    private lateinit var editTextCep: TextInputEditText
    private lateinit var editTextCidade: TextInputEditText
    private lateinit var editTextTelefone: TextInputEditText
    private lateinit var editTextFax: TextInputEditText
    private lateinit var editTextCnpj: TextInputEditText
    private lateinit var editTextIe: TextInputEditText
    private lateinit var btnSalvar: MaterialButton
    private lateinit var inputLayoutCnpj: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicao_empresa)

        empresaId = intent.getIntExtra("EMPRESA_ID", -1)
        if (empresaId == -1) {
            Toast.makeText(this, "Erro ao carregar empresa", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        dbHelper = DatabaseHelper(this)
        setupViews()
        setupToolbar()
        setupCnpjValidation()
        carregarDadosEmpresa()
    }

    private fun setupViews() {
        editTextNomeFantasia = findViewById(R.id.editTextNomeFantasia)
        editTextEndereco = findViewById(R.id.editTextEndereco)
        editTextBairro = findViewById(R.id.editTextBairro)
        editTextCep = findViewById(R.id.editTextCep)
        editTextCidade = findViewById(R.id.editTextCidade)
        editTextTelefone = findViewById(R.id.editTextTelefone)
        editTextFax = findViewById(R.id.editTextFax)
        editTextCnpj = findViewById(R.id.editTextCnpj)
        editTextIe = findViewById(R.id.editTextIe)
        btnSalvar = findViewById(R.id.buttonSave)
        inputLayoutCnpj = findViewById(R.id.inputLayoutCnpj)

        btnSalvar.setOnClickListener {
            if (validarCampos()) {
                atualizarEmpresa()
            }
        }
    }

    private fun setupCnpjValidation() {
        editTextCnpj.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().isNotEmpty()) {
                    val cnpj = s.toString().replace(Regex("[^0-9]"), "")
                    if (cnpj.length <= 14) {
                        val formatted = CnpjValidator.formatCnpj(cnpj)
                        if (formatted != s.toString()) {
                            editTextCnpj.removeTextChangedListener(this)
                            editTextCnpj.setText(formatted)
                            editTextCnpj.setSelection(formatted.length)
                            editTextCnpj.addTextChangedListener(this)
                        }

                        // Validate and show error if needed
                        if (cnpj.length == 14 && !CnpjValidator.isValid(cnpj)) {
                            inputLayoutCnpj.error = "CNPJ inválido"
                            inputLayoutCnpj.setErrorTextColor(
                                ContextCompat.getColorStateList(
                                    this@EditEmpresaActivity,
                                    android.R.color.holo_red_dark
                                )
                            )
                        } else {
                            inputLayoutCnpj.error = null
                        }
                    }
                } else {
                    inputLayoutCnpj.error = null
                }
            }
        })
    }

    private fun validarCampos(): Boolean {
        var isValid = true

        // Validate Nome Fantasia
        if (editTextNomeFantasia.text.isNullOrBlank()) {
            editTextNomeFantasia.error = "Campo obrigatório"
            isValid = false
        }

        // Validate Endereco
        if (editTextEndereco.text.isNullOrBlank()) {
            editTextEndereco.error = "Campo obrigatório"
            isValid = false
        }

        // Validate Bairro
        if (editTextBairro.text.isNullOrBlank()) {
            editTextBairro.error = "Campo obrigatório"
            isValid = false
        }

        // Validate CEP
        if (editTextCep.text.isNullOrBlank()) {
            editTextCep.error = "Campo obrigatório"
            isValid = false
        }

        // Validate Cidade
        if (editTextCidade.text.isNullOrBlank()) {
            editTextCidade.error = "Campo obrigatório"
            isValid = false
        }

        // Validate Telefone
        if (editTextTelefone.text.isNullOrBlank()) {
            editTextTelefone.error = "Campo obrigatório"
            isValid = false
        }

        // Validate CNPJ
        if (editTextCnpj.text.isNullOrBlank()) {
            inputLayoutCnpj.error = "Campo obrigatório"
            isValid = false
        } else {
            val cnpj = editTextCnpj.text.toString().replace(Regex("[^0-9]"), "")
            if (!CnpjValidator.isValid(cnpj)) {
                inputLayoutCnpj.error = "CNPJ inválido"
                isValid = false
            }
        }

        // Validate IE
        if (editTextIe.text.isNullOrBlank()) {
            editTextIe.error = "Campo obrigatório"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios corretamente", Toast.LENGTH_LONG).show()
        }

        return isValid
    }

    private fun carregarDadosEmpresa() {
        val cursor = dbHelper.getEmpresa(empresaId)
        cursor?.use {
            if (it.moveToFirst()) {
                editTextNomeFantasia.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_NOME_FANTASIA)))
                editTextEndereco.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_ENDERECO)))
                editTextBairro.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_BAIRRO)))
                editTextCep.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_CEP)))
                editTextCidade.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_CIDADE)))
                editTextTelefone.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_TELEFONE)))
                editTextFax.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_FAX)))
                editTextCnpj.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_CNPJ)))
                editTextIe.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_IE)))
            }
        }
    }

    private fun atualizarEmpresa() {
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_NOME_FANTASIA, editTextNomeFantasia.text.toString().trim())
            put(DatabaseHelper.COL_ENDERECO, editTextEndereco.text.toString().trim())
            put(DatabaseHelper.COL_BAIRRO, editTextBairro.text.toString().trim())
            put(DatabaseHelper.COL_CEP, editTextCep.text.toString().trim())
            put(DatabaseHelper.COL_CIDADE, editTextCidade.text.toString().trim())
            put(DatabaseHelper.COL_TELEFONE, editTextTelefone.text.toString().trim())
            put(DatabaseHelper.COL_FAX, editTextFax.text.toString().trim())
            put(DatabaseHelper.COL_CNPJ, editTextCnpj.text.toString().trim())
            put(DatabaseHelper.COL_IE, editTextIe.text.toString().trim())
        }

        val rowsUpdated = dbHelper.updateEmpresa(values, empresaId)
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Empresa atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Erro ao atualizar empresa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Editar Empresa"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}