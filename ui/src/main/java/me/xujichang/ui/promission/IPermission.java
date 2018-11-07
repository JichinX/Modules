package me.xujichang.ui.promission;


import java.util.List;

/**
 * Des:对权限的处理,定义基本方法，子类需遵从的规范
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-上午10:01
 */
public interface IPermission {
    int REQUEST_PERMISSION = 22;

    /**
     * 检测某一权限 是否被允许
     *
     * @param pm
     * @return
     */
    boolean checkPermission(String pm);

    /**
     * 检测某一权限 是否被允许
     *
     * @param pms
     * @return 每个权限对应的结果
     */
    boolean checkPermission(List<String> pms);

    List<Integer> checkPermissionWithResult(List<String> pms);

    /**
     * 请求权限
     *
     * @param pm
     */
    void requestPermission(String pm);

    /**
     * 请求权限
     *
     * @param pm
     */
    void requestPermission(List<String> pm);

    boolean onPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void requestPermission(List<String> pms, IPermissionCallBack callBack);
}
