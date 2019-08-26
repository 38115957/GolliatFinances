package com.example.golliatfinances

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidThreeTen.init(this)

        val servicioPublicoCredito = ServicioPublicoCredito()
        var arrayResult = arrayListOf<String>()

        doAsync {

            for (i in 33000000..33000099) {
                arrayResult.add(
                    servicioPublicoCredito.obtenerEstadoCliente(
                        i, getString(R.string.codigo_financiera)
                    ).toString()
                )
            }

            uiThread {
                for(str in arrayResult){
                    println(str)
                    text_init.append("\n"+str)
                }
            }

        }


    }

}