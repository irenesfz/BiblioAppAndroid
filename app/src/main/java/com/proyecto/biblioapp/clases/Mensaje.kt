package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Mensaje() : Parcelable {
    var mensaje: String? = null

    constructor(parcel: Parcel) : this() {
        mensaje = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mensaje)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mensaje> {
        override fun createFromParcel(parcel: Parcel): Mensaje {
            return Mensaje(parcel)
        }

        override fun newArray(size: Int): Array<Mensaje?> {
            return arrayOfNulls(size)
        }
    }
}