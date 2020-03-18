package com.ztj.h5webview;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;

import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.feature.internal.sdk.SDK;

public class H5WebViewModeListener implements ICore.ICoreStatusListener {

    private Activity activity;
    private IWebview web_view;
    private ViewGroup root_view;

    H5WebViewModeListener(Activity activity, ViewGroup rootView) {
        this.activity = activity;
        root_view = rootView;
        root_view.setBackgroundColor(0xffffffff);
        root_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                web_view.onRootViewGlobalLayout(root_view);
            }
        });
    }

    @Override
    public void onCoreInitEnd(ICore coreHandler) {
        String app_id = "h5_web_view";
        String url = "http://android-web-view.html.ztj.xyz/";
        web_view = SDK.createWebview(activity, url, app_id, new IWebviewStateListener() {
            @Override
            public Object onCallBack(int pType, Object pArgs) {
                switch (pType) {
                    case IWebviewStateListener.ON_WEBVIEW_READY:
                        // 准备完毕之后添加 WebView 到父 View 中
                        // 设置排版不显示状态，避免内容排版错乱问题
                        ((IWebview) pArgs).obtainFrameView().obtainMainView().setVisibility(View.INVISIBLE);
                        SDK.attach(root_view, ((IWebview) pArgs));
                        break;
                    case IWebviewStateListener.ON_PAGE_FINISHED:
                        // 页面加载完毕，显示 WebView
                        web_view.obtainFrameView().obtainMainView().setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        });
        final WebView web_view_instance = web_view.obtainWebview();
        // 监听返回键
        web_view_instance.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (web_view_instance.canGoBack()) {
                        web_view_instance.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onCoreReady(ICore coreHandler) {
        try {
            SDK.initSDK(coreHandler);
            SDK.requestAllFeature();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCoreStop() {
        return false;
    }
}
