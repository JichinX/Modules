package me.xujichang.ui.activity.watermark;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import me.xujichang.ui.utils.BitmapUtil;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/10-下午2:11
 */
public class DrawableWaterMark implements IWaterMark {
    private Context context;
    private String waterMark = "未设置水印文字";
    private int width;
    private int height;
    private int textSize = 18;
    private float textAlpha = 0.1f;
    private int rotate = 45;
    private View markView;
    private int divide = 50;

    public DrawableWaterMark(Context context) {
        this.context = context;
    }

    @Override
    public void setMark(String text) {
        waterMark = text;
    }

    @Override
    public void setMarkSize(int size) {
        textSize = size;
    }

    @Override
    public void setAlpha(float alpha) {
        textAlpha = alpha;
    }

    @Override
    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    @Override
    public void setRect(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void setDivide(int divideWidth) {
        divide = divideWidth;
    }

    @Override
    public View getView() {
        if (markView != null) {
            return markView;
        }
        markView = new View(context);
        markView.setBackground(BitmapUtil.getMarkTextBitmapDrawable(context, waterMark, width, height, textAlpha, divide, textSize, rotate));
        return markView;
    }

    @Override
    public void clear() {
        if (null == markView) {
            return;
        }
        ViewGroup parent = (ViewGroup) markView.getParent();
        parent.removeView(markView);
        markView = null;
    }

    @Override
    public boolean isViewNull() {
        return markView == null;
    }
}
