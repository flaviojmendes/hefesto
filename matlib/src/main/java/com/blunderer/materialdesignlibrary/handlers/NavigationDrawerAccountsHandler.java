package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.blunderer.materialdesignlibrary.models.Account;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerAccountsHandler {

    private final Context mContext;
    private List<Account> mItems;
    private boolean mUseSmallAccountsLayout;

    public NavigationDrawerAccountsHandler(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
        mUseSmallAccountsLayout = false;
    }

    public NavigationDrawerAccountsHandler addAccount(String title,
                                                      String description,
                                                      int pictureResource,
                                                      int backgroundResource) {
        Account item = new Account();
        item.setTitle(title);
        item.setDescription(description);
        item.setPicture(mContext, pictureResource);
        item.setBackground(backgroundResource);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsHandler addAccount(String title,
                                                      String description,
                                                      Drawable picture,
                                                      int backgroundResource) {
        Account item = new Account();
        item.setTitle(title);
        item.setDescription(description);
        item.setPicture(picture);
        item.setBackground(backgroundResource);
        mItems.add(item);
        return this;
    }

    public NavigationDrawerAccountsHandler addAccount(String title,
                                                      String description,
                                                      Drawable picture,
                                                      Drawable background) {
        Account item = new Account();
        item.setTitle(title);
        item.setDescription(description);
        item.setPicture(picture);
        item.setBackground(background);
        mItems.add(item);
        return this;
    }

    public List<Account> getNavigationDrawerAccounts() {
        return mItems;
    }

    public boolean useSmallAccountsLayout() {
        return mUseSmallAccountsLayout;
    }

    /**
     * Enables the Small Accounts Layout. Only the current account will be shown.
     *
     * @return The NavigationDrawerAccountsHandler.
     */
    public NavigationDrawerAccountsHandler enableSmallAccountsLayout() {
        mUseSmallAccountsLayout = true;

        return this;
    }

}
