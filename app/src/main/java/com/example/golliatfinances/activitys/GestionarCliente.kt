package com.example.golliatfinances.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.R
import com.example.golliatfinances.databinding.GestionarClienteBinding
import com.example.golliatfinances.modelo.Cliente


class GestionarCliente : AppCompatActivity() {

    val datosPersistentes = DatosPersistentes()
    lateinit var binding: GestionarClienteBinding
    var dni = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.gestionar_cliente)
        clienteInit()

        if (intent.hasExtra("dni")) {
            dni = intent.getStringExtra("dni")
            datosPersistentes.read(dni, DatosPersistentes.Tipo.CLIENTE)
        }

    }


    override fun onStart() {
        super.onStart()
        datosPersistentes.read(dni, DatosPersistentes.Tipo.CLIENTE)
    }

    init {
        datosPersistentes.read(dni, DatosPersistentes.Tipo.CLIENTE)
    }

    fun clienteInit() {
        datosPersistentes.liveDataCliente.observe(this, Observer {
            if(it.isNull()){
                it.identificador = dni
            }
            binding.cliente = it
            binding.notifyChange()
        })
    }

    fun save(view: View?) {
        var cliente = Cliente()

        cliente.nombres = binding.gestClienteNombre.text.toString()
        cliente.apellidos = binding.gestClienteApellido.text.toString()
        cliente.sueldo =
            com.example.golliatfinances.utils.TextUtils.toBigDecimal(binding.gestClienteSueldo.text.toString())
        cliente.identificador = binding.gestClienteDocumento.text.toString()
        cliente.domicilio = binding.gestClienteDomicilio.text.toString()
        cliente.telefono = binding.gestClienteTelefono.text.toString()

        datosPersistentes.write(cliente)

        val intent = Intent()
        setResult(1, intent)
        finish()//finishing activity
    }

}