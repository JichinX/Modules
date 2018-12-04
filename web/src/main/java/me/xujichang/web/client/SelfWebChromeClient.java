package me.xujichang.web.client;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import me.xujichang.util.tool.LogTool;
import me.xujichang.web.interfaces.IWebBase;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/26 10:33.
 */

public class SelfWebChromeClient extends WebChromeClient {
    private IWebBase mWebBase;
    private View customView;
    private CustomViewCallback mViewCallback;

    public SelfWebChromeClient(IWebBase webBase) {
        mWebBase = webBase;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //加载进度
        mWebBase.onPageProgress(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        //获取Title
        mWebBase.onPageReceiveTitle(view, title);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        //全屏
        LogTool.d("进入全屏...");
        mWebBase.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        //取消全屏
        LogTool.d("退出全屏...");
        mWebBase.onHideCustomView();
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        //选择文件
        return mWebBase.onPageFileChooser(webView, filePathCallback, fileChooserParams);
    }

//    @Override
//    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
//        super(uploadFile, acceptType, capture);
//    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        //prompt事件
        return mWebBase.onPagePrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        //打印Console消息
        return mWebBase.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        //alert
        return mWebBase.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        //confirm事件 如果拦截 必须调用 result.confirm
        return mWebBase.onJsConfirm(view, url, message, result);
    }
}
