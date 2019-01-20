package com.cfwin.cfwinblockchain.activity.mail.fragment.abs

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.mail.SecretActivity

/**
 * 服务器协议设置，抽像父类
 */
abstract class AbsProtocolFragment: SubBaseFragment() {
    /**配置服务器标题*/
    @BindView(R.id.method)protected lateinit var method: TextView
    /**配置服务器地址*/
    @BindView(R.id.service_address)protected lateinit var serviceAddress: TextView
    /**配置账号显示名称*/
    @BindView(R.id.account)protected lateinit var accountTitle: TextView
    /**配置账号*/
    @BindView(R.id.userAccount)protected lateinit var userAccount: TextView
    /**配置密码显示名称*/
    @BindView(R.id.pwd)protected lateinit var pwdTitle: TextView
    /**配置密码*/
    @BindView(R.id.userPwd)protected lateinit var userPwd: TextView
    /**配置端口名称*/
    @BindView(R.id.port_title)protected lateinit var portTitle: TextView
    /**配置端口*/
    @BindView(R.id.port)protected lateinit var port: TextView
    /**配置加密名称*/
    @BindView(R.id.ssl)protected lateinit var sslTitle: TextView
    /**配置加密*/
    @BindView(R.id.secrect)protected lateinit var secret: CheckBox

    protected var checkId = 0

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
}