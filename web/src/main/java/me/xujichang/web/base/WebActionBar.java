package me.xujichang.web.base;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.xujichang.ui.activity.actionbar.ActionWhich;
import me.xujichang.ui.activity.actionbar.IActionClick;
import me.xujichang.ui.activity.actionbar.IDefaultActionBar;
import me.xujichang.web.R;

/**
 * Des:Modules - me.xujichang.web.base
 *
 * @author xujichang
 * @date 2019/3/19
 * <p>
 * modify:
 */
public class WebActionBar implements IDefaultActionBar {

    private TextView mWebBack;
    private TextView mWebExit;
    private TextView mWebTitle;
    private LinearLayout mWebLlRight;

    private View mActionBar;
    private ImageView mRightImg;
    private IWebActionClick mWebActionClick;

    public WebActionBar(IWebActionClick actionClick) {
        mWebActionClick = actionClick;
    }

    @Override
    public void attachRoot(LinearLayout view) {

    }

    @Override
    public int getResourceID() {
        return R.layout.layout_actionbar_web;
    }

    @Override
    public void onInflate(View inflated) {
        initView(inflated);
    }

    @Override
    public void setTitle(String text) {
        mWebTitle.setText(text);
    }

    @Override
    public void setActionClick(IActionClick click) {

    }

    @Override
    public void hide(ActionWhich which) {

    }

    @Override
    public void showBack() {
        mWebBack.setVisibility(View.VISIBLE);
    }

    public void hideBack() {
        mWebBack.setVisibility(View.GONE);
    }

    @Override
    public void setActionBarDrawable(Drawable drawable) {

    }

    @Override
    public View getActionBar() {
        return mActionBar;
    }

    @Override
    public void setLeftText(String text) {

    }

    @Override
    public void setLeftImage(Drawable drawable) {

    }

    @Override
    public void setRightText(String text) {

    }

    @Override
    public void setRightImage(Drawable drawable) {
        if (null == mRightImg) {
            mRightImg = createImageView(drawable);
            mWebLlRight.addView(mRightImg);
        } else {
            mRightImg.setImageDrawable(drawable);
        }
    }

    private ImageView createImageView(Drawable drawable) {
        ImageView imageView = new ImageView(mActionBar.getContext());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    public ImageView getRightImg() {
        return mRightImg;
    }

    public TextView getTitle() {
        return mWebTitle;
    }

    private void initView(View inflated) {
        mActionBar = inflated;
        mWebBack = inflated.findViewById(R.id.web_tv_actionbar_back);
        mWebExit = inflated.findViewById(R.id.web_iv_actionbar_exit);
        mWebTitle = inflated.findViewById(R.id.web_tv_title);
        mWebLlRight = inflated.findViewById(R.id.web_ll_actionbar_right);

        mWebLlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebActionClick.onRightClick();
            }
        });
        mWebBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebActionClick.onBack();
            }
        });
        mWebExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebActionClick.onExit();
            }
        });
    }

    public void setRightImageRes(int id) {
        setRightImage(ContextCompat.getDrawable(mActionBar.getContext(), id));
    }

    public void hideExit() {
        mWebExit.setVisibility(View.GONE);
    }

    public void showExit() {
        mWebExit.setVisibility(View.VISIBLE);
    }
}
