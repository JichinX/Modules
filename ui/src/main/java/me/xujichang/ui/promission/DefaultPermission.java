package me.xujichang.ui.promission;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xujichang.ui.utils.GlobalUtil;

/**
 * Des:默认的权限管理
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-上午10:58
 */
public class DefaultPermission implements IPermission {
    private IPermissionCallBack callBack;

    @Override
    public boolean checkPermission(String pm) {
        return checkPermission(Collections.singletonList(pm));
    }

    @Override
    public boolean checkPermission(@NonNull List<String> pms) {

        return !checkPermissionWithResult(pms).contains(PermissionChecker.PERMISSION_DENIED);
    }

    @Override
    public List<Integer> checkPermissionWithResult(List<String> pms) {
        List<Integer> results = new ArrayList<>(pms.size());
        for (String pm : pms) {
            results.add(ContextCompat.checkSelfPermission(GlobalUtil.getCurrentContext(), pm));
        }
        return results;
    }

    @Override
    public void requestPermission(String pm) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (GlobalUtil.getCurrentContext().shouldShowRequestPermissionRationale(pm)) {
//
//            }
//        }
        requestPermission(Collections.singletonList(pm));
    }

    @Override
    public void requestPermission(List<String> pm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strings = new String[pm.size()];
            GlobalUtil.getCurrentActivity().requestPermissions(pm.toArray(strings), REQUEST_PERMISSION);
        }
    }

    @Override
    public boolean onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION || callBack == null) {
            return false;
        }
        int deniedIndex = Ints.asList(grantResults).indexOf(PermissionChecker.PERMISSION_DENIED);
        if (deniedIndex == -1) {
            callBack.onGain();
        } else {
            requestPermission(permissions[deniedIndex]);
        }
        return true;
    }

    @Override
    public void requestPermission(List<String> pms, IPermissionCallBack callBack) {
        this.callBack = callBack;
        requestPermission(pms);
    }

    public static IPermission instance() {
        return Holder.instance;
    }

    private static class Holder {
        private static DefaultPermission instance = new DefaultPermission();
    }

    private DefaultPermission() {
    }
}
