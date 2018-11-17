@file:JvmMultifileClass
package com.cfwin.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.cfwin.base.utils.CrashHandler

/**
 * 应用程序上下文
 */
lateinit var mAppContext: Context

/**
 * 应用程序上下文
 * 加入分包方式<br/>
 * @create at 2018-06-11 by Yao
 */
open class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        CrashHandler.getInstance().init(this)
        mAppContext = this
    }

    companion object Tmp{
        /**
         * 应用程序上下文,能全局调用
         */
        fun getAppContext(): Context {
            return mAppContext
        }
    }
}