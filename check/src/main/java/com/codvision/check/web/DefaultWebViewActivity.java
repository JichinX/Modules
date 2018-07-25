package com.codvision.check.web;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.codvision.check.R;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import org.json.JSONObject;

import me.xujichang.hybirdbase.base.HybirdConst;
import me.xujichang.hybirdbase.module.web.HyBirdWebViewActivity;
import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 14:07.
 */

public class DefaultWebViewActivity extends HyBirdWebViewActivity {

    private boolean isRightIconActionCallback = false;
    private MaterialDialog alertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionbarTitle().setVisibility(View.INVISIBLE);
        String url = getIntent().getStringExtra(HybirdConst.FLAG.WEB_URL);
        loadUrl(url);
    }

    @Override
    protected void initWebSetting(WebSettings settings) {
        settings.supportMultipleWindows();
//
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setAppCacheEnabled(true);

        //允许DOM 显示地图
        settings.setDomStorageEnabled(true);
//        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        //适应宽度
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //设置缩放
        settings.setBuiltInZoomControls(true);
        //隐藏缩放
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        //定位
        settings.setGeolocationEnabled(true);
        settings.setSupportZoom(true);

    }

    @Override
    protected void initExtHandler(IWebJsCallBack callBack) {
        new InformationHandler(getWebView()).addJsCallBack(this);
    }

    @Override
    protected void initActionBar() {
        showBackArrow();
        setRightImg(me.xujichang.hybirdbase.R.drawable.ic_refresh);
    }

    @Override
    protected void setActionBarTitle(String title) {
        TextView actionbarTitle = getActionbarTitle();
        if (TextUtils.isEmpty(title)) {
            actionbarTitle.setVisibility(View.INVISIBLE);
        } else {
            actionbarTitle.setVisibility(View.VISIBLE);
            actionbarTitle.setText(title);
            actionbarTitle.setOnClickListener(this);
        }
    }

    @Override
    public void onError(WebView view, int errorCode, String description, String failingUrl) {
        LogTool.d("onError:" + errorCode + "  " + description + "   " + failingUrl);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        isRightIconActionCallback = false;
        setRightImg(R.drawable.ic_refresh);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogTool.d("onPageFinished:" + url);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
            LogTool.d("onJsAlert:" + message + "  " + url);
            return true;
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onPageReceiveTitle(WebView view, String title) {
        super.onPageReceiveTitle(view, title);
    }

    @Override
    public void onJsCallBack(String type, String data, CallBackFunction function) {
        LogTool.d("---------type:" + type + "    data:" + data);
        switch (type) {
            case InformationHandler.CALLBACK_LOCATION:
                //调用定位
                LocationForWeb.getInstance(function)
                        .withContext(this)
                        .withOptions(data);
                break;
            case InformationHandler.CALLBACK_CAMERA:
                //相机
                PictureForWeb.getInstance()
                        .justCamera(true)
                        .withFunction(function)
                        .withOptions(data)
                        .withContext(this)
                        .execute();
                break;
            case InformationHandler.CALLBACK_PICTURE:
                //选择图片
                PictureForWeb.getInstance()
                        .withFunction(function)
                        .withOptions(data)
                        .withContext(this)
                        .execute();
                break;
            case InformationHandler.CALLBACK_USERID:
                String unionid = "";
                String response = null;
                if (TextUtils.isEmpty(unionid)) {
                    response = DataType.createErrorRespData(WebConst.StatusCode.STATUS_DATA_NULL, "未获取到UserID");
                } else {
                    response = DataType.createRespData(WebConst.StatusCode.STATUS_OK, "成功获取UserID", unionid);
                }
                function.onCallBack(response);
                break;
            case InformationHandler.CALLBACK_UPLOAD:
                //上传文件
                new FilesUploadForWeb().uploadFiles(data, function);
                break;
            case InformationHandler.CALLBACK_UPLOAD_NEW:
                //上传文件
                new FilesUploadForWeb().uploadFilesNew(data, function);
                break;

            case InformationHandler.CALLBACK_SET_RIRGT_ICON:
                if (null == data) {
                    break;
                }
                try {
                    JSONObject object = new JSONObject(data);
                    String url = object.getString("url");
                    if (!Strings.isNullOrEmpty(url)) {
                        getActionbarRightImg().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        Glide.with(this).load(url).into(getActionbarRightImg());
                        isRightIconActionCallback = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;


            case InformationHandler.OPEN_LINK:
                if (null == data) {
                    break;
                }
                if (data.startsWith("http")) {
                    Uri uri = Uri.parse(data);
                    Intent intent = new Intent();
                    intent.setClass(DefaultWebViewActivity.this, DefaultWebViewActivity.class);
                    intent.putExtra(HybirdConst.FLAG.WEB_URL, uri.toString());
                    startActivity(intent);
                } else {
                    Uri uri = Uri.parse(data);
                    Intent intent = new Intent();
                    //将功能Scheme以URI的方式传入data
                    intent.setData(uri);
                    //启动该页面即可
                    startActivity(intent);
                }

                break;
            case InformationHandler.NAV:
                if (null == data) {
                    break;
                }
                double lng = 0;
                double lat = 0;
                String name = "目的地";
                try {
                    JSONObject object = new JSONObject(data);
                    name = object.getString("name");
                    if (Strings.isNullOrEmpty(name)) {
                        name = "目的地";
                    }
                    lng = object.getDouble("lng");
                    lat = object.getDouble("lat");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (0 != lng && 0 != lat) {

                    NavForWeb.getInstance()
                            .withFunction(function)
                            .withDestination(name, lng, lat)
                            .withContext(this)
                            .execute();
                }
                break;
            default:
                super.onJsCallBack(type, data, function);
        }
    }

    @Override
    protected void onLeftAreaClick() {
        finish();
    }

    @Override
    protected void onRightAreaClick() {
        if (isRightIconActionCallback) {
            getWebView().callHandler("onRightIconClick", null, null);
        } else {
            getWebView().reload();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean catched = false;
        catched = PictureForWeb.getInstance().onActivityResult(requestCode, resultCode, data);

        if (!catched) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        WebView webView = getWebView();
        if (webView != null) {
            final ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(webView);
            }
            webView.clearHistory();
            webView.clearCache(true);
            webView.destroy();
        }
        super.onDestroy();
        LogTool.d("onDestroy()");
    }

    @Override
    protected long getActivityExitDuration() {
        return WebConst.EXIT_DURATION;
    }
}
