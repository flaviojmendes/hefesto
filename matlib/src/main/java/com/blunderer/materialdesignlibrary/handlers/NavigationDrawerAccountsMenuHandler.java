package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemIntent;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemOnClick;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerAccountsMenuHandler {

    private final Context mContext;
    private List<NavigationDrawerAccountsListItem> mItems;

    public NavigationDrawerAccountsMenuHandler(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
    }

    public NavigationDrawerAccountsMenuHandler addAddAccount(Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.ADD_ACCOUNT);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addAddAccount(View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.ADD_ACCOUNT);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addManageAccounts(Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.MANAGE_ACCOUNTS);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addManageAccounts(
            View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.MANAGE_ACCOUNTS);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       int iconResource,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(mContext, iconResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       int iconResource,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setIcon(mContext, iconResource);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       Drawable icon,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(icon);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       Drawable icon,
                                                       Intent intent) {
        NavigationDrawerAccountsListItemIntent item = new NavigationDrawerAccountsListItemIntent(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setIcon(icon);
        item.setIntent(intent);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       int iconResource,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(mContext, iconResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       int iconResource,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setIcon(mContext, iconResource);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(int titleResource,
                                                       Drawable icon,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(mContext, titleResource);
        item.setIcon(icon);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsMenuHandler addItem(String title,
                                                       Drawable icon,
                                                       View.OnClickListener onClickListener) {
        NavigationDrawerAccountsListItemOnClick item = new NavigationDrawerAccountsListItemOnClick(
                mContext, NavigationDrawerAccountsListItem.CUSTOM);
        item.setTitle(title);
        item.setIcon(icon);
        item.setOnClickListener(onClickListener);
        mItems.add(item);
        return this;
    }

    public List<NavigationDrawerAccountsListItem> getNavigationDrawerAccountsMenuItems() {
        return mItems;
    }

}
