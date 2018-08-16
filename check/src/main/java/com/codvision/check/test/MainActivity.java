package com.codvision.check.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codvision.check.R;
import com.codvision.check.WebContainerActivity;
import com.codvision.check.handler.CheckHandler;
import com.google.common.base.Strings;

import me.xujichang.hybirdbase.base.HybirdConst;

/**
 * @author xujichang
 */
public class MainActivity extends AppCompatActivity {
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
        }
        Intent intent = new Intent(this, WebContainerActivity.class);
        intent.putExtra(HybirdConst.FLAG.WEB_URL, url);
        intent.putExtra(CheckHandler.REQUEST_TOKEN, "测试Token");
        startActivity(intent);
    }

    private void initView() {
        mEtUrl = findViewById(R.id.et_url);
    }
}
