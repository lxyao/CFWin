package com.cfwin.cfwinblockchain.db.tables

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.cfwin.base.db.AbsTableOpera
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.beans.LoginLogItem
import com.cfwin.cfwinblockchain.beans.response.log.LogList

/**
 * 登录日志表操作对象
 */
class LogOperaDao constructor(db: SQLiteDatabase) : AbsTableOpera(db){

    init {
        mTableName = "login_log"
    }

    fun insertLog(bean: LoginLogItem){
        val values = ContentValues()
        values.put("loginUrl", bean.loginUrl)
        values.put("loginAccount", bean.loginAccount)
        values.put("loginTime", bean.time)
        values.put("sign", bean.sign)
        val index = insert(null, values, SQLiteDatabase.CONFLICT_ABORT)
        LogUtil.e(TAG, "插入成功 id=$index")
    }

    fun queryData(address: String, page: Int = 1, pageSize: Int= 20): MutableList<LoginLogItem>{
        val data = ArrayList<LoginLogItem>()
        val c = query(null, "loginAccount = ? limit ${((page - 1) * pageSize)},$pageSize", arrayOf(address), null, null, null)
        c?.let {
            while (c.moveToNext()){
                data.add(getLogItem(c))
            }
            it.close()
        }
        return data
    }

    fun queryData(logList: LogList): MutableList<LoginLogItem>{
        val data = ArrayList<LoginLogItem>()
        val c = query(null, "loginAccount = ? and loginUrl = ? and sign = ?", arrayOf(logList.address, logList.requestedAddress, logList.sign), null, null, null)
        c?.let {
            while (c.moveToNext()){
                data.add(getLogItem(c))
            }
            it.close()
        }
        return data
    }

    private fun getLogItem(c: Cursor):LoginLogItem{
        return LoginLogItem(loginUrl = c.getString(c.getColumnIndex("loginUrl")),
                loginAccount = c.getString(c.getColumnIndex("loginAccount")),
                time = c.getString(c.getColumnIndex("loginTime")),
                sign = c.getString(c.getColumnIndex("sign")))
    }

}