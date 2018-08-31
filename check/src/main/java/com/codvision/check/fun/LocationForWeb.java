package com.codvision.check.fun;

import android.content.Context;
import android.util.Log;

import com.codvision.check.CheckInit;
import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import me.xujichang.util.tool.LocationTool;
import me.xujichang.web.WebConst;
import me.xujichang.web.bean.Location;

/**
 * Des:获取位置信息
 *
 * @author xjc
 * Created on 2017/11/27 16:51.
 */

public class LocationForWeb {
    private static LocationForWeb instance;
    private static CallBackFunction sFunction;
    private Context mContext;

    private LocationForWeb() {

    }

    public static LocationForWeb getInstance(CallBackFunction function) {
        if (null == instance) {
            instance = ClassHolder.instance;
        }
        sFunction = function;
        return instance;
    }

    public void withOptions(String data) {
        if (!Boolean.valueOf(data)) {
            //不需要堵塞
            Location sLocation = new Location();
            sLocation.init(CheckInit.location);
            sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "获取位置信息", sLocation));
            return;
        }
        LocationTool
                .getInstance()
                .startGetLocation(mContext, new LocationTool.SimpleLocalizationListener() {
                            @Override
                            public void onGpsLocation(android.location.Location location) {
                                if (null == location) {
                                    return;
                                }
                                Location sLocation = new Location();
                                sLocation.init(location);
                                Log.d("111", "onGpsLocation " + location.getLongitude() + "," + location.getLatitude());
                                sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "成功获取位置信息", sLocation));
                            }
                        }
                );
    }

    public LocationForWeb withContext(Context context) {
        mContext = context;
        return this;
    }

    private static class ClassHolder {
        private static LocationForWeb instance = new LocationForWeb();
    }

    public interface LocationRequest {
        void onGetLocation(Location location);
    }
}
