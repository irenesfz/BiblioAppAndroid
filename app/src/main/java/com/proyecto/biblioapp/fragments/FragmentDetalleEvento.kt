package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proyecto.biblioapp.MainActivity.Companion.eventosFiltrados
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.clases.Evento
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.proyecto.biblioapp.databinding.FragmentDetalleEventoBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import com.squareup.picasso.Picasso


class FragmentDetalleEvento : Fragment() {
    private lateinit var binding: FragmentDetalleEventoBinding
    private val model: ItemsViewModel by activityViewModels()
    var mItem: Int? = null
    var tipo: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bundle para pasarle el evento
        if (savedInstanceState != null) {
            mItem = savedInstanceState.getInt("POSICION")
            tipo = savedInstanceState.getInt("TIPO")
        } else {
            if (arguments != null) {
                mItem = arguments?.getInt("POSICION")
                tipo = arguments?.getInt("TIPO")
            } else mItem = -1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleEventoBinding.inflate(inflater, container, false)
        if (mItem != -1) {
            var evento: Evento
            if (tipo == 1)
                evento = model.getEventos().value?.get(mItem!!)!!
            else evento = eventosFiltrados[mItem!!]
            binding.TituloEvento.text = evento.titulo.toString()
            binding.DescripcionEvento.text = evento.descripcion.toString()
            //para mostrar solo la fecha
            binding.FechaEvento.text = evento.fechaEvento.toString().substring(0, 10)
                    .replace('Z', ' ')
            //para mostrar la fecha y la hora
            binding.FechaPublicacion.text =
                evento.fechaPublicacion?.substring(0, 19)?.replace('Z', ' ')?.replace("[UTC]", "")?.replace('T', ' ')
            if (evento.imagen.toString() != "") {
                binding.ImagenEvento.visibility = View.VISIBLE
                if (evento.imagen.toString().startsWith("http"))
                   // binding.ImagenEvento.setImageBitmap(ImagenUtilidad.convertirURLBitmap(evento.imagen,context!!))
                    Picasso.get().load(evento.imagen.toString()).error(R.drawable.no_image).into(binding.ImagenEvento)
                else
                    binding.ImagenEvento.setImageBitmap(ImagenUtilidad.convertirStringBitmap(evento.imagen.toString()))
            } else binding.ImagenEvento.visibility = View.GONE
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("POSICION", mItem!!)
        outState.putInt("TIPO", mItem!!)
    }
}


