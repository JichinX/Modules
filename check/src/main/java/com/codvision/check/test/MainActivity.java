package com.codvision.check.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codvision.check.R;
import com.codvision.check.impl.WebContainerActivity;
import com.codvision.check.handler.CheckHandler;
import com.google.common.base.Strings;

import me.xujichang.ui.activity.DefaultActionBarActivity;
import me.xujichang.web.WebConst;


/**
 * @author xujichang
 */
public class MainActivity extends DefaultActionBarActivity {
    private EditText mEtUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sdk_main);
        initView();
    }

    public void goToWeb(View view) {
        String url = mEtUrl.getText().toString();
        if (Strings.isNullOrEmpty(url)) {
            url = "file:///android_asset/web/index.html";
        } else {
            //保存
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit().putString("url", url).apply();
        }
        Intent intent = new Intent(this, WebContainerActivity.class);
        intent.putExtra(WebConst.FLAG.WEB_URL, url);
        intent.putExtra(CheckHandler.REQUEST_TOKEN, "测试Token");
        startActivity(intent);
    }

    private void initView() {
        setActionBarTitle("Web测试Demo");
        mEtUrl = findViewById(R.id.et_url);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String cachedUrl = preferences.getString("url", "");
        mEtUrl.setText(cachedUrl);
    }
}
