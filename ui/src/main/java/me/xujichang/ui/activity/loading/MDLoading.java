package me.xujichang.ui.activity.loading;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Strings;

import me.xujichang.ui.utils.GlobalUtil;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/27-上午11:34
 */
public class MDLoading implements ILoading {
    private MaterialDialog loading;
    private static int loadingNum = 0;
    private String currentTitle, currentMsg;
    private int currentProgress = 0;
    private LoadingType currentType = LoadingType.LOADING;

    public static MDLoading instance() {
        return Holder.loading;
    }

    private MDLoading() {

    }

    @Override
    public void showProgress(String title, LoadingStatus status, String msg, int progress) {
        if (status != LoadingStatus.UPDATE) {
            currentTitle = title;
            currentMsg = msg;
        }
        currentProgress = progress;
        if (currentType == LoadingType.LOADING) {
            close();
        }
        currentType = LoadingType.PROGRESS;
        if (status == LoadingStatus.START) {
            startLoading();
        } else if (status == LoadingStatus.STOP) {
            stopLoading();
        } else if (status == LoadingStatus.UPDATE) {
            updateProgress();
        }
    }

    private void updateProgress() {
        if (!Strings.isNullOrEmpty(currentTitle)) {
            loading.setTitle(currentTitle);
        }
        if (!Strings.isNullOrEmpty(currentMsg)) {
            loading.setContent(currentMsg);
        }
        if (currentType == LoadingType.PROGRESS) {
            loading.setProgress(currentProgress);
        }
    }

    private void stopLoading() {
        if (null == loading) {
            return;
        }
        loadingNum--;
        if (loadingNum > 0) {
            return;
        }
        close();
    }

    private void close() {
        loadingNum = 0;
        if (null != loading) {
            loading.dismiss();
            loading = null;
        }
    }

    /**
     * 开始Loading
     */
    private void startLoading() {
        //loading不为空的时候,更新内容
        if (null != loading) {
            updateProgress();
            loadingNum++;
            return;
        }
        createDialog();
    }

    private void createDialog() {
        MaterialDialog.Builder builder =
                new MaterialDialog
                        .Builder(GlobalUtil.getCurrentContext());
        if (!Strings.isNullOrEmpty(currentTitle)) {
            builder.title(currentTitle);
        }
        if (!Strings.isNullOrEmpty(currentMsg)) {
            builder.content(currentMsg);
        }
        builder.progress(currentType == LoadingType.LOADING, 100);

        loading = builder
                .cancelable(false)
                .autoDismiss(false)
                .build();
        show();
    }

    private void show() {
        if (null != loading) {
            loading.show();
        }
    }

    @Override
    public void showLoading(String title, LoadingStatus status, String msg) {
        currentTitle = title;
        currentMsg = msg;
        if (currentType == LoadingType.PROGRESS) {
            close();
        }
        currentType = LoadingType.LOADING;
        if (status == LoadingStatus.START) {
            startLoading();
        } else if (status == LoadingStatus.STOP) {
            stopLoading();
        }
    }

    private static class Holder {
        private static MDLoading loading = new MDLoading();
    }
}
