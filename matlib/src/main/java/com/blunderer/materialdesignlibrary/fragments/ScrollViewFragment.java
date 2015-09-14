package com.blunderer.materialdesignlibrary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ScrollView;

import com.blunderer.materialdesignlibrary.R;

public abstract class ScrollViewFragment extends Fragment
        implements com.blunderer.materialdesignlibrary.interfaces.ScrollView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ScrollView mScrollView;
    private ScrollViewFragment mFragment;

    public ScrollViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mdl_fragment_scrollview, container, false);

        mScrollView = (android.widget.ScrollView) view.findViewById(R.id.fragment_scrollview);

        ViewStub stub = (ViewStub) view.findViewById(R.id.fragment_scrollview_view);
        try {
            stub.setLayoutResource(getContentView());
            stub.inflate();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ContentView must have a valid layoutResource");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout)
                view.findViewById(R.id.fragment_scrollview_refresh);
        if (pullToRefreshEnabled()) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    mFragment.onRefresh();
                }

            });
            mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        } else mSwipeRefreshLayout.setEnabled(false);

        return view;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(refreshing);
    }

}
