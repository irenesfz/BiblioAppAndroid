package com.proyecto.biblioapp.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Libro
import com.proyecto.biblioapp.clases.Mensaje
import com.proyecto.biblioapp.databinding.FragmentDetalleLibroBinding
import com.proyecto.biblioapp.notificationes.ServicioAlarmaReserva
import com.proyecto.biblioapp.utils.ImagenUtilidad
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class FragmentDetalleLibro : Fragment() {
    private lateinit var binding: FragmentDetalleLibroBinding
    private val model: ItemsViewModel by activityViewModels()
    var mItem: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bundle para pasarle el libro
        if (savedInstanceState != null) mItem = savedInstanceState?.getInt("POSICION")
        else {
            if (arguments != null) {
                mItem = arguments?.getInt("POSICION")
            } else mItem = -1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleLibroBinding.inflate(inflater, container, false)

        if (mItem != -1) {
            val libro = model.getLibroMuestra().value?.get(mItem!!)
            binding.TituloText.text = libro?.titulo.toString()
            binding.AutorText.text = libro?.autores.toString()
            binding.DescripcionText.text = libro?.descripcion.toString()
            binding.IdiomaText.text = libro?.idioma.toString()
            binding.PaginasText.text = libro?.paginas.toString()
            binding.PublicacionText.text = libro?.anyoPublicacion.toString()
            if (libro?.editorial.toString() != "")
                binding.EditorialText.text = libro?.editorial.toString()
            else binding.EditorialText.text = "Editorial no disponible."
            binding.CategoriaText.text = libro?.categoria.toString()
            if (libro?.subcategorias.toString() != "" && libro?.subcategorias != null)
                binding.SubcategoriasText.text = libro?.subcategorias.toString()
            else {
                binding.SubcategoriasText.visibility = View.GONE
                binding.SubcategoriasEtiqueta.visibility = View.GONE
            }
            binding.ISBNText.text = libro?.isbn.toString()

            if (libro?.imagen.toString().startsWith("http"))
            // binding.PortadaLibro.setImageBitmap(ImagenUtilidad.convertirURLBitmap(libro?.imagen, context!!))
                Picasso.get().load(libro?.imagen.toString()).error(R.drawable.no_image)
                    .into(binding.PortadaLibro)
            else
                binding.PortadaLibro.setImageBitmap(ImagenUtilidad.convertirStringBitmap(libro?.imagen.toString()))

            binding.botonReservar.setOnClickListener {
                var mensajeReserva: Mensaje? = null
                CoroutineScope(Dispatchers.Main).launch {
                    if (model.getUnidadLibroReservas.value?.idLibro != null) {
                        mensajeReserva = ApiRestAdapter.reservaLibro(
                            model.getUnidadLibroReservas.value?.idLibro!!,
                            model.getSocio.value?.idSocio!!,
                            model.getLoginJWT.value?.JWT.toString()
                        ).await()
                    }
                    if (mensajeReserva?.mensaje.toString() == "Reserva insertada") {
                        createNofitication()
                        binding.botonReservar.visibility = View.GONE
                        //carga de nuevo reservas
                        //cargo los libros reservados
                        model.setReservasNoFinalizadas(
                            ApiRestAdapter.cargaReservasNoFinalizadosSocio(
                                model.getSocio.value?.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al reservar el libro",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val nameObserver = Observer<Libro> {

                if (model.getUnidadLibroPrestamo.value?.disponible == true) {
                    binding.libroDisponibleText.text = "Préstamo disponible"
                } else {
                    "Préstamo no disponible".also { binding.libroDisponibleText.text = it }
                }
            }

            model.getUnidadLibroPrestamo.observe(requireActivity(), nameObserver)

            val nameObserver2 = Observer<Libro> {
                if (model.getVisibilidadSocios.value == true) {
                    if (model.getUnidadLibroReservas.value?.reservado == true || model.getUnidadLibroReservas.value?.reservado == null) {//porque es uint
                        binding.botonReservar.visibility = View.GONE
                    } else {
                        binding.botonReservar.visibility = View.VISIBLE
                    }
                } else {
                    binding.botonReservar.visibility = View.GONE
                }
            }

            model.getUnidadLibroReservas.observe(requireActivity(), nameObserver2)

            if (model.getUnidadLibroPrestamo.value?.disponible == true) {
                binding.libroDisponibleText.text = "Préstamo disponible"
            } else {
                binding.libroDisponibleText.text = "Préstamo no disponible"
            }

            //boton reservas
            if (model.getVisibilidadSocios.value == true) {
                if (model.getUnidadLibroReservas.value?.reservado == true || model.getUnidadLibroReservas.value?.reservado == null ) {
                    binding.botonReservar.visibility = View.GONE
                } else {
                    binding.botonReservar.visibility = View.VISIBLE
                }
            } else {
                binding.botonReservar.visibility = View.GONE
            }
        }

        //crea canal de notificaciones
        createNotificationChannel()

        return binding.root
    }
    private fun createNofitication() {
        var alarmManager: AlarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val thuReq: Long = Calendar.getInstance().timeInMillis
        var reqReqCode = thuReq.toInt()

        val intent = Intent(requireContext(), ServicioAlarmaReserva::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(requireContext(), reqReqCode, intent,PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(requireContext(), reqReqCode, intent, PendingIntent.FLAG_ONE_SHOT)
        }
     //   val pendingIntent = PendingIntent.getBroadcast(requireContext(), reqReqCode, intent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //(1)
            val name = getString(R.string.basic_channel_name)   // (2)
            val channelId = "111" // (3)
            val descriptionText = getString(R.string.basic_channel_description) // (4)
            val importance = NotificationManager.IMPORTANCE_DEFAULT // (5)

            val channel = NotificationChannel(channelId, name, importance).apply { // (6)
                description = descriptionText
            }

            val nm: NotificationManager =
                getActivity()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // (7)
            nm.createNotificationChannel(channel) // (8)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("POSICION", mItem!!)
    }
}