package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.blunderer.materialdesignlibrary.models.ListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemHeader;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopFragment;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopIntent;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerTopHandler {

    private final Context mContext;
    private List<ListItem> mItems;

    public NavigationDrawerTopHandler(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
    }

    public NavigationDrawerTopHandler addSection(int titleResource) {
        NavigationDrawerListItemHeader item = new NavigationDrawerListItemHeader();
        item.setTitle(mContext, titleResource);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addSection(String title) {
        NavigationDrawerListItemHeader item = new NavigationDrawerListItemHeader();
        item.setTitle(title);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource, Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(mContext, titleResource);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title, Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(title);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource,
                                              int iconResource,
                                              Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(mContext, titleResource);
        item.setIcon(mContext, iconResource);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title,
                                              int iconResource,
                                              Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(title);
        item.setIcon(mContext, iconResource);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource,
                                              Drawable icon,
                                              Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(mContext, titleResource);
        item.setIcon(icon);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title,
                                              Drawable icon,
                                              Fragment fragment) {
        NavigationDrawerListItemTopFragment item = new NavigationDrawerListItemTopFragment();
        item.setTitle(title);
        item.setIcon(icon);
        item.setFragment(fragment);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(mContext, titleResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(title);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource, int iconResource, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(mContext, titleResource);
        item.setIcon(mContext, iconResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title, int iconResource, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(title);
        item.setIcon(mContext, iconResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(int titleResource, Drawable icon, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(mContext, titleResource);
        item.setIcon(icon);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerTopHandler addItem(String title, Drawable icon, Intent intent) {
        NavigationDrawerListItemTopIntent item = new NavigationDrawerListItemTopIntent();
        item.setTitle(title);
        item.setIcon(icon);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public List<ListItem> getNavigationDrawerTopItems() {
        return mItems;
    }

}
