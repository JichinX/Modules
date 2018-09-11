package me.xujichang.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/6-下午8:10
 */
public class BitmapUtil {
    /**
     * @param gContext 上下文
     * @param gText    水印文字
     * @param width    显示区域宽度
     * @param height   显示区域高度
     * @param alpha    透明度
     * @param divide   间隔大小
     * @param size     文字大小
     * @param rotate   旋转角度
     * @return
     */
    public static Bitmap getMarkTextBitmap(Context gContext, String gText, int width, int height, float alpha, int divide, int size, int rotate) {
        float textSize;
        float inter;
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, gContext.getResources().getDisplayMetrics());
        inter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, divide, gContext.getResources().getDisplayMetrics());

        int sideLength;
        if (width > height) {
            sideLength = (int) Math.sqrt(2 * (width * width));
        } else {
            sideLength = (int) Math.sqrt(2 * (height * height));
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rect = new Rect();
        paint.setTextSize(textSize);
        //获取文字长度和宽度
        paint.getTextBounds(gText, 0, gText.length(), rect);

        int strwid = rect.width();
        int strhei = rect.height();

        Bitmap markBitmap = null;
        try {
            markBitmap = Bitmap.createBitmap(sideLength, sideLength, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(markBitmap);
            //创建透明画布
            canvas.drawColor(Color.TRANSPARENT);

            paint.setColor(Color.BLACK);
            paint.setAlpha((int) (alpha * 255f));
            // 获取跟清晰的图像采样
            paint.setDither(true);
            paint.setFilterBitmap(true);

            //先平移，再旋转才不会有空白，使整个图片充满
            if (width > height) {
                canvas.translate(width - sideLength - inter, sideLength - width + inter);
            } else {
                canvas.translate(height - sideLength - inter, sideLength - height + inter);
            }

            //将该文字图片逆时针方向倾斜45度
            canvas.rotate(-rotate);

            for (int i = 0; i <= sideLength; ) {
                int count = 0;
                for (int j = 0; j <= sideLength; count++) {
                    if (count % 2 == 0) {
                        canvas.drawText(gText, i, j, paint);
                    } else {
                        //偶数行进行错开
                        canvas.drawText(gText, i + strwid / 2, j, paint);
                    }
                    j = (int) (j + inter + strhei);
                }
                i = (int) (i + strwid + inter);
            }
            canvas.save(Canvas.ALL_SAVE_FLAG);
        } catch (OutOfMemoryError e) {
            if (markBitmap != null && !markBitmap.isRecycled()) {
                markBitmap.recycle();
                markBitmap = null;
            }
        }

        return markBitmap;
    }

    /**
     * 获得文字水印的图片
     *
     * @param width
     * @param height
     * @return
     */
    public static Drawable getMarkTextBitmapDrawable(Context gContext, String gText, int width, int height, float alpha, int divide, int size, int rotate) {
        Bitmap bitmap = getMarkTextBitmap(gContext, gText, width, height, alpha, divide, size, rotate);
        if (bitmap != null) {
            BitmapDrawable drawable = new BitmapDrawable(gContext.getResources(), bitmap);
            drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            drawable.setDither(true);
            return drawable;
        }
        return null;
    }

    /**
     * 获得文字水印的图片
     *
     * @param width
     * @param height
     * @return
     */
    public static Drawable getMarkTextBitmapDrawable(Context gContext, String gText, int width, int height) {
        return getMarkTextBitmapDrawable(gContext, gText, width, height, 0.1f, 50, 18, 45);
    }

    public static Drawable getMarkTextBitmapDrawable(Context gContext, String gText, int width, int height, float alpha) {
        return getMarkTextBitmapDrawable(gContext, gText, width, height, alpha, 50, 18, 45);
    }
}
