package com.cfwin.cfwinblockchain.activity.mail

import android.app.Activity
import android.content.Intent
import android.widget.ListView
import butterknife.BindView
import butterknife.OnItemClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.adapter.mail.SecretAdapter

/**
 * 邮箱协议 加密方式选择界面
 */
class SecretActivity: SubBaseActivity() {

    @BindView(android.R.id.list)internal lateinit var listView:ListView
    private lateinit var adapter: SecretAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_secret
    }

    override fun initView() {
        topTitle.text = getString(R.string.secret)
    }

    override fun initData() {
        val secretRes: Array<String> = resources.getStringArray(R.array.secrets)
        val tmp = mutableListOf<String>()
        for(str in secretRes)tmp.add(str)
        val checkId = intent.getIntExtra("checkId", 0)
        adapter = SecretAdapter(this, tmp, checkId)
        listView.adapter = adapter
    }

    @OnItemClick(android.R.id.list)
    fun onItemClick(position: Int){
        setResult(Activity.RESULT_OK,
                Intent().putExtra("position", position))
        finish()
    }
}