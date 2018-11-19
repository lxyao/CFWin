package com.cfwin.cfwinblockchain.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.cfwin.base.db.AbsDBManager
import com.cfwin.cfwinblockchain.Constant

/**
 * 本地数据库管理对象
 */
class LocalDBManager constructor(context: Context) : AbsDBManager(context, Constant.DB_NAME, Constant.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val userSql = "create table if not exists user(_id INTEGER PRIMARY KEY AUTOINCREMENT,userName varchar(20),accountName varchar(20),address text not null,type int, integral varchar(20), serial int)"
        db?.execSQL(userSql)
        val logSql = "create table if not exists login_log(_id INTEGER PRIMARY KEY AUTOINCREMENT, loginUrl text, loginAccount nvarchar(100), loginTime nvarchar(35), sign text)"
        db?.execSQL(logSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion < 2){
            val userSql = "alter table user add column integral varchar(20)"
            db?.execSQL(userSql)
        }
        if(oldVersion < 3){
            val userSql = "alter table login_log add column sign varchar(20)"
            db?.execSQL(userSql)
        }
        if(oldVersion < 4){
            val userSql = "alter table user add column serial int"
            db?.execSQL(userSql)
        }

    }

}