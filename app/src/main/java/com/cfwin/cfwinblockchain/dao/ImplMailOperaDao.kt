package com.cfwin.cfwinblockchain.dao

import android.content.SharedPreferences
import com.cfwin.base.dao.MailOperaDao
import com.cfwin.cfwinblockchain.Constant

class ImplMailOperaDao: MailOperaDao() {


    fun getSharedMailConfig(shared: SharedPreferences, type: Int): Array<String?>{
        val params = arrayOfNulls<String>(5)
        params[0] = shared.getString(Constant.MAIL_CONFIG.SERVICE_ADDRESS+type, "")
        params[1] = shared.getString(Constant.MAIL_CONFIG.USER_CODE+type, "")
        params[2] = shared.getString(Constant.MAIL_CONFIG.USER_PWD+type, "")
        params[3] = shared.getString(Constant.MAIL_CONFIG.SECRET_METHOD+type, ":")
        params[4] = shared.getString(Constant.MAIL_CONFIG.SERVICE_PORT+type, "")
        return params
    }
}