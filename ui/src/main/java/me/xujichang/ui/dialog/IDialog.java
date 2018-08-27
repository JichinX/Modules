package me.xujichang.ui.dialog;

import android.app.Dialog;
import android.support.annotation.DrawableRes;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/25-下午4:29
 */
public interface IDialog {
    void showError(String msg, String neutral, IDialogCallBack callback);

    void showWaining(String msg, IDialogCallBack callBack);

    void showTips(String msg, IDialogCallBack callBack);

    void showDialog(String msg, String title, @DrawableRes int titleIcon, String neutral, String negative, String positive, boolean cancel, boolean autoDismiss, IDialogCallBack callback);

    void close();

    void show();

    Dialog getDialog();
}
