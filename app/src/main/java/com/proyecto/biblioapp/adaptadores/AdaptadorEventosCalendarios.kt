package com.proyecto.biblioapp.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.clases.Evento

class AdaptadorEventosCalendarios(private var eventosFiltrados: ArrayList<Evento>) :
    RecyclerView.Adapter<AdaptadorEventosCalendarios.HolderEventosCalendario>(),
    View.OnClickListener {
    private lateinit var listenerClick: View.OnClickListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderEventosCalendario {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linear_recycler_evento_calendario, viewGroup, false)
        //para que se le pueda hacer click
        itemView.setOnClickListener(this)
        return HolderEventosCalendario(itemView)
    }

    override fun onBindViewHolder(holder: HolderEventosCalendario, position: Int) {
        val item: Evento = eventosFiltrados[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return eventosFiltrados.size
    }

    fun onMiClick(listener: View.OnClickListener) {
        this.listenerClick = listener
    }

    override fun onClick(p0: View?) {
        listenerClick?.onClick(p0)
    }

    //*******************
    //HOLDER
    //*******************
    inner class HolderEventosCalendario(v: View) : RecyclerView.ViewHolder(v) {
        val textTitulo: TextView? = v.findViewById(R.id.TituloRecycler)

        fun bind(entity: Evento) {
            textTitulo?.text = entity.titulo
        }

    }
}