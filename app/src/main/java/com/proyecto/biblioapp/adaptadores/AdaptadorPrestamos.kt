package com.proyecto.biblioapp.adaptadores

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Prestamo
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdaptadorPrestamos(private var prestamos: ArrayList<Prestamo>) :
    RecyclerView.Adapter<AdaptadorPrestamos.HolderPrestamos>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderPrestamos {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linear_recycler_prestamos, viewGroup, false)

        return HolderPrestamos(itemView)
    }

    override fun onBindViewHolder(holder: HolderPrestamos, position: Int) {
        val item: Prestamo = prestamos[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return prestamos.size
    }



//*******************
//HOLDER
//*******************
    inner class HolderPrestamos(v: View) : RecyclerView.ViewHolder(v) {
        val FechaDevolucionFinalRecycler: TextView?
        val fechaDevueltaLinear: LinearLayout?
        val fechaTopeLinear: LinearLayout?
        val FechaDevolucionRecycler: TextView?
        val FechaPrestamoRecycler: TextView?
        val textDescripcion: TextView?
        val mostrarDatos: TextView?
        private var view: View? = null
        var imagen: ImageView? = null
        var linea: CardView? = null
        val textTitulo: TextView?

        @SuppressLint("SetTextI18n")
        fun bind(entity: Prestamo) {

            FechaPrestamoRecycler?.text =
                entity.fechaPrestamo?.substring(0, 10)?.replace('Z', ' ')

            FechaDevolucionRecycler?.text =
                entity.fechaTope?.substring(0, 10)
                    ?.replace('Z', ' ')

            //PARA MOSTRAR FECHA DEVOLUCIÓN O FECHA MÁXIMA PARA DEVOLVERLO
            if (entity.fechaDevolucion != null) {
                FechaDevolucionFinalRecycler?.text =
                    entity.fechaDevolucion?.substring(0, 10)?.replace('Z', ' ')
                fechaTopeLinear?.visibility = View.GONE
            } else {
                fechaDevueltaLinear?.visibility = View.GONE
            }

            //RECOGE LOS DATOS DEL LIBRO AL QUE PERTENECE EL PRÉSTAMO
            CoroutineScope(Dispatchers.Main).launch {
                var libro = ApiRestAdapter.cargaLibro(entity.idLibro!!).await()
                if (libro.titulo!!.length > 25) {
                    textTitulo?.text = libro.titulo!!.substring(0, 27) + "..."
                } else {
                    textTitulo?.text = libro.titulo
                }
                //  textTitulo?.text = libro.titulo?.toString()
                Log.d("Error", libro.imagen.toString())
                if (libro.imagen!!.startsWith("http"))
                    Picasso.get().load(libro.imagen.toString()).error(R.drawable.no_image)
                        .into(imagen)
                else
                    imagen?.setImageBitmap(ImagenUtilidad.convertirStringBitmap(libro.imagen?.toString()))
            }

            //cambia los colores de la linea según si está finalizado o no el préstamo
            if (entity.fechaDevolucion != null) {
                linea!!.setCardBackgroundColor(ContextCompat.getColor(view!!.context, R.color.red))
            }
            /*if (entity.titulo!!.length > 20)
                textTitulo?.text = entity.titulo!!.substring(0, 27) + "..."
            else
                textTitulo?.text = entity.titulo

            if (entity.descripcion!!.length > 140)
                textDescripcion!!.text = entity.descripcion!!.substring(0, 140) + "..."
            else textDescripcion?.text = entity.descripcion*/
        }

        init {
            FechaDevolucionFinalRecycler = v.findViewById(R.id.FechaDevolucionFinalRecycler)
            FechaDevolucionRecycler = v.findViewById(R.id.FechaDevolucionRecycler)
            FechaPrestamoRecycler = v.findViewById(R.id.FechaPrestamoRecycler)
            fechaDevueltaLinear = v.findViewById(R.id.fechaDevueltaLinear)
            textDescripcion = v.findViewById(R.id.DescripcionRecycler)
            fechaTopeLinear = v.findViewById(R.id.fechaTopeLinear)
            textTitulo = v.findViewById(R.id.TituloRecycler)
            mostrarDatos = v.findViewById(R.id.mostrarDatos)
            imagen = v.findViewById(R.id.ImgRecycler)
            linea = v.findViewById(R.id.linea)
            view = v
        }
    }
}