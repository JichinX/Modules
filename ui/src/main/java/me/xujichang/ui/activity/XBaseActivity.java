package me.xujichang.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.xujichang.ui.R;
import me.xujichang.ui.UIConfig;
import me.xujichang.ui.activity.actionbar.ActionWhich;
import me.xujichang.ui.activity.actionbar.IActionBar;
import me.xujichang.ui.activity.actionbar.IActionClick;
import me.xujichang.ui.activity.dialog.DialogWhich;
import me.xujichang.ui.activity.dialog.IDialog;
import me.xujichang.ui.activity.dialog.IDialogCallBack;
import me.xujichang.ui.activity.loading.ILoading;
import me.xujichang.ui.activity.loading.LoadingStatus;
import me.xujichang.ui.activity.toast.IToast;
import me.xujichang.ui.activity.watermark.DrawableWaterMark;
import me.xujichang.ui.activity.watermark.IWaterMark;
import me.xujichang.ui.promission.IPermission;
import me.xujichang.ui.promission.IPermissionCallBack;
import me.xujichang.ui.utils.GlobalUtil;
import me.xujichang.ui.utils.IExitCallBack;
import me.xujichang.ui.utils.StringUtil;
import me.xujichang.util.simple.interfaces.XOnClickListener;
import me.xujichang.util.tool.LogTool;
import me.xujichang.util.tool.ViewTool;

/**
 * Des:最基础的Activity
 * 包括ActionBar的设置
 * 水印添加
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午7:57
 */
