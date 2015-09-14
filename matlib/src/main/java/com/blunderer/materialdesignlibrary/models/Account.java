package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class Account {

    private String mTitle;
    private String mDescription;
    private Drawable mPicture;
    private int mBackgroundResource;
    private Drawable mBackgroundDrawable;

    private boolean mUseBackgroundDrawable;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String name) {
        this.mTitle = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String email) {
        this.mDescription = email;
    }

    public Drawable getPicture() {
        return mPicture;
    }

    public void setPicture(Context context, int pictureResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPicture = context.getDrawable(pictureResource);
        } else mPicture = context.getResources().getDrawable(pictureResource);
    }

    public void setPicture(Drawable picture) {
        mPicture = picture;
    }

    public int getBackgroundResource() {
        return mBackgroundResource;
    }

    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    public void setBackground(int backgroundResource) {
        mUseBackgroundDrawable = false;
        mBackgroundResource = backgroundResource;
    }

    public void setBackground(Drawable background) {
        mUseBackgroundDrawable = true;
        mBackgroundDrawable = background;
    }

    public boolean useBackgroundDrawable() {
        return mUseBackgroundDrawable;
    }

}
