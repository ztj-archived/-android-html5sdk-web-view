package com.ztj.h5webview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import io.dcloud.EntryProxy;
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
}
