package com.cfwin.cfwinblockchain.utils

import com.cfwin.base.utils.encoded.EcKeyUtils
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH

object UrlSignUtil {

    @JvmStatic
    fun signLogin(pwd: String?, dir: String, address: String, urlAddress: String): String{
        val prikeystr = EcKeyUtils.getPrivateKey(pwd,  dir,  "$address$KEY_END_WITH")
        return EcKeyUtils.signReturnHexString(prikeystr, urlAddress.toByteArray())
    }
}