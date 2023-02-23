package com.proyecto.biblioapp.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Mensaje
import com.proyecto.biblioapp.clases.Prestamo
import com.proyecto.biblioapp.clases.Reservas
import com.proyecto.biblioapp.clases.Socio
import com.proyecto.biblioapp.databinding.DialogoCategoriaInteresBinding
import com.proyecto.biblioapp.databinding.DialogoModificaCorreoBinding
import com.proyecto.biblioapp.databinding.FragmentPerfilBinding
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.proyecto.biblioapp.utils.ImagenUtilidad.ConvertirImagenString
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentPerfil : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private lateinit var bindingDialogo: DialogoModificaCorreoBinding
    private lateinit var bindingDialogoCategoriaInteres: DialogoCategoriaInteresBinding
    private val model: ItemsViewModel by activityViewModels()

    //cámara y galería
    private lateinit var resultadoCamara: ActivityResultLauncher<Intent>
    private lateinit var resultadoGaleria: ActivityResultLauncher<Intent>
    private lateinit var registerPermisosTomarFoto: ActivityResultLauncher<String>
    private lateinit var registerPermisosStorage: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //controla el botón de volver para no ir cambiando de fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
        //tomar foto o cogerla de la galería
        registerPermisosTomarFoto =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            {
                if (it == true) tomarFoto()
            }
        /*registerPermisosStorage =
             registerForActivityResult(ActivityResultContracts.RequestPermission())
             {
                 if (it == true) tomarGaleria()
             }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)

        binding.infoBoton.setOnClickListener {
            Snackbar.make(
                it,
                "Mantén pulsada la foto de perfil o el correo electrónico para editarlos.",
                Snackbar.LENGTH_LONG
            ).show()

        }
        creaContratos()

        binding.fotoBoton.setOnCreateContextMenuListener(this)

        binding.botonCorreo.setOnClickListener {
            enviarCorreo()
        }

        binding.botonLlamar.setOnClickListener {
            llamar()
        }

        //CARGA SOCIO
        val nameObserver = Observer<Socio?> {
            if (it != null) {
                binding.datoNombre.text = it.nombre
                binding.datoApellidos.text = it.apellidos
                binding.datoCorreo.text = it.correo
                binding.datoTelefono.text = it.telefono.toString()
                if (it.categoriasInteres.isNullOrEmpty())
                    binding.datoCategorias.text = "ninguna"
                else
                    binding.datoCategorias.text = it.categoriasInteres?.replace("-", ", ")
                binding.fotoBoton.setImageBitmap(ImagenUtilidad.convertirStringBitmap(it.imagen.toString()))
            }
        }
        model.getSocio.observe(requireActivity(), nameObserver)

        //cambia el numero de préstamos
        val nameObserverNumPrestamos = Observer<ArrayList<Prestamo>> {
            binding.datoPrestamo.text = it.size.toString()
        }
        model.getPrestamosNoFinalizados.observe(requireActivity(), nameObserverNumPrestamos)

        //cambia el numero de reservas
        val nameObserverNumReservas = Observer<ArrayList<Reservas>> {
            binding.datoReserva.text = it.size.toString()
        }
        model.getReservasNoFinalizadas.observe(requireActivity(), nameObserverNumReservas)

        //cambia categoria interes
        binding.datoCategorias.setOnLongClickListener {
            bindingDialogoCategoriaInteres = DialogoCategoriaInteresBinding.inflate(layoutInflater)
            var mensaje: Mensaje? = null
            var check: Boolean? = false
            val dialogo = AlertDialog.Builder(context)
            bindingDialogoCategoriaInteres.categoriasText.setText(model.getSocio.value?.categoriasInteres.toString())
            dialogo.setView(bindingDialogoCategoriaInteres.root)
                .setPositiveButton("Aceptar") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        //comprueba si ha caducado la sesión
                        check =
                            ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                        if (check == true) {
                            val socio = model.getSocio.value
                            //modifico los datos del socio
                            if (bindingDialogoCategoriaInteres.categoriasText.text.toString()
                                    .isBlank()
                            )
                                socio!!.categoriasInteres = "nada"
                            else
                                socio!!.categoriasInteres =
                                    bindingDialogoCategoriaInteres.categoriasText.text.toString()
                            mensaje = ApiRestAdapter.cambiaCategorias(
                                socio!!.categoriasInteres.toString(),
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                            //aplico los cambios del socio en el view model si el socio se ha actualizado correctamente en la bd
                            if (mensaje!!.mensaje.toString() == "Socio actualizado") {
                                model.setSocio(socio)
                                Toast.makeText(
                                    requireContext(),
                                    "Categorías actualizadas",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "La sesión ha caducado",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.setNegativeButton("Cancelar") { _, _ -> }
            val dialog = dialogo.create()
            dialog.show()
            true
        }

        //cambia correo electrónico
        binding.datoCorreo.setOnLongClickListener {
            var mensaje: Mensaje? = null
            var check: Boolean? = false
            val dialogo = AlertDialog.Builder(context)
            bindingDialogo = DialogoModificaCorreoBinding.inflate(layoutInflater)
            bindingDialogo.emailText.setText(model.getSocio.value?.correo.toString())
            dialogo.setView(bindingDialogo.root)
                .setPositiveButton("Aceptar") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        check =
                            ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                        if (check == true) {
                            val socio = model.getSocio.value
                            //modifico los datos del socio
                            socio!!.correo = bindingDialogo.emailText.text.toString()
                            mensaje = ApiRestAdapter.cambiaCorreoElectronico(
                                bindingDialogo.emailText.text.toString(),
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                            //aplico los cambios del socio en el view model si el socio se ha actualizado correctamente en la bd
                            if (mensaje!!.mensaje.toString() == "Socio actualizado") {
                                model.setSocio(socio)
                                Toast.makeText(
                                    requireContext(),
                                    "Correo electrónico actualizado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "La sesión ha caducado",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.setNegativeButton("Cancelar") { _, _ -> }
            val dialog = dialogo.create()
            dialog.show()
            true
        }

        binding.botonCambios.setOnClickListener {
            actualizaImg()
        }

        binding.cambiarContrasenya.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_fragmentPerfil_to_fragmentCambiarContrasenya)
        }
        return binding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val inflater = MenuInflater(v.context)
        inflater.inflate(R.menu.menu_context_foto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hacerFoto -> {
                registerPermisosTomarFoto.launch(Manifest.permission.CAMERA)
            }
            /*   R.id.abrirGaleria -> {
                   registerPermisosStorage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
               }*/
            R.id.borrarFoto -> borrarFoto()
        }
        binding.botonCambios.visibility = View.VISIBLE
        return super.onContextItemSelected(item)
    }

    private fun actualizaImg() {
        var check: Boolean? = false
        CoroutineScope(Dispatchers.Main).launch {
            check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
            if (check == true) {
                var mensaje: Mensaje? = null
                val socio = model.getSocio.value
                //modifico los datos del socio
                val bmdrawable = binding.fotoBoton.drawable?.let { it as BitmapDrawable }
                // contacto.foto = ConvertirImagenString(bmdrawable!!.bitmap)
                socio!!.imagen = ConvertirImagenString(bmdrawable!!.bitmap)
                //cambiaImagenPerfil
                mensaje = ApiRestAdapter.cambiaImagenPerfil(
                    ConvertirImagenString(bmdrawable.bitmap),
                    model.getSocio.value!!.idSocio!!,
                    model.getLoginJWT.value?.JWT.toString()
                ).await()
                //aplico los cambios del socio en el view model si el socio se ha actualizado correctamente en la bd
                if (mensaje!!.mensaje.toString() == "Socio actualizado") {
                    model.setSocio(socio)
                    Toast.makeText(
                        requireContext(),
                        "Foto de perfil actualizada",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.botonCambios.visibility = View.GONE
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "La sesión ha caducado",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun borrarFoto() {
        binding.fotoBoton.setImageBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.no_image
            )
        )
    }

    private fun tomarFoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultadoCamara.launch(cameraIntent)
    }

    private fun tomarGaleria() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultadoGaleria.launch(cameraIntent)
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

    private fun creaContratos() {
        resultadoCamara =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK)
                    binding.fotoBoton.setImageBitmap(result.data?.extras?.get("data") as Bitmap)
            }
        /* resultadoGaleria =
             registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                 if (result.resultCode == Activity.RESULT_OK)
                     binding.fotoBoton.setImageURI(result.data?.data)
             }*/
    }
}