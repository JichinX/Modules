package me.xujichang.modules;

import com.codvision.base.BaseApplication;
import com.codvision.base.BaseConst;
import com.codvision.base.utils.InitUtil;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/7-下午8:53
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseConst.enablePacketService = true;

        InitUtil.initModulesSpeed(Const.Modules.inits, this);
        InitUtil.initModulesLow(Const.Modules.inits, this);
    }
}
