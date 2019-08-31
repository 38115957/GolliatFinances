package com.example.golliatfinances.activitys

import android.content.Intent
import android.content.res.ColorStateList
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

    fun crearCreditoInit(){
        viewModel.liveDataActivity.observe(this, Observer {
            val intent = Intent(this, CrearCredito::class.java)
            intent.putExtra("dni", binding.buscarDni.text.toString())
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


/*


    fun buscarCliente(v: View) {

        val dni = getIntEditText(buscarDni.text.toString()).toInt()

        findViewById<View>(R.id.layoutCrearCredito).visibility = View.GONE

        findViewById<TextView>(R.id.textEstado).setText("Espere a que se confirmen los datos")

        datosDelCredito.setText("")

        datosPersistentes.getCliente(dni)
        sevicioPublico.obtenerEstadoCliente(dni, getString(R.string.codigo_financiera))

    }

    fun ocultarGestionUsuario() {

        findViewById<LinearLayout>(R.id.modificarUsuario).visibility = View.GONE

    }

    fun mostrarGestionUsuario(view: View?) {

        if (view == null) {

            findViewById<LinearLayout>(R.id.modificarUsuario).visibility = View.VISIBLE

        } else {
            if (findViewById<LinearLayout>(R.id.modificarUsuario).visibility == View.VISIBLE) {

                findViewById<LinearLayout>(R.id.modificarUsuario).visibility = View.GONE

            } else {
                findViewById<LinearLayout>(R.id.modificarUsuario).visibility = View.VISIBLE

            }
        }

    }

    fun cargarGestionUsuario(cliente: Cliente) {

        findViewById<MaterialEditText>(R.id.gestClienteDocumento).setText(cliente.documentoUnico.toString())
        findViewById<MaterialEditText>(R.id.gestClienteApellido).setText(cliente.apellidos)
        findViewById<MaterialEditText>(R.id.gestClienteDomicilio).setText(cliente.domicilio)
        findViewById<MaterialEditText>(R.id.gestClienteNombre).setText(cliente.nombres)
        findViewById<MaterialEditText>(R.id.gestClienteSueldo).setText(cliente.sueldo.toString())
        findViewById<MaterialEditText>(R.id.gestClienteTelefono).setText(cliente.telefono.toString())

    }

    fun guardarGestionUsuario(view: View?) {

        val cliente = datosPersistentes.cliente

        cliente.documentoUnico =
            getIntEditText(findViewById<MaterialEditText>(R.id.gestClienteDocumento).text.toString()).toInt()
        cliente.telefono =
            getIntEditText(findViewById<MaterialEditText>(R.id.gestClienteTelefono).text.toString()).toInt()
        cliente.sueldo =
            getIntEditText(findViewById<MaterialEditText>(R.id.gestClienteSueldo).text.toString())
        cliente.apellidos = findViewById<MaterialEditText>(R.id.gestClienteApellido).text.toString()
        cliente.domicilio =
            findViewById<MaterialEditText>(R.id.gestClienteDomicilio).text.toString()
        cliente.nombres = findViewById<MaterialEditText>(R.id.gestClienteNombre).text.toString()

        datosPersistentes.saveCliente(cliente)

    }

    private fun suscribe(datosPersistentes: DatosPersistentes) {

        datosPersistentes.liveDataCliente.observe(this, Observer<Cliente> {

            cargarGestionUsuario(it)

            datosPersistentes.cliente = it

            if (it.apellidos.isEmpty() or it.nombres.isEmpty()) {

                mostrarGestionUsuario(null)

            } else {

                ocultarGestionUsuario()

            }

        })

        datosPersistentes.livedataString.observe(this, Observer<String> {

            Snackbar.make(this.imageView, it, Snackbar.LENGTH_LONG)
                .show();

        })

        sevicioPublico.liveDataCliente.observe(this, Observer<ResultadoEstadoCliente> {

            if (it.Error!!.isEmpty()) {
                textEstado.text = String.format(
                    getString(R.string.estadoCliente),
                    it.CantidadCreditosActivos.toString(),
                    boolSINO(it.TieneDeudas)
                )

                if ((it.TieneDeudas == false) and (it.CantidadCreditosActivos < 3)) {

                    val credFinanciera = datosPersistentes.cliente.creditoCheck()

                    if (credFinanciera == "none") {

                        findViewById<View>(R.id.layoutCrearCredito).visibility = View.VISIBLE


                    } else {

                        findViewById<TextView>(R.id.textEstado).append(credFinanciera)

                    }


                }

            } else {
                textEstado.text = it.Error
            }

        })

        datosPersistentes.livedataPlanesAdapter.observe(this, Observer {

            if (it.size > 1) {
                val spinnerArrayAdapter = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, it
                )

                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                val spinner = findViewById(R.id.spinnerPlanes) as Spinner
                spinner.adapter = spinnerArrayAdapter
            } else {

                ocultarGestionUsuario()

                textEstado.text =
                    ("\nNO SE CUENTAN CON LA CANTIDAD MÍNIMA DE PLANES PARA OFRECER EL SERVICIO")

            }


        })

        datosPersistentes.liveDataPlan.observe(this, Observer {


            datosPersistentes.plan = it
            crearCreditPorcentajeInteres.setText(it.porcentajeInteresMensual.toPlainString())
            if (it.modalidadDePago.equals(Plan.Modalidad.ADELANTADA)) {
                findViewById<MaterialEditText>(R.id.crearCreditPorcentajeGastos).visibility =
                    View.GONE

            } else {
                findViewById<MaterialEditText>(R.id.crearCreditPorcentajeGastos).visibility =
                    View.VISIBLE

            }
            crearCreditPorcentajeGastos.setText(it.costoAdministrativo.toPlainString())

        })


        sevicioPublico.liveDataResultadoOtorgado.observe(this, Observer {


        })


    }

    fun generarCredito(view: View?) {

        val plan = datosPersistentes.plan
        var credito = datosPersistentes.credito

        val monto = getIntEditText(crearCreditMonto.text.toString())
        plan.costoAdministrativo = getIntEditText(crearCreditPorcentajeGastos.text.toString())
        plan.costoAdministrativo = getIntEditText(crearCreditPorcentajeGastos.text.toString())

        credito = Credito(datosPersistentes.financiera.interesMoroso, plan, monto.toDouble())

        datosDelCredito.setText(credito.toString())


    }

    fun setPlanes(pos: Int) {

        if (pos == 0) {
            datosPersistentes.getPlanesText(Plan.Modalidad.ADELANTADA)

        } else {
            datosPersistentes.getPlanesText(Plan.Modalidad.VENCIDA)
        }

    }





    fun init() {

        spinnerModalidad.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                setPlanes(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        setPlanes(0)

        spinnerPlanes.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                datosPersistentes.getPlan(spinnerPlanes.selectedItem.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })


    }


    fun confirmarCredito(view: View?) {

        val cliente = datosPersistentes.cliente
        val credito = datosPersistentes.credito

        cliente.addCredito(credito)

        datosPersistentes.saveCliente(cliente)

        sevicioPublico.informarCreditoOtorgado(
            cliente.documentoUnico,
            getString(R.string.codigo_financiera),
            credito.plan.identificadorPlan,
            credito.monto.toInt()
        )

    }


 */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if ((requestCode == GESTIONAR_CLIENTE_ACTIVITY) and (resultCode == 1)) {
            viewModel.gestionarClienteCompletado()

        }
    }
}