package com.mayi.library;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * 作者 by yugai 时间 2017/7/28.
 * ＊ 邮箱 784787081@qq.com
 */

public class FlipboardSheetView extends CoordinatorLayout {
    private static final String TAG = "FlipboardView";


    /**
     * 弹窗展开全部
     */
    public static final int EXPANDED = 1;

    /**
     * 弹窗收缩到只有头部
     */
    public static final int COLLAPSED = 2;

    /**
     * 弹窗隐藏
     */
    public static final int HIDDEN = 3;


    @RestrictTo(LIBRARY_GROUP)
    @IntDef({EXPANDED, COLLAPSED, HIDDEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    //黑色阴影
    FrameLayout group;
    BottomSheetBehavior behavior;
    //头部的高度
    int peekHeight;
    //间距
    int zoomMargin;

    @State
    int state = HIDDEN;

    //是否在动画执行中
    boolean isAnimation;

    LayoutParams params;
    //缩放的View
    View content;

    //左右需要移动的距离
    int left;
    //底部需要移动的距离
    int bottom;
    //手指是否在拖拽中
    boolean isDragging;

    //动画插值器
    TimeInterpolator interpolator;

    OnStateChangeCallback mStateChangeCallback;

    public FlipboardSheetView(Context context) {
        this(context, null, 0);
    }

    public FlipboardSheetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipboardSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlipboardSheetView,
                defStyleAttr, 0);
        if (a.hasValue(R.styleable.FlipboardSheetView_sheet_layout)) {
            addSheetLayout(a.getResourceId(R.styleable.FlipboardSheetView_sheet_layout, 0), (int) a.getDimension(R.styleable.FlipboardSheetView_peek_height, 0));
        }
        zoomMargin = (int) a.getDimension(R.styleable.FlipboardSheetView_zoom_margin, 0);
    }

    private void init(Context context) {
        group = new FrameLayout(context);
        group.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        group.setAlpha(0);
        addView(group, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addSheetLayout(@LayoutRes int layoutResID, int peekHeight) {
        this.peekHeight = peekHeight;
        View sheet = LayoutInflater.from(getContext()).inflate(layoutResID, this, false);
        LayoutParams params = (LayoutParams) sheet.getLayoutParams();
        params.setBehavior(new BottomSheetBehavior());
        sheet.setLayoutParams(params);
        behavior = BottomSheetBehavior.from(sheet);
        behavior.setHideable(true);
        behavior.setPeekHeight(peekHeight);
        addView(sheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    public void switchState() {
        isAnimation = true;
        if (state == HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            stateAnimator();
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            stateAnimator();
        }
    }

    public void addStateChangeCallback(OnStateChangeCallback stateChangeCallback) {
        mStateChangeCallback = stateChangeCallback;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setState(@State int state) {
        switch (state) {
            case HIDDEN:
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case COLLAPSED:
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case EXPANDED:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    public int getState() {
        return state;
    }

    private void stateAnimator() {
        Log.i(TAG, "stateAnimator: " + isDragging);
        if (isDragging) return;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(params.topMargin, zoomMargin - params.topMargin);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                params.setMargins(value, value, value, value * bottom / zoomMargin);
                content.setLayoutParams(params);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd: ");
                isAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "onAnimationRepeat: ");
            }
        });
        if (interpolator != null) {
            valueAnimator.setInterpolator(interpolator);
        }
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: " + getChildCount());
        if (getChildCount() < 3) {
            throw new Resources.NotFoundException("FliboardView下面必须有一个子视图");
        } else {
            if (getChildCount() > 3)
                Log.e(TAG, "FlipboardView: 只会取第一个View缩放");
            content = getChildAt(2);
            getChildAt(0).bringToFront();
            getChildAt(0).bringToFront();
        }

        left = peekHeight / 2 + zoomMargin;
        bottom = zoomMargin + peekHeight;

        params = (LayoutParams) content.getLayoutParams();

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (!isAnimation) {
                    //由隐藏到显示头部
                    if (state == HIDDEN && newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        Log.i(TAG, "onStateChanged: A");
                        stateAnimator();
                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        stateAnimator();
                        Log.i(TAG, "onStateChanged: B");
                    }
                }

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        state = COLLAPSED;
                        isDragging = false;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        state = EXPANDED;
                        isDragging = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        state = HIDDEN;
                        isDragging = false;
                        break;
                }
                if (mStateChangeCallback != null) {
                    mStateChangeCallback.onStateChange(state);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                group.setAlpha(slideOffset);
                int height = (int) (getHeight() - bottomSheet.getY());
                if (height <= peekHeight && !isAnimation) {
                    isDragging = true;
                    int value = height * zoomMargin / peekHeight;
                    params.setMargins(value, value, value, value * bottom / zoomMargin);
                    content.setLayoutParams(params);
                }
            }
        });
    }

    public interface OnStateChangeCallback {
        void onStateChange(@State int state);
    }
}
