package com.cfwin.cfwinblockchain;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.cfwin.base.activity.BaseActivity;
import com.cfwin.cfwinblockchain.activity.home.MainActivity;
import com.cfwin.cfwinblockchain.activity.user.CreateUserActivity;
import com.cfwin.cfwinblockchain.activity.user.ImportUserActivity;
import com.cfwin.cfwinblockchain.db.LocalDBManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Create4ImportUserActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create4importuser;
    }

    @Override
    protected void initView() {}

    @Override
    protected void initData() {
        isGrant(201, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA});
    }

    @Override
    public void onClick(@Nullable View v) {
        switch (v.getId()){
            case R.id.createUser:
                startActivity(new Intent(this, CreateUserActivity.class));
                break;
            case R.id.importUser:
                startActivity(new Intent(this, ImportUserActivity.class));
                break;
        }
    }

    @Override
    public void permissionGrant(int requestCode, @NotNull String[] permission) {
        super.permissionGrant(requestCode, permission);
        new LocalDBManager(this);
        //判断是否首次使用
        boolean tmp  = getSharedPreferences(Constant.configFileName, MODE_PRIVATE).getBoolean(Constant.isFirstUse, true);
        if(!tmp){
            //进入主界面
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
