package com.example.golliatfinances.vistas

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.databinding.CrearCreditoBinding
import com.example.golliatfinances.modelo.Credito
import com.example.golliatfinances.modelo.Financiera
import com.example.golliatfinances.modelo.Plan
import com.example.golliatfinances.utils.TextUtils
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class VMCrearCredito : ViewModel() {

    val livedataPlanesAdapter: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val livedataClose: MutableLiveData<Boolean> = MutableLiveData()

    var plan = Plan()
    var credito = Credito.create()
    lateinit var binding: CrearCreditoBinding
    val datosPersistentes = DatosPersistentes()
    var dni = ""

    fun init() {

        binding.spinnerModalidad.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                setPlanes(position)

                if (position == 0) {
                    binding.crearCreditPorcentajeGastos.visibility = View.GONE
                } else {
                    binding.crearCreditPorcentajeGastos.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.spinnerPlanes.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                datosPersistentes.read(binding.spinnerPlanes.selectedItem.toString(), DatosPersistentes.Tipo.PLAN)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

    fun setPlanes(pos: Int) {

        if (pos == 0) {
            datosPersistentes.read(Plan.Modalidad.ADELANTADA)


        } else {
            datosPersistentes.read(Plan.Modalidad.VENCIDA)


        }

    }

    fun setData(plan: Plan) {

        binding.crearCreditPorcentajeGastos.setText(plan.costoAdministrativo.toString())
        binding.crearCreditPorcentajeInteres.setText(plan.porcentajeInteresMensual.toString())

        this.plan = plan
    }

    fun generarCredito(financiera: Financiera) {


        val monto = TextUtils.toBigDecimal(binding.crearCreditMonto.text.toString())
        plan.costoAdministrativo =
            TextUtils.toBigDecimal(binding.crearCreditPorcentajeGastos.text.toString())
        plan.costoAdministrativo =
            TextUtils.toBigDecimal(binding.crearCreditPorcentajeGastos.text.toString())

        credito = Credito(financiera.interesMoroso, plan, monto.toDouble())

        binding.datosDelCredito.setText(credito.toString())


    }

    fun notificarCredito(view: View) {

        añadirCredito(Credito.create())

    }

    fun guardarCredito(view: View) {

        añadirCredito(credito)

    }

    fun añadirCredito(credito: Credito) {
        if (credito.monto < BigDecimal.ZERO) {
            generarCredito(datosPersistentes.financiera)
        } else {

            datosPersistentes.addCredito(dni, credito)

            Snackbar.make(
                binding.root,
                ("Guardado con éxito"), Snackbar.LENGTH_LONG
            ).show()

            livedataClose.postValue(true)
        }
    }

    fun init(lifecycleOwner: LifecycleOwner) {
        datosPersistentes.liveDataPlan.observe(lifecycleOwner, Observer {

            setData(it)

        })


        datosPersistentes.livedataPlanesAdapter.observe(lifecycleOwner, Observer {

            if (it.size > 1) {

                livedataPlanesAdapter.postValue(it)

            } else {
                Snackbar.make(
                    binding.root,
                    "NO SE CUENTAN CON LA CANTIDAD MÍNIMA DE PLANES PARA OFRECER EL SERVICIO",
                    Snackbar.LENGTH_LONG
                ).show()


            }

        })
    }


}