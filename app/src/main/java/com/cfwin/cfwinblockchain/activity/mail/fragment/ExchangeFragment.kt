package com.cfwin.cfwinblockchain.activity.mail.fragment

import android.app.Activity
import android.text.TextUtils
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsProtocolFragment

/**
 * Exchange协议设置界面
 */
class ExchangeFragment: AbsProtocolFragment(), CompoundButton.OnCheckedChangeListener {

    override fun getLayoutId(): Int {
        return R.layout.merge_protocol_layout
    }

    override fun initView() {
        super.initView()
        method.text = getString(R.string.mail_service)
        accountTitle.text = getString(R.string.area)
        pwdTitle.text = getString(R.string.userCode)
        portTitle.text =getString(R.string.pwd)
        sslTitle.text = getString(R.string.ssl_type)
        secret.isChecked = true
        secret.isClickable = true
        secret.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_radio_selector, 0, 0, 0)
        fragmentView?.findViewById<LinearLayout>(R.id.ssl_group)?.isClickable = false
        initData()
    }

    override fun initData() {
        setConfig(Constant.exchangeConfig, 0)
        secret.setOnCheckedChangeListener(this)
    }

    override fun setConfig(fileName: String, type: Int) {
        super.setConfig(fileName, type)
        val txt = secret.text
        secret.text = ""
        secret.isChecked = (TextUtils.isEmpty(txt) || txt == "true")
        setCancel(false)
        secret.addTextChangedListener(null)
    }

    override fun saveData(type: Int) {
        super.saveData(type)
        //重新保存 加密方式
        context!!.getSharedPreferences(Constant.exchangeConfig, Activity.MODE_PRIVATE)
                .edit()
                .putString(Constant.MAIL_CONFIG.SECRET_METHOD+type, "${secret.isChecked}")
                .apply()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        setCancel(true)
    }
}