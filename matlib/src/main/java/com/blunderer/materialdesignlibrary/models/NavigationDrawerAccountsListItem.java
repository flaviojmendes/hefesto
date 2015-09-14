package com.blunderer.materialdesignlibrary.models;

import android.content.Context;

import com.blunderer.materialdesignlibrary.R;

public class NavigationDrawerAccountsListItem extends ListItem {

    public final static int CUSTOM = -1;
    public final static int ADD_ACCOUNT = 0;
    public final static int MANAGE_ACCOUNTS = 1;

    public NavigationDrawerAccountsListItem(Context context, int definedItem) {
        switch (definedItem) {
            case ADD_ACCOUNT:
                setTitle(context, R.string.mdl_add_account);
                setIcon(context, R.drawable.ic_add);
                break;
            case MANAGE_ACCOUNTS:
                setTitle(context, R.string.mdl_manage_accounts);
                setIcon(context, R.drawable.ic_settings);
                break;
            default:
                break;
        }
    }

}
