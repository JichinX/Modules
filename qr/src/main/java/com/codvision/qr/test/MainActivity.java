package com.codvision.qr.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codvision.qr.R;
import com.google.zxing.client.android.CaptureActivity;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/13-下午9:31
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_main);
    }

    public void goCapture(View view) {
        startActivity(new Intent(this, CaptureActivity.class));
    }
}
