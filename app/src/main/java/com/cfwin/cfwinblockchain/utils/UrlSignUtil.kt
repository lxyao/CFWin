package com.cfwin.cfwinblockchain.utils

import com.cfwin.base.utils.encoded.EcKeyUtils
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH

object UrlSignUtil {

    @JvmStatic
    fun signLogin(pwd: String?, dir: String, address: String, urlAddress: String): String{
        val prikeystr = EcKeyUtils.getPrivateKey(pwd,  dir,  "$address$KEY_END_WITH")
        return EcKeyUtils.signReturnHexString(prikeystr, urlAddress.toByteArray())
    }


    fun signTrans(pwd: String?, dir: String, fileName: String, data: ByteArray): String{
        val prikeystr = EcKeyUtils.getPrivateKey(pwd, dir, fileName)
        val signContent = EcKeyUtils.signReturnBase64(prikeystr, data)
        return replacePlusAndSlash(signContent)
    }

    fun signLog(pwd: String?, dir: String, fileName: String, data: ByteArray): String{
        val prikeystr = EcKeyUtils.getPrivateKey(pwd, dir, fileName)
        val signContent = EcKeyUtils.signReturnBase64Url(prikeystr, data)
        return replacePlusAndSlash(signContent)
    }

    /**
     * 将字符串中的+、/替换成-、_
     * @param str
     * @return
     */
    fun replacePlusAndSlash(str: String): String {
        var str = str
        str = str.replace('+', '-')
        str = str.replace('/', '_')
        return str
    }
}