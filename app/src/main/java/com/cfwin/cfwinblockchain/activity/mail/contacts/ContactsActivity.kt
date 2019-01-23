package com.cfwin.cfwinblockchain.activity.mail.contacts

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ListView
import butterknife.BindView
import butterknife.OnItemClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.adapter.mail.ContactsAdapter
import com.cfwin.cfwinblockchain.beans.mail.ContactBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.ContactsOperaDao

/**
 * 邮箱联系人界面
 */
class ContactsActivity: SubBaseActivity(), TextWatcher{

    /**搜索*/
    @BindView(R.id.serach)lateinit var serach: EditText
    /**列表*/
    @BindView(android.R.id.list)lateinit var listView: ListView
    private lateinit var adapter: ContactsAdapter
    private lateinit var contactDao: ContactsOperaDao
    private var isSelected = false

    override fun getLayoutId(): Int {
        return R.layout.activity_contacts
    }

    override fun initView() {
        topTitle.text = getString(R.string.mail_contact)
        isSelected = intent.getBooleanExtra("isSelected", false)
        if(!isSelected){
            topMenu.text = getString(R.string.add)
        }
        serach.addTextChangedListener(this)
    }

    override fun initData() {
        contactDao = LocalDBManager(this).getTableOperation(ContactsOperaDao::class.java)
        val tmp = contactDao.queryData("")
        adapter = ContactsAdapter(this, tmp)
        listView.adapter = adapter
    }

    @OnItemClick(android.R.id.list)
    fun onItemClick(position: Int){
        val bean = adapter.getItem(position) as ContactBean
        if(isSelected){
            setResult(Activity.RESULT_OK, Intent()
                    .putExtra("user", bean))
            finish()
            return
        }
        startActivityForResult(Intent(this, DetailActivity::class.java)
                .putExtra("bean", bean),
                201)
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
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                201->{
                    //刷新联系人界面，有数据更新
                    val tmp = contactDao.queryData("")
                    adapter.addData(tmp, true)
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        val tmp = contactDao.queryData(s.toString())
        adapter.addData(tmp, true)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}