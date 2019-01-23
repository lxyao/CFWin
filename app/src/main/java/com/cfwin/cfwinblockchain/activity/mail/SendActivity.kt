package com.cfwin.cfwinblockchain.activity.mail

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.*
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.contacts.ContactsActivity
import com.cfwin.cfwinblockchain.beans.mail.ContactBean

/**
 * 邮件发送界面
 */
class SendActivity: SubBaseActivity() {

    /**发送者头像*/
    @BindView(R.id.sendIcon)lateinit var sendIcon: ImageView
    /**发送者昵称*/
    @BindView(R.id.nickName)lateinit var nickName: TextView
    /**发送者邮箱*/
    @BindView(R.id.mailAddress)lateinit var mailAddress: TextView
    /**收件人邮箱*/
    @BindView(R.id.inUser)lateinit var inUser: EditText
    /**邮箱主题*/
    @BindView(R.id.subject)lateinit var subject: EditText
    /**邮箱内容*/
    @BindView(R.id.content)lateinit var content: EditText
    /**发送方式*/
    @BindView(R.id.sendMethod)lateinit var sendMethod: RadioGroup

    override fun getLayoutId(): Int {
        return R.layout.activity_send_mail
    }

    override fun initView() {
        topMenu.text = "发送"
        sendMethod.setOnCheckedChangeListener{group, checkedId -> onChecked(group, checkedId) }
    }

    override fun initData() {
    }

    private fun onChecked(group: RadioGroup, checkedId: Int){
        if(checkedId == R.id.pgp){
            showToast(getString(R.string.pgp_pubkey_hint))
            group.findViewById<RadioButton>(checkedId).isChecked = false
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.contacts->{
                startActivityForResult(Intent(mContext!!, ContactsActivity::class.java)
                        .putExtra("isSelected", true),
                        201)
            }
            R.id.toolbar_menu->{
                //发送
            }
            else-> super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 201){
                val user = data?.getParcelableExtra<ContactBean>("user")
                inUser.setText(user!!.mail)
            }
        }
    }
}