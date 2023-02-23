package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.auth.Token
import com.proyecto.biblioapp.databinding.FragmentEstaticoBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentEstatico : Fragment() {
    private val model: ItemsViewModel by activityViewModels()
    private lateinit var binding: FragmentEstaticoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEstaticoBinding.inflate(inflater, container, false)

        binding.info.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentContacto)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentContacto)
                R.id.fragmentLogin -> navController.navigate(R.id.action_fragmentLogin_to_fragmentContacto)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentContacto)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentContacto)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentContacto)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentContacto)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentContacto)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentContacto)
            }
        }
        //login
        binding.login.setOnClickListener {
            model.setLoginJWT(Token("ERROR"))
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentLogin)
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentLogin)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentLogin)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentLogin)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentLogin)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentLogin)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentLogin)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentLogin)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentLogin)
            }
        }
        //logout
        binding.salir.setOnClickListener {
            model.setLoginJWT(Token("ERROR"))
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentPrincipal)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentPrincipal)
                R.id.fragmentLogin -> navController.navigate(R.id.action_fragmentLogin_to_fragmentPrincipal)
                R.id.fragmentPerfil -> navController.navigate(R.id.action_fragmentPerfil_to_fragmentPrincipal)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentPrincipal)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentPrincipal)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentPrincipal)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentPrincipal)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentPrincipal)
                R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPrincipal)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentPrincipal)
            }
            //  model.setSocio(socio!!)
            //AÑADIR MÁS
            model.setVisibilidadSocios(false)
            //borro el socio
            model.setSocio(null)
            //oculta el menu
            model.setVisibilidadMenu(false)

            CoroutineScope(Dispatchers.Main).launch {
                ApiRestAdapter.logout().await()
                ApiRestAdapter.destroySession().await()
               // model.setLogin(Sesion("", "", "", ""))
            }

        }
        //eventos
        binding.inicio.setOnClickListener {
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
            //oculta el menu
            model.setVisibilidadMenu(false)
        }

        //mi perfil
        binding.perfilSocio.setOnClickListener {
            //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentPerfil)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentPerfil)
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentPerfil)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentPerfil)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentPerfil)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentPerfil)
                R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPerfil)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentPerfil)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentPerfil)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentPerfil)
            }
            //oculta el menu
            model.setVisibilidadMenu(false)
        }

        //mis prestamos
        binding.prestamos.setOnClickListener {
            //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentPrestamos)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentPrestamos)
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentPrestamos)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentPrestamos)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentPrestamos)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentPrestamos)
                R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPrestamos)
                R.id.fragmentPerfil -> navController.navigate(R.id.action_fragmentPerfil_to_fragmentPrestamos)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentPrestamos)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentPrestamos)
            }
            //oculta el menu
            // model.setVisibilidadMenu(false)
        }

        //reservas
        binding.reservas.setOnClickListener {
            //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentReservas)
                R.id.fragmentLibros -> navController.navigate(R.id.action_fragmentLibros_to_fragmentReservas)
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentReservas)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentReservas)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentReservas)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentReservas)
                R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentReservas)
                R.id.fragmentPerfil -> navController.navigate(R.id.action_fragmentPerfil_to_fragmentReservas)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentReservas)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentReservas)
            }
            //oculta el menu
            // model.setVisibilidadMenu(false)
        }

        //zona libros
        binding.buscar.setOnClickListener {

            val navController = NavHostFragment.findNavController(this)
            when (navController.currentDestination?.id) {
                R.id.fragmentPrincipal -> navController.navigate(R.id.action_fragmentPrincipal_to_fragmentLibros)
                R.id.fragmentContacto -> navController.navigate(R.id.action_fragmentContacto_to_fragmentLibros)
                R.id.fragmentLogin -> navController.navigate(R.id.action_fragmentLogin_to_fragmentLibros)
                R.id.fragmentPerfil -> navController.navigate(R.id.action_fragmentPerfil_to_fragmentLibros)
                R.id.fragmentDetalleEvento -> navController.navigate(R.id.action_fragmentDetalleEvento_to_fragmentLibros)
                R.id.fragmentDetalleLibro -> navController.navigate(R.id.action_fragmentDetalleLibro_to_fragmentLibros)
                R.id.fragmentCalendarioEventos -> navController.navigate(R.id.action_fragmentCalendarioEventos_to_fragmentLibros)
                R.id.fragmentCambiarContrasenya -> navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentLibros)
                R.id.fragmentPrestamos -> navController.navigate(R.id.action_fragmentPrestamos_to_fragmentLibros)
                R.id.fragmentReservas -> navController.navigate(R.id.action_fragmentReservas_to_fragmentLibros)
                R.id.fragmentCalendarioPrestamos -> navController.navigate(R.id.action_fragmentCalendarioPrestamos_to_fragmentLibros)
            }
            //muestra el menu
            model.setVisibilidadMenu(true)
            //(activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }

        val nameObserver = Observer<Boolean> {
            //cuando ha iniciado sesion se muestran los datos del socio
            if (it == true) {
                binding.botonMisLibros.visibility = View.VISIBLE
                binding.cerrarSesion.visibility = View.VISIBLE
                binding.LinearPerfil.visibility = View.VISIBLE
                binding.botonMisReservas.visibility = View.VISIBLE
                binding.LinearSocio.visibility = View.GONE
                binding.LinearInfo.visibility = View.GONE
            }
            //cuando no ha iniciado sesion no se muestran los datos del socio
            else {
                binding.LinearSocio.visibility = View.VISIBLE
                binding.LinearInfo.visibility = View.VISIBLE
                binding.botonMisReservas.visibility = View.GONE
                binding.LinearPerfil.visibility = View.GONE
                binding.botonMisLibros.visibility = View.GONE
                binding.cerrarSesion.visibility = View.GONE
            }
        }
        model.getVisibilidadSocios.observe(requireActivity(), nameObserver)
        return binding.root
    }
}

