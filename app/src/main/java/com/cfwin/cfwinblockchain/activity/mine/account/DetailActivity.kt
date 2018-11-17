package com.cfwin.cfwinblockchain.activity.mine.account

import android.content.Intent
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mine.account.fragment.*
import com.cfwin.cfwinblockchain.adapter.HomePageAdapter
import com.cfwin.cfwinblockchain.beans.AccountItem
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 账户记录详细信息 界面
 */
class DetailActivity :SubBaseActivity(), ViewPager.OnPageChangeListener{

    @BindView(R.id.viewPager)lateinit var viewPager: ViewPager
    @BindView(R.id.tabLine)lateinit var tabLine: View
    private lateinit var item: UserBean

    /**
     * 下划线最大宽度
     */
    var maxWidth = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_account_detail
    }

    override fun initView() {
        setTopNavBackground()
        topMenu.text = getString(R.string.helper_word)
        item = intent.getParcelableExtra("item")
        topTitle.text = item.accountName
        topTitle.setSingleLine()
        viewPager.addOnPageChangeListener(this)
        maxWidth = resources.displayMetrics.widthPixels shr 2
        callocation(0)
    }

    override fun initData() {
        //, OutFragment(), InFragment(), FailedFragment()
        val tmp = listOf(AllFragment())
        viewPager.adapter = HomePageAdapter(tmp, supportFragmentManager)
    }

    override fun onPageScrollStateChanged(p0: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(p0: Int) {
        callocation(p0)
    }

    @OnClick(R.id.tabAll, R.id.tabOut, R.id.tabIn, R.id.tabFailed, R.id.show_account, R.id.change_score)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tabAll-> {
                viewPager.currentItem = 0
                callocation(viewPager.currentItem)
            }
            R.id.tabOut-> {
                viewPager.currentItem = 1
                callocation(viewPager.currentItem)
            }
            R.id.tabIn-> {
                viewPager.currentItem = 2
                callocation(viewPager.currentItem)
            }
            R.id.tabFailed-> {
                viewPager.currentItem = 3
                callocation(viewPager.currentItem)
            }
            R.id.show_account->{
                //显示账户
                startActivity(Intent(this, ShowAccountActivity::class.java)
                        .putExtra("item", item))
            }
            R.id.change_score-> {
                //转赠
                startActivityForResult(Intent(this, PresentActivity::class.java)
                        .putExtra("item", item), 202)
            }
            R.id.toolbar_menu-> {
                //显示助记词
                showDialog(title = "查看助记词", contentId = R.layout.show_alert_input)
                        .setCanceledOnTouchOutside(false)
            }
            else ->super.onClick(v)
        }
    }

    private fun callocation(position: Int){
        val lp = tabLine.layoutParams as ViewGroup.MarginLayoutParams
        lp.leftMargin = position * maxWidth
        lp.width = maxWidth
        tabLine.layoutParams = lp
    }
}
