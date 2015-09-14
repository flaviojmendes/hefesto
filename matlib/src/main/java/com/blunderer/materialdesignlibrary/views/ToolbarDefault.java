package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.blunderer.materialdesignlibrary.R;

public class ToolbarDefault extends Toolbar {

    public ToolbarDefault(Context context) {
        this(context, null);
    }

    public ToolbarDefault(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarDefault(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mdl_toolbar_default, this, true);

        mToolbar = (android.support.v7.widget.Toolbar) getChildAt(0);
    }

    public android.support.v7.widget.Toolbar getToolbar() {
        return mToolbar;
    }

}
