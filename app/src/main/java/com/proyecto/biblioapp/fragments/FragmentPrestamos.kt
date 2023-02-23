package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.util.Log
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
import com.proyecto.biblioapp.adaptadores.AdaptadorPrestamos
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Prestamo
import com.proyecto.biblioapp.databinding.FragmentPrestamosBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentPrestamos : Fragment() {
    private lateinit var binding: FragmentPrestamosBinding
    private val model: ItemsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //controla el botón de volver para no ir cambiando de fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrestamosBinding.inflate(inflater, container, false)
        var historial = true
        //observa el cambio en el view model de préstamos y carga adaptador
        val nameObserverNumPrestamos = Observer<ArrayList<Prestamo>> {
            cargarAdaptador(it)
            if (it.size == 0)
                binding.mostrarDatos.visibility = View.VISIBLE
        }
        model.getPrestamosNoFinalizados.observe(requireActivity(), nameObserverNumPrestamos)

        binding.historialBoton.setOnClickListener {
            //ir cambiando en prestamos no finalizados/historial
            historial = if (historial) {
                cargarAdaptador(model.getPrestamos.value!!)
                binding.textoPrestamos.text = "HISTORIAL PRÉSTAMOS"
                false
            } else {
                binding.textoPrestamos.text = "    PRÉSTAMOS      "
                cargarAdaptador(model.getPrestamosNoFinalizados.value!!)
                true
            }
        }

        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            var check : Boolean? = false
            //carga los datos de los préstamos
            CoroutineScope(Dispatchers.Main).launch {
                check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                if (check == true) {
                    //cargo los préstamos dependiendo si está en historial o no
                    if (!historial) {
                        model.setPrestamos(
                            ApiRestAdapter.cargaHistorialPrestamos(
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            )
                                .await()
                        )
                        cargarAdaptador(model.getPrestamos.value!!)
                        binding.textoPrestamos.text = "HISTORIAL PRÉSTAMOS"
                    } else {
                        model.setPrestamosNoFinalizado(
                            ApiRestAdapter.cargaPrestamosNoFinalizadosSocio(
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                        )
                        cargarAdaptador(model.getPrestamosNoFinalizados.value!!)
                        binding.textoPrestamos.text = "    PRÉSTAMOS      "
                    }
                }else{
                    Toast.makeText(requireContext(),"La sesión ha caducado", Toast.LENGTH_LONG).show()
                }

                binding.swipeRefresh.isRefreshing = false
            }

        }

            binding.calendarioBoton.setOnClickListener {
                val navController = NavHostFragment.findNavController(this)
                navController.navigate(R.id.action_fragmentPrestamos_to_fragmentCalendarioPrestamos)
            }

            cargarAdaptador(model.getPrestamosNoFinalizados.value!!)
            return binding.root
        }


        private fun cargarAdaptador(prestamos: ArrayList<Prestamo>) {
            adaptador = AdaptadorPrestamos(prestamos)
            binding.recyclerContainer.setHasFixedSize(true)
            binding.recyclerContainer.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
            binding.recyclerContainer.adapter = adaptador
            //on click para ver el préstamo
            /*  FragmentLibros.adaptador?.onMiClick { v ->
                  val bundle = Bundle()
                  bundle.putInt("POSICION", binding.recyclerContainer.getChildAdapterPosition(v))
                  model.setLibroMuestra(libros)
                  val navController = NavHostFragment.findNavController(this)
                  navController.navigate(R.id.action_fragmentLibros_to_fragmentDetalleLibro, bundle)
                  // Toast.makeText(requireContext(), "asd", Toast.LENGTH_LONG).show()
              }*/
        }

        companion object {
            var adaptador: AdaptadorPrestamos? = null
        }
    }