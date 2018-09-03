package me.xujichang.web.base;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import java.net.URL;
import java.net.URLDecoder;

import me.xujichang.ui.activity.DefaultActionBarActivity;
import me.xujichang.ui.activity.actionbar.ActionWhich;
import me.xujichang.ui.utils.GlobalUtil;
import me.xujichang.util.tool.LogTool;
import me.xujichang.web.R;
import me.xujichang.web.SystemOperate;
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

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/26 10:21.
 */

public abstract class BaseWebViewActivity extends DefaultActionBarActivity implements IWebBase, IWebJsCallBack {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        initView();
        initWebView();
        SmsUtil.getInstance().registerReceiver(getContext());
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

    protected void onLeftAreaClick() {

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
        mLoading.stop();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mLoading.start();
    }

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
        LogTool.d("onOverrideKeyEvent:" + event.getKeyCode());
        return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onOverrideKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onOverrideKeyEvent(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return false;
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
        SmsUtil.getInstance().unregisterReceiver(getContext());
        super.onDestroy();
    }
}
