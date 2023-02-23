package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.biblioapp.MainActivity.Companion.eventosFiltrados
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.adaptadores.AdaptadorEventosCalendarios
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.databinding.FragmentCalendarioEventosBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCalendarioEventos : Fragment() {
    private lateinit var binding: FragmentCalendarioEventosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarioEventosBinding.inflate(inflater, container, false)

        binding.Calendario.setOnDateChangeListener { _, i, i2, i3 ->
            eventosFiltrados.clear()
            val fecha: String?
            val dia: String = if (i3 < 10) "0$i3" else i3.toString()
            val mesDigito = i2 + 1
            val mes = if (mesDigito < 10) "0$mesDigito" else mesDigito.toString()
            fecha = "$i-$mes-$dia"

            CoroutineScope(Dispatchers.Main).launch {
                eventosFiltrados = ApiRestAdapter.cargaEventosPorFecha(fecha).await()
                cargarAdaptador()
            }

        }
        return binding.root
    }

    private fun cargarAdaptador() {
        adaptador = AdaptadorEventosCalendarios(eventosFiltrados)
        binding.recyclerContainer.setHasFixedSize(true)
        binding.recyclerContainer.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerContainer.adapter = adaptador
        //on click para ver el evento
        adaptador?.onMiClick { v ->
            val bundle = Bundle()
            bundle.putInt("POSICION", binding.recyclerContainer.getChildAdapterPosition(v))
            bundle.putInt("TIPO", 2)//tipo 2 porque se va a mostrar la lista de eventos filtrados
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(
                R.id.action_fragmentCalendarioEventos_to_fragmentDetalleEvento,
                bundle
            )
        }
    }

    companion object {
        var adaptador: AdaptadorEventosCalendarios? = null
    }
}