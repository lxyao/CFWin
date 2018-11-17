package com.cfwin.cfwinblockchain.activity.mine

import android.content.Intent
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mine.account.ManagerActivity

/**
 * 设置界面
 */
class SettingsActivity :SubBaseActivity() {

    @BindView(R.id.mine_account)
    lateinit var accountSet: TextView
    @BindView(R.id.mine_address)
    lateinit var urlAddress: TextView
    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = getString(R.string.settings)
        accountSet.text = "  ${accountSet.text}"
        urlAddress.text = "  ${urlAddress.text}"
    }

    override fun initData() {}

    @OnClick(R.id.ll_accountset, R.id.mine_account, R.id.ll_address, R.id.mine_address)
    override fun onClick(v: View?) {
        when(v?.id){
            in arrayOf(R.id.ll_accountset, R.id.mine_account) ->{
                //账户设置
                startActivity(Intent(this, ManagerActivity::class.java))
            }
            in arrayOf(R.id.ll_address, R.id.mine_address) ->{
                //服务地址设置
                startActivity(Intent(this, ServiceAddressActivity::class.java))
            }
            else ->super.onClick(v)
        }
    }
}