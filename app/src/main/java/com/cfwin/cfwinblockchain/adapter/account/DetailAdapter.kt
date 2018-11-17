package com.cfwin.cfwinblockchain.adapter.account

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder
import com.cfwin.cfwinblockchain.beans.AccountUseItem

/**
 * 账户详细使用情况数据
 */
class DetailAdapter constructor(context: Context, data: MutableList<AccountUseItem>):ImplBaseAdapter<AccountUseItem>(context, data) {
    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_detail
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
        val tmpHolder = holder as ViewHolder
        val item = data[position]
        if(item.state){
            tmpHolder.img.setImageResource(R.mipmap.ic_change_success)
            tmpHolder.img.visibility = View.VISIBLE
        }else{
            tmpHolder.img.setImageResource(R.mipmap.ic_change_failed)
            tmpHolder.img.visibility = View.INVISIBLE
        }
    }

    class ViewHolder(v: View, resId: Int) :SubBaseViewHolder(v, resId){
        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.type) lateinit var type: TextView
        @BindView(R.id.time) lateinit var time: TextView
        @BindView(R.id.num) lateinit var num: TextView
        @BindView(R.id.state) lateinit var img: ImageView
    }
}