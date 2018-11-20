package com.cfwin.cfwinblockchain.beans.response.log

data class LogData(val records: MutableList<LogList>)

data class LogList(val address: String, val loginUrl: String, val sign: String, val loginTime: String ="")