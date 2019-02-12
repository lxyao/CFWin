package com.cfwin.cfwinblockchain.activity.mail.fragment.abs

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.mail.SecretActivity
import com.cfwin.cfwinblockchain.dao.ImplMailOperaDao

/**
 * 服务器协议设置，抽像父类
 */
abstract class AbsProtocolFragment: SubBaseFragment(), IDataChangeCallback, TextWatcher{
    /**配置服务器标题*/
    @BindView(R.id.method)protected lateinit var method: TextView
    /**配置服务器地址*/
    @BindView(R.id.service_address)protected lateinit var serviceAddress: EditText
    /**配置账号显示名称*/
    @BindView(R.id.account)protected lateinit var accountTitle: TextView
    /**配置账号*/
    @BindView(R.id.userAccount)protected lateinit var userAccount: EditText
    /**配置密码显示名称*/
    @BindView(R.id.pwd)protected lateinit var pwdTitle: TextView
    /**配置密码*/
    @BindView(R.id.userPwd)protected lateinit var userPwd: EditText
    /**配置端口名称*/
    @BindView(R.id.port_title)protected lateinit var portTitle: TextView
    /**配置端口*/
    @BindView(R.id.port)protected lateinit var port: EditText
    /**配置加密名称*/
    @BindView(R.id.ssl)protected lateinit var sslTitle: TextView
    /**配置加密*/
    @BindView(R.id.secrect)protected lateinit var secret: CheckBox

    protected var checkId = 0
    private lateinit var shared: SharedPreferences
    private var isChange = false

    @OnClick(R.id.ssl_group)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ssl_group-> startActivityForResult(Intent(mContext!!, SecretActivity::class.java)
                    .putExtra("checkId", checkId),
                    201)
            else-> super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 201 && resultCode == Activity.RESULT_OK){
            checkId = data?.getIntExtra("position", 0)!!
            secret.text = resources.getStringArray(R.array.secrets)[checkId]
        }
    }

    override fun afterTextChanged(s: Editable?) {
        LogUtil.e(TAG!!, "数据改变 -- ${s?.toString()}")
        isChange = true
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun isChanged(): Boolean {
        return isChange
    }

    override fun setCancel(isCancel: Boolean) {
        this.isChange = isCancel
    }

    override fun saveData(type: Int) {
        shared.edit()
                .putString(Constant.MAIL_CONFIG.SERVICE_ADDRESS+type, serviceAddress.text.toString().trim())
                .putString(Constant.MAIL_CONFIG.USER_CODE+type, userAccount.text.toString().trim())
                .putString(Constant.MAIL_CONFIG.USER_PWD+type, userPwd.text.toString().trim())
                .putString(Constant.MAIL_CONFIG.SECRET_METHOD+type, secret.text.toString().trim()+":"+checkId)
                .putString(Constant.MAIL_CONFIG.SERVICE_PORT+type, port.text.toString().trim())
                .apply()
        isChange = false
    }

    /**
     * 设置读取配置文件的信息
     * @param fileName 配置文件的名称
     * @param type 类型
     */
    open fun setConfig(fileName: String, type: Int){
        shared = context!!.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        val params = ImplMailOperaDao().getSharedMailConfig(shared = shared, type = type)
        serviceAddress.setText(params[0])
        userAccount.setText(params[1])
        userPwd.setText(params[2])
        val strs = params[3]!!.split(":")
        secret.text = strs[0]
        checkId = try {
            strs[1].toInt()
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
            0
        }
        port.setText(params[4])
        setTextChange(true)
    }

    private fun setTextChange(isChange: Boolean){
        serviceAddress.addTextChangedListener(if(isChange)this else null)
        userAccount.addTextChangedListener(if(isChange)this else null)
        userPwd.addTextChangedListener(if(isChange)this else null)
        secret.addTextChangedListener(if(isChange)this else null)
        port.addTextChangedListener(if(isChange)this else null)
    }
}