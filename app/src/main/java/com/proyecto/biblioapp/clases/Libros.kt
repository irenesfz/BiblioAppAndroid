package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class Libros() : Parcelable {
    var isbn:String? = null
    var titulo:String? = null
    var autores: String? = null
    var descripcion: String? = null
    var paginas: Int? = null
    var categoria: String? = null
    var subcategorias: String? = null
    var anyoPublicacion: Int? = null
    var editorial: String? = null
    var idioma: String? = null
    var imagen: String? = null

    constructor(parcel: Parcel) : this() {
        anyoPublicacion = parcel.readValue(Int::class.java.classLoader) as? Int
        autores = parcel.readString()
        categoria = parcel.readString()
        descripcion = parcel.readString()
        editorial = parcel.readString()
        idioma = parcel.readString()
        imagen = parcel.readString()
        isbn = parcel.readString()
        paginas = parcel.readValue(Int::class.java.classLoader) as? Int
        subcategorias = parcel.readString()
        titulo = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(isbn)
        parcel.writeString(titulo)
        parcel.writeString(autores)
        parcel.writeString(descripcion)
        parcel.writeValue(paginas)
        parcel.writeString(categoria)
        parcel.writeString(subcategorias)
        parcel.writeValue(anyoPublicacion)
        parcel.writeString(editorial)
        parcel.writeString(idioma)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Libros> {
        override fun createFromParcel(parcel: Parcel): Libros {
            return Libros(parcel)
        }

        override fun newArray(size: Int): Array<Libros?> {
            return arrayOfNulls(size)
        }
    }


}