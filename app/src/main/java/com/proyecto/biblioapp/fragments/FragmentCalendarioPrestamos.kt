package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.biblioapp.adaptadores.AdaptadorPrestamosCalendario
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Prestamo
import com.proyecto.biblioapp.databinding.FragmentCalendarioPrestamosBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCalendarioPrestamos : Fragment() {
    private val model: ItemsViewModel by activityViewModels()
    private lateinit var binding: FragmentCalendarioPrestamosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarioPrestamosBinding.inflate(inflater, container, false)
        binding.Calendario.setOnDateChangeListener { _, i, i2, i3 ->
            model.getPrestamosFiltrados.value?.clear()
            val fecha: String?
            val dia: String = if (i3 < 10) "0$i3" else i3.toString()
            val mesDigito = i2 + 1
            val mes = if (mesDigito < 10) "0$mesDigito" else mesDigito.toString()
            fecha = "$i-$mes-$dia"

            CoroutineScope(Dispatchers.Main).launch {
                val check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                if (check == true) {
                    model.setPrestamosFiltrados(
                        ApiRestAdapter.cargaPrestamosPorFecha(
                            fecha,
                            model.getSocio.value?.idSocio!!,
                            model.getLoginJWT.value?.JWT.toString()
                        ).await()
                    )
                    model.getPrestamosFiltrados.value?.let { cargarAdaptador(it) }
                }else{
                    model.setCheckJWTVariable(check!!)
                }
            }

        }

        return binding.root
    }


    private fun cargarAdaptador(listaPrestamos: ArrayList<Prestamo>) {
        adaptador = AdaptadorPrestamosCalendario(listaPrestamos)
        binding.recyclerContainer.setHasFixedSize(true)
        binding.recyclerContainer.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerContainer.adapter = adaptador
    }

    companion object {
        var adaptador: AdaptadorPrestamosCalendario? = null
    }
}