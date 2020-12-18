package com.yz.drawerlibrary;

/**
 * @Desc: {@link DrawerView} 的控制接口，根据 {@link ViewState} 进行控制
 * @Author: YiZe
 */
public interface ViewStateManager {

    /**
     * 根据 viewState，切换 {@link DrawerView} 的状态, 带动画
     * 1. {@link ViewState#FULL}:   全屏
     * 2. {@link ViewState#HOVER}:  悬停
     * 3. {@link ViewState#CLOSE}:  关闭(隐藏)
     *
     * @param viewState 给定的 {@link ViewState}
     */
    void changeState(ViewState viewState);

    /**
     * 由 isSmoothScroll 控制是否带有动画,
     * 其余同 {@link #changeState(ViewState)}
     *
     * @param viewState      给定的 {@link ViewState}
     * @param isSmoothScroll 是否需要动画
     */
    void changeState(ViewState viewState, boolean isSmoothScroll);

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    ViewState getState();
}
