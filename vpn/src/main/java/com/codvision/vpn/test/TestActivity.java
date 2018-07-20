package com.codvision.vpn.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.codvision.vpn.R;
import com.codvision.vpn.VpnCenter;
import com.sangfor.ssl.BaseMessage;
import com.sangfor.ssl.IConstants;
import com.sangfor.ssl.OnStatusChangedListener;
import com.sangfor.ssl.StatusChangedReason;
import com.sangfor.ssl.common.ErrorCode;

/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 10:03 PM
 */
public class TestActivity extends AppCompatActivity implements OnStatusChangedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new VpnCenter.Builder()
                .withContext(this)
                .withAccount(Const.VPN_USER_NAME)
                .withPwd(Const.VPN_USER_PWD)
                .withIPAddress(Const.VPN_HOST)
                .withStatusChangedListener(this)
                .build()
                .login(new VpnCenter.SimpleResultListener() {
                    @Override
                    public void onLoginFailed(ErrorCode errorCode, String s) {
                        super.onLoginFailed(errorCode, s);
                    }

                    @Override
                    public void onLoginProcess(int i, BaseMessage baseMessage) {
                        super.onLoginProcess(i, baseMessage);
                    }

                    @Override
                    public void onLoginSuccess() {
                        super.onLoginSuccess();
                    }
                });
    }

    @Override
    public void onStatusCallback(IConstants.VPNStatus vpnStatus, StatusChangedReason statusChangedReason) {

    }
}
