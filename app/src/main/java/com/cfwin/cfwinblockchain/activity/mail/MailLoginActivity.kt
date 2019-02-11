package com.cfwin.cfwinblockchain.activity.mail

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.base.utils.PatternUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.sun.mail.imap.IMAPFolder
import java.security.Security
import javax.mail.*

/**
 * 邮箱登陆界面
 */
class MailLoginActivity: SubBaseActivity() {

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
        object: Thread(){
            override fun run() {
                //进行登陆
                login()
            }
        }.start()
    }

    private fun login(){
        startActivity(Intent(this, MsgListActivity::class.java))
//        finish()
        val host = "pop3.baisonmail.com"//"partner.outlook.cn"
        val port = 995//993
        val username = "dingyao.zhang@baisonmail.com"//"leiyu@cfwin.com"
        val password = "baison@631"//""password"
        val sslFactory = "javax.net.ssl.SSLSocketFactory"
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
        val props = System.getProperties()
//        props["mail.imap.port"] = "993"
        props["mail.store.protocol"] = "pop3"//"imap"
        props["mail.imap.host"] = host
//        // 如果设置了ssltls需要下面的设置
//        props.setProperty("mail.imap.socketFactory.class", sslFactory)
        props.setProperty("mail.pop3.socketFactory.class", sslFactory)
        props.setProperty("mail.smtp.socketFactory.fallback", "false")
//        props.setProperty("mail.imap.socketFactory.port","993")
//        props.setProperty("mail.imap.port", "993")
        props["mail.smtp.auth"] = "true"
//        props.setProperty("mail.imap.auth.login.disable", "true")
        props.setProperty("mail.pop3.auth.login.disable", "true")
        val session = Session.getDefaultInstance(props, object: Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
        session.debug = true
        var folder: IMAPFolder? = null
        var store: Store? = null
        try {
            // 使用imap会话机制，连接服务器
//            store = session.getStore("imap") as IMAPStore?
            store = session.getStore("pop3")
            store?.let {
                it.connect(host, port, username,password)
                //收件箱
                folder = it.getFolder("Sent Messages") as IMAPFolder?
                val defaultFolder: Folder = store.defaultFolder
                val allFolder = defaultFolder.list()
                for (i in allFolder.indices) {
                    val str = allFolder[i].fullName
                    System.out.println("这个是服务器中的文件夹=$str")
                }
                // 收件箱
                val folderInbox: IMAPFolder = store.getFolder("INBOX") as IMAPFolder
                folderInbox.open(Folder.READ_WRITE)
                // 使用只读方式打开收件箱
                //folder.open(Folder.READ_WRITE)
                val size = folderInbox.messageCount
                System.out.println("这里是打印的条数==$size")
                val mess=folderInbox.messages
                //  Message message = folder.getMessage(size)
                for (i in mess.indices) {
                    val from = mess[i].from[0].toString()
                    val subject = mess[i].subject
                    val date = mess[i].sentDate
                    System.out.println("From: $from")
                    System.out.println("Subject: $subject")
                    System.out.println("Date: $date")
                }
                folder?.close(false)
                store.close()
            }
        }catch (e: MessagingException) {
            e.printStackTrace()
            runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
        }catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread { findViewById<Button>(R.id.login).isEnabled = true }
        }
    }
}