package com.cfwin.cfwinblockchain.activity.home

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity

/**
 * 糖果详细界面
 */
class CandyDetailActivity :SubBaseActivity() {

    @BindView(R.id.web)lateinit var webView:WebView

    override fun getLayoutId(): Int {
        return R.layout.activity_candy_detail
    }

    override fun initView() {
        setTopNavBackground()
        val title = intent.getStringExtra("title")
        topTitle.text = "${title}糖果任务"
    }

    override fun initData() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = false
        settings.useWideViewPort = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.loadUrl(StringBuilder(intent.getStringExtra("detailUrl"))
                .append(Constant.API.CANDY_DETAIL).append(intent.getStringExtra("id")).toString(), null)
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                super.shouldOverrideUrlLoading(view, url)
                view?.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view?.loadUrl(request?.url.toString())
                    true
                }else super.shouldOverrideUrlLoading(view, request)
            }
        }
    }

}