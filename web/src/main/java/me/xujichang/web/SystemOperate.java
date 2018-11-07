package me.xujichang.web;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/31-下午4:41
 */
public enum SystemOperate {
    /**
     * 短信
     */
    SMS("sms"),
    /**
     * 电话
     */
    TEL("tel"),
    /**
     * 邮件
     */
    MAIL("mailto");

    private String scheme;

    private SystemOperate(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }
}
