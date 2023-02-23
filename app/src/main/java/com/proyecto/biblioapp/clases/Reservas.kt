package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Reservas() : Parcelable {
    var fechaReserva: String? = null
    var finalizada: Boolean? = null
    var idReserva: Int? = null
    var libroId: Int? = null
    var socioId: Int? = null
    var notificacion: Boolean? = null


    constructor(
        fechaReservaDate: Date?, finalizada: Boolean?,
        idReserva: Int?, libroId: Int?, socioId: Int?,
        notificacion: Boolean?
    ) : this() {
        this.fechaReserva = fechaReservaDate.toString()
        this.finalizada = finalizada
        this.idReserva = idReserva
        this.libroId = libroId
        this.notificacion = notificacion
        this.socioId = socioId
    }

    constructor(parcel: Parcel) : this() {
        fechaReserva = parcel.readString()
        finalizada = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        idReserva = parcel.readValue(Int::class.java.classLoader) as? Int
        libroId = parcel.readValue(Int::class.java.classLoader) as? Int
        notificacion = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        socioId = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fechaReserva)
        parcel.writeValue(finalizada)
        parcel.writeValue(idReserva)
        parcel.writeValue(libroId)
        parcel.writeValue(notificacion)
        parcel.writeValue(socioId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reservas> {
        override fun createFromParcel(parcel: Parcel): Reservas {
            return Reservas(parcel)
        }

        override fun newArray(size: Int): Array<Reservas?> {
            return arrayOfNulls(size)
        }
    }


}