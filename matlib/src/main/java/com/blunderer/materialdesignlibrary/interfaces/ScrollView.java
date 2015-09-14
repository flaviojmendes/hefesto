package com.blunderer.materialdesignlibrary.interfaces;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface ScrollView {

    void setRefreshing(boolean refreshing);

    int getContentView();

    boolean pullToRefreshEnabled();

    int[] getPullToRefreshColorResources();

    void onRefresh();

}
