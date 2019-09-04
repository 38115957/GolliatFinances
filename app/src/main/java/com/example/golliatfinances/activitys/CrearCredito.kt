package com.example.golliatfinances.activitys

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.golliatfinances.BR
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.R
import com.example.golliatfinances.databinding.CrearCreditoBinding
import com.example.golliatfinances.vistas.VMCrearCredito
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class CrearCredito : AppCompatActivity() {

    //Precondiciones:
    //  El cliente existe

    var dni = "0"
    lateinit var binding: CrearCreditoBinding
    lateinit var viewModel: VMCrearCredito
    var datosPersistentes = DatosPersistentes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = setContentView(this, R.layout.crear_credito)

        if (intent.hasExtra("dni")) {
            dni = intent.getStringExtra("dni")
        }

        //Inicia el View model de esta actividad
        viewModel = ViewModelProviders.of(this).get(VMCrearCredito::class.java)
        viewModel.binding = binding
        viewModel.init()

        binding.setVariable(BR.VMCrearCredito, viewModel)

        binding.lifecycleOwner = this

        PlanesInit()
    }

    fun PlanesInit() {

        viewModel.livedataCredito.observe(this, Observer {

            if (it.monto < BigDecimal.ZERO) {
                viewModel.generarCredito(datosPersistentes.financiera)
            } else {

                datosPersistentes.addCredito(dni, it)

                Snackbar.make(
                    binding.root,
                    ("Guardado con éxito"), Snackbar.LENGTH_LONG
                ).show()

                finish()

            }
        })

        datosPersistentes.liveDataPlan.observe(this, Observer {

            viewModel.setData(it)

        })

        viewModel.livedataPlan.observe(this, Observer {

            datosPersistentes.read(it, DatosPersistentes.Tipo.PLAN)

        })

        viewModel.livedataTipoPlan.observe(this, Observer {

            datosPersistentes.read(it)

        })

        datosPersistentes.livedataPlanesAdapter.observe(this, Observer {

            if (it.size > 1) {
                val spinnerArrayAdapter =
                    ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, it)

                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerPlanes.adapter = spinnerArrayAdapter


            } else {

                Snackbar.make(
                    binding.root,
                    getString(R.string.no_enought_plans), Snackbar.LENGTH_LONG
                ).show()

                finish()

            }

        })


    }


}