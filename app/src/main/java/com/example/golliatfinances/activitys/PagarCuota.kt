package com.example.golliatfinances.activitys

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.golliatfinances.BR
import com.example.golliatfinances.R
import com.example.golliatfinances.adapters.AdapterCuotas
import com.example.golliatfinances.databinding.PagarCuotaBinding
import com.example.golliatfinances.modelo.Cliente
import com.example.golliatfinances.modelo.Credito
import com.example.golliatfinances.vistas.VMPagarCuota

class PagarCuota : AppCompatActivity() {

    lateinit var viewModel: VMPagarCuota
    lateinit var binding: PagarCuotaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.pagar_cuota)

        //Inicia el View model de esta actividad
        viewModel = ViewModelProviders.of(this).get(VMPagarCuota::class.java)
        viewModel.binding = binding

         binding.setVariable(BR.VMPagarCuota, viewModel)

        if (intent.hasExtra("dni")) {
            viewModel.dni = intent.getStringExtra("dni")
        }

        binding.lifecycleOwner = this
        viewModel.init(this)

        initListeners()
    }

    fun initListeners() {
        viewModel.liveDataCliente.observe(this, Observer { setSpinnerCreditos(it)})
        viewModel.liveDataCuotas.observe(this, Observer { setAdapterCuotas(it) })
    }

    fun setAdapterCuotas(credito: Credito){

        var adapter = AdapterCuotas(credito.obtenerInforme(),this)
        binding.estadoCredito.adapter = adapter


    }


    fun setSpinnerCreditos(cliente: Cliente) {
        val spinnerArrayAdapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cliente.listaCreditos()
            )

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCredito.adapter = spinnerArrayAdapter
    }


}