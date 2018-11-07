package me.xujichang.ui.activity.watermark;

import android.view.View;

/**
 * Des:定义水印的统一接口
 *
 * @author xujichang
 * <p>
 * created by 2018/9/10-上午11:03
 */
public interface IWaterMark {
    /**
     * 设置水印文字
     *
     * @param text
     */
    void setMark(String text);

    /**
     * 文字大小
     *
     * @param size
     */
    void setMarkSize(int size);

    /**
     * 文字透明度
     *
     * @param alpha
     */
    void setAlpha(float alpha);

    /**
     * 设置水印倾斜角度
     *
     * @param rotate
     */
    void setRotate(int rotate);

    /**
     * 设置宽度高度
     *
     * @param width
     * @param height
     */
    void setRect(int width, int height);

    /**
     * 设置间隔
     */
    void setDivide(int divideWidth);

    /**
     * 获取水印View
     *
     * @return
     */
    View getView();

    void clear();

    boolean isViewNull();
}
