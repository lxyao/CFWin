package com.cfwin.cfwinblockchain.beans.response

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

open class ScoreResponse<T> : Parcelable{

    val STATE_SUCCESS = "success"

    var msg:String = ""
    var result:String = ""
    var code: Int = 0
    open var data: T?

    init {
        data = null
    }

    constructor()

    protected constructor(parcel: Parcel){
        val type = parcel.readString()
        when(type){
            SubVolleyResponse.TypeObj.LIST->{
                val tmpList: List<Any> = ArrayList()
                parcel.readList(tmpList, this.javaClass.classLoader)
                data = tmpList as T
            }
            SubVolleyResponse.TypeObj.MAP->{
                val tmpMap: Map<Any, Any> = HashMap()
                parcel.readMap(tmpMap, this.javaClass.classLoader)
                data = tmpMap as T
            }
            SubVolleyResponse.TypeObj.SERIALIZABLE-> data = parcel.readSerializable() as T
            SubVolleyResponse.TypeObj.PARCELABLE -> data = parcel.readParcelable<Parcelable>(this.javaClass.classLoader) as T
            SubVolleyResponse.TypeObj.STRING -> data = parcel.readString() as T
        }
        msg = parcel.readString()
        result = parcel.readString()
        code = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (data is List<*>) {
            parcel.writeString(SubVolleyResponse.TypeObj.LIST)
            parcel.writeList(data as List<*>)
        } else if (data is Map<*, *>) {
            parcel.writeString(SubVolleyResponse.TypeObj.MAP)
            parcel.writeMap(data as Map<*, *>)
        } else if (data is Serializable) {
            parcel.writeString(SubVolleyResponse.TypeObj.SERIALIZABLE)
            parcel.writeSerializable(data as Serializable)
        } else if (data is Parcelable) {
            parcel.writeString(SubVolleyResponse.TypeObj.PARCELABLE)
            parcel.writeParcelable(data as Parcelable, flags)
        } else {
            parcel.writeString(SubVolleyResponse.TypeObj.STRING)
            parcel.writeString(data.toString())
        }
        parcel.writeString(msg)
        parcel.writeString(result)
        parcel.writeInt(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    private companion object CREATOR : Parcelable.Creator<ScoreResponse<Any>> {
        override fun createFromParcel(parcel: Parcel): ScoreResponse<Any> {
            return ScoreResponse(parcel)
        }

        override fun newArray(size: Int): Array<ScoreResponse<Any>?> {
            return arrayOfNulls(size)
        }
    }
}