package com.yz.drawerlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @Desc: HoverView抽屉控件
 * @Author: YiZe
 */
public class DrawerView extends LinearLayout implements ViewStateManager {
    /**
     * 悬浮时的高度比
     */
    private float mTopHover = 0.6f;
    private static final float TOP_FULL = 0.0f;
    private static final float TOP_CLOSE = 1.0f;

    public float getTopHover() {
        return mTopHover;
    }

    public void setTopHover(float mTopHover) {
        this.mTopHover = mTopHover;
    }

    public DrawerView(Context context) {
        super(context);
    }

    public DrawerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化方法
     *
     * @param context Context对象
     * @param attrs   自定义属性数组
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DrawerView);
        mTopHover = ta.getFloat(R.styleable.DrawerView_top_hover, mTopHover);
        ta.recycle();
    }

    /**
     * 得到全屏时的高度比
     *
     * @return 全屏时的高度比
     */
    public float getTopFull() {
        return TOP_FULL;
    }

    /**
     * 得到关闭时的高度比
     *
     * @return 关闭时的高度比
     */
    public float getTopClose() {
        return TOP_CLOSE;
    }

    /**
     * 获取HoverViewContainer对象，这个可以帮助获取抽屉的总高度
     *
     * @return HoverViewContainer对象
     */
    public DrawerViewContainer getContainer() {
        if (this.getParent() instanceof DrawerViewContainer) {
            return (DrawerViewContainer) this.getParent();
        }
        return null;
    }

    /**
     * 接口方法实现，切换 {@link DrawerView} 的状态
     *
     * @param viewState 给定的 {@link ViewState}
     */
    @Override
    public void changeState(ViewState viewState) {
        changeState(viewState, true);
    }

    /**
     * 接口方法实现，切换 {@link DrawerView} 的状态，可以自行设置是否需要动画
     *
     * @param viewState      给定的 {@link ViewState}
     * @param isSmoothScroll 是否需要动画
     */
    @Override
    public void changeState(ViewState viewState, boolean isSmoothScroll) {
        if (getContainer() != null) {
            getContainer().changeState(viewState, isSmoothScroll);
        }
    }

    /**
     * 接口方法实现，获取 {@link DrawerView} 的状态
     *
     * @return 当前状态
     */
    @Override
    public ViewState getState() {
        if (getContainer() != null) {
            return getContainer().getState();
        }
        return ViewState.CLOSE;
    }
}
