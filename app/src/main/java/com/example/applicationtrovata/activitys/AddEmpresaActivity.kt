package com.example.applicationtrovata.activitys

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.applicationtrovata.R
import com.example.applicationtrovata.api.RetrofitClient
import com.example.applicationtrovata.api.CepResponse
import com.example.applicationtrovata.database.DatabaseHelper
import com.example.applicationtrovata.utils.CnpjValidator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEmpresaActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var editTextNomeFantasia: EditText
    private lateinit var editTextRazaoSocial: EditText
    private lateinit var editTextEndereco: EditText
    private lateinit var editTextBairro: EditText
    private lateinit var editTextCep: EditText
    private lateinit var editTextCidade: EditText
    private lateinit var editTextTelefone: EditText
    private lateinit var editTextFax: EditText
    private lateinit var editTextCnpj: EditText
    private lateinit var editTextIe: EditText
    private lateinit var buttonSalvar: Button

    private var empresaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_form)

        dbHelper = DatabaseHelper(this)

        // Inicializa as views
        initializeViews()

        empresaId = intent.getIntExtra("EMPRESA_ID", -1)
        if (empresaId != -1) {
            loadEmpresaData()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilita o botão de voltar
            title = "Cadastro de Produto" // Define um título mais adequado
        }

        setupListeners()
    }

    private fun initializeViews() {
        editTextNomeFantasia = findViewById(R.id.editTextNomeFantasia)
        editTextRazaoSocial = findViewById(R.id.editTextRazaoSocial)
        editTextEndereco = findViewById(R.id.editTextEndereco)
        editTextBairro = findViewById(R.id.editTextBairro)
        editTextCep = findViewById(R.id.editTextCep)
        editTextCidade = findViewById(R.id.editTextCidade)
        editTextTelefone = findViewById(R.id.editTextTelefone)
        editTextFax = findViewById(R.id.editTextFax)
        editTextCnpj = findViewById(R.id.editTextCnpj)
        editTextIe = findViewById(R.id.editTextIe)
        buttonSalvar = findViewById(R.id.buttonSalvar)
    }

    private fun setupListeners() {
        editTextCep.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                preencherEndereco()
            }
        }

        buttonSalvar.setOnClickListener {
            salvarEmpresa()
        }

        // Add CNPJ formatting
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
                    }
                }
            }
        })
    }

    private fun loadEmpresaData() {
        val cursor = dbHelper.getEmpresa(empresaId)
        cursor.use {
            if (it.moveToFirst()) {
                editTextNomeFantasia.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_NOME_FANTASIA)))
                editTextRazaoSocial.setText(it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RAZAO_SOCIAL)))
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

    private fun preencherEndereco() {
        val cep = editTextCep.text.toString().trim()
        if (cep.isNotEmpty()) {
            RetrofitClient.api.buscarCep(cep).enqueue(object : Callback<CepResponse> {
                override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
                    if (response.isSuccessful) {
                        val cepResponse = response.body()
                        if (cepResponse != null) {
                            editTextEndereco.setText(cepResponse.logradouro)
                            editTextBairro.setText(cepResponse.bairro)
                            editTextCidade.setText(cepResponse.localidade)
                        }
                    } else {
                        showToast("Erro ao obter endereço")
                    }
                }

                override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                    showToast("Erro ao obter endereço")
                }
            })
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        // Validate required fields
        if (editTextNomeFantasia.text.toString().trim().isEmpty()) {
            editTextNomeFantasia.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextRazaoSocial.text.toString().trim().isEmpty()) {
            editTextRazaoSocial.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextEndereco.text.toString().trim().isEmpty()) {
            editTextEndereco.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextBairro.text.toString().trim().isEmpty()) {
            editTextBairro.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextCep.text.toString().trim().isEmpty()) {
            editTextCep.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextCidade.text.toString().trim().isEmpty()) {
            editTextCidade.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextTelefone.text.toString().trim().isEmpty()) {
            editTextTelefone.error = "Campo obrigatório"
            isValid = false
        }

        if (editTextCnpj.text.toString().trim().isEmpty()) {
            editTextCnpj.error = "Campo obrigatório"
            isValid = false
        } else {
            val cnpj = editTextCnpj.text.toString().replace(Regex("[^0-9]"), "")
            if (!CnpjValidator.isValid(cnpj)) {
                editTextCnpj.error = "CNPJ inválido"
                isValid = false
            }
        }

        if (editTextIe.text.toString().trim().isEmpty()) {
            editTextIe.error = "Campo obrigatório"
            isValid = false
        }

        return isValid
    }

    private fun salvarEmpresa() {
        if (!validateFields()) {
            showToast("Por favor, preencha todos os campos obrigatórios corretamente")
            return
        }

        val contentValues = ContentValues().apply {
            put(DatabaseHelper.COL_NOME_FANTASIA, editTextNomeFantasia.text.toString().trim())
            put(DatabaseHelper.COL_RAZAO_SOCIAL, editTextRazaoSocial.text.toString().trim())
            put(DatabaseHelper.COL_ENDERECO, editTextEndereco.text.toString().trim())
            put(DatabaseHelper.COL_BAIRRO, editTextBairro.text.toString().trim())
            put(DatabaseHelper.COL_CEP, editTextCep.text.toString().trim())
            put(DatabaseHelper.COL_CIDADE, editTextCidade.text.toString().trim())
            put(DatabaseHelper.COL_TELEFONE, editTextTelefone.text.toString().trim())
            put(DatabaseHelper.COL_FAX, editTextFax.text.toString().trim())
            put(DatabaseHelper.COL_CNPJ, editTextCnpj.text.toString().trim())
            put(DatabaseHelper.COL_IE, editTextIe.text.toString().trim())
        }

        if (empresaId == -1) {
            insertEmpresa(contentValues)
        } else {
            updateEmpresa(contentValues)
        }
    }

    private fun insertEmpresa(contentValues: ContentValues) {
        val id = dbHelper.insertEmpresa(contentValues)
        if (id != -1L) {
            showToast("Empresa cadastrada com sucesso!")
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            showToast("Erro ao cadastrar empresa.")
        }
    }

    private fun updateEmpresa(contentValues: ContentValues) {
        val rowsUpdated = dbHelper.updateEmpresa(contentValues, empresaId)
        if (rowsUpdated > 0) {
            showToast("Empresa atualizada com sucesso!")
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            showToast("Erro ao atualizar empresa.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
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