package com.cfwin.cfwinblockchain.adapter

import android.content.Context
import android.view.View
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R

/**
 * 账户选择数据列表
 */
class SelectedAdapter constructor(context: Context, data: MutableList<Any>) : ImplBaseAdapter<Any>(context, data) {

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_account_selected
    }

    override fun getHolder(position: Int, view: View?): BaseViewHolder? {
        var holder: ViewHolder? = null
        if(view != null){
            val tag = view.tag
            holder = tag as? ViewHolder ?: ViewHolder(view, getLayoutId(position))
        }
        return holder
    }

    override fun <B : BaseViewHolder> showView(position: Int, holder: B) {
    }

    class ViewHolder(view: View, resId: Int): SubBaseViewHolder(view, resId){}
}