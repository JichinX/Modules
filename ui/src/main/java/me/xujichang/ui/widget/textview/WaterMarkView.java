package me.xujichang.ui.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/6-下午7:53
 */
public class WaterMarkView extends android.support.v7.widget.AppCompatTextView {
    public WaterMarkView(Context context) {
        super(context);
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaterMarkView(Context context, String waterMark) {
        super(context);
        setText(waterMark);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
