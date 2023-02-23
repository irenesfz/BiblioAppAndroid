package com.proyecto.biblioapp.auth

import android.os.Parcel
import android.os.Parcelable

class Sesion() : Parcelable {
    var id_session: String? = null
    var mensaje: String? = null
    var usuario: String? = null
    var rol: String? = null

    constructor(
        id_session: String,
        usuario: String,
        rol: String,
        mensaje: String
    ) : this() {
        this.id_session = id_session
        this.usuario = usuario
        this.rol = rol
        this.mensaje = mensaje
    }

    constructor(parcel: Parcel) : this() {
        id_session = parcel.readString()
        usuario = parcel.readString()
        rol = parcel.readString()
        mensaje = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_session)
        parcel.writeString(usuario)
        parcel.writeString(rol)
        parcel.writeString(mensaje)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sesion> {
        override fun createFromParcel(parcel: Parcel): Sesion {
            return Sesion(parcel)
        }

        override fun newArray(size: Int): Array<Sesion?> {
            return arrayOfNulls(size)
        }
    }

}