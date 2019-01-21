package com.cfwin.cfwinblockchain.activity.mail.contacts

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.mail.ContactBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.ContactsOperaDao

/**
 * 邮箱联系人 详细界面
 */
class DetailActivity: SubBaseActivity() {

    @BindView(R.id.head)lateinit var head: ImageView
    @BindView(R.id.nickName)lateinit var nickName: EditText
    @BindView(R.id.mail)lateinit var mail: TextView
    @BindView(R.id.ID)lateinit var id: EditText
    @BindView(R.id.pgpKey)lateinit var pgpKey: EditText
    private lateinit var bean: ContactBean

    override fun getLayoutId(): Int {
        return R.layout.activity_contact_detail
    }

    override fun initView() {
        topTitle.text = getString(R.string.mail_contact)+"详情"
        topMenu.text = getString(R.string.edit)
    }

    override fun initData() {
        bean = intent.getParcelableExtra("bean")
        nickName.setText(bean.nickName)
        mail.text = bean.mail
        id.setText(bean.friendId)
        pgpKey.setText(bean.pgpKey)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.double_sure-> finish()
            R.id.iv_back-> onBackPressed()
            R.id.toolbar_menu->{
                val text = v as TextView
                toggleState(text)
            }
            else-> super.onClick(v)
        }
    }

    override fun onBackPressed() {
        if(getString(R.string.complete) == topMenu.text.toString().trim()){
            showDialog("是否要放弃本次操作", isDoubleBtn = true, type = 1)
            return
        }
        super.onBackPressed()
    }

    private fun toggleState(text: TextView){
        val name = text.text.toString().trim()
        if(name == getString(R.string.edit)){
            text.text = getString(R.string.complete)
            nickName.isEnabled = true
            id.isEnabled = true
            pgpKey.isEnabled = true
        }else{
            text.text = getString(R.string.edit)
            save()
        }
    }

    private fun save(){
        val name = nickName.text.toString().trim()
        if(TextUtils.isEmpty(name)){
            showToast(getString(R.string.friend_nick_hint))
            return
        }
        bean.nickName = name
        bean.mail = mail.text.toString().trim()
        bean.friendId = id.text.toString().trim()
        bean.pgpKey = pgpKey.text.toString().trim()
        //修改数据源
        LocalDBManager(this).getTableOperation(ContactsOperaDao::class.java).addContact(bean)
        showToast("修改成功")
        //关闭界面
        setResult(Activity.RESULT_OK)
        finish()
    }
}