package com.example.golliatfinances.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.golliatfinances.modelo.Pago
import com.example.golliatfinances.R



class AdapterPagos(val items: List<Pago>, val context: Context) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {

        var convertView = convertView

        val item = items[position]

        if (convertView == null) {
            convertView =
                inflater.inflate(R.layout.pago_item, container, false)
        }



        (convertView?.findViewById<TextView>(R.id.fecha))?.setText(item.fecha.toString())
        (convertView?.findViewById<TextView>(R.id.monto))?.setText(item.montoPagado.toString())


        return convertView!!

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


