package com.fionera.base.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * DetailWebView
 * Created by Victor on 16/2/1.
 */
public class DetailWebView
        extends WebView {
    private Activity activity;

    public DetailWebView(Context context) {
        super(context);
        init();
    }

    public DetailWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                playSoundEffect(SoundEffectConstants.CLICK);
                if (url.contains("https://back")) {
                    if (activity != null) {
                        activity.finish();
                        activity = null;
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setInitialScale(1);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    public void loadData(String data) {
        loadData("<head><meta content=\"width=device-width,initial-scale=1.0,minimum-scale=.5," +
                         "maximum-scale=3\" name=\"viewport\"></head><body style='margin:0;" +
                         "padding:0;'>" + data + "</body>",
                 "text/html;charset=utf-8", null);
    }
    public void loadWebData(String data){
        loadData("<head><meta content=\"width=.._wapper,initial-scale=1.0,minimum-scale=.5," +
                        "maximum-scale=3\" name=\"viewport\"></head>" +
                "<body style='margin:0;" +
                        "padding:0;'>" + data + "" +
                "</body>",
                "text/html;charset=utf-8", null);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
