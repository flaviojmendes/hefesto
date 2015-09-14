package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.view.View;

public class NavigationDrawerAccountsListItemOnClick extends NavigationDrawerAccountsListItem {

    private View.OnClickListener mOnClickListener;

    public NavigationDrawerAccountsListItemOnClick(Context context, int definedItem) {
        super(context, definedItem);
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
