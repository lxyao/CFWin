package com.cfwin.cfwinblockchain.activity.mail.contacts

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import butterknife.BindView
import com.cfwin.base.utils.PatternUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.mail.ContactBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.ContactsOperaDao
import com.google.zxing.CaptureActivity

/**
 * 邮箱联系人 添加界面
 */
class AddActivity: SubBaseActivity() {

    /**昵称*/
    @BindView(R.id.nickName)lateinit var nickName: EditText
    /**联系邮箱*/
    @BindView(R.id.contactMail)lateinit var contactMail: EditText
    /**好友id*/
    @BindView(R.id.friendId)lateinit var friendId: EditText
    /**PGP公约*/
    @BindView(R.id.pgp_key)lateinit var pgp_key: EditText

    override fun getLayoutId(): Int {
        return R.layout.activity_add_contact
    }

    override fun initView() {
        topTitle.text = getString(R.string.add)+getString(R.string.mail_contact)
        topMenu.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_scan, 0)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar_menu-> startActivityForResult(Intent(mContext!!, CaptureActivity::class.java)
                    .putExtra("title", getString(R.string.scan_title)),
                    201)
            R.id.add-> checkInfo(v)
            else-> super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 201){
                //获取扫描的好友id
                val tmp = data?.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN)
                friendId.setText(tmp)
            }
        }
    }

    //检查信息
    private fun checkInfo(v: View?){
        val name = nickName.text.toString().trim()
        if(TextUtils.isEmpty(name)){
            showToast(getString(R.string.friend_nick_hint))
            return
        }
        val mail = contactMail.text.toString().trim()
        if(TextUtils.isEmpty(mail)){
            showToast(getString(R.string.contact_mail_hint))
            return
        }
        if(!PatternUtil.isMail(mail)){
            showToast(getString(R.string.please_input, " 正确的邮箱地址"))
            return
        }
        v?.isEnabled = false
        val friendId = friendId.text.toString().trim()
        val pgpKey = pgp_key.text.toString().trim()
        val bean = ContactBean(nickName = name, mail = mail, friendId = friendId, pgpKey = pgpKey)
        //添加到本地库
        LocalDBManager(this).getTableOperation(ContactsOperaDao::class.java).addContact(bean)
        setResult(Activity.RESULT_OK)
        finish()
    }

}