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
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.auth.Token
import com.proyecto.biblioapp.auth.Usuario
import com.proyecto.biblioapp.clases.Socio
import com.proyecto.biblioapp.databinding.FragmentEstaticoBinding
import com.proyecto.biblioapp.databinding.FragmentLoginBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var bindingEstatico: FragmentEstaticoBinding
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

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        bindingEstatico = FragmentEstaticoBinding.inflate(inflater, container, false)

        //cuando le da al boton de entrar (si son correctos los datos)
        //se "desbloquea" la vista de la aplicacion dirigida a los socios
        binding.entrarButton.setOnClickListener {
            if (binding.dniText.text.toString().isEmpty() ||
                binding.passwordText.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Error al introducir los datos",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                //*******************************************************************************************************************************************
                var token: Token?
                //post para identificarme en la API
                CoroutineScope(Dispatchers.Main).launch {
                    token = ApiRestAdapter.loginJWT(
                        Usuario(
                            binding.dniText.text.toString(),
                            binding.passwordText.text.toString()
                        )
                    ).await()
                    //si el token no es nulo y no devuelve error, es válido
                    if (token != null && binding.dniText.text.toString() != "admin") {
                        if (token!!.JWT != "ERROR")
                            model.setLoginJWT(token!!)
                    } else {
                        binding.dniText.error = "Datos incorrectos"
                        binding.passwordText.error = "Datos incorrectos"
                        model.setLoginJWT(Token("ERROR"))
                    }
                }
            }
            //*******************************************************************************************************************************************

            val nameObserverToken = Observer<Token> {
                var socio: Socio?
                if (it.JWT != "ERROR") {
                    model.setVisibilidadCategorias(false)
                    CoroutineScope(Dispatchers.Main).launch {
                        //CARGO EL SOCIO
                        socio = ApiRestAdapter.cargaSocio2(
                            binding.dniText.text.toString(),
                            it.JWT!!
                        ).await()
                        if (socio != null) {
                            //modifico los datos del socio
                            model.setSocio(
                                ApiRestAdapter.cargaSocio2(
                                    binding.dniText.text.toString(),
                                    it.JWT!!
                                ).await()
                            )
                            //cargo los préstamos
                            model.setPrestamosNoFinalizado(
                                ApiRestAdapter.cargaPrestamosNoFinalizadosSocio(
                                    socio!!.idSocio!!,
                                    model.getLoginJWT.value?.JWT.toString()
                                ).await()
                            )
                            model.setPrestamos(
                                ApiRestAdapter.cargaHistorialPrestamos(
                                    socio!!.idSocio!!,
                                    model.getLoginJWT.value?.JWT.toString()
                                )
                                    .await()
                            )
                            //cargo los libros reservados
                            model.setReservasNoFinalizadas(
                                ApiRestAdapter.cargaReservasNoFinalizadosSocio(
                                    socio!!.idSocio!!,
                                    model.getLoginJWT.value?.JWT.toString()
                                ).await()
                            )
                            model.setReservas(
                                ApiRestAdapter.cargaReservas(
                                    socio!!.idSocio!!,
                                    model.getLoginJWT.value?.JWT.toString()
                                ).await()
                            )
                        }
                    }
                }
            }
            model.getLoginJWT.observe(requireActivity(), nameObserverToken)

            //recibe los datos del socio modificado para cambiar de ventana a la del perfil del socio
            val nameObserver = Observer<Socio?> {
                if (it != null) {
                    model.setVisibilidadSocios(true)
                    val navController = NavHostFragment.findNavController(this)
                    if (navController.currentDestination?.id == R.id.fragmentLogin)
                        navController.navigate(R.id.action_fragmentLogin_to_fragmentPrincipal)
                }
            }
            model.getSocio.observe(requireActivity(), nameObserver)

        }
        return binding.root
    }
}