package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Socio() : Parcelable {
    var apellidos: String? = null
    var rol: String? = null
    var categoriasInteres: String? = null
    var contrasenya: String? = null
    var correo: String? = null
    var direccion: String? = null
    var dni: String? = null
    var fechaNacimiento: String? = null
    var idSocio: Int? = null
    var telefono: Int? = null
    var imagen: String? = null
    var nombre: String? = null

    constructor(parcel: Parcel) : this() {
        apellidos = parcel.readString()
        rol = parcel.readString()
        categoriasInteres = parcel.readString()
        contrasenya = parcel.readString()
        correo = parcel.readString()
        direccion = parcel.readString()
        dni = parcel.readString()
        fechaNacimiento = parcel.readString()
        idSocio = parcel.readValue(Int::class.java.classLoader) as? Int
        telefono = parcel.readValue(Int::class.java.classLoader) as? Int
        imagen = parcel.readString()
        nombre = parcel.readString()
    }


    constructor(
        apellidos: String,
        categoriasInteres: String,
        rol: String,
        contrasenya: String,
        direccion: String,
        correo: String,
        dni: String,
        fechaNacimiento: String,
        idSocio: Int,
        telefono: Int,
        imagen: String,
        nombre: String
    ) : this() {
        this.idSocio = idSocio
        this.dni = dni
        this.rol = rol
        this.nombre = nombre
        this.apellidos = apellidos
        this.direccion = direccion
        this.correo = correo
        this.fechaNacimiento = fechaNacimiento
        this.contrasenya = contrasenya
        this.imagen = imagen
        this.telefono = telefono
        this.categoriasInteres = categoriasInteres
    }
//93847584H
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idSocio)
        parcel.writeValue(telefono)
        parcel.writeString(dni)
        parcel.writeString(rol)
        parcel.writeString(nombre)
        parcel.writeString(apellidos)
        parcel.writeString(direccion)
        parcel.writeString(correo)
        parcel.writeString(fechaNacimiento)
        parcel.writeString(contrasenya)
        parcel.writeString(imagen)
        parcel.writeString(categoriasInteres)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Socio> {
        override fun createFromParcel(parcel: Parcel): Socio {
            return Socio(parcel)
        }

        override fun newArray(size: Int): Array<Socio?> {
            return arrayOfNulls(size)
        }
    }

}