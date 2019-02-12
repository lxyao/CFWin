package com.cfwin.cfwinblockchain.activity.mail

import android.content.Intent
import android.view.View
import android.widget.ListView
import android.widget.RadioGroup
import butterknife.BindView
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.MyApplication
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mail.contacts.ContactsActivity
import com.cfwin.cfwinblockchain.adapter.mail.MsgListAdapter
import com.cfwin.cfwinblockchain.beans.mail.MsgBean
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Folder
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress

/**
 * 邮箱消息列表界面
 */
class MsgListActivity: SubBaseActivity() {

    /**邮箱状态*/
    @BindView(R.id.mail_state)internal lateinit var mailState: RadioGroup
    /**列表*/
    @BindView(android.R.id.list)internal lateinit var listView: ListView
    private var adapter: MsgListAdapter? = null
    private lateinit var params: Array<String>
    private lateinit var inBoxFolder: Folder
    private lateinit var sendBoxFolder: Folder

    override fun getLayoutId(): Int {
        return R.layout.activity_secret_mail
    }

    override fun initView() {
        topTitle.text = getString(R.string.secret_mail_title)
        topMenu.text = getString(R.string.mail_contact)
        mailState.setOnCheckedChangeListener{group, checkedId -> onCheckedChanged(group, checkedId)}
    }

    override fun initData() {
        params = intent.getStringArrayExtra("params")
        inBoxFolder = MyApplication.store!!.getFolder("INBOX")
        sendBoxFolder = MyApplication.store!!.getFolder(intent.getStringExtra("sendBox"))
        onCheckedChanged(mailState, R.id.in_box)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.send_mail->{
                //进入发送邮箱
                startActivity(Intent(mContext!!, SendActivity::class.java)
                        .putExtra("params", params))
            }
            R.id.toolbar_menu->{
                //进入联系人
                startActivity(Intent(mContext!!, ContactsActivity::class.java))
            }
            else-> super.onClick(v)
        }
    }

    fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        adapter?.addData(mutableListOf(), true)
        when(checkedId){
            R.id.in_box->{
                //收件箱
                Thread{
                    showPage(folder = inBoxFolder)
                }.start()
            }
            R.id.out_box->{
                //发件箱
                Thread{showPage(folder = sendBoxFolder)}.start()
            }
        }
    }

    private fun showPage(page: Int = 1, folder: Folder){
        try {
            folder.open(Folder.READ_ONLY)
            val size = folder.messageCount - 1
            val start = size - ((page - 1)*Constant.PAGE_SIZE)
            val end = if(start - Constant.PAGE_SIZE <= 0){ 0 } else {start - Constant.PAGE_SIZE + 1}
            val mess = folder.messages
            val data = mutableListOf<MsgBean>()
            for (i in start downTo end) {
                val from = (mess[i].from[0] as InternetAddress).address
                val subject = mess[i].subject
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(mess[i].sentDate)
                data.add(MsgBean(from, subject, date))
                LogUtil.e(TAG!!, "消息数据 $from \n $subject \n $date", true)
            }
            folder.close(false)
            runOnUiThread { updateData(data, page) }
        }catch (e: MessagingException){
            e.printStackTrace()
        }
    }

    private fun updateData(data: MutableList<MsgBean>, page: Int){
        if(adapter == null || page == 1){
            adapter = MsgListAdapter(this, data)
            listView.adapter = adapter
        }else adapter!!.addData(data, false)
    }
}