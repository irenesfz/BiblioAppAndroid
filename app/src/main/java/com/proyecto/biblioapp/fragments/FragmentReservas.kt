package com.proyecto.biblioapp.fragments

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.biblioapp.R
import com.proyecto.biblioapp.adaptadores.AdaptadorReservas
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.clases.Reservas
import com.proyecto.biblioapp.databinding.FragmentReservasBinding
import com.proyecto.biblioapp.notificationes.ServicioAlarma
import com.proyecto.biblioapp.utils.SwipeDetector
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class FragmentReservas : Fragment(), View.OnClickListener {
    var swipeDetector: SwipeDetector = SwipeDetector()
    private lateinit var binding: FragmentReservasBinding
    private val model: ItemsViewModel by activityViewModels()
    var posicion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //controla el botón de volver para no ir cambiando de fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
        //createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservasBinding.inflate(inflater, container, false)
        createNotificationChannel()
        var historial = true
        //observer pendiente al cambio para volver a cargar adapter
        val nameObserver = Observer<ArrayList<Reservas>> {
            cargarAdaptador(it)
            if (it.size == 0)
                binding.mostrarDatos.visibility = View.VISIBLE
        }
        model.getReservasNoFinalizadas.observe(requireActivity(), nameObserver)

        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            var check: Boolean? = false
            //carga los datos de los préstamos
            CoroutineScope(Dispatchers.Main).launch {
                check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                if (check == true) {
                    //cargo los préstamos dependiendo si está en historial o no
                    if (!historial) {
                        model.setReservas(
                            ApiRestAdapter.cargaReservas(
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                        )
                        cargarAdaptador(model.getReservas.value!!)
                    } else {
                        model.setReservasNoFinalizadas(
                            ApiRestAdapter.cargaReservasNoFinalizadosSocio(
                                model.getSocio.value!!.idSocio!!,
                                model.getLoginJWT.value?.JWT.toString()
                            ).await()
                        )
                        cargarAdaptador(model.getReservasNoFinalizadas.value!!)
                    }
                } else {
                    binding.textReservas.text = "RESERVAS NO FINALIZADAS"
                    historial = false
                    Toast.makeText(requireContext(), "La sesión ha caducado", Toast.LENGTH_LONG)
                        .show()
                }
            }
            binding.swipeRefresh.isRefreshing = false
        }

        binding.historialBoton.setOnClickListener {
            //ir cambiando en reservas no finalizadas/historial
            historial = if (historial) {
                cargarAdaptador(model.getReservas.value!!)
                binding.textReservas.text = "HISTORIAL DE RESERVAS"
                false
            } else {
                cargarAdaptador(model.getReservasNoFinalizadas.value!!)
                binding.textReservas.text = "RESERVAS NO FINALIZADAS"
                true
            }
        }

        //carg adapter por si acaso
        // cargarAdaptador(model.getReservasNoFinalizadas.value!!)
        return binding.root
    }

    private fun createNofitication(dateString: String, id: Int, titulo: String) {
        //convierto la cadena fecha en Date
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateString)
        val calendarReserva: Calendar = Calendar.getInstance()
        calendarReserva.time = date
        val alarmManager: AlarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager
        //convierto la fecha en milisegundos
        val thuReq: Long = Calendar.getInstance().timeInMillis
        val reqReqCode = thuReq.toInt()
        //intent con la clase broadcast
        val intent = Intent(requireContext(), ServicioAlarma::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        //le paso el id de la reserva y el titulo del libro
        intent.putExtra("VALUE", id)
        intent.putExtra("Titulo", titulo)
        var pendingIntent: PendingIntent? = null

        //if para cambiar el tipo de pendingItent según las versiones soportadas
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                requireContext(),
                reqReqCode,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                reqReqCode,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

        val alarmTimeMilsec = calendarReserva.timeInMillis
        //creo la alarma/notificación
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,//tipo de alarma
            alarmTimeMilsec,//fecha en milisegundos
            pendingIntent //operación pendingIntent
        )
    }


    private fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(requireContext()).cancel(id)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //(1)
            val name = getString(R.string.basic_channel_name)   // (2)
            val channelId = getString(R.string.basic_channel_id) // (3)
            val descriptionText = getString(R.string.basic_channel_description) // (4)
            val importance = NotificationManager.IMPORTANCE_DEFAULT // (5)

            val channel = NotificationChannel(channelId, name, importance).apply { // (6)
                description = descriptionText
            }

            val nm: NotificationManager =
                activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // (7)
            nm.createNotificationChannel(channel) // (8)
        }
    }

    fun cargaReservas() {
        CoroutineScope(Dispatchers.Main).launch {
            model.setReservasNoFinalizadas(
                ApiRestAdapter.cargaReservasNoFinalizadosSocio(
                    model.getSocio.value!!.idSocio!!,
                    model.getLoginJWT.value?.JWT.toString()
                ).await()
            )
            model.setReservas(
                ApiRestAdapter.cargaReservas(
                    model.getSocio.value!!.idSocio!!,
                    model.getLoginJWT.value?.JWT.toString()
                ).await()
            )
        }
    }

    fun cargarAdaptador(reservas: ArrayList<Reservas>) {
        adaptador = AdaptadorReservas(reservas)
        binding.recyclerContainer.setHasFixedSize(true)
        binding.recyclerContainer.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        //listener para desplazamiento izquierda y derecha sobre un elemento de la lista
        swipeDetector = SwipeDetector()
        adaptador?.setOnTouchListener(swipeDetector)
        //listener para onClick sobre un elemento del recyclerView
        adaptador?.setOnClickListener(this)
        binding.recyclerContainer.adapter = adaptador

    }

    override fun onClick(view: View) {
        posicion = binding.recyclerContainer.getChildAdapterPosition(view)
        if (binding.textReservas.text == "RESERVAS NO FINALIZADAS") {
            if (swipeDetector.swipeDetected()) {
                if (swipeDetector.action === SwipeDetector.Action.LR) {
                    if (model.getReservasNoFinalizadas.value?.get(
                            posicion
                        )?.notificacion == true
                    ) {
                        dialogoDesactivarNotificacion(view, posicion)
                    } else {
                        dialogoActivarNotificacion(view, posicion)

                    }
                } else if (swipeDetector.action === SwipeDetector.Action.RL) eliminaReserva(posicion)
            }
        }
    }

    private fun eliminaReserva(posicion: Int) {
        var check: Boolean?
        val dialogo = AlertDialog.Builder(requireContext())
        dialogo.setMessage("¿Quieres finalizar la reserva?")
            .setPositiveButton("Aceptar") { dialog, which ->
                CoroutineScope(Dispatchers.Main).launch {
                    check = ApiRestAdapter.checkJWT(model.getLoginJWT.value?.JWT.toString()).await()
                    if (check == true) {
                        //elimina
                        var mensaje = ApiRestAdapter.quitaReserva(
                            model.getReservasNoFinalizadas.value?.get(
                                posicion
                            )?.idReserva!!,
                            model.getReservasNoFinalizadas.value?.get(
                                posicion
                            )?.libroId!!,
                            model.getLoginJWT.value!!.JWT.toString()
                        ).await()
                        Log.d("Error", mensaje.mensaje.toString())
                        if (mensaje.mensaje == "Reserva finalizada") {
                            //pone disponible la unidad de libros
                            mensaje = ApiRestAdapter.unidadLibroDisponibleReserva(
                                model.getReservasNoFinalizadas.value?.get(
                                    posicion
                                )?.libroId!!,
                                model.getLoginJWT.value!!.JWT.toString()
                            ).await()

                            //elimina la notificación ti la tuviera puesta
                            if (model.getReservasNoFinalizadas.value!![posicion].notificacion == true) {
                                cancelNotification(posicion)
                            }
                            cargaReservas()
                            cargarAdaptador(model.getReservasNoFinalizadas.value!!)
                            if (mensaje.mensaje == "Libro actualizado")
                                Toast.makeText(
                                    requireContext(),
                                    "Reserva finalizada",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "La sesión ha caducado", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, which ->

            }
        val dialog = dialogo.create()
        dialog.show()
    }

    private fun dialogoActivarNotificacion(v: View, posicion: Int) {
        val dialogo = AlertDialog.Builder(requireContext())
        dialogo.setMessage("¿Quieres recibir una notificación cuando esté disponible este libro?")
            .setPositiveButton("Aceptar") { dialog, which ->

                //actualiza en la bd
                CoroutineScope(Dispatchers.Main).launch {
                    val mensaje = ApiRestAdapter.poneNotification(
                        model.getReservasNoFinalizadas.value?.get(
                            binding.recyclerContainer.getChildAdapterPosition(
                                v
                            )
                        )?.idReserva!!,
                        model.getLoginJWT.value!!.JWT.toString()
                    ).await()
                    Log.d("Error", mensaje.mensaje!!)
                    if (mensaje.mensaje == "Notificación puesta") {
                        model.getReservasNoFinalizadas.value?.get(
                            binding.recyclerContainer.getChildAdapterPosition(
                                v
                            )
                        )?.notificacion = true
                        //activa notificación
                        Toast.makeText(
                            requireContext(),
                            "Notificación activada",
                            Toast.LENGTH_SHORT
                        ).show()

                        createNofitication(
                            model.getReservasNoFinalizadas.value?.get(posicion)?.fechaReserva!!,
                            posicion, //posición para guardarlo como id de notificación
                            ApiRestAdapter.cargaLibro(
                                model.getReservasNoFinalizadas.value?.get(
                                    posicion
                                )!!.libroId!!
                            )
                                .await().titulo.toString() //titulo del libro para mostrarlo en la notificacion
                        )

                        cargarAdaptador(model.getReservasNoFinalizadas.value!!)
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, which ->

            }
        val dialog = dialogo.create()
        dialog.show()
    }


    private fun dialogoDesactivarNotificacion(v: View, posicion: Int) {
        val dialogo = AlertDialog.Builder(requireContext())
        dialogo.setMessage("¿Quieres desactivar la notificación de este libro?")
            .setPositiveButton("Aceptar") { dialog, which ->

                //actualiza en la bd
                CoroutineScope(Dispatchers.Main).launch {
                    val mensaje = ApiRestAdapter.quitaNotification(
                        model.getReservasNoFinalizadas.value?.get(
                            binding.recyclerContainer.getChildAdapterPosition(
                                v
                            )
                        )?.idReserva!!,
                        model.getLoginJWT.value!!.JWT.toString()
                    ).await()
                    Log.d("Error", mensaje.mensaje!!)
                    if (mensaje.mensaje == "Notificación quitada") {
                        model.getReservasNoFinalizadas.value?.get(
                            binding.recyclerContainer.getChildAdapterPosition(
                                v
                            )
                        )?.notificacion = false
                        //activa noti
                        Toast.makeText(
                            requireContext(),
                            "Notificación desactivada",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        cancelNotification(posicion)
                        cargarAdaptador(model.getReservasNoFinalizadas.value!!)
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, which ->

            }
        val dialog = dialogo.create()
        dialog.show()
    }

    companion object {
        var adaptador: AdaptadorReservas? = null
    }


}