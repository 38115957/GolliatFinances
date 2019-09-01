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
    val datosPersistentes = DatosPersistentes()
    val sevicioPublico = ServicioPublicoCredito()
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: VMFachada
    val GESTIONAR_CLIENTE_ACTIVITY = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        //Inicia los servicios de Fecha y DB
        AndroidThreeTen.init(this)
        Paper.init(this)

        //Inicia el View model de esta actividad
        viewModel = ViewModelProviders.of(this).get(VMFachada::class.java)
        binding.setVariable(BR.fachada, viewModel)
        viewModel.binding = binding

        binding.lifecycleOwner = this

        //Inicializo las funciones que son llamadas mediante "post"
        buscarPersonaInit()
        gestionarClienteInit()
        crearCreditoInit()

        datosPersistentes.createPlanes()

    }

    fun gestionarClienteInit() {
        viewModel.livedataGestionar.observe(this, Observer {
            gestionarPersona(it)
        })
    }

    fun crearCreditoInit() {
        viewModel.liveDataActivity.observe(this, Observer {

            intent = when (it) {
                VMFachada.Actividades.PagarCuota -> Intent(this, PagarCuota::class.java)
                VMFachada.Actividades.CrearCredito -> Intent(this, CrearCredito::class.java)
            }

            intent.putExtra("dni", viewModel.dni)
            startActivityForResult(intent, GESTIONAR_CLIENTE_ACTIVITY)
        })
    }


    fun buscarPersonaInit() {
        viewModel.livedataBuscar.observe(this, Observer {
            //Busca si el cliente existe en esta financiera, cuando lo encuentra devuelve
            datosPersistentes.read(it, DatosPersistentes.Tipo.CLIENTE)

            //Obtiene el estado del cliente desde el servicio web, cuando lo encuentra lo devuelve
            sevicioPublico.obtenerEstadoCliente(
                TextUtils.toBigDecimal(it).toInt(),
                getString(R.string.codigo_financiera)
            )
        })

        datosPersistentes.liveDataCliente.observe(this, Observer {
            viewModel.mostrarCliente(it)
        })

        sevicioPublico.liveDataCliente.observe(this, Observer {
            viewModel.estadoCliente(it)
        })
    }

    fun click(view: View?) {

        var button = view as Button

        button.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.primary_light)));
        button.isClickable = false

    }

    fun crearCredito(dni: String) {
        val intent = Intent(this, CrearCredito::class.java)
        intent.putExtra("dni", dni)
        startActivityForResult(intent, GESTIONAR_CLIENTE_ACTIVITY)
    }

    fun gestionarPersona(dni: String) {
        val intent = Intent(this, GestionarCliente::class.java)
        intent.putExtra("dni", dni)
        startActivityForResult(intent, GESTIONAR_CLIENTE_ACTIVITY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if ((requestCode == GESTIONAR_CLIENTE_ACTIVITY) and (resultCode == 1)) {
            viewModel.gestionarClienteCompletado()

        }
    }
}