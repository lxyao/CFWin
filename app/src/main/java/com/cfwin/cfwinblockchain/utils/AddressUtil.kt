package com.cfwin.cfwinblockchain.utils

import com.cfwin.base.utils.encoded.EcKeyUtils
import com.cfwin.cfwinblockchain.activity.user.EC_DIR
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH
import com.cfwin.cfwinblockchain.activity.user.WALLET_DIR
import com.cfwin.cfwinblockchain.activity.user.WORD_END_WITH

class AddressUtil constructor(private val filesDir: String) {

    var address: Array<String?> = arrayOfNulls(2)

    /**
     * 生成助记词
     */
    fun createWord(): String = EcKeyUtils.createMnemonic()

    fun genIdentify(mnemonicStr: String, pwd: String): String?{
        val params = createPub(mnemonicStr)
        // 保存私钥 address.prik
        if (!EcKeyUtils.savePrivateKey(params[1], null,  "${this.filesDir}$EC_DIR", "${params[2]}$KEY_END_WITH")) {
            return null
        }
        // 存储助记词   address.mcw
        if (!EcKeyUtils.saveMnemonic(pwd, params[0], "${this.filesDir}$EC_DIR", "${params[2]}$WORD_END_WITH")) {
            return null
        }
        address[0] = params[2]
        return params[0]
    }

    fun genWallet(mnemonicStr: String, pwd: String): String?{
        val params = createPub(mnemonicStr)
        // 保存私钥 address.prik
        if (!EcKeyUtils.savePrivateKey(params[1], pwd, "$filesDir$WALLET_DIR",  "${params[2]}$KEY_END_WITH")) {
            return null
        }
        // 存储助记词   address.mcw
        if (!EcKeyUtils.saveMnemonic(pwd, params[0], "$filesDir$WALLET_DIR", "${params[2]}$WORD_END_WITH")) {
            return null
        }
        address[1] = params[2]
        return params[0]
    }

    private fun createPub(mnemonicStr: String): Array<String>{
        // 生成私钥
        val privateKeyStr = EcKeyUtils.mnemonic2PrivateKey(mnemonicStr)
        // 加密私钥
        // 生成地址
        val address = EcKeyUtils.privateKey2Address(privateKeyStr)
        return arrayOf(mnemonicStr, privateKeyStr, address)
    }
}