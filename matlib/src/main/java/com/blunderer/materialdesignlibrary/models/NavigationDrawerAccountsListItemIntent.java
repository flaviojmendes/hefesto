package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.content.Intent;

public class NavigationDrawerAccountsListItemIntent extends NavigationDrawerAccountsListItem {

    private Intent mIntent;

    public NavigationDrawerAccountsListItemIntent(Context context, int definedItem) {
        super(context, definedItem);
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

}
