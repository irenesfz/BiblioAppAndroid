package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Prestamo() : Parcelable {
    var fechaDevolucion: String? = null
    var fechaPrestamo: String? = null
    var fechaTope: String? = null
    var idLibro: Int? = null
    var idPrestamo: Int? = null
    var idSocio: Int? = null

    constructor(parcel: Parcel) : this() {
        fechaDevolucion = parcel.readString()
        fechaPrestamo = parcel.readString()
        fechaTope = parcel.readString()
        idLibro = parcel.readValue(Int::class.java.classLoader) as? Int
        idPrestamo = parcel.readValue(Int::class.java.classLoader) as? Int
        idSocio = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fechaDevolucion)
        parcel.writeString(fechaPrestamo)
        parcel.writeString(fechaTope)
        parcel.writeValue(idLibro)
        parcel.writeValue(idPrestamo)
        parcel.writeValue(idSocio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Prestamo> {
        override fun createFromParcel(parcel: Parcel): Prestamo {
            return Prestamo(parcel)
        }

        override fun newArray(size: Int): Array<Prestamo?> {
            return arrayOfNulls(size)
        }
    }


}