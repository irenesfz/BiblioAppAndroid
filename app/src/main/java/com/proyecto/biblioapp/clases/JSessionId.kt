package com.proyecto.biblioapp.clases

import android.os.Parcel
import android.os.Parcelable

class JSessionId() : Parcelable {
    var session : String? = null

    constructor(parcel: Parcel) : this() {
        session = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(session)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JSessionId> {
        override fun createFromParcel(parcel: Parcel): JSessionId {
            return JSessionId(parcel)
        }

        override fun newArray(size: Int): Array<JSessionId?> {
            return arrayOfNulls(size)
        }
    }
}