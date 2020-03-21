package com.ztj.h5webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.feature.internal.sdk.SDK;


public class MainActivity extends AppCompatActivity {
    EntryProxy entry_proxy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (entry_proxy == null) {
            FrameLayout root_view = new FrameLayout(this);
            // 创建 5+ 内核运行事件监听
            H5WebViewModeListener wm = new H5WebViewModeListener(this, root_view);
            // 初始化 5+ 内核
            entry_proxy = EntryProxy.init(this, wm);
            // 启动 5+ 内核
            entry_proxy.onCreate(this, savedInstanceState, SDK.IntegratedMode.WEBVIEW, null);
            setContentView(root_view);
        }

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return entry_proxy.onActivityExecute(this, SysEventType.onCreateOptionMenu, menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        entry_proxy.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        entry_proxy.onResume(this);
    }

    @SuppressLint("WrongConstant")
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getFlags() != 0x10600000) {
            // 非点击icon调用activity时才调用newintent事件
            entry_proxy.onNewIntent(this, intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entry_proxy.onStop(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean _ret = entry_proxy.onActivityExecute(this, SysEventType.onKeyDown, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean _ret = entry_proxy.onActivityExecute(this, SysEventType.onKeyUp, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean _ret = entry_proxy.onActivityExecute(this, SysEventType.onKeyLongPress, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyLongPress(keyCode, event);
    }

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        try {
            int temp = this.getResources().getConfiguration().orientation;
            if (entry_proxy != null) {
                entry_proxy.onConfigurationChanged(this, temp);
            }
            super.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        entry_proxy.onActivityExecute(this, SysEventType.onActivityResult, new Object[]{requestCode, resultCode, data});
    }
}
