package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemBottom;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerBottomHandler {

    private final Context mContext;
    private List<NavigationDrawerListItemBottom> mItems;

    public NavigationDrawerBottomHandler(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
    }

    public NavigationDrawerBottomHandler addSettings(View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.SETTINGS);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addHelpAndFeedback(View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.HELP_AND_FEEDBACK);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(int titleResource,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(String title,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(title);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(int titleResource,
                                                 int iconResource,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(mContext, iconResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(String title,
                                                 int iconResource,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(title);
        item.setIcon(mContext, iconResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(int titleResource,
                                                 Drawable icon,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(icon);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerBottomHandler addItem(String title,
                                                 Drawable icon,
                                                 View.OnClickListener onClickListener) {
        NavigationDrawerListItemBottom item = new NavigationDrawerListItemBottom(mContext,
                NavigationDrawerListItemBottom.CUSTOM);
        item.setTitle(title);
        item.setIcon(icon);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public List<NavigationDrawerListItemBottom> getNavigationDrawerBottomItems() {
        return mItems;
    }

}
