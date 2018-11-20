package com.cfwin.cfwinblockchain.beans

import android.os.Parcel
import android.os.Parcelable

/**
 * 账户使用情况数据对象
 */
data class AccountUseItem(val title: String="",
                          val time: String="",
                          val num: String ="",
                          val state: Int = 0,
                          val txId:String = "",
                          val fromAccount:String,
                          val toAccount:String): Parcelable{
    protected constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(time)
        parcel.writeString(num)
        parcel.writeInt(state)
        parcel.writeString(txId)
        parcel.writeString(fromAccount)
        parcel.writeString(toAccount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccountUseItem> {
        override fun createFromParcel(parcel: Parcel): AccountUseItem {
            return AccountUseItem(parcel)
        }

        override fun newArray(size: Int): Array<AccountUseItem?> {
            return arrayOfNulls(size)
        }
    }

}