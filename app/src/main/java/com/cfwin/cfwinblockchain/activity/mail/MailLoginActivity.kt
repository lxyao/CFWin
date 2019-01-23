package com.cfwin.cfwinblockchain.activity.mail

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.base.utils.PatternUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity

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
        //进行登陆
        login()
    }

    private fun login(){
        startActivity(Intent(this, MsgListActivity::class.java))
        finish()
    }
}