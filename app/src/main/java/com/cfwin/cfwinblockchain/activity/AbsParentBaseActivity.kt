package com.cfwin.cfwinblockchain.activity

import android.content.Context
import com.cfwin.base.activity.BaseActivity
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.beans.UserBean
import com.google.gson.Gson

/**
 * 统一处理客户端必要参数
 */
abstract class AbsParentBaseActivity : BaseActivity() {

    /**
     * 获取服务器地址
     */
    fun getServer(index: Int): String{
        val serverApi = getSharedPreferences(Constant.configFileName, Context.MODE_PRIVATE)
                .getStringSet(Constant.SERVICE_API, LinkedHashSet())
        for (str in serverApi.toTypedArray()){
            val tmp = str.split(",")
            if(index == tmp[0].toInt()){
                return tmp[1]
            }
        }
        return ""
    }

    open fun getUser(): UserBean {
        val info = mContext?.getSharedPreferences(Constant.configFileName, Context.MODE_PRIVATE)
                ?.getString(Constant.CURRENT_ACCOUNT, "{}")
        return Gson().fromJson(info, UserBean::class.java)
    }
}