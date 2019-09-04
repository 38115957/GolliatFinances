package com.example.golliatfinances.activitys

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.golliatfinances.BR
import com.example.golliatfinances.R
import com.example.golliatfinances.databinding.CrearCreditoBinding
import com.example.golliatfinances.vistas.VMCrearCredito

class CrearCredito : AppCompatActivity() {

    //Precondiciones:
    //  El cliente existe

    var dni = "0"
    lateinit var binding: CrearCreditoBinding
    lateinit var viewModel: VMCrearCredito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = setContentView(this, R.layout.crear_credito)

        if (intent.hasExtra("dni")) {
            viewModel.dni = intent.getStringExtra("dni")
        }

        //Inicia el View model de esta actividad
        viewModel = ViewModelProviders.of(this).get(VMCrearCredito::class.java)
        viewModel.binding = binding
        viewModel.init()

        binding.setVariable(BR.VMCrearCredito, viewModel)

        binding.lifecycleOwner = this

        observablesInit()

        viewModel.init(this)

    }

    fun observablesInit(){
        viewModel.livedataPlanesAdapter.observe(this, Observer {
            val spinnerArrayAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, it)

            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPlanes.adapter = spinnerArrayAdapter

        })

        viewModel.livedataClose.observe(this, Observer { finish() })
    }




}