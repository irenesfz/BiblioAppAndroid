package com.proyecto.biblioapp.auth

import android.os.Parcel
import android.os.Parcelable

class Token() : Parcelable {
    var JWT: String? = null

    constructor(
        JWT: String
    ) : this() {
        this.JWT = JWT
    }

    constructor(parcel: Parcel) : this() {
        JWT = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(JWT)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Token> {
        override fun createFromParcel(parcel: Parcel): Token {
            return Token(parcel)
        }

        override fun newArray(size: Int): Array<Token?> {
            return arrayOfNulls(size)
        }
    }
}