package com.cfwin.cfwinblockchain.beans.response.candy

import android.os.Parcel
import android.os.Parcelable
import com.cfwin.cfwinblockchain.beans.CandyItem
import com.cfwin.cfwinblockchain.beans.response.CandyResponse
import com.cfwin.cfwinblockchain.beans.response.PageBean

class CandyList : CandyResponse<CandyList.DataBean> {
    var Request:String

    init {
        Request = ""
    }

    constructor():super()

    protected constructor(parcel: Parcel):super(parcel){
        Request = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(Request)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CandyList> {
        override fun createFromParcel(parcel: Parcel): CandyList {
            return CandyList(parcel)
        }

        override fun newArray(size: Int): Array<CandyList?> {
            return arrayOfNulls(size)
        }
    }

    class DataBean : PageBean(){
        var Items:List<CandyItem> = ArrayList()
    }
}