package me.xujichang.basic;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/30 - 11:02 AM
 */
@Deprecated
public class BaseConst {
    /**
     * 默认Token key
     */
    public static volatile String TOKEN_KEY = "token";
    /**
     * 默认不启用 心跳Service
     */
    public static volatile boolean enablePacketService = false;
    /**
     * 默认 module 数量，用来初始化心跳Service中的线程池
     */
    public static volatile int MODULE_SIZE = 5;
    /**
     * 默认 心跳Service的间隔
     */
    public static volatile int PACKET_DURATION = 60 * 1000;

}
