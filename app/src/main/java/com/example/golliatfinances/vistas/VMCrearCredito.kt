package com.example.golliatfinances.vistas

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.databinding.CrearCreditoBinding
import com.example.golliatfinances.modelo.Credito
import com.example.golliatfinances.modelo.Financiera
import com.example.golliatfinances.modelo.Plan
import com.example.golliatfinances.utils.TextUtils

class VMCrearCredito : ViewModel() {

    var plan = Plan()
    var credito = Credito.create()

    val livedataTipoPlan: MutableLiveData<Plan.Modalidad> = MutableLiveData()
    val livedataPlan: MutableLiveData<String> = MutableLiveData()
    val livedataCredito: MutableLiveData<Credito> = MutableLiveData()
    lateinit var binding: CrearCreditoBinding
    val datosPersistentes = DatosPersistentes()

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

                livedataPlan.postValue(binding.spinnerPlanes.selectedItem.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

    fun setPlanes(pos: Int) {

        if (pos == 0) {
            livedataTipoPlan.postValue(Plan.Modalidad.ADELANTADA)

        } else {
            livedataTipoPlan.postValue(Plan.Modalidad.VENCIDA)

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
        livedataCredito.postValue(Credito.create())
    }

    fun guardarCredito(view: View) {
        livedataCredito.postValue(credito)

    }


}