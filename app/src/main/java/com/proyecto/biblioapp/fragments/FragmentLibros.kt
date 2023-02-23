package com.proyecto.biblioapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.adaptadores.AdaptadorLibros
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Libros
import com.proyecto.biblioapp.databinding.DialogoFiltraLibrosBinding
import com.proyecto.biblioapp.databinding.FragmentLibrosBinding
import com.proyecto.biblioapp.utils.FiltroBuscarLibro
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentLibros : Fragment(), SearchView.OnQueryTextListener,
    android.widget.SearchView.OnQueryTextListener {
    private val model: ItemsViewModel by activityViewModels()
    private lateinit var binding: FragmentLibrosBinding
    private lateinit var bindingDialogo: DialogoFiltraLibrosBinding
    private var palabraABuscar: String? = ""

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
        binding = FragmentLibrosBinding.inflate(inflater, container, false)
        binding.search.setOnQueryTextListener(this)
        model.setEstadoFiltroLibro(FiltroBuscarLibro.Todo)
        //carga datos
        CoroutineScope(Dispatchers.Main).launch {
            model.setLibros(ApiRestAdapter.cargaCatalogo().await())
            cargarAdaptador(model.getLibros().value!!)
            model.setLibrosFiltrados(model.getLibros().value!!)
        }
        // cargarAdaptador(model.getLibros().value!!)
        var visibilidadCategorias = false
        val nameObserverCategorias = Observer<Boolean> {
            if (it == true) {
                binding.categoriasButton.visibility = View.VISIBLE
            } else {
                binding.categoriasButton.visibility = View.GONE
            }
        }
        model.getVisibilidadSocios.observe(requireActivity(), nameObserverCategorias)

        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            var check: Boolean? = false
            //carga los datos de los libros
            CoroutineScope(Dispatchers.Main).launch {
                if (!visibilidadCategorias) {
                    model.setEstadoFiltroLibro(FiltroBuscarLibro.Todo)
                    model.setLibros(ApiRestAdapter.cargaCatalogo().await())
                    cargarAdaptador(model.getLibros().value!!)
                } else {
                    check =
                        ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                    if (check == false) {
                        Toast.makeText(
                            requireContext(),
                            "La sesión ha caducado",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        cargaAdaptadorFiltrado(model.getSocio.value?.categoriasInteres.toString())
                    }
                }

            }
            binding.swipeRefresh.isRefreshing = false
        }


        //botón categorias de interés libros
        binding.categoriasButton.setOnClickListener {
            var check: Boolean? = false
            CoroutineScope(Dispatchers.Main).launch {
                check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()

                if (check == true) {
                    if (!visibilidadCategorias) {
                        //carga todos los libros
                        binding.categoriasButton.setImageResource(R.drawable.fav)
                        visibilidadCategorias = true
                        model.setEstadoFiltroLibro(FiltroBuscarLibro.Categoria)
                        cargaAdaptadorFiltrado(model.getSocio.value?.categoriasInteres.toString())
                        binding.listaLibrosText.text =
                            "LISTA DE LIBROS SEGÚN TUS CATEGORÍAS DE INTERÉS"
                    } else {
                        //carga categorias interes
                        binding.categoriasButton.setImageResource(R.drawable.nofav)
                        binding.listaLibrosText.text = "LISTA DE LIBROS"
                        model.setEstadoFiltroLibro(FiltroBuscarLibro.Todo)
                        cargarAdaptador(model.getLibros().value!!)
                        //cargaAdaptadorFiltrado("")
                        visibilidadCategorias = false
                    }
                } else {
                    Toast.makeText(requireContext(), "La sesión ha caducado", Toast.LENGTH_LONG)
                        .show()
                    if (visibilidadCategorias) {
                        binding.categoriasButton.setImageResource(R.drawable.nofav)
                        binding.listaLibrosText.text = "LISTA DE LIBROS"
                        model.setEstadoFiltroLibro(FiltroBuscarLibro.Todo)
                        //cargarAdaptador(model.getLibros().value!!)
                        cargaAdaptadorFiltrado("")
                        visibilidadCategorias = false
                    }
                }
            }
        }

        //BOTÓN FILTRAR
        binding.filtrarButton.setOnClickListener {
            val dialogo = AlertDialog.Builder(requireContext())
            bindingDialogo = DialogoFiltraLibrosBinding.inflate(layoutInflater)
            dialogo.setView(bindingDialogo.root)
                .setPositiveButton("Aceptar") { dialog, which ->
                    when {
                        bindingDialogo.AutoresRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.Autores
                        )
                        bindingDialogo.TituloRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.Titulo
                        )
                        bindingDialogo.CategoriasRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.Categoria
                        )
                        bindingDialogo.EditorialRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.Editorial
                        )
                        bindingDialogo.ISBNRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.ISBN
                        )
                        bindingDialogo.FechaPublicacionRadioButton.isChecked -> model.setEstadoFiltroLibro(
                            FiltroBuscarLibro.AnyoPublicacion
                        )
                    }
                    //cuando le da a aceptar, carga adaptador con los cambios de la palabra que estaba escrita
                    cargaAdaptadorFiltrado(palabraABuscar)
                    binding.listaLibrosText.text = "LISTA DE LIBROS PERSONALIZADA"
                    //cambia el icono si no está filtrado
                    binding.filtrarButton.setImageResource(R.drawable.filtar)
                }.setNegativeButton("Deshacer filtro") { _, _ ->
                    model.setEstadoFiltroLibro(FiltroBuscarLibro.Todo)
                    cargaAdaptadorFiltrado(palabraABuscar)
                    binding.listaLibrosText.text = "LISTA DE LIBROS"
                    Toast.makeText(
                        requireContext(),
                        "Has eliminado los filtros",
                        Toast.LENGTH_SHORT
                    ).show()
                    //cambia el icono si está filtrado
                    binding.filtrarButton.setImageResource(R.drawable.no_filtrar)
                }
            val dialog = dialogo.create()
            dialog.show()
        }

        val nameObserver = Observer<ArrayList<Libros>> {
            cargarAdaptador(model.getLibros().value!!)
        }
        model.getLibros().observe(requireActivity(), nameObserver)


        //observer para cuando se filtren libros
        val nameObserverFiltro = Observer<ArrayList<Libros>> {
            //cuando ha iniciado sesion se muestran los datos del socio
            cargarAdaptador(it)
        }
        model.getLibrosFiltrados().observe(requireActivity(), nameObserverFiltro)

        return binding.root
    }



    private fun cargarAdaptador(libros: ArrayList<Libros>) {
        adaptador = AdaptadorLibros(libros)
        binding.recyclerContainer.setHasFixedSize(true)
        binding.recyclerContainer.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerContainer.adapter = adaptador
        //on click para ver el libro
        adaptador?.onMiClick { v ->
            val bundle = Bundle()
            bundle.putInt("POSICION", binding.recyclerContainer.getChildAdapterPosition(v))
            model.setLibroMuestra(libros)
            CoroutineScope(Dispatchers.Main).launch {
                var libro = ApiRestAdapter.cargaUnidadLibroReserva(
                    model.getLibroMuestra().value?.get(
                        binding.recyclerContainer.getChildAdapterPosition(v)
                    )?.isbn.toString()
                ).await()
                model.setUnidadLibroReservas(libro)

                libro = ApiRestAdapter.cargaUnidadLibroPrestamo(
                    // model.getLibroMuestra().value?.get(binding.recyclerContainer.getChildAdapterPosition(v))?.isbn.toString()
                    libro.isbn.toString()
                ).await()
                model.setUnidadLibroPrestamo(libro)
            }
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_fragmentLibros_to_fragmentDetalleLibro, bundle)
            // Toast.makeText(requireContext(), "asd", Toast.LENGTH_LONG).show()
        }
    }

    private fun cargaAdaptadorFiltrado(palabraABuscar: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            if (palabraABuscar!!.isNotEmpty()) {
                //cuando le da a buscar, si no está vacío busca con filtrado(por defecto está titulo)
                model.setLibrosFiltrados(
                    ApiRestAdapter.cargaLibrosFiltrados(
                        palabraABuscar,
                        model.getEstadoFiltroLibro.value!!
                    ).await()
                )

            } else {
                //si está vacío y le da a buscar, aparece todo el catálogo
                model.setLibros(ApiRestAdapter.cargaCatalogo().await())
            }
        }
    }

    companion object {
        var adaptador: AdaptadorLibros? = null
    }

    //**********************
    //SEARCH
    //**********************

    //Se llama cuando el usuario envía la consulta (LE DA A BUSCAR)
    override fun onQueryTextSubmit(palabraABuscar: String?): Boolean {
        //carga el adaptador con los filtros necesarios
        cargaAdaptadorFiltrado(palabraABuscar)
        return false
    }

    //Se llama cuando el usuario cambia el texto de la consulta
    override fun onQueryTextChange(p0: String?): Boolean {
        CoroutineScope(Dispatchers.Main).launch {
            //si está vacío (ha borrado lo que ha escrito) carga el catálogo entero
            palabraABuscar = p0
            if (p0!!.isEmpty()) {
                model.setLibrosFiltrados(ApiRestAdapter.cargaCatalogo().await())
            }
        }
        return true
    }
}