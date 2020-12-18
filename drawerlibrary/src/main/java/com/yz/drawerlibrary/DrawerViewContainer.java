package com.yz.drawerlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * @Desc: DrawerViewContainer控件，{@link DrawerView} 的外部容器，主要功能在本类实现
 * @Author: YiZe
 */
public class DrawerViewContainer extends FrameLayout implements ViewStateManager {
    /**
     * {@link DrawerView} 控件
     */
    private DrawerView mDrawerView, mBottomView;
    /**
     * ViewDragHelper是用于编写自定义ViewGroup的实用程序类。
     * 它提供了许多有用的操作和状态跟踪，
     * 允许用户在其父级ViewGroup中拖动和重新放置视图。
     * 详情可查看 {@link ViewDragHelper}
     */
    private ViewDragHelper mViewDragHelper;
    /**
     * 子视图状态
     */
    private ViewState mViewState = ViewState.CLOSE;
    /**
     * 是否重新测量控件（用于抽屉开合时抽屉高度的测量，防止出现展开抽屉时内容显示不全的Bug）
     */
    private boolean mIsRemeasure = false;
    /**
     * 用于保存 {@link #getTopOfState(ViewState)} 得到的数值
     */
    private float mGetMeasure;


    public DrawerViewContainer(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DrawerViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化方法
     */
    private void init(Context context) {
        //初始化ViewDragHelper帮助类
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            //捕获 mBottomView
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child == mBottomView;
            }
        });
        //初始化空的控件
        mDrawerView = new DrawerView(context);
        mDrawerView.setLayoutParams(new LayoutParams(0, 0));
    }

    /**
     * 控件加载完成后子控件绑定方法
     * 初始化时期调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //绑定子控件
        mBottomView = findHoverView();
        if (mBottomView == mDrawerView) {
            addView(mDrawerView);
        }
    }

    /**
     * 查找 {@link DrawerView} 对象
     * 初始化时期调用
     *
     * @return 得到的 {@link DrawerView} 对象
     */
    private DrawerView findHoverView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof DrawerView) {
                return (DrawerView) getChildAt(i);
            }
        }
        return mDrawerView;
    }

    /**
     * onLayout测量方法
     * 当高度重新设定时，该方法会反复调用
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //设置当前抽屉的开合状态
        changeState(mBottomView.getState(), false);
    }

    /**
     * 有动画的移动方法
     *
     * @param finalTop {@link DrawerView} 的当前高度
     */
    private void smoothScrollTo(int finalTop) {
        mViewDragHelper.smoothSlideViewTo(mBottomView, 0, finalTop);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * {@link #smoothScrollTo(int)} 中用到 mScroller，不要忘了配合本方法
     * 该方法在初始化和抽屉开合时会进行调用，其中抽屉开合时，该方法会多次调用。
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //滚动计算完成方法
        rollingCalculationCompleted();
    }

    /**
     * 滚动计算完成方法
     * 核心：ViewDragHelper中的continueSettling方法将捕获的沉降视图移动当前时间的适当量。
     * 该方法有返回值，调用该方法会返回一个是否需要继续计算的标志值，类型为布尔类型。
     * 提示：如果continueSettling为true，则调用方应在下一帧再次调用它以继续。
     *
     * @see ViewDragHelper#continueSettling(boolean)
     */
    private void rollingCalculationCompleted() {
        //获取标志值
        boolean continueSettling = mViewDragHelper.continueSettling(true);
        if (continueSettling) {
            mIsRemeasure = true;
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            //设置这个判断是防止界面初始化时条件内方法执行而形成抽屉反复开合的死循环
            if (mIsRemeasure) {
                mIsRemeasure = false;
                LayoutParams params = (LayoutParams) mBottomView.getLayoutParams();
                params.height = (int) (mGetMeasure * mBottomView.getContainer().getMeasuredHeight());
                mBottomView.setLayoutParams(params);
            }
        }
    }

    /**
     * 无动画的移动方法
     *
     * @param finalTop {@link DrawerView} 的当前高度
     */
    private void scrollTo(int finalTop) {
        ViewCompat.offsetTopAndBottom(mBottomView, finalTop - getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 通过{@link ViewState}获取{@link DrawerView}的当前高度
     * 在初始化和每次抽屉开合时执行该方法
     *
     * @param viewState {@link ViewState} 对象
     * @return {@link DrawerView} 的当前高度
     */
    private int getTopOfState(ViewState viewState) {
        return viewState.getTop(mBottomView);
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
        mViewState = viewState;
        switch (getState()) {
            case FULL: {
                mGetMeasure = (mBottomView.getTopClose() - mBottomView.getTopFull());
            }
            break;
            case HOVER: {
                mGetMeasure = (mBottomView.getTopClose() - mBottomView.getTopHover());
            }
            break;
            case CLOSE: {
                mGetMeasure = mBottomView.getTopClose();
            }
            break;
            default: {
            }
            break;
        }
        if (isSmoothScroll) {
            smoothScrollTo(getTopOfState(viewState));
        } else {
            scrollTo(getTopOfState(viewState));
        }
    }

    /**
     * 接口方法实现，获取 {@link DrawerView} 的状态
     *
     * @return 当前状态
     */
    @Override
    public ViewState getState() {
        return mViewState;
    }
}
