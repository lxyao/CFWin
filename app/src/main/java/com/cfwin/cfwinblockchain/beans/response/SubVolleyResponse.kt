package com.cfwin.cfwinblockchain.beans.response

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

open class SubVolleyResponse<T> : VolleyResponse {

    open var data: T?

    init {
        data = null
    }

    constructor()

    protected constructor(parcel: Parcel): super(parcel){
        val type = parcel.readString()
        when(type){
            TypeObj.LIST->{
                val tmpList: List<Any> = ArrayList()
                parcel.readList(tmpList, this.javaClass.classLoader)
                data = tmpList as T
            }
            TypeObj.MAP->{
                val tmpMap: Map<Any, Any> = HashMap()
                parcel.readMap(tmpMap, this.javaClass.classLoader)
                data = tmpMap as T
            }
            TypeObj.SERIALIZABLE-> data = parcel.readSerializable() as T
            TypeObj.PARCELABLE -> data = parcel.readParcelable<Parcelable>(this.javaClass.classLoader) as T
            TypeObj.STRING -> data = parcel.readString() as T
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        if (data is List<*>) {
            parcel.writeString(TypeObj.LIST)
            parcel.writeList(data as List<*>)
        } else if (data is Map<*, *>) {
            parcel.writeString(TypeObj.MAP)
            parcel.writeMap(data as Map<*, *>)
        } else if (data is Serializable) {
            parcel.writeString(TypeObj.SERIALIZABLE)
            parcel.writeSerializable(data as Serializable)
        } else if (data is Parcelable) {
            parcel.writeString(TypeObj.PARCELABLE)
            parcel.writeParcelable(data as Parcelable, flags)
        } else {
            parcel.writeString(TypeObj.STRING)
            parcel.writeString(data.toString())
        }
    }

    private companion object CREATOR : Parcelable.Creator<SubVolleyResponse<Any>> {
        override fun createFromParcel(parcel: Parcel): SubVolleyResponse<Any> {
            return SubVolleyResponse(parcel)
        }

        override fun newArray(size: Int): Array<SubVolleyResponse<Any>?> {
            return arrayOfNulls(size)
        }
    }



    object TypeObj{
        const val LIST = "List"
        const val MAP = "Map"
        const val SERIALIZABLE = "Serializable"
        const val PARCELABLE = "Parcelable"
        const val STRING = "String"
    }
}