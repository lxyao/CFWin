package com.cfwin.cfwinblockchain.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.beans.LoginLogItem

/**
 * 登录日志的数据
 */
class LoginLogAdapter constructor(context: Context, data: MutableList<LoginLogItem>) : ImplBaseAdapter<LoginLogItem>(context, data) {
    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_loginlog
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
        val bean = data[position]
        tmpHolder.icon.isChecked = bean.state
        tmpHolder.title.text = context.getString(R.string.item_append, context.getString(R.string.login_url), bean.loginUrl)
        tmpHolder.subTitle.text =context.getString(R.string.item_append, context.getString(R.string.login_time), bean.time)
    }

    class ViewHolder(view: View, resId: Int):SubBaseViewHolder(view, resId){
        @BindView(R.id.item_icon)
        lateinit var icon: CheckBox
        @BindView(R.id.item_title)
        lateinit var title: TextView
        @BindView(R.id.item_subtitle)
        lateinit var subTitle: TextView
    }
}