package com.cfwin.cfwinblockchain.beans

import android.os.Parcel
import android.os.Parcelable

data class UserBean(var _id:Int = -1,
                    var userName:String,
                    var accountName:String,
                    var address:String,
                    var integral:String = "0",
                    var type:Int = 0) : Parcelable{

    protected constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_id)
        parcel.writeString(userName)
        parcel.writeString(accountName)
        parcel.writeString(address)
        parcel.writeString(integral)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserBean> {
        override fun createFromParcel(parcel: Parcel): UserBean {
            return UserBean(parcel)
        }

        override fun newArray(size: Int): Array<UserBean?> {
            return arrayOfNulls(size)
        }
    }

}