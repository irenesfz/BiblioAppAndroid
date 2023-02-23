package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.adaptadores.AdaptadorEventos
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Evento
import com.proyecto.biblioapp.databinding.FragmentPrincipalBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentPrincipal : Fragment() {
    private val model: ItemsViewModel by activityViewModels()
    private lateinit var binding: FragmentPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //controla el botón de volver para no ir cambiando de fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentPrincipalBinding.inflate(inflater, container, false)

        binding.calendarioBoton.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_fragmentPrincipal_to_fragmentCalendarioEventos)
        }

        //observer para cambiar de fragment y cerrar sesión si validate = false
        /*val nameObserverCheck = Observer<Boolean> {
            if (it == false || it == null) {
                CoroutineScope(Dispatchers.Main).launch {
                    ApiRestAdapter.logout().await()
                    ApiRestAdapter.destroySession().await()
                }
                model.setVisibilidadSocios(false)
                //oculta el menu
                model.setVisibilidadMenu(false)

                //cambia a la ventana de login
                val navController = NavHostFragment.findNavController(this)
                when (navController.currentDestination?.id) {
                    R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentPrincipal)
                    R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentPrincipal)
                    R.id.fragmentLogin -> navController.navigate(R.id.action_fragmentLogin_to_fragmentPrincipal)
                    R.id.fragmentPerfil -> navController.navigate(R.id.action_fragmentPerfil_to_fragmentPrincipal)
                    R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentPrincipal)
                    R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentPrincipal)
                    R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentPrincipal)
                    R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPrincipal)
                    R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentPrincipal)
                    R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentPrincipal)
                    R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentPrincipal)
                }
            }
        }
        model.getCheckJWTVariable.observe(requireActivity(), nameObserverCheck)*/


        val nameObserver = Observer<ArrayList<Evento>> {
            cargarAdaptador()
        }
        model.getEventos().observe(requireActivity(), nameObserver)
        // cargarAdaptador()

        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            //carga los datos de los eventos
            CoroutineScope(Dispatchers.Main).launch {
                model.setEventos(ApiRestAdapter.cargaEventos().await())
            }
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun cargarAdaptador() {
        adaptador = AdaptadorEventos(model.getEventos().value!!)
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
            bundle.putInt("TIPO", 1)
            // Toast.makeText(requireContext(), MainActivity.eventos.get(binding.recyclerContainer.getChildAdapterPosition(v)).imagen, Toast.LENGTH_LONG).show()
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_fragmentPrincipal_to_fragmentDetalleEvento, bundle)
        }
    }

    companion object {
        var adaptador: AdaptadorEventos? = null
    }
}