package com.proyecto.biblioapp.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.proyecto.biblioapp.databinding.FragmentContactoBinding

class FragmentContacto : Fragment() {
    private lateinit var binding: FragmentContactoBinding
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
        binding = FragmentContactoBinding.inflate(inflater, container, false)

        binding.botonCorreo.setOnClickListener {
            enviarCorreo()
        }
        binding.botonLlamar.setOnClickListener {
            llamar()
        }

        return binding.root
    }

    private fun llamar() {
        //000000000
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        }
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:000000000")))
        //startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:000000000")))
    }

    private fun enviarCorreo() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("biblioapp22@gmail.com"))
        intent.putExtra(Intent.EXTRA_TEXT, "Estimada biblioteca ")
        startActivity(Intent.createChooser(intent, "Tipo mensaje"))
    }
}