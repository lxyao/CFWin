package com.cfwin.cfwinblockchain

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cfwin.base.App

lateinit var queue: RequestQueue

class MyApplication : App() {

    override fun onCreate() {
        super.onCreate()
        queue = Volley.newRequestQueue(this)
    }

    fun Tmp.getVolleyQueue(): RequestQueue{
        return queue
    }
}