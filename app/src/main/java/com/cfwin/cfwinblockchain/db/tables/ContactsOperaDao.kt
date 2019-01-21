package com.cfwin.cfwinblockchain.db.tables

import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.cfwin.base.db.AbsTableOpera
import com.cfwin.cfwinblockchain.beans.mail.ContactBean

/**
 * 邮箱联系人数据操作实体
 */
class ContactsOperaDao(db: SQLiteDatabase): AbsTableOpera(db) {
    init {
        mTableName = "mailContacts"
    }

    /**
     * 添加联系人
     */
    fun addContact(bean: ContactBean){
        val id: String? = if(bean.id > 0) bean.id.toString() else null
        mSqliteDB.execSQL("insert or replace into $mTableName " +
                "values($id, '${bean.nickName}', '${bean.mail}', '${bean.friendId}', '${bean.pgpKey}')")
        //, '${bean.indexChar}'
    }

    /**
     * @param where 可以是联系人 昵称 邮箱
     */
    fun queryData(where: String): MutableList<ContactBean>{
        val sql = "select * from $mTableName "+(if(TextUtils.isEmpty(where))"" else "where nickName like '%$where%' or mail like '%$where%'")
        val cursor = mSqliteDB.rawQuery(sql, null)
        val data = mutableListOf<ContactBean>()
        cursor?.let {
            while (it.moveToNext()){
                val bean = ContactBean(id = it.getInt(it.getColumnIndex("id")),
                        nickName = it.getString(it.getColumnIndex("nickName")),
                        mail = it.getString(it.getColumnIndex("mail")),
                        friendId = it.getString(it.getColumnIndex("friendId")),
                        pgpKey = it.getString(it.getColumnIndex("pgpKey")))
                data.add(bean)
            }
            it.close()
        }
        return data
    }
}