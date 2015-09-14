package com.blunderer.materialdesignlibrary.models;

import android.content.Context;

import com.blunderer.materialdesignlibrary.listeners.OnMoreAccountClickListener;

public class NavigationDrawerAccountsListItemAccount extends NavigationDrawerAccountsListItem {

    private OnMoreAccountClickListener mOnClickListener;

    public NavigationDrawerAccountsListItemAccount(Context context) {
        super(context, CUSTOM);
    }

    public OnMoreAccountClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnMoreAccountClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
