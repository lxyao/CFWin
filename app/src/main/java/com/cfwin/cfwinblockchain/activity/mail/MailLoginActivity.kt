package com.cfwin.cfwinblockchain.activity.mail

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.base.utils.PatternUtil
import com.cfwin.cfwinblockchain.BuildConfig
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.mail.StoreBean
import com.cfwin.cfwinblockchain.dao.ImplMailOperaDao
import java.security.Security
import javax.mail.*

/**
 * 邮箱登陆界面
 */
class MailLoginActivity: SubBaseActivity() {

    private lateinit var shared: SharedPreferences;
    /**
     * 邮箱用户
     */
    @BindView(R.id.mail_code)internal lateinit var mailUser:EditText
    /**
     * 邮箱密码
     */
    @BindView(R.id.mail_pwd)internal lateinit var mailPwd:EditText

    override fun getLayoutId(): Int {
        return R.layout.activity_mail_login
    }

    override fun initView() {
        topTitle.text = getString(R.string.add_mail)
    }

    override fun initData() {
        shared = getSharedPreferences(Constant.configFileName, Activity.MODE_PRIVATE)
    }

    @OnClick(R.id.mail_settings)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login-> checkLogin(v)
            R.id.mail_settings-> startActivity(Intent(this, SettingsActivity::class.java)
                    .putExtra("mailUser", mailUser.text.toString()))
            else-> super.onClick(v)
        }
    }

    /**登陆判断*/
    private fun checkLogin(v: View?){
        val user = mailUser.text.toString().trim()
        if(TextUtils.isEmpty(user)){
            showToast(getString(R.string.please_input, " 邮箱账号"))
            return
        }
        if(!PatternUtil.isMail(user)){
            showToast(getString(R.string.please_input, " 正确的邮箱地址"))
            return
        }
        val pwd = mailPwd.text.toString().trim()
        if(TextUtils.isEmpty(pwd)){
            showToast(getString(R.string.please_input, " 邮箱密码"))
            return
        }
        v?.isEnabled = false
        Thread{
            useProtocol(user, pwd)
//            login()
        }.start()
    }

    private fun login(){
        val host = "partner.outlook.cn"
        val port = 995 //993
        val username = "leiyu@cfwin.com"
        val password = "password"
        val sslFactory = "javax.net.ssl.SSLSocketFactory"
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
        val props = System.getProperties()
        props["mail.store.protocol"] = "pop3"//"imap"
//        props["mail.imap.host"] = host
        props["mail.pop3.host"] = host
        // 如果设置了ssltls需要下面的设置
//        props["mail.imap.socketFactory.class"] = sslFactory
        props["mail.pop3.socketFactory.class"] = sslFactory
        props["mail.smtp.socketFactory.fallback"] = "false"
        props["mail.smtp.auth"] = "true"
//        props["mail.imap.auth.login.disable"] =  "true"
        props["mail.pop3.auth.login.disable"] =  "true"
        val session = Session.getInstance(props, object: Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
        session.debug = BuildConfig.DEBUG
        var store: Store? = null
        try {
            // 使用imap会话机制，连接服务器
            store = session.getStore("pop3")//imap
            store?.let {
                it.connect(host, port, username, password)
                //收件箱
                val defaultFolder: Folder = store.defaultFolder
                val allFolder = defaultFolder.list()
                for (i in allFolder.indices) {
                    val str = allFolder[i].fullName
                    System.out.println("这个是服务器中的文件夹=$str")
                }
                // 收件箱
                val folderInbox = store.getFolder("INBOX")
                folderInbox.open(Folder.READ_WRITE)
                // 使用只读方式打开收件箱
                val size = folderInbox.messageCount
                System.out.println("这里是打印的条数==$size")
                val mess=folderInbox.messages
                for (i in mess.indices) {
                    val from = mess[i].from[0].toString()
                    val subject = mess[i].subject
                    val date = mess[i].sentDate
                    System.out.println("From: $from")
                    System.out.println("Subject: $subject")
                    System.out.println("Date: $date")
                }
                folderInbox?.close(false)
                store.close()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
            runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
        } catch (e: Exception){
            e.printStackTrace()
            runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
        }
    }

    private fun useProtocol(user: String, pwd: String){
        val type = shared.getInt(Constant.configType, R.id.exchange)
        val mailDao = ImplMailOperaDao()
        var params = arrayOfNulls<String>(5)
        val session = when(type){
            R.id.imap->{
                //imap协议
                val shared = getSharedPreferences(Constant.imapConfig, Activity.MODE_PRIVATE)
                params = mailDao.getSharedMailConfig(shared, type = 0)
                params = params.plus(mailDao.getSharedMailConfig(shared, type = 1))
                mailDao.imapLogin(params)
            }
            R.id.pop3->{
                //pop3协议
                val shared = getSharedPreferences(Constant.popConfig, Activity.MODE_PRIVATE)
                params = mailDao.getSharedMailConfig(shared, type = 0)
                params = params.plus(mailDao.getSharedMailConfig(shared, type = 1))
                mailDao.popLogin(params)
            }
            R.id.exchange->{
                //exchange协议
                val shared = getSharedPreferences(Constant.exchangeConfig, Activity.MODE_PRIVATE)
                params = mailDao.getSharedMailConfig(shared, type = 0)
                mailDao.exchangeLogin(params)
            }
            else -> { return }
        }
        params[1] = user
        params[2] = pwd
        val store = session.getStore("pop3")
        store?.let {
            try{
                it.connect(user, pwd)
                startActivity(Intent(this, MsgListActivity::class.java)
                        .putExtra("store", StoreBean(params, it)))
                finish()
            } catch (e: MessagingException){
                e.printStackTrace()
                runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
            } catch (e: Exception){
                e.printStackTrace()
                runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
            }
        }
    }
}