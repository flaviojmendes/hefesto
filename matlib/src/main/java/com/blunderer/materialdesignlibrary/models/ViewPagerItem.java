package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.support.v4.app.Fragment;

public class ViewPagerItem {

    private String mTitle;
    private Fragment mFragment;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(Context context, int titleResource) {
        this.mTitle = context.getString(titleResource);
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

}
