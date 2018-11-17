package com.cfwin.cfwinblockchain.adapter.mine

import android.content.Context
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder
import com.cfwin.cfwinblockchain.beans.MsgItem

/**
 * 系统消息 数据适配器
 */
class MsgAdapter constructor(context: Context, data: MutableList<MsgItem>) : ImplBaseAdapter<MsgItem>(context, data) {

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_msg
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
        val tmpHolder = holder as ViewHolder
    }

    class ViewHolder(v: View, resId: Int) : SubBaseViewHolder(v, resId){
        @BindView(R.id.item_title)
        lateinit var title: TextView
        @BindView(R.id.item_time)
        lateinit var time: TextView
    }
}