package com.cfwin.cfwinblockchain.db.tables

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.cfwin.base.db.AbsTableOpera
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 用户表操作对象
 */
class UserOperaDao constructor(db: SQLiteDatabase) : AbsTableOpera(db) {

    init {
        mTableName = "user"
    }

    fun insertUser(user: UserBean){
        val values = ContentValues()
        values.put("userName", user.userName)
        values.put("accountName", user.accountName)
        values.put("address", user.address)
        values.put("integral", user.integral)
        values.put("type", user.type)
        val id = insert(null, values, SQLiteDatabase.CONFLICT_ABORT)
        LogUtil.e(TAG, "插入成功 id=$id")
    }

    fun updateIntegral(num: String, serial: Int, address: String){
        val values = ContentValues()
        values.put("integral", num)
        values.put("serial", serial)
        val index = update(values, "address = '$address'", null)
        LogUtil.e(TAG, "修改积分 index = $index")
//        val result= query(arrayOf("integral"), "address = '$address'", null)
//        if(result != null && result.isNotEmpty()){
//            val tmp = if(TextUtils.isEmpty(result[0][0]) || result[0][0] == "0.0")"0" else result[0][0]
//        }
    }

    fun queryUser(accountName: String= ""): MutableList<UserBean>{
        val data = ArrayList<UserBean>()
        var where = ""
        if(!TextUtils.isEmpty(accountName))where = "accountName = $accountName"
        val c = query(null, where, null, null, null, "type asc")
        c?.let {
            while (c.moveToNext()){
                val bean = UserBean(_id = c.getInt(c.getColumnIndex("_id")),
                        userName = c.getString(c.getColumnIndex("userName")),
                        accountName = c.getString(c.getColumnIndex("accountName")),
                        address = c.getString(c.getColumnIndex("address")),
                        integral = c.getString(c.getColumnIndex("integral")),
                        type = c.getInt(c.getColumnIndex("type")),
                        serial = c.getInt(c.getColumnIndex("serial")))
                data.add(bean)
            }
            it.close()
        }
        return data
    }
}