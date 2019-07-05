package me.xujichang.ui.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;

import me.xujichang.ui.activity.dialog.DialogWhich;
import me.xujichang.ui.activity.dialog.IDialog;
import me.xujichang.ui.activity.dialog.IDialogCallBack;
import me.xujichang.ui.test.App;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Des:
 * 全局
 *
 * @author xujichang
 * <p>
 * created by 2018/8/25-上午11:49
 */
public class GlobalUtil implements Application.ActivityLifecycleCallbacks {
    private static WeakReference<Activity> currentContext;
    private static volatile long start = 0;
    private Application mApplication;

    public static Context getCurrentContext() {
        if (null == getCurrentActivity()) {
            return Holder.instance.mApplication;
        }
        return getCurrentActivity();
    }

    public static Activity getCurrentActivity() {
        return currentContext.get();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentContext = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentContext = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(instance(application));
    }

    private static GlobalUtil instance(Application vApplication) {
        Holder.instance.mApplication = vApplication;
        return Holder.instance;
    }

    public static Drawable getDrawableWithSc(@DrawableRes int id) {
        if (checkContext()) {
            return currentContext.get().getResources().getDrawable(id);
        }
        return null;
    }

    private static boolean checkContext() {
        if (currentContext.get() == null) {
            return false;
        }
        return true;
    }

    public static String getString(@StringRes int id) {
        if (checkContext()) {
            return currentContext.get().getString(id);
        }
        return null;
    }

    public static void postExit(long exit, IExitCallBack callBack) {
        if (checkContext()) {
            long current = System.currentTimeMillis();
            if (current - start < exit) {
                getCurrentActivity().finish();
                System.gc();
                if (null != callBack) {
                    callBack.onExit();
                }
            } else {
                boolean tip = false;
                if (null != callBack) {
                    tip = callBack.onTip();
                }
                if (!tip) {
                    showToast("再次点击将退出程序");
                }
            }
            start = current;
        }
    }

    private static void showToast(String msg) {
        Toast.makeText(getCurrentContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private static class Holder {
        static GlobalUtil instance = new GlobalUtil();
    }

    public static boolean isAppBaseActivity() {
        String className = getCurrentContext().getClass().getName();
        return className.equals(getAppBaseActivity());
    }

    /**
     * 获取
     *
     * @return
     */
    private static String getAppBaseActivity() {
        ActivityManager activityManager = (ActivityManager) getCurrentContext().getSystemService(ACTIVITY_SERVICE);
        if (null != activityManager) {
            return activityManager.getRunningTasks(1).get(0).baseActivity.getClassName();
        }
        return null;
    }

    private GlobalUtil() {
    }

    public static MaterialDialog.SingleButtonCallback convertCallBack(IDialogCallBack<MaterialDialog> callback) {
        if (null == callback) {
            return null;
        }
        MaterialDialog.SingleButtonCallback singleButtonCallback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    callback.onClick(dialog, DialogWhich.POSITIVE);
                } else if (which == DialogAction.NEGATIVE) {
                    callback.onClick(dialog, DialogWhich.NEGATIVE);
                } else if (which == DialogAction.NEUTRAL) {
                    callback.onClick(dialog, DialogWhich.NEUTRAL);
                }
            }
        };
        return singleButtonCallback;
    }

    public static IDialogCallBack<MaterialDialog> convertCallBack(MaterialDialog.SingleButtonCallback callback) {
        if (null == callback) {
            return null;
        }
        IDialogCallBack<MaterialDialog> iDialogCallBack = new IDialogCallBack<MaterialDialog>() {

            @Override
            public void onClick(MaterialDialog dialog, DialogWhich which) {
                if (which == DialogWhich.POSITIVE) {
                    callback.onClick(dialog, DialogAction.POSITIVE);
                } else if (which == DialogWhich.NEGATIVE) {
                    callback.onClick(dialog, DialogAction.NEGATIVE);
                } else if (which == DialogWhich.NEUTRAL) {
                    callback.onClick(dialog, DialogAction.NEUTRAL);
                }
            }
        };
        return iDialogCallBack;
    }
}
