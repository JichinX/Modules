package com.codvision.check.fun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codvision.check.R;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.util.ArrayList;

import me.xujichang.util.thirdparty.AppUtils;
import me.xujichang.util.tool.BaiduTransform;
import me.xujichang.util.tool.Transform;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/27 15:54.
 */

public class NavForWeb {
    private static NavForWeb instance;
    private CallBackFunction sFunction;
    private Context mContext;
    private double lng;
    private double lat;
    private String destination;

    public static NavForWeb getInstance() {
        if (null == instance) {
            instance = ClassHolder.instance;
        }
        return instance;
    }

    private NavForWeb() {

    }

    public NavForWeb withDestination(String destination, double lng, double lat) {
        this.destination = destination;
        this.lng = lng;
        this.lat = lat;
        return instance;
    }

    public NavForWeb withContext(Context context) {
        mContext = context;
        return instance;
    }

    public void execute() {
        if (null == mContext) {
            throw new RuntimeException("the Context is null");
        }
        final Activity activity = (Activity) mContext;

        final ArrayList<String> items = new ArrayList<>();
        if (AppUtils.isInstallApp("com.baidu.BaiduMap")) {
            items.add("百度地图");
        }
        if (AppUtils.isInstallApp("com.autonavi.minimap")) {
            items.add("高德地图");
        }

        if (items.size() == 0) {
            Toast.makeText(mContext, "请先安装导航软件", Toast.LENGTH_LONG).show();
            return;
        }

        new MaterialDialog.Builder(mContext)
                .title("导航软件")
                .items(items)
                .listSelector(R.color.material_grey_100)//列表的背景颜色
                .autoDismiss(true)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position,
                                            CharSequence text) {
                        if ("百度地图".equals(items.get(position))) {
                            double[] mars = Transform.WGS842Mars(lat, lng);
                            double[] baidu = BaiduTransform.TransMars2Baidu(mars[1], mars[0]);
                            Intent i1 = new Intent();
                            i1.setData(Uri.parse(String.format("baidumap://map/direction?destination=name:%s|latlng:%f,%f&sy=0", destination, baidu[1], baidu[0])));
                            activity.startActivity(i1);
                        } else {
                            Intent i1 = new Intent();
                            i1.setData(Uri.parse("androidamap://route?sourceApplication=amap&dlat=" + lat + "&dlon=" + lng + "&dname=" + destination + "&dev=1&t=0"));
                            activity.startActivity(i1);
                        }
                    }
                })
                .show();
    }

    public NavForWeb withFunction(CallBackFunction function) {
        sFunction = function;
        return instance;
    }


    private static class ClassHolder {
        public static NavForWeb instance = new NavForWeb();
    }
}
