package com.blunderer.materialdesignlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewStub;
import android.widget.ScrollView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

public abstract class ScrollViewActivity extends AActivity
        implements com.blunderer.materialdesignlibrary.interfaces.ScrollView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ScrollView mScrollView;
    private ScrollViewActivity mActivity;

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.mdl_activity_scrollview);

        mActivity = this;

        mScrollView = (ScrollView) findViewById(R.id.activity_scrollview);

        ViewStub stub = (ViewStub) findViewById(R.id.activity_scrollview_view);
        try {
            stub.setLayoutResource(getContentView());
            stub.inflate();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ContentView must have a valid layoutResource");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_scrollview_refresh);
        if (pullToRefreshEnabled()) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    mActivity.onRefresh();
                }

            });
            mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        } else mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToolbarSearch.SEARCH_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
