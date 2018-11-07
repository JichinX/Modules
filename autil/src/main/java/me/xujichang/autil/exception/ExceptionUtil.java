package me.xujichang.autil.exception;

import me.xujichang.util.tool.StringTool;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午10:31
 */
public class ExceptionUtil {
    /**
     * 获取错误的打印信息
     *
     * @param e
     * @return
     */
    public static String getErrorMsg(Throwable e) {

        return StringTool.getErrorMsg(e);
    }
}
