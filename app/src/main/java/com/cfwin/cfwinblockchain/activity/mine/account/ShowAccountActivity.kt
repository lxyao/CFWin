package com.cfwin.cfwinblockchain.activity.mine.account

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.UserBean
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.utils.BitMatrix2BitmapUtil
import java.io.FileOutputStream


/**
 * 显示账户二位码
 */
class ShowAccountActivity :SubBaseActivity() {

    @BindView(R.id.copy)lateinit var copy: TextView
    @BindView(R.id.download)lateinit var save: TextView
    @BindView(R.id.code2)lateinit var code2: ImageView
    private lateinit var bit: Bitmap

    override fun getLayoutId(): Int {
        return R.layout.activity_show_account_code
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = getString(R.string.coll_gift)
        save.text = "${save.text}  "
    }

    override fun initData() {
        val item: UserBean = intent.getParcelableExtra("item")
        copy.text = item.address+"  "
        val width = resources.getDimensionPixelSize(R.dimen.qrcode_w)
        val hints = mutableMapOf(EncodeHintType.CHARACTER_SET to "utf-8",
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
                EncodeHintType.MARGIN to "0")
        bit = BitMatrix2BitmapUtil().createQRCode(context = item.address, hints= hints, point = Point(width, width))
        code2.setImageBitmap(bit)
    }

    @OnClick(R.id.copy, R.id.download)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.copy->{
                //复制到剪切板
                val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("cfwin_address", (v as TextView).text.toString())
                cmb.primaryClip = clipData
                showToast("已复制至粘贴板")
            }
            R.id.download->{
                v.isEnabled = false
                val img = this.getExternalFilesDir("img")
                if(!img.exists())img.mkdirs()
                //放到sdcard中
                val outstream = FileOutputStream("$img/${copy.text.toString().trim()}.jpg")
                bit.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                outstream.flush()
                outstream.close()
                v.isEnabled = true
            }
        }
        super.onClick(v)
    }
}