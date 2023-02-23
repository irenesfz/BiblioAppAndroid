package com.proyecto.biblioapp.adaptadores

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Reservas
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdaptadorReservas(private var reservas: ArrayList<Reservas>) :
    RecyclerView.Adapter<AdaptadorReservas.HolderReservas>(), View.OnClickListener,
    View.OnTouchListener {
    private lateinit var listener: View.OnClickListener
    private lateinit var listenerTouch: View.OnTouchListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HolderReservas {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.linear_recycler_reservas, viewGroup, false)

        //para que se le pueda hacer click
        itemView.setOnClickListener(this)
        itemView.setOnTouchListener(this)
        return HolderReservas(itemView)
    }

    override fun onBindViewHolder(holder: HolderReservas, position: Int) {
        val item: Reservas = reservas[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return reservas.size
    }


    fun setOnTouchListener(listener: View.OnTouchListener) {
        listenerTouch = listener
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (listenerTouch != null) {
            listenerTouch.onTouch(v, event)
        }
        return false
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        if (listener != null) {
            listener.onClick(v)
        }
    }

    //*******************
//HOLDER
//*******************
    inner class HolderReservas(v: View) : RecyclerView.ViewHolder(v) {
        val textTitulo: TextView?
        val disponibilidad: TextView?
        val fechaReserva: TextView?
        var imagen: ImageView? = null
        var imagenNotification: ImageView? = null
        var linea: CardView? = null
        private var view: View? = null

        @SuppressLint("SetTextI18n")
        fun bind(entity: Reservas) {

            fechaReserva?.text =
                entity.fechaReserva?.substring(0, 10)?.replace('Z', ' ')


            CoroutineScope(Dispatchers.Main).launch {
                val libro = entity.libroId?.let { ApiRestAdapter.cargaLibro(it).await() }
                if (libro?.titulo!!.length > 25) {
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

            //visibilidad finalizada o no
            if (entity.finalizada == true) {
                disponibilidad!!.text = "Sí"
                linea!!.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view!!.context,
                        R.color.coral
                    )
                )
            } else {
                disponibilidad!!.text = "No"
            }

            //visibilidad campana notificationes
            if (entity.notificacion == true) {
                imagenNotification!!.visibility = View.VISIBLE
            } else {
                imagenNotification!!.visibility = View.GONE
            }

        }

        init {
            imagenNotification = v.findViewById(R.id.ImgNotification)
            fechaReserva = v.findViewById(R.id.FechaPrestamoRecycler)
            disponibilidad = v.findViewById(R.id.disponibilidad)
            textTitulo = v.findViewById(R.id.TituloRecycler)
            imagen = v.findViewById(R.id.ImgRecycler)
            linea = v.findViewById(R.id.linea)
            view = v
        }
    }


}