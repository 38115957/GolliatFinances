package com.example.golliatfinances

import androidx.lifecycle.MutableLiveData
import com.example.golliatfinances.modelo.*
import io.paperdb.Paper
import org.jetbrains.anko.doAsync

class DatosPersistentes {

    val liveDataCliente: MutableLiveData<Cliente> = MutableLiveData()
    val liveDataPlan: MutableLiveData<Plan> = MutableLiveData()
    val livedataString: MutableLiveData<String> = MutableLiveData()
    val livedataPlanesAdapter: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun createPlanes() {

        for (i in 1..12) {
            val plan = Plan()

            val numCuotas = i * 4
            plan.costoAdministrativo = (Math.random() / 5).toBigDecimal()

            if (Math.random() > 0.5) {
                plan.modalidadDePago = Plan.Modalidad.ADELANTADA
            } else {
                plan.modalidadDePago = Plan.Modalidad.VENCIDA
            }
            plan.porcentajeInteresMensual = (Math.random() / 5).toBigDecimal()
            plan.numeroDeCuotas(numCuotas)
            plan.identificador = "Plan $numCuotas"

            write(plan)
        }

    }

    enum class Tipo {
        PLAN, EMPLEADO, PLANES, CLIENTE
    }

    fun addCredito(identificador: String, credito: Credito) {

        val cliente = Paper.book(cliente_db).read(identificador, Cliente())

        cliente.addCredito(credito)

        Paper.book(cliente_db).write(identificador, cliente)

    }

    fun listOf(tipo: Tipo): MutableList<String> {
        when {
            tipo == Tipo.PLAN -> return Paper.book(plan_db).allKeys
            tipo == Tipo.CLIENTE -> return Paper.book(cliente_db).allKeys
        }
        return arrayListOf(String())
    }

    fun write(item: Any) {
        doAsync {
            when {
                item is Cliente -> Paper.book(cliente_db).write(item.identificador, item)
                item is Empleado -> Paper.book(empleado_db).write(item.identificador, item)
                item is Plan -> Paper.book(plan_db).write(item.identificador, item)
            }
            livedataString.postValue("Se ha guardado con exito!")

        }
    }

    fun read(identificador: String, tipo: Tipo) {
        doAsync {
            when {
                tipo.equals(Tipo.CLIENTE) -> liveDataCliente.postValue(
                    Paper.book(cliente_db).read(
                        identificador,
                        Cliente()
                    )
                )
                tipo.equals(Tipo.PLAN) -> liveDataPlan.postValue(
                    Paper.book(plan_db).read(
                        identificador,
                        Plan()
                    )
                )
            }
        }
    }

    fun read(tipo: Plan.Modalidad) {
        doAsync {
            val nombres = arrayListOf(String())
            nombres.clear()

            Paper.book(plan_db).allKeys.forEach {

                if ((Paper.book(plan_db).read(it) as Plan).modalidadDePago == tipo) {
                    nombres.add(it)
                }

            }

            livedataPlanesAdapter.postValue(nombres)
        }
    }


    val financiera = Financiera(
        "Golliat Finances",
        "GOLLIAT SA",
        "30-65326752-1",
        "MEDRANO PEDRO AV. 951 – BARRIO: IEEE (UTN) Código postal: 1179",
        0.35.toBigDecimal()
    )

    val cliente_db = "cliente"
    val plan_db = "plan"
    val empleado_db = "empleado"


}