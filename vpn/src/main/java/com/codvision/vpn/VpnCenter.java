package com.codvision.vpn;

import android.app.Activity;
import android.content.Context;

import com.google.common.base.Strings;
import com.sangfor.ssl.BaseMessage;
import com.sangfor.ssl.IConstants;
import com.sangfor.ssl.OnStatusChangedListener;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuthManager;
import com.sangfor.ssl.StatusChangedReason;
import com.sangfor.ssl.common.ErrorCode;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import me.xujichang.util.tool.LogTool;
import me.xujichang.util.tool.StringTool;

/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 7:38 PM
 */
public class VpnCenter {
    private static final VpnCenter vpnCenter = new VpnCenter();
    private String vpnPort;
    private String IPAddress;
    private IConstants.VPNMode vpnMode;
    private LoginIfo loginIfo;
    private String account;
    private String pwd;
    private int timeOut;
    private OnStatusChangedListener statusChangedListener;
    private IResultListener resultListener;
    private boolean isNoPwd;
    private SangforAuthManager mSFManager;
    private WeakReference<Activity> contextWeakReference;

    public VpnCenter() {

    }

    public String getVpnPort() {
        return vpnPort;
    }

    public void setVpnPort(String vpnPort) {
        this.vpnPort = vpnPort;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public IConstants.VPNMode getVpnMode() {
        return vpnMode;
    }

    public void setVpnMode(IConstants.VPNMode vpnMode) {
        this.vpnMode = vpnMode;
    }

    public LoginIfo getLoginIfo() {
        return loginIfo;
    }

    public void setLoginIfo(LoginIfo loginIfo) {
        this.loginIfo = loginIfo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public WeakReference<Activity> getContextWeakReference() {
        return contextWeakReference;
    }

    public void setContextWeakReference(WeakReference<Activity> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public OnStatusChangedListener getStatusChangedListener() {
        return statusChangedListener;
    }

    public void setStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        this.statusChangedListener = statusChangedListener;
    }

    public IResultListener getResultListener() {
        return resultListener;
    }

    public void setResultListener(IResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public boolean isNoPwd() {
        return isNoPwd;
    }

    public void setNoPwd(boolean noPwd) {
        isNoPwd = noPwd;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(VpnCenter copy) {
        Builder builder = new Builder();
        builder.vpnPort = copy.getVpnPort();
        builder.IPAddress = copy.getIPAddress();
        builder.vpnMode = copy.getVpnMode();
        builder.loginIfo = copy.getLoginIfo();
        builder.account = copy.getAccount();
        builder.pwd = copy.getPwd();
        builder.timeOut = copy.getTimeOut();
        builder.statusChangedListener = copy.getStatusChangedListener();
        builder.resultListener = copy.getResultListener();
        builder.isNoPwd = copy.isNoPwd();
        builder.context = copy.getContextWeakReference().get();
        return builder;
    }

    public void login() {
        login(true);
    }

    public void login(boolean ticketAuth) {
        Activity context = contextWeakReference.get();
        if (null == context) {
            resultListener.onLoginError("Context is already null");
            return;
        }
        initLoginParms();
        //判断是否开启免密，如果免密直接进行一次登录，如果无法免密或免密登录失败，走正常流程
        //允许免密，直接走免密流程
        if (ticketAuth && mSFManager.ticketAuthAvailable(context)) {
            loginTicketAuth(context);
        } else {
            loginWithAccount(context);
        }
    }

    private void loginWithAccount(Activity context) {
        try {
            mSFManager.addStatusChangedListener(statusChangedListener);
            mSFManager.startPasswordAuthLogin(context.getApplication(), context, vpnMode,
                    connectUrl(), account, pwd);
        } catch (SFException e) {
            e.printStackTrace();
            resultListener.onLoginError(StringTool.getErrorMsg(e));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            resultListener.onLoginError(StringTool.getErrorMsg(e));
        }
    }

    private void loginTicketAuth(Activity context) {
        try {
            LogTool.d("VPN Login ticketAuth ---------");
            mSFManager.startTicketAuthLogin(context.getApplication(), context, vpnMode);
        } catch (SFException e) {
            e.printStackTrace();
            resultListener.onLoginError(StringTool.getErrorMsg(e));
        }
    }


    private URL connectUrl() throws MalformedURLException {
        String url = String.format("https://%s:%s", IPAddress, vpnPort);
        return new URL(url);
    }

    private void initLoginParms() {
        // 1.构建SangforAuthManager对象
        mSFManager = SangforAuthManager.getInstance();

        // 2.设置VPN认证结果回调
        try {
            mSFManager.setLoginResultListener(resultListener);
        } catch (SFException e) {
            LogTool.d("VPN Exception:reason[" + e.getMessage() + "]");
            resultListener.onLoginError(StringTool.getErrorMsg(e));
        }

        //3.设置登录超时时间，单位为秒
        mSFManager.setAuthConnectTimeOut(timeOut);
    }

    public void login(SimpleResultListener simpleResultListener) {
        resultListener = simpleResultListener;
        login();
    }

    /**
     * {@code VpnCenter} builder static inner class.
     */
    public static final class Builder {
        private String vpnPort = "443";
        private String IPAddress;
        private IConstants.VPNMode vpnMode = IConstants.VPNMode.EASYAPP;
        private LoginIfo loginIfo;
        private String account;
        private String pwd;
        private int timeOut = 3;
        private OnStatusChangedListener statusChangedListener;
        private IResultListener resultListener;
        private boolean isNoPwd = true;
        private Activity context;

        public Builder() {
        }

        /**
         * Sets the {@code vpnPort} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code vpnPort} to set
         * @return a reference to this Builder
         */
        public Builder withVpnPort(String val) {
            vpnPort = val;
            return this;
        }

        /**
         * Sets the {@code IPAddress} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code IPAddress} to set
         * @return a reference to this Builder
         */
        public Builder withIPAddress(String val) {
            IPAddress = val;
            return this;
        }

        /**
         * Sets the {@code vpnMode} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code vpnMode} to set
         * @return a reference to this Builder
         */
        public Builder withVpnMode(IConstants.VPNMode val) {
            vpnMode = val;
            return this;
        }

        /**
         * Sets the {@code loginIfo} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code loginIfo} to set
         * @return a reference to this Builder
         */
        public Builder withLoginIfo(LoginIfo val) {
            loginIfo = val;
            account = loginIfo.getAccount();
            pwd = loginIfo.getPwd();
            return this;
        }

        /**
         * Sets the {@code account} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code account} to set
         * @return a reference to this Builder
         */
        public Builder withAccount(String val) {
            account = val;
            if (null == loginIfo) {
                loginIfo = new LoginIfo();
            }
            loginIfo.setAccount(account);
            return this;
        }

        /**
         * Sets the {@code pwd} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code pwd} to set
         * @return a reference to this Builder
         */
        public Builder withPwd(String val) {
            pwd = val;
            if (null == loginIfo) {
                loginIfo = new LoginIfo();
            }
            loginIfo.setPwd(account);
            return this;
        }

        /**
         * Sets the {@code timeOut} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code timeOut} to set
         * @return a reference to this Builder
         */
        public Builder withTimeOut(int val) {
            timeOut = val;
            return this;
        }

        /**
         * Sets the {@code statusChangedListener} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code statusChangedListener} to set
         * @return a reference to this Builder
         */
        public Builder withStatusChangedListener(OnStatusChangedListener val) {
            statusChangedListener = val;
            return this;
        }

        /**
         * Sets the {@code resultListener} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code resultListener} to set
         * @return a reference to this Builder
         */
        public Builder withResultListener(IResultListener val) {
            resultListener = val;
            return this;
        }

        /**
         * Sets the {@code isNoPwd} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code isNoPwd} to set
         * @return a reference to this Builder
         */
        public Builder withIsNoPwd(boolean val) {
            isNoPwd = val;
            return this;
        }

        public Builder withContext(Activity val) {
            context = val;
            return this;
        }

        /**
         * Returns a {@code VpnCenter} built from the parameters previously set.
         *
         * @return a {@code VpnCenter} built with parameters of this {@code VpnCenter.Builder}
         */
        public VpnCenter build() {
            return vpnCenter.initWithBuilder(this);
        }
    }

    private VpnCenter initWithBuilder(Builder builder) {
        vpnPort = builder.vpnPort;
        IPAddress = builder.IPAddress;
        vpnMode = builder.vpnMode;
        loginIfo = builder.loginIfo;
        account = builder.account;
        pwd = builder.pwd;
        timeOut = builder.timeOut;
        statusChangedListener = builder.statusChangedListener;
        resultListener = builder.resultListener;
        isNoPwd = builder.isNoPwd;
        contextWeakReference = new WeakReference<>(builder.context);
        if (null == statusChangedListener) {
            statusChangedListener = new SimpleStatusChangedListener();
        }
        if (null == resultListener) {
            resultListener = new SimpleResultListener();
        }
        if (Strings.isNullOrEmpty(IPAddress)) {
            throw new RuntimeException("");
        }
        if (Strings.isNullOrEmpty(account)) {
            throw new RuntimeException("");
        }
        if (Strings.isNullOrEmpty(pwd)) {
            throw new RuntimeException("");
        }
        return vpnCenter;
    }

    public static class SimpleStatusChangedListener implements OnStatusChangedListener {
        @Override
        public void onStatusCallback(IConstants.VPNStatus vpnStatus, StatusChangedReason statusChangedReason) {
            LogTool.d("VPN status changed:status[" + vpnStatus.name() + "] , reason[" + statusChangedReason.getReasonCode() + " " + statusChangedReason.getReasonDes() + "]");
        }
    }

    public static class SimpleResultListener implements IResultListener {
        @Override
        public void onLoginFailed(ErrorCode errorCode, String s) {
            LogTool.d("VPN Login Failed:code[" + errorCode.name() + "] , msg[" + s + "]");
        }

        @Override
        public void onLoginProcess(int i, BaseMessage baseMessage) {
            LogTool.d("VPN Login Process:int [" + i + "] , msg[" + baseMessage.getErrorCode() + "  " + baseMessage.getErrorStr() + "]");
        }

        @Override
        public void onLoginSuccess() {
            LogTool.d("VPN Login Success ---------------");
        }

        @Override
        public void onLoginError(String msg) {
            LogTool.d("VPN Login Error:msg [" + msg + "]");
        }
    }
}
