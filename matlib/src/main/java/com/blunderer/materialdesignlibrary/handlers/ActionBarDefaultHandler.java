package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;

import com.blunderer.materialdesignlibrary.views.Toolbar;
import com.blunderer.materialdesignlibrary.views.ToolbarDefault;

public class ActionBarDefaultHandler extends ActionBarHandler {

    public ActionBarDefaultHandler(Context context) {
        super(context);
    }

    /**
     * Build the Toolbar to be the Default Toolbar.
     *
     * @return The Toolbar.
     */
    @Override
    public Toolbar build() {
        return new ToolbarDefault(mContext);
    }

}
