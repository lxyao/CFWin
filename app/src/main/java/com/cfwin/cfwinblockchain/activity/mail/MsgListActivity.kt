package com.cfwin.cfwinblockchain.activity.mail

import android.content.Intent
import android.view.View
import android.widget.ListView
import android.widget.RadioGroup
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.contacts.ContactsActivity
import com.cfwin.cfwinblockchain.adapter.mail.MsgListAdapter
import com.cfwin.cfwinblockchain.beans.mail.StoreBean

/**
 * 邮箱消息列表界面
 */
class MsgListActivity: SubBaseActivity() {

    /**邮箱状态*/
    @BindView(R.id.mail_state)internal lateinit var mailState: RadioGroup
    /**列表*/
    @BindView(android.R.id.list)internal lateinit var listView: ListView
    private lateinit var adapter: MsgListAdapter
    private lateinit var storeBean: StoreBean

    override fun getLayoutId(): Int {
        return R.layout.activity_secret_mail
    }

    override fun initView() {
        topTitle.text = getString(R.string.secret_mail_title)
        topMenu.text = getString(R.string.mail_contact)
        mailState.setOnCheckedChangeListener{group, checkedId -> onCheckedChanged(group, checkedId)}
    }

    override fun initData() {
        storeBean = intent.getSerializableExtra("store") as StoreBean
        val tmp = mutableListOf<String>()
        for(i in 0..4)tmp.add("${i+1}")
        adapter = MsgListAdapter(this, tmp)
        listView.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.send_mail->{
                //进入发送邮箱
                startActivity(Intent(mContext!!, SendActivity::class.java))
            }
            R.id.toolbar_menu->{
                //进入联系人
                startActivity(Intent(mContext!!, ContactsActivity::class.java))
            }
            else-> super.onClick(v)
        }
    }

    fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.in_box->{
                //收件箱
            }
            R.id.out_box->{
                //发件箱
            }
        }
    }
}