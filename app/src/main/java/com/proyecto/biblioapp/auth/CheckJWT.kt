package com.proyecto.biblioapp.auth

import android.os.Parcel
import android.os.Parcelable

class CheckJWT() : Parcelable {
    var Subject: String? = null
    var Issuer: String? = null
    var IssuedAt: String? = null
    var Expiration: String? = null
    var usuario: String? = null
    var id_sesion_recibida: String? = null
    var id_sesion_actual: String? = null
    var validate_session: Boolean? = null
    var validate_expiration: Boolean? = null
    var validate: Boolean? = null
    var resul: String? = null

    constructor(parcel: Parcel) : this() {
        Subject = parcel.readString()
        Issuer = parcel.readString()
        IssuedAt = parcel.readString()
        Expiration = parcel.readString()
        usuario = parcel.readString()
        id_sesion_recibida = parcel.readString()
        id_sesion_actual = parcel.readString()
        validate_session = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        validate_expiration = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        validate = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        resul = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Subject)
        parcel.writeString(Issuer)
        parcel.writeString(IssuedAt)
        parcel.writeString(Expiration)
        parcel.writeString(usuario)
        parcel.writeString(id_sesion_recibida)
        parcel.writeString(id_sesion_actual)
        parcel.writeValue(validate_session)
        parcel.writeValue(validate_expiration)
        parcel.writeValue(validate)
        parcel.writeString(resul)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckJWT> {
        override fun createFromParcel(parcel: Parcel): CheckJWT {
            return CheckJWT(parcel)
        }

        override fun newArray(size: Int): Array<CheckJWT?> {
            return arrayOfNulls(size)
        }
    }


}