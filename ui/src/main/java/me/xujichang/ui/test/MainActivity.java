package me.xujichang.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.xujichang.ui.R;
import me.xujichang.ui.activity.DefaultActionBarActivity;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午9:20
 */
public class MainActivity extends DefaultActionBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_test);
        initView();
    }

    private void initView() {
        initActionBar();
    }

    private void initActionBar() {
//        showBack();
        setLeftText("返回");
        setActionBarTitle("首页");
        setRightImage(R.drawable.ic_arrow_forward_white);
    }

    public void onWarning(View view) {
        showWarning("警告测试");
    }

    public void onError(View view) {
        showError("出错提示");
    }

    public void onTip(View view) {
        showTips("提示测试");
    }
}