public abstract class XBaseActivity extends AppCompatActivity implements IActivity, IActionClick {
    private ViewStub mVsActionBarContainer;
    private FrameLayout mFlViewRoot;
    private IActionBar iActionBar;
    private ViewStub.OnInflateListener inflateListener;
    private LinearLayout mRoot;
    private IDialog iDialog;
    private ILoading iLoading;
    private ViewTool mViewTool;
    private IPermission iPermission;
    private IToast iToast;
    private IWaterMark iWaterMark;
    //===================================View拦截================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_x_base);
        initConfigurations();
        patchView();
    }

    private void initConfigurations() {
        mViewTool = ViewTool.getInstance(this);
        iActionBar = onObtainActionBar();
        iDialog = obtainDialog();
        iLoading = obtainLoading();
        iPermission = obtainPermission();
        iToast = obtainToast();
    }

    protected abstract IToast obtainToast();

    protected abstract IPermission obtainPermission();

    protected abstract ILoading obtainLoading();

    protected abstract IDialog obtainDialog();

    @Override
    public void setContentView(View view) {
        mFlViewRoot.addView(view);
    }


    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    //===========================水印-=======================

    /**
     * 创建水印
     *
     * @param waterMark
     * @param width
     * @param height
     * @return
     */
    protected IWaterMark onCreateWaterMarkView(String waterMark, int width, int height) {
        IWaterMark mark = new DrawableWaterMark(getContext());
        mark.setRect(width, height);
        mark.setMark(waterMark);
        return mark;
    }

    protected void enableWaterMark(String mark) {
        enableWaterMark(mark, 0.1f, 18, 50, 45);
    }

    protected void enableWaterMark(String mark, float alpha) {
        enableWaterMark(mark, alpha, 18, 50, 45);
    }

    /**
     * 开启水印
     *
     * @param mark
     * @param alpha
     * @param textsize
     */
    protected void enableWaterMark(String mark, float alpha, int textsize, int divide, int rotate) {
        if (Strings.isNullOrEmpty(mark)) {
            mark = "请设置水印文字";
        }

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;
        if (null == iWaterMark) {
            iWaterMark = onCreateWaterMarkView(mark, width, height);
            iWaterMark.setAlpha(alpha);
            iWaterMark.setMarkSize(textsize);
            iWaterMark.setRotate(rotate);
            iWaterMark.setDivide(divide);
        }
        if (!iWaterMark.isViewNull()) {
            return;
        }
        final ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        //可对水印布局进行初始化操作
        View view = iWaterMark.getView();
        rootView.addView(view);
    }

    /**
     * 取消掉水印
     */
    protected void disableWaterMark() {
        if (null != iWaterMark) {
            iWaterMark.clear();
        }
    }

    //=====================================ActionBar========================================

    private void patchView() {
        //取ActionBar
        mRoot = findViewById(R.id.lv_root);
        mVsActionBarContainer = findViewById(R.id.vs_action_bar_container);
        mFlViewRoot = findViewById(R.id.fl_view_root);
        //绘制ActionBar
        inflateListener = new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                paletteColor(inflated);
                iActionBar.onInflate(inflated);
            }
        };
        mVsActionBarContainer.setLayoutResource(iActionBar.getResourceID());
        mVsActionBarContainer.setOnInflateListener(inflateListener);
        mVsActionBarContainer.setVisibility(View.VISIBLE);
        iActionBar.setActionClick(this);
    }

    protected abstract IActionBar onObtainActionBar();

    /**
     * 对StatusBar做处理 默认提取ActionBar的颜色
     */
    private void initStatusBar(int color) {
        Window window = getWindow();
        int version = Build.VERSION.SDK_INT;
        //小于4.4 无法实现
        if (version < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //KeyBoardListener.getInstance(this).attachListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //>=21
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else {
            //>=19 <21
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup systemContent = findViewById(android.R.id.content);

            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            statusBarView.setBackgroundColor(color);

            systemContent.getChildAt(0).setFitsSystemWindows(true);

            systemContent.addView(statusBarView, 0, lp);
        }
    }

    /**
     * 获取状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    private void paletteColor(View view) {
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            initStatusBar(color);
            LogTool.d("Color Drawable");
        } else {
            LogTool.d("Bitmap Drawable");
            BitmapDrawable bitmapDrawable = (BitmapDrawable) background;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    //1.获取活力颜色值
                    Palette.Swatch a = palette.getVibrantSwatch();
                    if (null != a) {
                        initStatusBar(a.getRgb());
                    }
                }
            });
        }
    }

    @Override
    public void showBack() {
        iActionBar.showBack();
    }

    @Override
    public void setActionBarTitle(String text) {
        iActionBar.setTitle(text);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Drawable getDrawableWithSc(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawable(id);
        } else {
            return getResources().getDrawable(id);
        }
    }


    public void setActionBarDrawable(Drawable drawable) {
        iActionBar.setActionBarDrawable(drawable);
        paletteColor(iActionBar.getActionBar());
    }

    @Override
    public void onClick(ActionWhich which) {

    }

    @Override
    public void onClick(ActionWhich which, int flag) {
        if (onClickWithFlag(flag)) {
            return;
        }
        onClick(which);
    }

    protected boolean onClickWithFlag(int flag) {
        return false;
    }

    public void hideActionBar() {
        mVsActionBarContainer.setVisibility(View.GONE);
    }

    public void showActionBar() {
        mVsActionBarContainer.setVisibility(View.VISIBLE);
    }

    public FrameLayout getmFlViewRoot() {
        return mFlViewRoot;
    }

    public ViewGroup getContentContainer() {
        return mFlViewRoot;
    }

    public ViewGroup getBaseRoot() {
        return mRoot;
    }

    public int getActionBarStatus() {
        return mVsActionBarContainer.getVisibility();
    }

    //=======================================Dialog弹窗/loading===========================================
    //Dialog
    public void showWarning(String msg) {
        iDialog.showWaining(msg, null);
        showWarning(msg, null);
    }

    public void showError(String msg) {
        IDialogCallBack callback = new IDialogCallBack() {
            @Override
            public void onClick(Dialog dialog, DialogWhich which) {
                dialog.dismiss();
            }
        };
        showError(msg, callback);
    }

    public void showError(String msg, IDialogCallBack callback) {
        showError(msg, null, callback);
    }

    public void showError(String msg, String neutral, IDialogCallBack
            callback) {
        iDialog.showError(msg, null, callback);
    }

    /**
     * 警告的提示
     */
    public void showWarning(String msg, IDialogCallBack callback) {
        iDialog.showWaining(msg, callback);
    }

    public void showTips(String msg) {
        showTips(msg, null);
    }

    public void showTips(String msg, IDialogCallBack callback) {
        iDialog.showTips(msg, callback);
    }

    private void reShowDialog() {
        iDialog.show();
    }

    public void showDialog(String msg, String title, @DrawableRes int iconRes, String
            natureText, String positiveText, String negativeText, boolean calcel,
                           boolean autoDissmiss, IDialogCallBack callback) {
        iDialog.showDialog(msg, title, iconRes, natureText, positiveText, negativeText, calcel, autoDissmiss, callback);
    }

    /**
     * 创建Dialog
     */
    private void closeDialog() {
        iDialog.close();
    }

    //Loading
    ///有进度条

    public void startProgress() {
        startProgress((String) null);
    }

    public void startProgress(String msg) {
        startProgress(msg, 0);
    }

    public void startProgress(String msg, int progress) {
        startProgress(null, msg, progress);
    }

    public void startProgress(String title, String msg) {
        startProgress(title, msg, 0);
    }

    public void startProgress(String title, String msg, int progress) {
        showProcess(title, LoadingStatus.START, msg, progress);
    }

    public void showProcess(String title, LoadingStatus status, String msg, int progress) {
        iLoading.showProgress(title, status, msg, progress);
    }

    public void stopProgress() {
        startProgress(LoadingStatus.STOP);
    }

    private void startProgress(LoadingStatus status) {
        showProcess(null, status, null, 0);
    }


    public void updateProcess(int process) {
        showProcess(null, LoadingStatus.UPDATE, null, process);
    }

    ///无需进度条

    public void startLoading() {
        startLoading(null);
    }

    public void startLoading(String msg) {
        startLoading(null, msg);
    }

    public void startLoading(String title, String msg) {
        showLoading(title, LoadingStatus.START, msg);
    }

    public void showLoading(String title, LoadingStatus status, String msg) {
        iLoading.showLoading(title, status, msg);
    }

    public void stopLoading() {
        showLoading(null, LoadingStatus.STOP, null);
    }

    //Toast
    protected void showToast(String msg) {
        iToast.showToast(msg);
    }
    //=======================================权限处理===========================================

    /**
     * 执行某一操作的时候，检测权限
     *
     * @param pm
     * @param callBack
     */
    public void workWithPermissionCheck(String pm, IPermissionCallBack callBack) {
        workWithPermissionCheck(Collections.singletonList(pm), callBack);
    }

    public void workWithPermissionCheck(List<String> pms, IPermissionCallBack callBack) {
        if (checkPermission(pms)) {
            if (null != callBack) {
                callBack.onGain();
            }
        } else {
            if (!callBack.onDenied()) {
                showWarning("因为未获得【" + StringUtil.connectStr(pms) + "】权限，不能执行该操作！");
            }
        }
    }

    /**
     * 检测权限 并请求
     *
     * @param pms
     * @param callBack
     */
    public void workWithPermissionRequest(List<String> pms, IPermissionCallBack callBack) {
        if (checkPermission(pms)) {
            if (null != callBack) {
                callBack.onGain();
            }
        } else {
            iPermission.requestPermission(pms, callBack);
        }
    }

    public void requestPermission(List<String> pms) {
        iPermission.requestPermission(pms);
    }

    protected boolean checkPermission(List<String> pms) {
        return iPermission.checkPermission(pms);
    }

    @Deprecated
    protected boolean checkPermission(String pm) {
        return checkPermission(Collections.singletonList(pm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean catched = iPermission.onPermissionsResult(requestCode, permissions, grantResults);
        if (!catched) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static abstract class SimplePermissionCallback implements IPermissionCallBack {
        @Override
        public boolean onDenied() {
            return false;
        }
    }
    //=======================================双击退出===========================================

    @Override
    public void onBackPressed() {
        if (hideKeyboard()) {
            return;
        }
        if (GlobalUtil.isAppBaseActivity()) {
            GlobalUtil.postExit(UIConfig.Time.EXIT_DURATION, new IExitCallBack() {
                @Override
                public void onExit() {
                    onAppExit();
                }

                @Override
                public boolean onTip() {
                    return false;
                }
            });
            return;
        }
        super.onBackPressed();
    }

    public void onAppExit() {

    }

    //=======================================输入法弹出框===========================================

    /**
     * 隐藏输入法
     *
     * @return
     */
    public boolean hideKeyboard() {
        boolean closed = false;
        InputMethodManager methodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        IBinder windowToken;
        View currentFocus = getCurrentFocus();
        if (null != currentFocus) {
            windowToken = currentFocus.getWindowToken();
        } else {
            windowToken = getWindow().getDecorView().getWindowToken();
        }
        closed = methodManager.hideSoftInputFromWindow(windowToken, 0);
        return closed;
    }

    /**
     * @param dialog
     * @return
     */
    public boolean hideDialogKeyboard(MaterialDialog dialog) {
        Context context = GlobalUtil.getCurrentContext();
        if (null == context) {
            return false;
        }
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = dialog.getCurrentFocus();
        IBinder windowToken;
        if (currentFocus != null) {
            windowToken = currentFocus.getWindowToken();
        } else {
            windowToken = dialog.getWindow().getDecorView().getWindowToken();
        }
        if (windowToken != null) {
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
        return true;
    }

    //=======================================防止点击事件抖动=========================================

    /**
     * @param view
     * @param listener
     * @param <T>
     */
    protected <T extends View> void proxyViewClick(T view, XOnClickListener<T> listener) {
        if (null != mViewTool) {
            mViewTool.proxyClickListener(view, listener);
        }
    }

    //==========================================Toast=============================================
    protected void toast(String msg) {
        iToast.showToast(msg);
    }
    //===================================其他方法==================================================

    /**
     * 进入应用对应的设置界面
     */
    protected void toAppSettingInfo() {
        //查看设置
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
    }

    //===================================跳转=====================================================

    /**
     * @param aclass
     */
    protected void toActivity(Class<? extends Activity> aclass) {
        toIntent(new Intent(getContext(), aclass));
    }

    protected void toActivity(Class<? extends Activity> aclass, Map<String, String> params) {
        Intent intent = new Intent(getContext(), aclass);
        if (null != params) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key));
            }
        }
        toIntent(intent);
    }

    protected void toIntent(Intent intent) {
        startActivity(intent);
    }

    //===================================兼容===================================================
    @Deprecated
    protected void showWarningDialog(String msg, MaterialDialog.SingleButtonCallback callback) {
        showWarning(msg, GlobalUtil.convertCallBack(callback));
    }

    @Deprecated
    protected void showWarningDialog(String msg) {
        showWarning(msg);
    }

    @Deprecated
    protected void showToast(String msg, int type) {
        showToast(msg);
    }

    @Deprecated
    protected void showErrorDialog(String msg) {
        showError(msg);
    }

    @Deprecated
    protected String[] checkPermissions(String[] permissions) {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(permissions));
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next())) {
                iterator.remove();
            }
        }
        return arrayList.toArray(new String[]{});
    }

}
