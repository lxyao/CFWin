package com.cfwin.cfwinblockchain.activity.mail

import android.app.Activity
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.fragment.ExchangeFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.IMAPFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.POP3Fragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.IDataChangeCallback

/**
 * 邮箱设置界面
 */
class SettingsActivity: SubBaseActivity(), RadioGroup.OnCheckedChangeListener {

    /**协议控件*/
    @BindView(R.id.type)internal lateinit var protocolType: RadioGroup
    /**邮箱用户*/
    @BindView(R.id.show_address)internal lateinit var mailUser: TextView
    var shared: SharedPreferences? = null
    var lastCheckId = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_mail_settings
    }

    override fun initView() {
        topTitle.text = getString(R.string.mail_service)
        protocolType.setOnCheckedChangeListener(this)
    }

    override fun initData() {
        mailUser.text = intent.getStringExtra("mailUser")
        shared = getSharedPreferences(Constant.configFileName, Activity.MODE_PRIVATE)
        shared?.let {
            lastCheckId = it.getInt(Constant.configType, R.id.exchange)
            (protocolType.findViewById(lastCheckId) as RadioButton).isChecked = true
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.double_sure->{
                //保存数据
                val type = v.contentDescription.toString().toInt()
                val fragment = getCurrentFragment()
                if(fragment is IDataChangeCallback)fragment.saveData(0)
            }
            R.id.double_cancel->{
                val fragment = getCurrentFragment()
                if(fragment is IDataChangeCallback)fragment.setCancel(false)
            }
            R.id.iv_back-> onBackPressed()
            else-> super.onClick(v)
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if(lastCheckId != checkedId){
            if(!save(group, checkedId))return
        }
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

    override fun onBackPressed() {
        if(save(protocolType, lastCheckId)){
            super.onBackPressed()
        }
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
        shared!!.edit()
                .putInt(Constant.configType, checkedId)
                .apply()
    }

    private fun getCurrentFragment(): Fragment{
        return supportFragmentManager.fragments[0]
    }

    private fun save(group: RadioGroup?, checkedId: Int): Boolean{
        val fragment = supportFragmentManager.fragments[0]
        if(fragment is IDataChangeCallback){
            if(fragment.isChanged()){
                LogUtil.e(TAG!!, "--- 数据显示 $fragment")
                showDialog(getString(R.string.data_change_save),isDoubleBtn = true, type = 1)
                with(group!!){
                    setOnCheckedChangeListener(null)
                    findViewById<RadioButton>(lastCheckId).isChecked = true
                    setOnCheckedChangeListener(this@SettingsActivity)
                }
                return false
            }else{
                lastCheckId = checkedId
            }
        }
        return true
    }
}