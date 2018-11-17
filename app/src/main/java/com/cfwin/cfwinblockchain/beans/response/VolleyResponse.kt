package com.cfwin.cfwinblockchain.beans.response

import android.os.Parcel
import android.os.Parcelable

open class VolleyResponse: Parcelable{

    var msg:String = ""
    var result:Boolean = false

    constructor()

    protected constructor(parcel: Parcel){
        msg = parcel.readString()
        result = (parcel.readInt() == 1)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(msg)
        parcel.writeInt(if(result) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    private companion object CREATOR : Parcelable.Creator<VolleyResponse> {
        override fun createFromParcel(parcel: Parcel): VolleyResponse {
            return VolleyResponse(parcel)
        }

        override fun newArray(size: Int): Array<VolleyResponse?> {
            return arrayOfNulls(size)
        }
    }

}