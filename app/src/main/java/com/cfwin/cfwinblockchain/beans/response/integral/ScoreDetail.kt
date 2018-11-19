package com.cfwin.cfwinblockchain.beans.response.integral

data class ScoreDetail(val blockNumber: Int, val tx_info: EnvelopesBean)

data class EnvelopesBean(val transactionEnvelopeInfo: EnvelopeInfo, val timestamp: String, val isValid: Boolean)

data class EnvelopeInfo(val transactionActionInfoArray: MutableList<ArgArray>)

data class ArgArray(val argArray: MutableList<String>)