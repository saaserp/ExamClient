package com.nengfei.widget;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);
    String getTextResId(int index);

    // From PagerAdapter
    int getCount();
}
