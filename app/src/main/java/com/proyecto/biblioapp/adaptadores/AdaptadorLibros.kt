package com.proyecto.biblioapp.adaptadores

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.proyecto.biblioapp.clases.Libros
import com.squareup.picasso.Picasso

class AdaptadorLibros(private var libros: ArrayList<Libros>) :
    RecyclerView.Adapter<AdaptadorLibros.HolderLibros>(), View.OnClickListener {
    private lateinit var listenerClick: View.OnClickListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderLibros {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.grid_recycler, viewGroup, false)
        //para que se le pueda hacer click
        itemView.setOnClickListener(this)
        return HolderLibros(itemView)
    }

    override fun onBindViewHolder(holder: HolderLibros, position: Int) {
        val c = libros[position]
        holder.bind(c)
    }

    override fun getItemCount(): Int {
        return libros.size
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
    inner class HolderLibros(v: View) : RecyclerView.ViewHolder(v) {
        val textTitulo: TextView?
        val textAutores: TextView?
        val textIsbn: TextView?
        val imagen: ImageView?
        val v: View = v
        fun bind(entity: Libros) {
            textTitulo?.text = entity.titulo
            textAutores?.text = entity.autores
            textIsbn?.text = entity.isbn

            if (entity.imagen!!.startsWith("http"))
               // imagen?.setImageBitmap(ImagenUtilidad.convertirURLBitmap(entity.imagen, v.context!!))
                   //lo he cambiado porque tardaba demasiado
                Picasso.get().load(entity.imagen).error(R.drawable.no_image).into(imagen)
            else
                imagen?.setImageBitmap(ImagenUtilidad.convertirStringBitmap(entity.imagen))
        }

        init {
            textTitulo = v.findViewById(R.id.TituloRecycler)
            textAutores = v.findViewById(R.id.AutoresRecycler)
            textIsbn = v.findViewById(R.id.ISBNRecycler)
            imagen = v.findViewById(R.id.ImgRecycler)
        }

    }
}
