package com.proyecto.biblioapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Mensaje
import com.proyecto.biblioapp.databinding.FragmentCambiarContrasenyaBinding
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentCambiarContrasenya : Fragment() {
    private lateinit var binding: FragmentCambiarContrasenyaBinding
    private val model: ItemsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCambiarContrasenyaBinding.inflate(inflater, container, false)

        binding.cancelarButton.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPerfil)
        }
        binding.guardarButton.setOnClickListener {
            var mensaje: Mensaje? = null
            if (binding.newpasswordText.text.toString().isEmpty() ||
                binding.newpassword2Text.text.toString().isEmpty() ||
                binding.passwordText.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Error al introducir los datos",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (binding.newpasswordText.text.toString() == binding.newpassword2Text.text.toString()) {
                    //contraseña vieja binding.passwordText.text
                    CoroutineScope(Dispatchers.Main).launch {
                        ApiRestAdapter.cambiaContrasenya(
                            model.getSocio.value!!.dni.toString(),
                            binding.newpasswordText.text.toString(),
                            binding.passwordText.text.toString(),
                            model.getLoginJWT.value?.JWT.toString()
                        ).await()?.let { it1 ->
                            model.setMensaje(
                                it1
                            )
                        }
                    }

                }
            }
        }
        //cuando cambie el mensaje mira
        val nameObserverLogin = Observer<Mensaje> {
            if (it?.mensaje == "Contraseña actualizada") {
                Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                val navController = NavHostFragment.findNavController(this)
                navController.navigate(R.id.action_fragmentCambiarContrasenya_to_fragmentPerfil)
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        model.getMensaje.observe(requireActivity(), nameObserverLogin)
        return binding.root
    }

}