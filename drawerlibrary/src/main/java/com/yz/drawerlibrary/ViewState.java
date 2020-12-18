package com.yz.drawerlibrary;

/**
 * @Desc: ViewState {@link DrawerView} 的切换状态
 * @Author: YiZe
 */
public enum ViewState {
    //全屏状态
    FULL {
        @Override
        int getTop(DrawerView drawerView) {
            return getTopByScale(drawerView, drawerView.getTopFull());
        }
    },

    //悬停状态
    HOVER {
        @Override
        int getTop(DrawerView drawerView) {
            return getTopByScale(drawerView, drawerView.getTopHover());
        }
    },

    //关闭: 完全藏在屏幕底部
    CLOSE {
        @Override
        int getTop(DrawerView drawerView) {
            return getTopByScale(drawerView, drawerView.getTopClose());
        }
    };

    /**
     * FILL, HOVER... 各自状态对应高度：即View.getTop()属性
     *
     * @param drawerView 指定的 hoverView
     * @return 当前高度
     */
    abstract int getTop(DrawerView drawerView);

    /**
     * 设置HoverView的显示高度
     * @param drawerView 当前{@link DrawerView} 的高度
     * @param scale 测量距离比
     * @return 待处理的显示高度
     */
    private static int getTopByScale(DrawerView drawerView, float scale) {
        if (drawerView.getContainer() != null) {
            return (int) (scale * drawerView.getContainer().getMeasuredHeight());
        }
        return 0;
    }
}
