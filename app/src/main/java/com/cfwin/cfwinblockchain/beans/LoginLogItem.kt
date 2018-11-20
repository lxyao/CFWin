package com.cfwin.cfwinblockchain.beans

import com.cfwin.cfwinblockchain.beans.response.log.LogList

/**
 * 糖果列表数据
 */
data class LoginLogItem(var loginAccount:String, var loginUrl: String ="", var time: String ="", var sign: String="", var state: Boolean=false) : Any(){

    fun equals(log: LogList): Boolean{
        if(log.address == loginAccount
                && log.loginUrl == loginUrl
                && log.sign == sign){
            return true
        }
        return false
    }
}