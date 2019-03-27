package me.xujichang.web.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import java.io.File;
import java.net.URLDecoder;

import me.xujichang.ui.activity.actionbar.ActionWhich;
import me.xujichang.ui.utils.GlobalUtil;
import me.xujichang.util.tool.LogTool;
import me.xujichang.web.R;
import me.xujichang.web.SystemOperate;
import me.xujichang.web.WebConst;
import me.xujichang.web.WebDataParse;
import me.xujichang.web.client.SelfWebChromeClient;
import me.xujichang.web.client.SelfWebViewClient;
import me.xujichang.web.handler.DefaultWebHandler;
import me.xujichang.web.interfaces.IWebBase;
import me.xujichang.web.interfaces.IWebJsCallBack;
import me.xujichang.web.interfaces.IWebLoading;
import me.xujichang.web.interfaces.IWebParseData;
import me.xujichang.web.loading.ProgressLoading;
import me.xujichang.web.util.SmsUtil;

import static android.webkit.WebSettings.LOAD_DEFAULT;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/26 10:21.
 */

public abstract class BaseWebViewActivity extends WebActionbarActivity implements IWebBase, IWebJsCallBack {
    /**
     * 要加载的Url
     */
    private String url;
    /**
     * loading
     */
    private IWebLoading mLoading;
    /**
     * WebView对象
     */
    private BridgeWebView mWebView;
    /**
     * Progress
     */
    private ProgressBar mProgressBar;
    /**
     * ChromeClient
     */
    private SelfWebChromeClient mChromeClient;
    /**
     * WebViewClient
     */
    private SelfWebViewClient mWebClient;
    /**
     * 对默认操作的数据 进行解析
     */
    private IWebParseData mParseData;
    private SmsUtil smsUtil;
    private View customView;
    private FullscreenHolder fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        initView();
        initWebView();
        smsUtil = SmsUtil.getInstance();
        smsUtil.registerReceiver(getContext());
    }

    public void setParseData(IWebParseData parseData) {
        mParseData = parseData;
    }

    /**
     * 初始化 WebView
     */
    private void initWebView() {
        mChromeClient = new SelfWebChromeClient(this);
        mWebClient = new SelfWebViewClient(mWebView, this);
        //加载Client
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebClient);
    }

    public void setWebClient(SelfWebViewClient webClient) {
        mWebClient = webClient;
    }

    public void setWebClient(SelfWebChromeClient webClient) {
        mChromeClient = webClient;
    }

    public void callJsHandler(String name, String data, CallBackFunction callBackFunction) {
        mWebView.callHandler(name, data, callBackFunction);
    }

    /**
     * 初始化 View
     */
    private void initView() {
        initActionBar();
        mWebView = findViewById(R.id.base_web_view);
        mProgressBar = findViewById(R.id.pb_loading_status);
        mLoading = new ProgressLoading(mProgressBar);
        mParseData = new WebDataParse();
        initWebHandler();
        initWebSetting(mWebView.getSettings());
    }

    protected void initWebSetting(WebSettings settings) {
        //配置基本的设置
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        //缓存设置为默认：本地 未过期
        settings.setCacheMode(LOAD_DEFAULT);
        //开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        //开启database storeage API功能
        settings.setDatabaseEnabled(true);
        //缓存路径
        //设置AppCaches缓存路径
        settings.setAppCachePath(Strings.isNullOrEmpty(WebConst.PATH.PATH_WEB_CACHE) ? useDefaultCachePath() : WebConst.PATH.PATH_WEB_CACHE);
        //开启AppCaches功能
        settings.setAppCacheEnabled(true);

        //缩放控件
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        //定位
        settings.setGeolocationEnabled(true);

        settings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private String useDefaultCachePath() {
        return getFilesDir().getAbsolutePath() + File.separator + "webcache";
    }

    private void initWebHandler() {
        new DefaultWebHandler(mWebView, this);
        initExtHandler(this);
    }

    protected abstract void initExtHandler(IWebJsCallBack callBack);

    /**
     * 加载Url
     *
     * @param url
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 初始化 ActionBar
     */
    protected void initActionBar() {
        showBack();
        setActionBarTitle(url);
        setRightImage(R.drawable.ic_refresh);
    }

    @Override
    protected void onRightAreaClick() {
        //刷新当前页面
        reloadUrl(null);
    }

    @Override
    public void onClick(ActionWhich which) {
        if (ActionWhich.RIGHT_AREA == which) {
            onRightAreaClick();
        } else if (ActionWhich.LEFT_AREA == which) {
            onLeftAreaClick();
        } else {
            super.onClick(which);
        }
    }

    @Override
    protected void onLeftAreaClick() {
        onBackPressed();
    }

    /**
     * 重新加载刚加载的页面
     */
    protected void reload() {
        reloadUrl(url);
    }

    /**
     * 设置Loading的方式
     *
     * @param loading
     */
    public void setLoading(IWebLoading loading) {
        mLoading = loading;
    }

    /**
     * 右键刷新
     * 默认刷新刚进入时的Url
     *
     * @param url
     */
    private void reloadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mWebView.reload();
        } else {
            mWebView.clearHistory();
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogTool.d("onPageFinished:" + url);
        mLoading.stop();
        checkBack();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogTool.d("onPageStarted:" + url);
        mLoading.start();
    }


    @Override
    public void onPageProgress(WebView view, int newProgress) {
        mLoading.progress(newProgress);
    }

    @Override
    public void onPageReceiveTitle(WebView view, String title) {
        setActionBarTitle(title);
    }

    @Override
    public boolean onPageFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    public boolean onPagePrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return false;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public void onDownLoadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        //默认调用系统下载
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onJsCallBack(String type, String data, CallBackFunction function) {
        switch (type) {
            case DefaultWebHandler.CALLBACK_DEFAULT:
                mParseData.parseData(data, function);
                break;
            default:
        }
    }

    public String getUrl() {
        return url;
    }

    public IWebLoading getLoading() {
        return mLoading;
    }

    public BridgeWebView getWebView() {
        return mWebView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public SelfWebChromeClient getChromeClient() {
        return mChromeClient;
    }

    public SelfWebViewClient getWebClient() {
        return mWebClient;
    }

    public IWebParseData getParseData() {
        return mParseData;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Deprecated
    protected void setRightImg(@DrawableRes int id) {
        setRightImage(id);
    }

    @Deprecated
    protected void showBackArrow() {
        showBack();
    }

    @Deprecated
    protected void showWarningDialog(String msg, MaterialDialog.SingleButtonCallback callback) {
        showWarning(msg, GlobalUtil.convertCallBack(callback));
    }

    @Override
    public boolean onSystemOperate(String url) {
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }
        if (url.startsWith(SystemOperate.MAIL.getScheme())) {
            return onMailTo(url);
        } else if (url.startsWith(SystemOperate.SMS.getScheme())) {
            return onSms(url);
        } else if (url.startsWith(SystemOperate.TEL.getScheme())) {
            return onTel(url);
        }
        return false;
    }

    /**
     * 打电话
     *
     * @param url 格式 tel:10086
     * @return
     */
    protected boolean onTel(final String url) {
        workWithPermissionCheck(Manifest.permission.CALL_PHONE, new SimplePermissionCallback() {
            @Override
            public void onGain() {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse(url);
                intent.setData(data);
                startActivity(intent);
            }
        });
        return true;
    }

    /**
     * 发短信
     *
     * @param url 格式 sms:10086,10010,10000?body=sadadsad阿打算打的实打实大的宣传
     * @return
     */
    protected boolean onSms(String url) {
        final String realUrl = URLDecoder.decode(url);
        workWithPermissionCheck(Manifest.permission.SEND_SMS, new SimplePermissionCallback() {
            @Override
            public void onGain() {
                SmsUtil.getInstance().sendMessages(getContext(), realUrl);
            }
        });
        return true;
    }

    /**
     * 发邮件
     *
     * @param url
     * @return
     */
    protected boolean onMailTo(String url) {
        return false;
    }

    @Override
    protected void onDestroy() {
        smsUtil.unregisterReceiver(getContext());
        super.onDestroy();
    }

    ///=================全屏=======================
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    public void onShowCustomView(View pView, WebChromeClient.CustomViewCallback pCallback) {
        //        if (customView != null) {
        //            pCallback.onCustomViewHidden();
        //            return;
        //        }
        //        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        //        fullscreenContainer = new FullscreenHolder(getContext());
        //        fullscreenContainer.addView(pView, COVER_SCREEN_PARAMS);
        //        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        //        customView = pView;
        ////        setStatusBarVisibility(false);
        //        customViewCallback = pCallback;
        ViewGroup parent = (ViewGroup) mWebView.getParent();
        parent.removeView(mWebView);

        // 设置背景色为黑色
        pView.setBackgroundColor(getResources().getColor(R.color.material_black));
        parent.addView(pView);
        customView = pView;

        setFullScreen();
        hideActionBar();
    }

    private void setFullScreen() {

    }

    //    private void setStatusBarVisibility(boolean visible) {
    //        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
    //        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    //    }

    @Override
    public void onHideCustomView() {
        if (customView == null) {
            return;
        }
        //        setStatusBarVisibility(true);
        //        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        //        decor.removeView(fullscreenContainer);
        //        fullscreenContainer = null;
        //        customView = null;
        //        customViewCallback.onCustomViewHidden();
        ViewGroup parent = (ViewGroup) customView.getParent();
        parent.removeView(customView);
        parent.addView(mWebView);
        customView = null;
        quitFullScreen();
        showActionBar();
    }

    private void quitFullScreen() {

    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    /**
     * 退出
     */
    protected void doExit() {
        showWarningDialog("退出此页面的数据将不会保留，确认退出？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    finish();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 返回上一页面
     */
    protected void doHistory() {
        WebView webView = getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            doExit();
        }
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            onHideCustomView();
            return;
        }
        if (mWebView.canGoBack()) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    //=======================按键事件监听==================
    //    @Override
    //    public boolean onKeyUp(int keyCode, KeyEvent event) {
    //        return super.onKeyUp(keyCode, event);
    //    }
    //
    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        if (onOverrideKeyEvent(keyCode, event)) {
    //            return true;
    //        }
    //        return super.onKeyDown(keyCode, event);
    //    }
    //
    //    protected boolean onOverrideKeyEvent(int keyCode, KeyEvent event) {
    //        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
    //            if (mWebView.canGoBack()) {
    //                mWebView.goBack();
    //                return true;
    //            }
    //        }
    //        return false;
    //    }

    /**
     * 是否拦截返回键事件
     *
     * @param view  WebView
     * @param event event
     * @return
     */
    @Override
    public boolean onOverrideKeyEvent(WebView view, KeyEvent event) {
        //默认拦截返回事件
        LogTool.d("onOverrideKeyEvent:code - " + event.getKeyCode() + "  repeat - " + event.getRepeatCount() + "   action - " + event.getAction());
        return false;
    }

    @Override
    public void onExit() {
        //退出
        doExit();
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    private void checkBack() {
        if (!mWebView.canGoBack()) {
            hideExit();
        } else {
            showExit();
        }
    }


}
