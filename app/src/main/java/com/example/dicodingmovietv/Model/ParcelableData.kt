package com.example.dicodingmovietv.Model

import android.os.Parcel
import android.os.Parcelable

data class ParcelableData(
    var id: Int?,
    val name: String?,
    val desc: String?,
    val img: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableData> {
        override fun createFromParcel(parcel: Parcel): ParcelableData {
            return ParcelableData(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableData?> {
            return arrayOfNulls(size)
        }
    }
}