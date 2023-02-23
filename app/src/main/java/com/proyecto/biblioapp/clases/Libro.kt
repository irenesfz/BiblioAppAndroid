package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Libro() : Parcelable {
    var disponible:Boolean? = null
    var idLibro:Int? = null
    var isbn: String? = null
    var reservado:Boolean? = null

    constructor(parcel: Parcel) : this() {
        disponible = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        idLibro = parcel.readValue(Int::class.java.classLoader) as? Int
        isbn = parcel.readString()
        reservado = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(disponible)
        parcel.writeValue(idLibro)
        parcel.writeString(isbn)
        parcel.writeValue(reservado)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Libro> {
        override fun createFromParcel(parcel: Parcel): Libro {
            return Libro(parcel)
        }

        override fun newArray(size: Int): Array<Libro?> {
            return arrayOfNulls(size)
        }
    }
}