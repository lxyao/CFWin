package com.cfwin.cfwinblockchain.activity.mail

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.fragment.ExchangeFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.IMAPFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.POP3Fragment

/**
 * 邮箱设置界面
 */
class SettingsActivity: SubBaseActivity() {

    /**协议控件*/
    @BindView(R.id.type)internal lateinit var protocolType: RadioGroup
    /**邮箱用户*/
    @BindView(R.id.show_address)internal lateinit var mailUser: TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_mail_settings
    }

    override fun initView() {
        topTitle.text = getString(R.string.mail_service)
        protocolType.setOnCheckedChangeListener { group, checkedId ->
            setSelectorColor(checkedId)
            when(checkedId){
                R.id.imap-> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.protocol_group, IMAPFragment())
                            .commit()
                }
                R.id.pop3-> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.protocol_group, POP3Fragment())
                            .commit()
                }
                R.id.exchange-> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.protocol_group, ExchangeFragment())
                            .commit()
                }
            }
        }
        (protocolType.getChildAt(0) as RadioButton).isChecked = true
    }

    override fun initData() {
        mailUser.text = intent.getStringExtra("mailUser")
    }

    private fun setSelectorColor(checkedId: Int){
        val count = protocolType.childCount
        for(i in 0 until count){
            val radio = protocolType.getChildAt(i) as RadioButton
            val color = if(radio.id == checkedId){
                R.color.white
            }else{
                R.color.bg_66
            }
            radio.setTextColor(resources.getColor(color))
        }
    }
}