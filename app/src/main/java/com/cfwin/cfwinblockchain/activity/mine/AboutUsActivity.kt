package com.cfwin.cfwinblockchain.activity.mine

import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.cfwin.cfwinblockchain.BuildConfig
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity

/**
 * 关于我们界面
 */
class AboutUsActivity :SubBaseActivity() {

    @BindView(R.id.version)
    lateinit var version: TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_aboutus
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = getString(R.string.about_us)
    }

    override fun initData() {
        val ver = "V${BuildConfig.VERSION_NAME}"
        version.text = getString(R.string.current_version, ver)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.ll_star){
            //去评分
        }else super.onClick(v)
    }

}