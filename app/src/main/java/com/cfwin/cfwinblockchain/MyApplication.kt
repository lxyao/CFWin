package com.cfwin.cfwinblockchain

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cfwin.base.App
import com.cfwin.base.db.AbsDBManager
import com.cfwin.cfwinblockchain.db.LocalDBManager
import javax.mail.Store

lateinit var queue: RequestQueue


class MyApplication : App() {

    override fun onCreate() {
        super.onCreate()
        queue = Volley.newRequestQueue(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        store?.close()
    }

    override fun getDBManager(type: Int): AbsDBManager? {
        return LocalDBManager(this)
    }

    companion object {

        /**
         * 保存连接会话
         */
        var store: Store? = null

        fun getQueue()= queue

        /**
         * 获取数据库管理对象
         * @param type 类型
         */
        fun getDBManager(type: Int): AbsDBManager{
            return (getAppContext() as MyApplication).getDBManager(type)!!
        }

    }
}