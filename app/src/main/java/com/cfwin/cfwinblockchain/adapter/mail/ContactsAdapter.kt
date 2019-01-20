package com.cfwin.cfwinblockchain.adapter.mail

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder

/**
 * 联系人列表数据展示
 */
class ContactsAdapter(context: Context, data: MutableList<String> ): ImplBaseAdapter<String>(context, data){
    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_contact
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
        tmp.nickName.text = "测试数据$position"
    }

    class ViewHolder(view: View, resId: Int): SubBaseViewHolder(view, resId){
        @BindView(R.id.head)lateinit var head: ImageView
        @BindView(R.id.nickName)lateinit var nickName: TextView
    }

}