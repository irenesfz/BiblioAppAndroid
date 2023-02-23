package com.proyecto.biblioapp.notificationes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.proyecto.biblioapp.R

class ServicioAlarmaReserva : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        generateNotification(p0!!)
    }


    private fun generateNotification(context: Context) {
        val notificationId = 0
        val channelId = "111"// (1)
        val largeIcon = BitmapFactory.decodeResource(null, R.drawable.logo3)
        val body = "Puedes consultar tus libros reservados en el apartado de reservas."

        val notification = NotificationCompat.Builder(context, channelId) // (3)
            .setLargeIcon(largeIcon) // (4)
            .setSmallIcon(R.drawable.notification) // (5)
            .setContentTitle("¡Has reservado un libro!") // (6)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentText(body) // (8)
            .setSubText("Biblioteca Pública") // (8)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // (9)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }


}