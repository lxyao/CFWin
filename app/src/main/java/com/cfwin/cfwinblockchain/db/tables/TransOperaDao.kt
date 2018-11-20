package com.cfwin.cfwinblockchain.db.tables

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.cfwin.base.db.AbsTableOpera
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.beans.AccountUseItem

/**
 * 交易表数据操作对象
 */
class TransOperaDao constructor(db: SQLiteDatabase) : AbsTableOpera(db) {

    init {
        mTableName = "transInfo"
    }

    fun insertData(bean: AccountUseItem){
        val cv = ContentValues()
        cv.put("tx_id", bean.txId)
        cv.put("fromAccount", bean.fromAccount)
        cv.put("toAccount", bean.toAccount)
        cv.put("integral", bean.num)
        cv.put("transTime", bean.time)
        cv.put("transState", bean.state)
        val index = insert(null, cv, SQLiteDatabase.CONFLICT_ABORT)
        LogUtil.e(TAG, "交易数据插入 index = $index")
    }

    fun isData(txId: String): Boolean{
        var isExists = false
        val result = query(arrayOf("tx_id"), "tx_id = ?", arrayOf(txId))
        if(result != null){
            isExists = !result.isEmpty()
        }
        return isExists
    }

    fun queryData(page: Int = 1, address: String): MutableList<AccountUseItem>{
        val data = ArrayList<AccountUseItem>()
        val c = query(null, "fromAccount = ? or toAccount = ? limit ${(page - 1) * 20},20", arrayOf(address, address), null, null, null)
        if(c != null){
            while (c.moveToNext()){
                val bean = AccountUseItem(time = c.getString(c.getColumnIndex("transTime")),
                        num = c.getString(c.getColumnIndex("integral")),
                        state = c.getInt(c.getColumnIndex("transState")),
                        fromAccount = c.getString(c.getColumnIndex("fromAccount")),
                        toAccount = c.getString(c.getColumnIndex("toAccount")))
                data.add(bean)
            }
        }
        return data
    }

}