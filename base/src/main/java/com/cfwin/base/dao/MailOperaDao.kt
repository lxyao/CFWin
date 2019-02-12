package com.cfwin.base.dao

import android.text.TextUtils
import com.cfwin.base.BuildConfig
import java.security.Security
import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

/**
 * 邮箱操作数据处理<br/>
 * 收信使用：POP3 IMAP 协议<br/>
 * 发信使用：SMTP协议<br/>
 * 关于邮箱加密：
 * POP3 非加密：110 加密：995
 * IMAP：非加密：143 加密：993
 * SMTP：非加密：25 加密：ssl/tls： starttls：587
 */
open class MailOperaDao {

    private val sslFactory = "javax.net.ssl.SSLSocketFactory"
    private lateinit var props: Properties
    private var authenticator: Authenticator? = null

    private fun initSecret(){
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
        props = System.getProperties()
    }

    private fun initAuth(name: String, pwd: String): Authenticator{
        return object: Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(name, pwd)
            }
        }
    }

    fun exchangeLogin(params: Array<String?>): Session {
        initSecret()
//        with(props){
//            val port = if(TextUtils.isEmpty(params[4])) 140 else 993
//            put("mail.store.protocol","imap")
//            put("mail.imap.port", port)
//            put("mail.imap.host", params[0])
//            if(params[4] != "无"){
//                // 设置了ssltls需要下面的设置
//                setProperty("mail.imap.socketFactory.class", sslFactory)
//                setProperty("mail.imap.socketFactory.port", "$port")
//                setProperty("mail.imap.port", "$port")
//                put("mail.smtp.auth", "true")
//                setProperty("mail.imap.auth.login.disable", "true")
//                setProperty("mail.smtp.socketFactory.fallback", "false")
//            }
//        }

//        "STARTTLS"->{
//            put("mail.smtp.host", "smtp.partner.outlook.cn")
//            put("mail.smtp.starttls.enable", "true")
//            put("mail.smtp.auth", "true")
//            put("mail.smtp.port", "587")
//        }
        return getMailSession(props)
    }

    fun popLogin(params: Array<String?>): Session {
        initSecret()
        with(props){
            val secret = parseSecret(params[3]!!)
            val port = if(TextUtils.isEmpty(secret) || "无" == secret){
                110
            } else {
                try {
                    params[4]!!.toInt()
                }catch (e: NumberFormatException){
                    e.printStackTrace()
                    995
                }
            }
            put("mail.store.protocol","pop3")
            put("mail.pop3.host", params[0])
            put("mail.pop3.port", port)
            when(secret){
                in arrayOf("SSL/TLS", "STARTTLS")->{
                    authenticator = initAuth(params[1]!!, params[2]!!)
                    put("mail.pop3.socketFactory.class", sslFactory)
                    put("mail.pop3.auth.login.disable", "true")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.socketFactory.fallback", "false")
                }
                else->{
                    remove("mail.pop3.socketFactory.class")
                }
            }
        }
        return getMailSession(props)
    }

    fun imapLogin(params: Array<String?>): Session {
        initSecret()
        with(props){
            val secret = parseSecret(params[3]!!)
            val port = if(TextUtils.isEmpty(secret)){
                140
            }else {
                try{
                    params[4]!!.toInt()
                }catch (e: NumberFormatException){
                    e.printStackTrace()
                    993
                }
            }
            put("mail.store.protocol","imap")
            put("mail.imap.port", port)
            put("mail.imap.host", params[0])
            if(secret != "无"){
                // 设置了ssltls需要下面的设置
                authenticator = initAuth(params[1]!!, params[2]!!)
                put("mail.imap.socketFactory.class", sslFactory)
                put("mail.smtp.auth", "true")
                put("mail.imap.auth.login.disable", "true")
                put("mail.smtp.socketFactory.fallback", "false")
            }else{
                remove("mail.imap.socketFactory.class")
            }
        }
        return getMailSession(props)
    }

    private fun getMailSession(props: Properties): Session{
        val session = Session.getInstance(props, if(authenticator == null)null else authenticator)
        session.debug = BuildConfig.DEBUG
        return session
    }

    private fun parseSecret(param: String)= try{
        param.split(":")[0]
    }catch (e: IndexOutOfBoundsException){
        e.printStackTrace()
        ""
    }
}