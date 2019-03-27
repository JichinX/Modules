package me.xujichang.web.client;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.google.common.base.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import me.xujichang.util.tool.LogTool;
import me.xujichang.web.interfaces.IWebBase;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/26 10:32.
 */

public class SelfWebViewClient extends BridgeWebViewClient {
    public static final String NATIVE_IMAGE = "NativeImage";
    public static final String NATIVE_AUDIO = "NativeAudio";
    public static final String NATIVE_FILE  = "NativeFile";
    public static final String NATIVE_VIDEO = "NativeVideo";


    private IWebBase mWebBase;

    public SelfWebViewClient(BridgeWebView webView, IWebBase base) {
        super(webView);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                mWebBase.onDownLoadStart(url, userAgent, contentDisposition, mimetype, contentLength);
            }
        });
        mWebBase = base;
    }

    //    /**
    //     * 必须调用 super,因为父类有额外操作{@link super#shouldOverrideUrlLoading(WebView, String)}
    //     *
    //     * @param view
    //     * @param url
    //     * @return
    //     */
    //    @Override
    //    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //        LogTool.d("shouldOverrideUrlLoading:" + url);
    //        //判断重定向的方式一
    ////        WebView.HitTestResult hitTestResult = view.getHitTestResult();
    ////        if (hitTestResult == null) {
    ////            return false;
    ////        }
    ////        if (hitTestResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
    ////            return false;
    ////        }
    //        view.loadUrl(url);
    ////        return mWebBase.onSystemOperate(url) || super.shouldOverrideUrlLoading(view, url);
    //        return true;
    //    }


    @Override
    protected boolean onCustomShouldOverrideUrlLoading(String url) {
        getWebView().loadUrl(url);
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        isHintResultUseful(view);
        return super.shouldOverrideUrlLoading(view, request);
    }

    private boolean isHintResultUseful(WebView pWebView) {
        WebView.HitTestResult hitTestResult = pWebView.getHitTestResult();
        if (null != hitTestResult) {
            LogTool.d("hitTestResult type - " + hitTestResult.getType() + "  extra - " + hitTestResult.getExtra());
            if (Strings.isNullOrEmpty(hitTestResult.getExtra())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.getSettings().setBlockNetworkImage(true);
        mWebBase.onPageStarted(view, url, favicon);
    }

    //    /**
    //     * 必须调用 super,因为父类有额外操作{@link super#onPageFinished(WebView, String)}
    //     *
    //     * @param view
    //     * @param url
    //     */
    //    @Override
    //    public void onPageFinished(WebView view, String url) {
    //        super.onPageFinished(view, url);
    //
    //    }
    @Override
    protected void onCustomPageFinishd(WebView view, String url) {
        view.getSettings().setBlockNetworkImage(false);
        mWebBase.onPageFinished(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = getResource(url);
        if (null == response) {
            return super.shouldInterceptRequest(view, url);
        } else {
            return response;
        }
    }

    private WebResourceResponse getResource(String url) {
        Uri uri = Uri.parse(url);
        return getResource(uri);
    }

    private WebResourceResponse getResource(Uri uri) {
        String scheme = uri.getScheme();
        if (null == scheme) {
            return null;
        }
        scheme = scheme.toLowerCase();

        if (scheme.startsWith("file") || scheme.startsWith(NATIVE_IMAGE.toLowerCase()) || scheme.startsWith(NATIVE_VIDEO.toLowerCase())) {
            try {
                LogTool.d("请求本地资源:" + scheme);
                File file = new File(uri.getPath());
                if (!file.exists()) {
                    return null;
                }
                String mimetype = getMimeType(file);
                LogTool.d("获取到的mimetype:" + mimetype);
                FileInputStream inputStream = new FileInputStream(file);
                return new WebResourceResponse(mimetype, "UTF-8", inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        //加载出错 errorCode 错误码 description 描述 failingUrl 加载错误的Url
        mWebBase.onError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //加载SSL出错 是否继续加载
        LogTool.d("ssl:" + error.getUrl());
        handler.proceed();// 接受所有网站的证书
        //        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        //拦截 按键事件
        return mWebBase.onOverrideKeyEvent(view, event);
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if ("".equals(fileName) || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        LogTool.d("suffix:" + suffix);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!Strings.isNullOrEmpty(type)) {
            LogTool.d("MimeTypeMap  getType:" + type);
            return type;
        }
        return "file/*";
    }


}
