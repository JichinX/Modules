package com.codvision.check.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.codvision.check.R;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerView;

import me.xujichang.ui.activity.DefaultActionBarActivity;

public class QrCodeActivity extends DefaultActionBarActivity {
    private static final String TAG = QrCodeActivity.class.getSimpleName();

    private ScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        hideActionBar();
        mScannerView = (ScannerView) findViewById(R.id.scanner_view);
        mScannerView.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            @Override
            public void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
                Log.e(TAG, "OnScannerCompletion: " + rawResult.toString());
                Intent intent = new Intent();
                intent.putExtra("qrcode", rawResult.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        mScannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }
}
