package com.cfwin.cfwinblockchain.beans

/**
 * 账户使用情况数据对象
 */
data class AccountUseItem(val title: String="", val time: String="", val num: String ="", val state: Int = 0, val txId:String = "", val fromAccount:String, val toAccount:String)