package com.cfwin.base.utils

/**
 * url地址判断工具
 */
object UrlUtil {

    fun isUrl(str: String): Boolean{
        if(str.startsWith("http://") || str.startsWith("https://"))return true
        return false
    }
}