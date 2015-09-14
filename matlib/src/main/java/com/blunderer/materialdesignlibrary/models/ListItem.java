package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public abstract class ListItem {

    protected String mTitle;
    private Drawable mIcon;
    protected boolean mUseTitle = false;
    private boolean mUseIconResource = false;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(Context context, int titleResource) {
        mUseTitle = true;
        mTitle = context.getString(titleResource);
    }

    public void setTitle(String title) {
        mUseTitle = true;
        mTitle = title;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Context context, int iconResource) {
        mUseIconResource = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIcon = context.getDrawable(iconResource);
        } else mIcon = context.getResources().getDrawable(iconResource);
    }

    public void setIcon(Drawable icon) {
        mUseIconResource = true;
        mIcon = icon;
    }

    public boolean useTitleResource() {
        return mUseTitle;
    }

    public boolean useIconResource() {
        return mUseIconResource;
    }

}
