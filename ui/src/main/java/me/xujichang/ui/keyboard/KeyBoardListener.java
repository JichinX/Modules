package me.xujichang.ui.keyboard;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Des:
 *
 * @author xujichang
 * Created on 2018/10/25 - 4:26 PM
 */
public class KeyBoardListener {

    private Activity mActivity;
    private View childView;

    private int usableHeightPrevious;

    public static KeyBoardListener sKeyBoardListener;
    private FrameLayout.LayoutParams frameLayoutParams;

    public static KeyBoardListener getInstance(Activity pActivity) {
        sKeyBoardListener = new KeyBoardListener();
        sKeyBoardListener.setActivity(pActivity);
        return sKeyBoardListener;
    }

    private void setActivity(Activity pActivity) {
        mActivity = pActivity;
    }

    private KeyBoardListener() {
    }

    public void attachListener() {

        FrameLayout content = (FrameLayout) mActivity
                .findViewById(android.R.id.content);
        childView = content.getChildAt(0);
        childView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) childView
                .getLayoutParams();

    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = childView.getRootView()
                    .getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
// keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard
                        - heightDifference;
            } else {
// keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            childView.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        childView.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

}
