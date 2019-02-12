package com.cfwin.cfwinblockchain.adapter.mail

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder
import com.cfwin.cfwinblockchain.beans.mail.MsgBean

/**
 * 邮箱信息列表
 */
class MsgListAdapter(context: Context, data: MutableList<MsgBean>): ImplBaseAdapter<MsgBean>(context, data) {

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_mail_msg
    }

    override fun getHolder(position: Int, view: View?): BaseViewHolder? {
        var holder: ViewHolder? = null
        if(view != null){
            val tag = view?.tag
            holder = tag as? ViewHolder ?: ViewHolder(view, getLayoutId(position))
        }
        return holder
    }

    override fun <B : BaseViewHolder> showView(position: Int, holder: B) {
        val tmp = holder as ViewHolder
        val bean = data[position]
        tmp.nickName.text = bean.from
        tmp.userId.text = bean.time
        tmp.content.text = bean.subject
    }

    class ViewHolder(view: View, resId: Int): SubBaseViewHolder(view, resId){
        @BindView(R.id.item_head)lateinit var head: ImageView
        @BindView(R.id.item_nickName)lateinit var nickName: TextView
        @BindView(R.id.item_id)lateinit var userId: TextView
        @BindView(R.id.item_content)lateinit var content: TextView
    }
}