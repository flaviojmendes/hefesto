package com.blunderer.materialdesignlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

public abstract class ListViewActivity extends AActivity
        implements com.blunderer.materialdesignlibrary.interfaces.ListView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ListView mListView;
    private ListViewActivity mActivity;

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.mdl_activity_listview);

        mActivity = this;

        ViewStub stub = (ViewStub) findViewById(R.id.activity_listview_view);
        View inflatedView;
        try {
            stub.setLayoutResource(useCustomContentView()
                    ? getCustomContentView() : R.layout.mdl_listview);
            inflatedView = stub.inflate();

            if (inflatedView instanceof ListView) mListView = (ListView) inflatedView;
            else mListView = (ListView) inflatedView.findViewById(android.R.id.list);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "CustomContentView must have a valid layoutResource");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_listview_refresh);
        if (pullToRefreshEnabled()) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    mActivity.onRefresh();
                }

            });
            mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        } else mSwipeRefreshLayout.setEnabled(false);

        if (mListView != null) {
            ListAdapter adapter = getListAdapter();
            if (adapter != null) {
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long id) {
                        mActivity.onItemClick(adapterView, view, position, id);
                    }

                });
                mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view, int position, long id) {
                        return mActivity.onItemLongClick(adapterView, view, position, id);
                    }

                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToolbarSearch.SEARCH_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}