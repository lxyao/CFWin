package com.cfwin.cfwinblockchain.beans.response.integral

data class ScoreList(val tx_id:String, val value:String)

data class DataBean(val data: MutableList<ScoreList>)