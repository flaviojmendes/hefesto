package com.blunderer.materialdesignlibrary.models;

import android.content.Context;
import android.view.View;

import com.blunderer.materialdesignlibrary.R;

public class NavigationDrawerListItemBottom extends ListItem {

    public final static int CUSTOM = -1;
    public final static int SETTINGS = 0;
    public final static int HELP_AND_FEEDBACK = 1;

    private View.OnClickListener mOnClickListener;

    public NavigationDrawerListItemBottom(Context context, int definedItem) {
        switch (definedItem) {
            case SETTINGS:
                setTitle(context, R.string.mdl_settings);
                setIcon(context, R.drawable.ic_settings);
                break;
            case HELP_AND_FEEDBACK:
                setTitle(context, R.string.mdl_help_and_feedback);
                setIcon(context, R.drawable.ic_help);
                break;
            default:
                break;
        }
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

}
