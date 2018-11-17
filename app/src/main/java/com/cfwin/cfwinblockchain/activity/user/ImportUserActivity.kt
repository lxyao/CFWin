package com.cfwin.cfwinblockchain.activity.user

import android.content.Intent
import android.view.View
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.home.MainActivity

/**
 * 恢复身份 界面
 */
class ImportUserActivity : SubBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_import_user
    }

    override fun initView() {
        topTitle.text = getString(R.string.import_user)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.next){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else super.onClick(v)
    }
}