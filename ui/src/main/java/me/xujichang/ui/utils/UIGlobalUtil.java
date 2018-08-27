package me.xujichang.ui.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.lang.ref.WeakReference;

/**
 * Des:
 * 全局
 *
 * @author xujichang
 * <p>
 * created by 2018/8/25-上午11:49
 */
public class UIGlobalUtil implements Application.ActivityLifecycleCallbacks {
    private static WeakReference<Context> currentContext;

    public static Context getCurrentContext() {
        return currentContext.get();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentContext = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

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
        application.registerActivityLifecycleCallbacks(instance());
    }

    private static UIGlobalUtil instance() {
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

    private static class Holder {
        static UIGlobalUtil instance = new UIGlobalUtil();
    }

    private UIGlobalUtil() {
    }
}
