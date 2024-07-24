package app.pay.panda.responsemodels.getOperators

import android.os.Parcelable
import android.os.Parcel

data class CustomerParam(
    val _id: String="",
    val dataType: String="",
    val maxLength: Int=0,
    val minLength: Int=0,
    val optional: Boolean=false,
    val paramName: String="",
    val regex: String="",
    val visibility: String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(dataType)
        parcel.writeInt(maxLength)
        parcel.writeInt(minLength)
        parcel.writeByte(if (optional) 1 else 0)
        parcel.writeString(paramName)
        parcel.writeString(regex)
        parcel.writeString(visibility)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerParam> {
        override fun createFromParcel(parcel: Parcel): CustomerParam {
            return CustomerParam(parcel)
        }

        override fun newArray(size: Int): Array<CustomerParam?> {
            return arrayOfNulls(size)
        }
    }
}