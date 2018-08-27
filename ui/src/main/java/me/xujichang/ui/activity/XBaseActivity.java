package me.xujichang.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
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

import me.xujichang.ui.R;
import me.xujichang.ui.actionbar.ActionWhich;
import me.xujichang.ui.actionbar.DefaultActionBar;
import me.xujichang.ui.actionbar.IActionBar;
import me.xujichang.ui.actionbar.IActionClick;
import me.xujichang.ui.dialog.DialogWhich;
import me.xujichang.ui.dialog.IDialog;
import me.xujichang.ui.dialog.IDialogCallBack;
import me.xujichang.ui.dialog.MDDialog;
import me.xujichang.ui.utils.UIGlobalUtil;
import me.xujichang.util.tool.LogTool;

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
    //===================================View拦截================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_x_base);
        iActionBar = onObtainActionBar();
        iDialog = obtainDialog();
        patchView();
    }

    protected IDialog obtainDialog() {
        return MDDialog.instance();
    }

    @Override
    public void setContentView(View view) {
        mFlViewRoot.addView(view);
    }


    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
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

    protected IActionBar onObtainActionBar() {
        return new DefaultActionBar();
    }

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

    }

    public void hideActionBar() {
        mVsActionBarContainer.setVisibility(View.GONE);
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
            public void onClick(IDialog dialog, DialogWhich which) {
                dialog.close();
            }
        };
        showError(msg, callback);
    }

    public void showError(String msg, IDialogCallBack callback) {
        //如果有Dialog显示 先关闭
        showError(msg, null, callback);
    }

    public void showError(String msg, String neutral, IDialogCallBack
            callback) {
        iDialog.showError(msg, null, callback);
        //如果有Dialog显示 先关闭
    }

    /**
     * 警告的提示
     */
    public void showWarning(String msg, IDialogCallBack callback) {
        //如果有Dialog显示 先关闭
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
    ///无需进度条
    public void showLoading(String title, LoadingStatus status, String msg) {
        if (status == LoadingStatus.START) {
            closeDialog();
            MaterialDialog.Builder builder = new MaterialDialog
                    .Builder(UIGlobalUtil.getCurrentContext())
                    .content(msg)
                    .progress(true, 100);
            if (!Strings.isNullOrEmpty(title)) {
                builder.title(title);
            }
            reShowDialog();
        } else if (status == LoadingStatus.STOP) {
            closeDialog();
        } else if (status == LoadingStatus.UPDATE) {
            //更新
        }
    }

    public void startLoading(String title, String msg) {
        showLoading(title, LoadingStatus.START, msg);
    }

    public void stopLoading() {
        showLoading(null, LoadingStatus.STOP, null);

    }

    public void startDeterminateLoading() {

    }

    //=======================================权限处理===========================================
    //=======================================双击退出===========================================
    //=======================================输入法弹出框===========================================
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

    public boolean hideDialogKeyboard(MaterialDialog dialog) {
        Context context = UIGlobalUtil.getCurrentContext();
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
    //=======================================防止点击事件抖动===========================================

}
