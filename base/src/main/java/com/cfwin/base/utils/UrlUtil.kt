package com.cfwin.base.utils

import android.net.Uri

/**
 * url地址判断工具
 */
object UrlUtil {

    fun isUrl(str: String): Boolean{
        if(str.startsWith("http://") || str.startsWith("https://"))return true
        return false
    }

    fun get3W(str: String): String{
        val uri = Uri.parse(str)
        val port = if(uri.port < 0)"" else ":${uri.port}"
        return "${uri.scheme}://"+uri.host+port
    }
}