package com.proyecto.biblioapp.notificationes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.proyecto.biblioapp.R

class ServicioAlarma() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //le paso la id de notificacion para distinguir entre libros y poder cancelarlo luego
        val id: Int = intent!!.getIntExtra("VALUE", 0)
        //titulo del libro para ponerlo en la notificación
        val titulo: String? = intent.getStringExtra("Titulo")
        generateNotification(context!!, id, titulo)
    }

    private fun generateNotification(context: Context, id: Int, titulo: String?) {
        val notificationId = id
        val channelId = "basic_channel" // (1)
        val body = "Estimado socio, ya puedes pasarte por la biblioteca " + // (2)
                "y solicitar el préstamo del libro '$titulo'. \nRecuerda que la reserva " +
                "será eliminada en un plazo de 72h."

        val notification = NotificationCompat.Builder(context, channelId) // (3)
            .setSmallIcon(R.drawable.notification) // (4)
            .setContentTitle("¡El libro que has reservado ya está disponible!") // (5)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))// (6)
            .setContentText(body) // (7)
            .setSubText("Biblioteca Pública") // (8)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // (9)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}