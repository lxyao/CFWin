package com.cfwin.cfwinblockchain.beans

import android.os.Parcel
import android.os.Parcelable

/**
 * 账户管理列表数据
 */
class AccountItem : Parcelable {
    var icon: String = ""
    var title: String = ""
    var subTitle: String = ""
    var score: String =""
    var state: Boolean = false

    protected constructor(parcel: Parcel){
        icon = parcel.readString()
        title = parcel.readString()
        subTitle = parcel.readString()
        score = parcel.readString()
        state = parcel.readByte() != 0.toByte()
    }

    constructor(): super()
    constructor(icon: String = "",
                title: String="",
                subTitle: String="",
                score: String="",
                state: Boolean=false):super(){
        this.icon = icon
        this.title = title
        this.subTitle = subTitle
        this.score = score
        this.state = state
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(score)
        parcel.writeByte(if (state) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccountItem> {
        override fun createFromParcel(parcel: Parcel): AccountItem {
            return AccountItem(parcel)
        }

        override fun newArray(size: Int): Array<AccountItem?> {
            return arrayOfNulls(size)
        }
    }

}