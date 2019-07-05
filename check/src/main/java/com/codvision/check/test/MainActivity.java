package com.codvision.check.test;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codvision.check.R;
import com.codvision.check.handler.CheckHandler;
import com.google.common.base.Strings;

import me.xujichang.ui.activity.DefaultActionBarActivity;
import me.xujichang.util.system.SystemInfo;
import me.xujichang.util.tool.LogTool;
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
        String[] propertys = {"ro.boot.serialno", "ro.serialno", "gsm.serial"};
        for (String key : propertys) {
            String sn = SystemInfo.getAndroidOsSystemProperties(key);
            LogTool.d("sn:" + sn);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LogTool.d("serial:" + Build.getSerial());
        } else {
            LogTool.d("serial:" + Build.SERIAL);
        }
    }

    public void goToWeb(View view) {
        String url = mEtUrl.getText().toString();
        if (null != url) {
            url = url.trim();
        }
        if (Strings.isNullOrEmpty(url)) {
            url = "file:///android_asset/web/test_index.html";
        } else {
            //保存
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit().putString("url", url).apply();
        }
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebConst.FLAG.WEB_URL, url);
        intent.putExtra(CheckHandler.REQUEST_TOKEN, "测试Token");
        startActivity(intent);
    }

    private void initView() {
        setActionBarTitle("测试");
        mEtUrl = findViewById(R.id.et_url);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String cachedUrl = preferences.getString("url", "");
        mEtUrl.setText(cachedUrl);
    }

    public void goToWeb2(View view) {
        mEtUrl.setText("https://openlayers.org/en/latest/examples/animation.html");
    }

    public void goToWeb1(View view) {
        mEtUrl.setText("http://maptalks.org/examples/en/map/load/");
    }
}
