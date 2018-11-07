package me.xujichang.ui.activity.dialog;

import android.app.Dialog;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Strings;

import me.xujichang.ui.R;
import me.xujichang.ui.utils.GlobalUtil;

import static me.xujichang.ui.utils.GlobalUtil.getString;

/**
 * Des:Dialog 使用MaterialDialog 样式
 * 特点：
 * 仅维护一个Dialog对象
 * 不通的Dialog叠加时 会将信息合并 展示
 *
 * @author xujichang
 * <p>
 * created by 2018/8/25-下午5:13
 */
public class MDDialog implements IDialog<MaterialDialog> {
    private MaterialDialog materialDialog;

    public static IDialog instance() {
        return Holder.instance;
    }

    private static class Holder {
        static MDDialog instance = new MDDialog();
    }

    private MDDialog() {

    }

    @Override
    public void showError(String msg, String neutral, IDialogCallBack callback) {
        showDialog(msg, getString(R.string.str_error), R.drawable.ic_dialog_error, null, getString(R.string.str_ensure), getString(R.string.str_cancel), false, false, callback);
    }

    @Override
    public void showWaining(String msg, IDialogCallBack callBack) {
        showDialog(msg, getString(R.string.str_warning), R.drawable.ic_dialog_warning, null, getString(R.string.str_ensure), null, true, true, callBack);
    }

    @Override
    public void showTips(String msg, IDialogCallBack callBack) {
        showDialog(msg, getString(R.string.str_tips), R.drawable.ic_dialog_tip, null, getString(R.string.str_ensure), null, true, true, callBack);
    }

    @Override
    public void showDialog(String msg, String title, int titleIcon, String neutral, String positive, String negative, boolean cancel, boolean autoDismiss, IDialogCallBack<MaterialDialog> callback) {
        close();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(GlobalUtil.getCurrentContext())
                .title(title)
                .iconRes(titleIcon)
                .content(connectMsg(msg))
                .onAny(GlobalUtil.convertCallBack(callback))
                .autoDismiss(autoDismiss)
                .cancelable(cancel);

        if (!Strings.isNullOrEmpty(positive)) {
            builder.positiveText(positive);
        }
        if (!Strings.isNullOrEmpty(neutral)) {
            builder.neutralText(neutral);
        }
        if (!Strings.isNullOrEmpty(negative)) {
            builder.negativeText(negative);
        }
        materialDialog = builder.build();
        show();
    }

    /**
     * 链接
     *
     * @param msg
     * @return
     */
    private String connectMsg(String msg) {
        return msg;
    }

    @Override
    public void close() {
        if (null != materialDialog) {
            if (materialDialog.isShowing()) {
                saveInfo();
            }
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

    /**
     * 保存现有Dialog内的数据
     */
    private void saveInfo() {

    }

    @Override
    public void show() {
        if (null != materialDialog) {
            materialDialog.show();
        }
    }

    @Override
    public Dialog getDialog() {
        return materialDialog;
    }
}
