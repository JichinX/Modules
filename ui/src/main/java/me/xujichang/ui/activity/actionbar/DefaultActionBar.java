package me.xujichang.ui.activity.actionbar;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import me.xujichang.ui.R;
import me.xujichang.ui.utils.GlobalUtil;

/**
 * Des:默认使用的ActionBar样式，
 * 左图片文字 tittle 右 图片文字
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午7:56
 */
public class DefaultActionBar implements IDefaultActionBar, View.OnClickListener {
    private ViewStub left;
    private ViewStub right;
    private TextView title;

    private ImageView leftImage;
    private TextView leftText;

    private ImageView rightImage;
    private TextView rightText;

    private IActionClick actionClick;
    private View actionBar;

    public DefaultActionBar() {

    }

    @Override
    public void attachRoot(LinearLayout view) {

    }

    @Override
    public int getResourceID() {
        return R.layout.layout_action_bar_default;
    }

    @Override
    public void onInflate(View inflated) {
        actionBar = inflated;
        //获取
        left = inflated.findViewById(R.id.vs_action_left);
        right = inflated.findViewById(R.id.vs_action_right);
        title = inflated.findViewById(R.id.tv_action_title);
        //设置左右
        left.setLayoutResource(obtainLeftResource());
        right.setLayoutResource(obtainRightResource());
        left.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                inflateLeft(stub, inflated);
            }
        });
        right.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                inflateRight(stub, inflated);
            }
        });
        left.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);

        title.setOnClickListener(this);
    }

    private void inflateRight(ViewStub stub, View inflated) {
        rightImage = inflated.findViewById(R.id.iv_action_default_right_image);
        rightText = inflated.findViewById(R.id.tv_action_default_right_text);
        rightImage.setOnClickListener(this);
        rightText.setOnClickListener(this);
    }

    private void inflateLeft(ViewStub stub, View inflated) {
        leftImage = inflated.findViewById(R.id.iv_action_default_left_image);
        leftText = inflated.findViewById(R.id.tv_action_default_left_text);
        leftImage.setOnClickListener(this);
        leftText.setOnClickListener(this);
    }

    private @LayoutRes
    int obtainRightResource() {
        return R.layout.layout_action_default_right;
    }

    private @LayoutRes
    int obtainLeftResource() {
        return R.layout.layout_action_default_left;
    }

    @Override
    public void setTitle(String text) {
        title.setText(Strings.nullToEmpty(text));
    }

    @Override
    public void setLeftText(String text) {
        leftText.setVisibility(View.VISIBLE);
        leftText.setText(Strings.nullToEmpty(text));
        updateLayout(ActionWhich.LEFT_TEXT, View.VISIBLE);
    }

    @Override
    public void setLeftImage(Drawable drawable) {
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setImageDrawable(drawable);
        updateLayout(ActionWhich.LEFT_IMAGE, View.VISIBLE);
    }

    @Override
    public void setRightText(String text) {
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(Strings.nullToEmpty(text));
        updateLayout(ActionWhich.RIGHT_TEXT, View.VISIBLE);
    }

    @Override
    public void setRightImage(Drawable drawable) {
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setImageDrawable(drawable);
        updateLayout(ActionWhich.RIGHT_IMAGE, View.VISIBLE);
    }


    @Override
    public void setActionClick(IActionClick click) {
        this.actionClick = click;
    }

    @Override
    public void hide(ActionWhich which) {
        if (which == ActionWhich.ALL) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        } else if (which == ActionWhich.LEFT_AREA) {
            left.setVisibility(View.GONE);
        } else if (which == ActionWhich.RIGHT_AREA) {
            right.setVisibility(View.GONE);
        } else if (which == ActionWhich.RIGHT_TEXT) {
            rightText.setVisibility(View.GONE);
        } else if (which == ActionWhich.RIGHT_IMAGE) {
            rightImage.setVisibility(View.GONE);
        } else if (which == ActionWhich.LEFT_TEXT) {
            leftText.setVisibility(View.GONE);
        } else if (which == ActionWhich.LEFT_IMAGE) {
            leftImage.setVisibility(View.GONE);
        }
        updateLayout(which, View.GONE);
    }

    /**
     * 重新布局 使布局保持对称 居中
     *
     * @param which
     * @param v
     */
    private void updateLayout(ActionWhich which, int v) {
        //取左右两侧 显示的View的数量
        int leftVisible = generateVisible(leftImage, leftText);
        int rightVisible = generateVisible(rightText, rightImage);
        //如果左右两侧显示的树龄相同，则是平衡的
        if (leftVisible == rightVisible) {
            return;
        } else if (leftVisible > rightVisible) {
            //如果不相等，就需要设置少的一方为INVISIBLE
            updateRight(leftVisible - rightVisible);
        } else {
            updateLeft(rightVisible - leftVisible);
        }
        //平衡调整之后，还要根据显示情况调整位置
        if (rightText.getVisibility() == View.VISIBLE && rightImage.getVisibility() == View.INVISIBLE) {
            exchangeView(rightImage, rightText);
        }
        if (leftText.getVisibility() == View.VISIBLE && leftImage.getVisibility() == View.INVISIBLE) {
            //交换位置
            exchangeView(leftImage, leftText);
        }
    }

    private void updateRight(int num) {
        if (num == 2) {
            rightImage.setVisibility(View.INVISIBLE);
            rightText.setVisibility(View.INVISIBLE);
        } else {
            if (rightText.getVisibility() == View.VISIBLE) {
                rightImage.setVisibility(View.INVISIBLE);
            } else {
                rightText.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateLeft(int num) {
        if (num == 2) {
            leftText.setVisibility(View.INVISIBLE);
            leftImage.setVisibility(View.INVISIBLE);
        } else {
            if (leftText.getVisibility() == View.VISIBLE) {
                leftImage.setVisibility(View.INVISIBLE);
            } else {
                leftText.setVisibility(View.INVISIBLE);
            }
        }
    }

    private int generateVisible(View view1, View view2) {
        int num = 0;
        if (view1.getVisibility() != View.GONE) {
            num++;
        }
        if (view2.getVisibility() != View.GONE) {
            num++;
        }
        return num;
    }

    private void exchangeView(View first, View second) {
        ViewGroup parent = (ViewGroup) first.getParent();
        int firstIndex = parent.indexOfChild(first);
        int secondIndex = parent.indexOfChild(second);
        parent.removeViewAt(firstIndex);
        parent.addView(first, secondIndex);
    }

    @Override
    public void onClick(View v) {
        if (null == actionClick) {
            return;
        }
        int id = v.getId();
        if (id == title.getId()) {
            actionClick.onClick(ActionWhich.TITLE);
        } else if (id == leftImage.getId()) {
            actionClick.onClick(ActionWhich.LEFT_AREA);
            actionClick.onClick(ActionWhich.LEFT_IMAGE);
        } else if (id == leftText.getId()) {
            actionClick.onClick(ActionWhich.LEFT_AREA);
            actionClick.onClick(ActionWhich.LEFT_TEXT);
        } else if (id == rightText.getId()) {
            actionClick.onClick(ActionWhich.RIGHT_AREA);
            actionClick.onClick(ActionWhich.RIGHT_TEXT);
        } else if (id == rightImage.getId()) {
            actionClick.onClick(ActionWhich.RIGHT_AREA);
            actionClick.onClick(ActionWhich.RIGHT_IMAGE);
        }
    }

    @Override
    public void showBack() {
        setLeftImage(GlobalUtil.getDrawableWithSc(R.drawable.ic_arrow_back_white));
    }

    @Override
    public void setActionBarDrawable(Drawable drawable) {
        actionBar.setBackground(drawable);
    }

    @Override
    public View getActionBar() {
        return actionBar;
    }

    public TextView getActionBarTitle() {
        return title;
    }

    public ImageView getImageView(ActionWhich which) {
        if (which == ActionWhich.LEFT_IMAGE) {
            return leftImage;
        } else if (which == ActionWhich.RIGHT_IMAGE) {
            return rightImage;
        } else {
            return null;
        }
    }

    public TextView getTextView(ActionWhich which) {
        if (which == ActionWhich.LEFT_TEXT) {
            return leftText;
        } else if (which == ActionWhich.RIGHT_TEXT) {
            return rightText;
        } else {
            return null;
        }
    }
}
