package com.cfwin.cfwinblockchain.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.beans.CandyItem

/**
 * 糖果列表数据适配器
 */
class CandyListAdapter : ImplBaseAdapter<CandyItem> {

    constructor(context : Context, data: MutableList<CandyItem>):super(context, data)

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_candy
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
        tmpHolder.title.text = bean.Link
        tmpHolder.subTitle.text = "奖励糖果${bean.Award}颗"
    }

    class ViewHolder constructor(view: View, resId: Int) : SubBaseViewHolder(view, resId){

        @BindView(R.id.item_icon)
        lateinit var icon: ImageView
        @BindView(R.id.item_title)
        lateinit var title: TextView
        @BindView(R.id.item_subtitle)
        lateinit var subTitle: TextView
    }
}