package com.cfwin.cfwinblockchain.activity.mail.contacts

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ListView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.adapter.mail.ContactsAdapter

/**
 * 邮箱联系人界面
 */
class ContactsActivity: SubBaseActivity() {

    /**搜索*/
    @BindView(R.id.serach)lateinit var serach: EditText
    /**列表*/
    @BindView(android.R.id.list)lateinit var listView: ListView
    private lateinit var adapter: ContactsAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_contacts
    }

    override fun initView() {
        topTitle.text = getString(R.string.mail_contact)
        topMenu.text = getString(R.string.add)
    }

    override fun initData() {
        val tmp = mutableListOf<String>()
        for(i in 0..9)tmp.add("测试数据$i")
        adapter = ContactsAdapter(this, tmp)
        listView.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar_menu-> startActivityForResult(Intent(mContext!!, AddActivity::class.java),
                    201)
            else-> super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 201){
            if(resultCode == Activity.RESULT_OK){
                //刷新联系人界面，有数据更新
            }
        }
    }
}