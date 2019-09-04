package com.example.golliatfinances.activitys

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.golliatfinances.BR
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.R
import com.example.golliatfinances.databinding.ActivityMainBinding
import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import com.example.golliatfinances.utils.TextUtils
import com.example.golliatfinances.vistas.VMFachada
import com.jakewharton.threetenabp.AndroidThreeTen
import io.paperdb.Paper


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: VMFachada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        Paper.init(this)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        //Inicia los servicios de Fecha y DB
        //  Paper.book("cliente").destroy()

        //Inicia el View model de esta actividad
        viewModel = ViewModelProviders.of(this).get(VMFachada::class.java)
        binding.setVariable(BR.fachada, viewModel)
        viewModel.binding = binding
        binding.lifecycleOwner = this

        //Inicializo las funciones que son llamadas mediante "post"
        crearCreditoInit()

        viewModel.init(this, getString(R.string.codigo_financiera))
    }


    fun crearCreditoInit() {

        //Inicia los observables que se encargan de definir que actividad se ejecutará
        viewModel.liveDataActivity.observe(this, Observer {

            intent = when (it) {
                VMFachada.Actividades.PagarCuota -> Intent(this, PagarCuota::class.java)
                VMFachada.Actividades.CrearCredito -> Intent(this, CrearCredito::class.java)
                VMFachada.Actividades.GestionarCliente -> Intent(this, GestionarCliente::class.java)
            }

            intent.putExtra("dni", viewModel.dni)
            startActivity(intent)
        })
    }




}