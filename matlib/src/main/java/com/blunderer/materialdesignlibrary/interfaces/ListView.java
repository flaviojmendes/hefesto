package com.blunderer.materialdesignlibrary.interfaces;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface ListView {

    void setRefreshing(boolean refreshing);

    ListAdapter getListAdapter();

    boolean useCustomContentView();

    int getCustomContentView();

    boolean pullToRefreshEnabled();

    int[] getPullToRefreshColorResources();

    void onRefresh();

    void onItemClick(AdapterView<?> adapterView, View view, int position, long l);

    boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l);

}
