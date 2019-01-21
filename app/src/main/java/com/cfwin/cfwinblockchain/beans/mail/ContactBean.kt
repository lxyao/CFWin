package com.cfwin.cfwinblockchain.beans.mail

import android.os.Parcel
import android.os.Parcelable

/**
 * 联系人实体对象
 */
data class ContactBean(var id: Int = -1,
                       var nickName: String,
                       var mail: String,
                       var friendId: String = "",
                       var pgpKey: String= "",
                       var indexChar: String=""): Parcelable {

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nickName)
        parcel.writeString(mail)
        parcel.writeString(friendId)
        parcel.writeString(pgpKey)
        parcel.writeString(indexChar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactBean> {
        override fun createFromParcel(parcel: Parcel): ContactBean {
            return ContactBean(parcel)
        }

        override fun newArray(size: Int): Array<ContactBean?> {
            return arrayOfNulls(size)
        }
    }
}