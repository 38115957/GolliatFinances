package com.example.golliatfinances.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.example.golliatfinances.modelo.CuotaInforme
import com.example.golliatfinances.R
import java.math.BigDecimal


class AdapterCuotas(val items: List<CuotaInforme>, val context: Context) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {

        var convertViewnew = convertView

        val item = items[position]

        if (convertViewnew == null) {
            convertViewnew =
                inflater.inflate(R.layout.cuota_item, container, false)
        }

        val adapter = AdapterPagos(item.pagos, context)

        if (position == 0) {
            (convertViewnew?.findViewById<TextView>(R.id.numCuota))?.setText(context.getString(R.string.cuota_inicial))
        } else {
            (convertViewnew?.findViewById<TextView>(R.id.numCuota))?.setText(
                String.format(
                    context.getString(
                        R.string.numCuota
                    ), position
                )
            )
        }
        (convertViewnew?.findViewById<TextView>(R.id.montoInicial))?.setText(
            String.format(
                context.getString(
                    R.string.montoInicial
                ), item.montoInicial.toString()
            )
        )
        (convertViewnew?.findViewById<TextView>(R.id.fechaVencimiento))?.setText(item.fechaVencimiento.toString())
        (convertViewnew?.findViewById<TextView>(R.id.estado))?.setText(
            String.format(
                context.getString(
                    R.string.estado
                ), item.estado
            )
        )
        (convertViewnew?.findViewById<TextView>(R.id.montoInicial))?.setText(
            String.format(
                context.getString(
                    R.string.montoInicial
                ), item.montoInicial.toString()
            )
        )

        if (item.estado.equals(CuotaInforme.Estado.PAGADA)) {
            (convertViewnew?.findViewById<TextView>(R.id.detalleEstado))?.setText(item.saldadaElDia.toString())
        } else {
            (convertViewnew?.findViewById<TextView>(R.id.detalleEstado))?.setText("")
        }

        if (!item.montoMoroso.equals(BigDecimal.ZERO)) {
            (convertViewnew?.findViewById<LinearLayout>(R.id.layoutMoroso))?.visibility = View.VISIBLE

            (convertViewnew?.findViewById<TextView>(R.id.diasMoroso))?.setText(
                String.format(
                    context.getString(
                        R.string.diasVencido
                    ), item.cantidadDeDiasMoroso.toString()
                )
            )
            (convertViewnew?.findViewById<TextView>(R.id.recargo))?.setText(
                String.format(
                    context.getString(
                        R.string.montoRecargo
                    ), item.montoMoroso
                )
            )

        } else {
            (convertViewnew?.findViewById<LinearLayout>(R.id.layoutMoroso))?.visibility = View.GONE

        }

        (convertViewnew?.findViewById<ListView>(R.id.pagos))?.adapter = adapter


        val innerListView = convertViewnew?.findViewById<ListView>(R.id.pagos)
        var params = innerListView?.layoutParams
        params?.height =
            context.resources.getDimension(R.dimen.pago_item_heigt).toInt() * (item.pagos.size+1)

        innerListView?.layoutParams = params

        innerListView?.setAdapter(adapter);

        convertViewnew?.invalidate();

        return convertViewnew!!

    }


    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}