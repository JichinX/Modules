package me.xujichang.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import me.xujichang.ui.R;
import me.xujichang.util.thirdparty.ScreenUtils;

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
            canvas.save();
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

    /**
     * 添加日期水印
     *
     * @param path
     * @param text
     * @param loc
     */
    public static void addDateWaterMask(String path, String text, WaterLoc loc) {
        //转为Bitmap
        Bitmap src = BitmapFactory.decodeFile(path);
        //合成一体
        Bitmap result = createWaterMask(src, text, loc);
        //重新生成图片文件
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedOutputStream os;
            os = new BufferedOutputStream(new FileOutputStream(file));
            result.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (!src.isRecycled()) {
                src.recycle();
            }
            if (!result.isRecycled()) {
                result.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param src
     * @param mask
     * @param loc
     * @return
     */
    private static Bitmap createWaterMask(Bitmap src, String mask, WaterLoc loc) {
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了

        // 创建一个新的和src宽度高度一样的位图
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入src
        cv.drawBitmap(src, 0, 0, null);
        drawText(cv, loc, mask, w, h);
        //TODO 测试
//        drawText(cv, WaterLoc.CENTER, mask, w, h);
//        drawText(cv, WaterLoc.CENTER_TOP, mask, w, h);
//        drawText(cv, WaterLoc.CENTER_BOTTOM, mask, w, h);
//        drawText(cv, WaterLoc.LEFT_BOTTOM, mask, w, h);
//        drawText(cv, WaterLoc.LEFT_CENTER, mask, w, h);
//        drawText(cv, WaterLoc.RIGHT_BOTTOM, mask, w, h);
//        drawText(cv, WaterLoc.RIGHT_CENTER, mask, w, h);
//        drawText(cv, WaterLoc.RIGHT_TOP, mask, w, h);
        cv.save();// 保存
        cv.restore();// 存储
        return newb;
    }

    private static void drawText(Canvas cv, WaterLoc loc, String mask, int w, int h) {
        //加入文字

        float textsize = (w / 30f + h / 40f) / 2;
        Typeface font = Typeface.create("宋体", Typeface.BOLD);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(font);
        textPaint.setTextSize(textsize);
        Rect bounds = new Rect();
        textPaint.getTextBounds(mask, 0, mask.length(), bounds);

        int fontX = 10;
        int fontY = 10 + bounds.height();
        switch (loc) {
            case CENTER:
                fontX = (w - bounds.width()) / 2;
                fontY = (h + bounds.height()) / 2;
                break;
            case RIGHT_TOP:
                fontX = w - bounds.width() - 10;
                fontY = bounds.height() + 10;
                break;
            case CENTER_TOP:
                fontX = (w - bounds.width()) / 2;
                fontY = bounds.height() + 10;
                break;
            case LEFT_BOTTOM:
                fontX = 10;
                fontY = h - 10;
                break;
            case LEFT_CENTER:
                fontX = 10;
                fontY = (h + bounds.height()) / 2;
                break;
            case RIGHT_BOTTOM:
                fontX = w - bounds.width() - 10;
                fontY = h - 10;
                break;
            case RIGHT_CENTER:
                fontX = w - bounds.width() - 10;
                fontY = (h + bounds.height()) / 2;
                break;
            case CENTER_BOTTOM:
                fontX = (w - bounds.width()) / 2;
                fontY = h - 10;
                break;
            case LEFT_TOP:
            default:
        }
        cv.drawText(mask, fontX, fontY, textPaint);
    }

    public static Bitmap waterMask(Bitmap src, Bitmap watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Log.i("WaterMask", "原图宽: " + w);
        Log.i("WaterMask", "原图高: " + h);
        // 设置原图想要的大小
        float newWidth = ScreenUtils.getScreenWidth();
        float newHeight = h * (newWidth / w);
        // 计算缩放比例
        float scaleWidth = (newWidth) / w;
        float scaleHeight = (newHeight) / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        src = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);

        //根据bitmap缩放水印图片
        float w1 = w / 5;
        float h1 = (float) (w1 / 5);
        //获取原始水印图片的宽、高
        int w2 = watermark.getWidth();
        int h2 = watermark.getHeight();

        //计算缩放的比例
        float scalewidth = ((float) w1) / w2;
        float scaleheight = ((float) h1) / h2;

        Matrix matrix1 = new Matrix();
        matrix1.postScale((float) 0.4, (float) 0.4);

        watermark = Bitmap.createBitmap(watermark, 0, 0, w2, h2, matrix1, true);
        //获取新的水印图片的宽、高
        w2 = watermark.getWidth();
        h2 = watermark.getHeight();

        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(result);
        //在canvas上绘制原图和新的水印图
        cv.drawBitmap(src, 0, 0, null);
        //水印图绘制在画布的右下角，距离右边和底部都为20
        cv.drawBitmap(watermark, src.getWidth() - w2 - 20, src.getHeight() - h2 - 20, null);
        cv.save();
        cv.restore();

        return result;
    }

    public enum WaterLoc {
        /**
         * 左上方
         */
        LEFT_TOP,
        /**
         * 左中
         */
        LEFT_CENTER,
        /**
         * 左下
         */
        LEFT_BOTTOM,
        /**
         * 右上
         */
        RIGHT_TOP,
        /**
         * 右中
         */
        RIGHT_CENTER,
        /**
         * 右下
         */
        RIGHT_BOTTOM,
        /**
         * 居中
         */
        CENTER,
        /**
         * 中上
         */
        CENTER_TOP,
        /**
         * 中下
         */
        CENTER_BOTTOM;


    }
}
