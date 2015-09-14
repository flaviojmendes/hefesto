package com.blunderer.materialdesignlibrary.models;

import android.support.v4.app.Fragment;

public class NavigationDrawerListItemTopFragment extends ListItem {

    private Fragment mFragment;

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

}
