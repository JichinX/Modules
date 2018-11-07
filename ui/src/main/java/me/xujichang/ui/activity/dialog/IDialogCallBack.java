package me.xujichang.ui.activity.dialog;

import android.app.Dialog;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/25-下午5:06
 */
public interface IDialogCallBack<T extends Dialog> {
    void onClick(T dialog, DialogWhich which);
}
