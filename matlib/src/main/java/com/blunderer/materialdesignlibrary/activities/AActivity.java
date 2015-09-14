package com.blunderer.materialdesignlibrary.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.views.Toolbar;
import com.blunderer.materialdesignlibrary.views.ToolbarDefault;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

public abstract class AActivity extends ActionBarActivity {

    private final static String TOOLBAR_SEARCH_CONSTRAINT_KEY = "ToolbarSearchConstraint";
    private final static String TOOLBAR_SEARCH_IS_SHOWN = "ToolbarSearchIsShown";

    private Toolbar mCustomToolbar;
//    private SoftKeyboard softKeyboard;

    public void onCreate(Bundle savedInstanceState, int contentView) {
        super.onCreate(savedInstanceState);

        setContentView(contentView);

        ActionBarHandler actionBarHandler = getActionBarHandler();
        if (actionBarHandler == null) mCustomToolbar = new ToolbarDefault(this);
        else mCustomToolbar = actionBarHandler.build();

        if (mCustomToolbar instanceof ToolbarSearch) {
            ToolbarSearch toolbarSearch = (ToolbarSearch) mCustomToolbar;
            toolbarSearch.setActivity(this);

            if (savedInstanceState != null) {
                toolbarSearch
                        .setConstraint(savedInstanceState.getString(TOOLBAR_SEARCH_CONSTRAINT_KEY));
                if (savedInstanceState.getBoolean(TOOLBAR_SEARCH_IS_SHOWN)) {
                    toolbarSearch.showSearchBar();
                }
            }
        }

        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content))
                .getChildAt(0);
        if (this instanceof NavigationDrawerActivity) rootView.addView(mCustomToolbar, 1);
        else rootView.addView(mCustomToolbar);

        mCustomToolbar.getToolbar()
                .setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(mCustomToolbar.getToolbar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCustomToolbar instanceof ToolbarSearch) {
            ToolbarSearch toolbarSearch = (ToolbarSearch) mCustomToolbar;
            outState.putBoolean(TOOLBAR_SEARCH_IS_SHOWN, toolbarSearch.isSearchBarShown());
            outState.putString(TOOLBAR_SEARCH_CONSTRAINT_KEY, toolbarSearch.getConstraint());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCustomToolbar instanceof ToolbarSearch) {
            MenuItem searchItem = menu
                    .add(0, R.id.mdl_toolbar_search_menu_item, Menu.NONE, "Search")
                    .setIcon(R.drawable.ic_action_search);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.mdl_toolbar_search_menu_item
                && mCustomToolbar instanceof ToolbarSearch) {
            ((ToolbarSearch) mCustomToolbar).showSearchBar();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mCustomToolbar instanceof ToolbarSearch) {
            ToolbarSearch toolbarSearch = ((ToolbarSearch) mCustomToolbar);
            if (toolbarSearch.isSearchBarShown()) {
                toolbarSearch.hideSearchBar();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToolbarSearch.SEARCH_REQUEST_CODE) {
            ((ToolbarSearch) mCustomToolbar).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
//        softKeyboard.unRegisterSoftKeyboardCallback();

        super.onDestroy();
    }

    public android.support.v7.widget.Toolbar getMaterialDesignActionBar() {
        return mCustomToolbar.getToolbar();
    }

    public void showActionBarSearch() {
        if (mCustomToolbar instanceof ToolbarSearch) {
            ((ToolbarSearch) mCustomToolbar).showSearchBar();
        }
    }

    public void hideActionBarSearch() {
        if (mCustomToolbar instanceof ToolbarSearch) {
            ((ToolbarSearch) mCustomToolbar).hideSearchBar();
        }
    }

    protected abstract ActionBarHandler getActionBarHandler();

}
