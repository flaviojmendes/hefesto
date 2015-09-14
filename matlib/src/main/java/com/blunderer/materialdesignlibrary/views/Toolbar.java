package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class Toolbar extends FrameLayout {

    protected android.support.v7.widget.Toolbar mToolbar;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public android.support.v7.widget.Toolbar getToolbar() {
        return mToolbar;
    }

}
