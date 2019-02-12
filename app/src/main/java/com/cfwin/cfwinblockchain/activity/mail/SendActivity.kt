package com.cfwin.cfwinblockchain.activity.mail

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.*
import butterknife.BindView
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.BuildConfig
import com.cfwin.cfwinblockchain.MyApplication
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.contacts.ContactsActivity
import com.cfwin.cfwinblockchain.beans.mail.ContactBean
import java.security.Security
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * 邮件发送界面
 */
class SendActivity: SubBaseActivity() {

    /**发送者头像*/
    @BindView(R.id.sendIcon)lateinit var sendIcon: ImageView
    /**发送者昵称*/
    @BindView(R.id.nickName)lateinit var nickName: TextView
    /**发送者邮箱*/
    @BindView(R.id.mailAddress)lateinit var mailAddress: TextView
    /**收件人邮箱*/
    @BindView(R.id.inUser)lateinit var inUser: EditText
    /**邮箱主题*/
    @BindView(R.id.subject)lateinit var subject: EditText
    /**邮箱内容*/
    @BindView(R.id.content)lateinit var content: EditText
    /**发送方式*/
    @BindView(R.id.sendMethod)lateinit var sendMethod: RadioGroup

    private var secretType = R.id.cf
    private lateinit var ts: Transport
    private lateinit var message: MimeMessage
    private var host = "smtp.sina.com"
    private var loginUser = "liulzx234567890@sina.com"
    private var loginPwd = "test"
    private lateinit var params: Array<String>

    override fun getLayoutId(): Int {
        return R.layout.activity_send_mail
    }

    override fun initView() {
        topMenu.text = "发送"
        sendMethod.setOnCheckedChangeListener{group, checkedId -> onChecked(group, checkedId) }
    }

    override fun initData() {
        val protocol = MyApplication.store!!.urlName.protocol
        params = intent.getStringArrayExtra("params")
        loginUser = params[1]
        loginPwd = params[2]
        host = if(protocol == "pop3" || protocol == "imap"){
            params[5]
        }else{
            params[0]
        }
        nickName.text = "用户"
        mailAddress.text = loginUser
        initSendInfo()
    }

    private fun onChecked(group: RadioGroup, checkedId: Int){
        secretType = checkedId
        if(checkedId == R.id.pgp){
            showToast(getString(R.string.pgp_pubkey_hint))
            group.findViewById<RadioButton>(checkedId).isChecked = false
            secretType = -1
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.contacts->{
                startActivityForResult(Intent(mContext!!, ContactsActivity::class.java)
                        .putExtra("isSelected", true),
                        201)
            }
            R.id.toolbar_menu->{
                //发送
                checkSendMsg()
            }
            R.id.iv_back-> onBackPressed()
            else-> super.onClick(v)
        }
    }

    override fun onBackPressed() {
        ts.close()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 201){
                val user = data?.getParcelableExtra<ContactBean>("user")
                inUser.setText(user!!.mail)
            }
        }
    }

    private fun checkSendMsg(){
        val inUser = inUser.text.toString().trim()
        val subject = subject.text.toString().trim()
        val content = content.text.toString().trim()
        if(TextUtils.isEmpty(inUser)){
            showToast(getString(R.string.inUser_hint))
            return
        }
        if(TextUtils.isEmpty(subject)){
            showToast(getString(R.string.subject_hint))
            return
        }
        if(TextUtils.isEmpty(content)){
            showToast(getString(R.string.send_content).replace("...", ""))
            return
        }
        //指明邮件的发件人
        message.setFrom(InternetAddress(loginUser))
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, InternetAddress(inUser))
        //邮件的标题
        message.subject = "test1 $subject"
        object: Thread(){
            override fun run() {
                super.run()
                sendMsg(content)
            }
        }.start()
    }

    private fun sendMsg(content: String){
        var changeContent = ""
        when(secretType){
            R.id.none->{
                //内容原样输出传递
                changeContent = content
            }
            R.id.cf->{
                //内容 使用cf加密
                changeContent = " cf $content cf"
            }
            R.id.pgp->{
                //内容 pgp加密
                changeContent = " pgp $content pgp"
            }
            else-> {
                runOnUiThread {
                    showToast(getString(R.string.send_method_hint))
                }
                return
            }
        }
        try {
            ts.connect(host, loginUser, loginPwd)
            //邮件的文本内容
            message.setText("This is a test mail. $changeContent")
            ts.sendMessage(message, message.allRecipients)
            LogUtil.e(TAG!!, "发送成功 $changeContent")
            finish()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun initSendInfo(){
        Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
        val prop = Properties()
        prop.setProperty("mail.host", host)
        prop.setProperty("mail.transport.protocol", "smtp")
        prop.setProperty("mail.smtp.auth", "true")
//        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        val session = Session.getInstance(prop)
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.debug = BuildConfig.DEBUG
        //2、通过session得到transport对象
        ts = session.transport
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        //4、创建邮件
        message = MimeMessage(session)
        //5、发送邮件
    }
}