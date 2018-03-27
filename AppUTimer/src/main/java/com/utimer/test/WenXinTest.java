package com.utimer.test;

import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;

import ahtewlg7.utimer.util.Logcat;

/**
 */

public class WenXinTest {
    public static final String TAG = WenXinTest.class.getSimpleName();

    public void toTest(ContextWrapper contextWrapper){
        Logcat.i(TAG,"to test");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://www.qq.com");
        intent.setData(content_url);
//        intent.setClassName("com.tencent.mm","com.tencent.mm.plugin.webview.ui.tools.MailWebViewUI");
//        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
        contextWrapper.startActivity(intent);
    }
}
