package com.google.zxing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cfwin.base.R;
import com.cfwin.base.activity.BaseActivity;
import com.cfwin.base.utils.LogUtil;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.utils.UriUtil;
import com.google.zxing.view.ViewfinderView;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {

    /**
     * 获取返回中的数据key
     */
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private Toolbar back;
    private ImageButton btnFlash;
    private TextView btnAlbum; // 相册
    private boolean isFlashOn = false;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_google_scanner;
    }


    private View.OnClickListener albumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, REQUEST_CODE_SCAN_GALLERY);
        }
    };


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
//                    getPath(data);
                    handleAlbumPic(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    ///////////////////////////////////////////////////////////////////////////////
    public String getPath(Intent data){
        //这里开始的第二部分，获取图片的路径：
        String[] imgPath = {MediaStore.Images.Media.DATA};

        //好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = managedQuery(data.getData(), imgPath, null, null, null);

        //按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        //将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();

        //最后根据索引值获取图片路径
        String path = cursor.getString(column_index);
        return path;
    }
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 处理选择的图片
     * @param data
     */
    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        photo_path = UriUtil.getRealPathFromUri(this, data.getData());
        mProgress = new ProgressDialog(CaptureActivity.this);
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.dismiss();
                Result result = scanningImage(photo_path);
                if (result != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(INTENT_EXTRA_KEY_QR_SCAN ,result.getText());
                    resultIntent.putExtras(bundle);
                    CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(CaptureActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 扫描二维码图片的方法
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if(TextUtils.isEmpty(path)){
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
            LogUtil.e(getTAG(), "sssssssssssssssss scan 0 = " + resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            Logger.d("saomiao",resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     *  闪光灯开关按钮
     */
    private View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                boolean isSuccess = CameraManager.get().setFlashLight(!isFlashOn);
                if(!isSuccess){
                    Toast.makeText(CaptureActivity.this, "暂时无法开启闪光灯", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isFlashOn) {
                    // 关闭闪光灯
                    btnFlash.setImageResource(R.mipmap.flash_off);
                    isFlashOn = false;
                } else {
                    // 开启闪光灯
                    btnFlash.setImageResource(R.mipmap.flash_on);
                    isFlashOn = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void initView() {
        CameraManager.init(getApplication());
        viewfinderView = findViewById(R.id.viewfinder_content);
        back =  findViewById(R.id.toolbar);
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setId(R.id.iv_back);
                CaptureActivity.this.onClick(view);
            }
        });
        btnFlash = findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(flashListener);
        btnAlbum = findViewById(R.id.toolbar_menu);
        btnAlbum.setOnClickListener(albumOnClick);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        String title = getIntent().getStringExtra("title");
        View view = findViewById(R.id.base_top_toolbar);
        if(!TextUtils.isEmpty(title)){
            view.setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.toolbar_title)).setText(title);
            btnAlbum.setText("相册");
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() { }

    @Override
    public void onClick(@Nullable View v) {
        if(v.getId() == R.id.iv_back){
            finish();
        }else
            super.onClick(v);

    }
}