package com.cfwin.cfwinblockchain.beans.response

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

open class CandyResponse<T> : Parcelable{

    var Msg:String = ""
    var Result:Boolean = false
    open var Data: T?

    init {
        Data = null
    }

    constructor()

    protected constructor(parcel: Parcel){
        val type = parcel.readString()
        when(type){
            SubVolleyResponse.TypeObj.LIST->{
                val tmpList: List<Any> = ArrayList()
                parcel.readList(tmpList, this.javaClass.classLoader)
                Data = tmpList as T
            }
            SubVolleyResponse.TypeObj.MAP->{
                val tmpMap: Map<Any, Any> = HashMap()
                parcel.readMap(tmpMap, this.javaClass.classLoader)
                Data = tmpMap as T
            }
            SubVolleyResponse.TypeObj.SERIALIZABLE-> Data = parcel.readSerializable() as T
            SubVolleyResponse.TypeObj.PARCELABLE -> Data = parcel.readParcelable<Parcelable>(this.javaClass.classLoader) as T
            SubVolleyResponse.TypeObj.STRING -> Data = parcel.readString() as T
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (Data is List<*>) {
            parcel.writeString(SubVolleyResponse.TypeObj.LIST)
            parcel.writeList(Data as List<*>)
        } else if (Data is Map<*, *>) {
            parcel.writeString(SubVolleyResponse.TypeObj.MAP)
            parcel.writeMap(Data as Map<*, *>)
        } else if (Data is Serializable) {
            parcel.writeString(SubVolleyResponse.TypeObj.SERIALIZABLE)
            parcel.writeSerializable(Data as Serializable)
        } else if (Data is Parcelable) {
            parcel.writeString(SubVolleyResponse.TypeObj.PARCELABLE)
            parcel.writeParcelable(Data as Parcelable, flags)
        } else {
            parcel.writeString(SubVolleyResponse.TypeObj.STRING)
            parcel.writeString(Data.toString())
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    private companion object CREATOR : Parcelable.Creator<CandyResponse<Any>> {
        override fun createFromParcel(parcel: Parcel): CandyResponse<Any> {
            return CandyResponse(parcel)
        }

        override fun newArray(size: Int): Array<CandyResponse<Any>?> {
            return arrayOfNulls(size)
        }
    }
}