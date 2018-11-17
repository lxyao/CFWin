package com.cfwin.cfwinblockchain.beans

/**
 * 糖果列表数据
 */
data class LoginLogItem(var loginAccount:String, var loginUrl: String ="", var time: String ="", var sign: String="", var state: Boolean=false) : Any()