package me.xujichang.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Strings;

import me.xujichang.ui.R;
import me.xujichang.util.simple.interfaces.XOnClickListener;

/**
 * Des:
 * 在逻辑基础上，加入View，暴露登录接口
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:20
 */
public abstract class SimpleLoginActivity<V extends ILoginView, P extends BasicLoginPresenter<V>> extends BasicLoginActivity<V, P> {
    protected TextInputLayout mEtName;
    protected TextInputLayout mEtPwd;
    /**
     * 登录
     */
    protected Button mBtnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_login_simple);
        initView();
        initData();
    }


    private void initView() {
        mEtName = findViewById(R.id.et_name);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnLogin = findViewById(R.id.btn_login);

        proxyViewClick(mBtnLogin, new XOnClickListener<Button>() {
            @Override
            public void onClick(Button view) {
                String account = mEtName.getEditText().getText().toString();
                String pwd = mEtPwd.getEditText().getText().toString();
                if (Strings.isNullOrEmpty(account)) {
                    onDataNull(mEtName);
                    return;
                }
                if (Strings.isNullOrEmpty(pwd)) {
                    onDataNull(mEtPwd);
                    return;
                }
                login(account, pwd);
            }
        });
    }

    /**
     * 参数为空的提示
     *
     * @param mEtName
     */
    protected void onDataNull(TextInputLayout mEtName) {
        mEtName.setError("不能为空");
    }

    protected abstract void initData();
}
