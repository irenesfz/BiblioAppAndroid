package com.proyecto.biblioapp.adaptadores

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.clases.Evento

class AdaptadorEventos(private var eventos: ArrayList<Evento>) :
    RecyclerView.Adapter<AdaptadorEventos.HolderEventos>(), View.OnClickListener {
    private lateinit var listenerClick: View.OnClickListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderEventos {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linear_recycler_eventos, viewGroup, false)

        //para que se le pueda hacer click
        itemView.setOnClickListener(this)
        return HolderEventos(itemView)
    }

    override fun onBindViewHolder(holder: HolderEventos, position: Int) {
        val item: Evento = eventos[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return eventos.size
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
    inner class HolderEventos(v: View) : RecyclerView.ViewHolder(v) {
        val textTitulo: TextView?
        val textDescripcion: TextView?
        val textFecha: TextView?
        var imagen: ImageView? = null
        val v: View? = v
        var foto: Boolean = false

        @SuppressLint("SetTextI18n")
        fun bind(entity: Evento) {
            textFecha?.text =
                entity.fechaPublicacion?.substring(0, 19)?.replace('Z', ' ')?.replace("[UTC]", "")?.replace('T', ' ')?.replace('Z', ' ')

            if (entity.titulo!!.length > 29)
                textTitulo?.text = entity.titulo!!.substring(0, 27) + "..."
            else
                textTitulo?.text = entity.titulo

            if (entity.descripcion!!.length > 140)
                textDescripcion!!.text = entity.descripcion!!.substring(0, 140) + "..."
            else textDescripcion?.text = entity.descripcion

        }

        init {
            textTitulo = v.findViewById(R.id.TituloRecycler)
            textDescripcion = v.findViewById(R.id.DescripcionRecycler)
            textFecha = v.findViewById(R.id.FechaRecycler)
            if (foto)
                imagen = v.findViewById(R.id.ImgRecycler)
        }
    }
}