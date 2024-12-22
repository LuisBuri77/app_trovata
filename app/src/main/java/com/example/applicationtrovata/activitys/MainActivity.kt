package com.example.applicationtrovata.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redireciona para a tela inicial
        val intent = Intent(this, EmpresasActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a MainActivity para evitar retorno
    }
}
