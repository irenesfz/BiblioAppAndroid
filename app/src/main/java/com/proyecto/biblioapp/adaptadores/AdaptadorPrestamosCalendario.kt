package com.proyecto.biblioapp.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Prestamo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdaptadorPrestamosCalendario(private var prestamosFiltrados: ArrayList<Prestamo>) :
    RecyclerView.Adapter<AdaptadorPrestamosCalendario.HolderPrestamosCalendario>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderPrestamosCalendario {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linear_recycler_prestamo_calendario, viewGroup, false)
        return HolderPrestamosCalendario(itemView)
    }

    override fun onBindViewHolder(holder: HolderPrestamosCalendario, position: Int) {
        val item: Prestamo = prestamosFiltrados[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return prestamosFiltrados.size
    }

    //*******************
    //HOLDER
    //*******************
    inner class HolderPrestamosCalendario(v: View) : RecyclerView.ViewHolder(v) {
        val textTitulo: TextView? = v.findViewById(R.id.TituloRecycler)

        fun bind(entity: Prestamo) {
            CoroutineScope(Dispatchers.Main).launch {
                var libro = ApiRestAdapter.cargaLibro(entity.idLibro!!).await()
                textTitulo?.text = libro.titulo
            }
        }
    }
}