package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Evento() : Parcelable {
    var idEvento: String? = null
    var titulo: String? = null
    var descripcion: String? = null
    var imagen: String? = null
    var fechaPublicacion: String? = null
    var fechaEvento: String? = null

    constructor(
        idEvento: String, titulo: String,
        descripcion: String, imagen: String,
        fecha_publicacion: String,
        fecha_evento: String
    ) : this() {
        this.idEvento = idEvento
        this.titulo = titulo
        this.descripcion = descripcion
        this.imagen = imagen
        this.fechaPublicacion = fecha_publicacion
        this.fechaEvento = fecha_evento
    }

    constructor(parcel: Parcel) : this() {
        idEvento = parcel.readString()
        titulo = parcel.readString()
        descripcion = parcel.readString()
        imagen = parcel.readString()
        fechaPublicacion = parcel.readString()
        fechaEvento = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idEvento)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeString(imagen)
        parcel.writeString(fechaPublicacion)
        parcel.writeString(fechaEvento)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Evento> {
        override fun createFromParcel(parcel: Parcel): Evento {
            return Evento(parcel)
        }

        override fun newArray(size: Int): Array<Evento?> {
            return arrayOfNulls(size)
        }
    }

}