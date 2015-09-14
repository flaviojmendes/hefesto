package com.blunderer.materialdesignlibrary.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.adapters.NavigationDrawerAdapter;
import com.blunderer.materialdesignlibrary.adapters.NavigationDrawerBottomAdapter;
import com.blunderer.materialdesignlibrary.fragments.ViewPagerFragment;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.interfaces.NavigationDrawer;
import com.blunderer.materialdesignlibrary.listeners.OnAccountChangeListener;
import com.blunderer.materialdesignlibrary.listeners.OnMoreAccountClickListener;
import com.blunderer.materialdesignlibrary.models.Account;
import com.blunderer.materialdesignlibrary.models.ListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemAccount;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemIntent;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemOnClick;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemBottom;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemHeader;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopFragment;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopIntent;
import com.blunderer.materialdesignlibrary.views.ANavigationDrawerAccountsLayout;
import com.blunderer.materialdesignlibrary.views.NavigationDrawerAccountsLayout;
import com.blunderer.materialdesignlibrary.views.NavigationDrawerAccountsLayoutSmall;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

import java.util.ArrayList;
import java.util.List;

public abstract class NavigationDrawerActivity extends AActivity
        implements NavigationDrawer, OnAccountChangeListener {

    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ANavigationDrawerAccountsLayout mAccountsLayout;
    protected ListView mTopListView;
    protected ListView mBottomListView;
    protected NavigationDrawerAdapter mListTopAdapter;
    protected NavigationDrawerBottomAdapter mListBottomAdapter;
    private View mDrawerLeft;
    private NavigationDrawerListItemTopFragment mCurrentItem;
    private int mCurrentItemPosition = 0;
    private List<ListItem> mNavigationDrawerItemsTop;
    private List<NavigationDrawerListItemBottom> mNavigationDrawerItemsBottom;
    private NavigationDrawerAccountsHandler mNavigationDrawerAccountsHandler;
    private int[] mAccountsPositions;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onAccountChange(Account account) {
        onNavigationDrawerAccountChange(account);
    }

    /**
     * Will update the Top ListView items.
     *
     * @param navigationDrawerTopHandler                  The handler that contains the new Top ListView items.
     * @param defaultNavigationDrawerItemSelectedPosition The position of the item that will be selected.
     */
    @Override
    public void updateNavigationDrawerTopHandler(
            NavigationDrawerTopHandler navigationDrawerTopHandler,
            int defaultNavigationDrawerItemSelectedPosition) {
        replaceTopItems(navigationDrawerTopHandler);
        selectDefaultItemPosition(defaultNavigationDrawerItemSelectedPosition, false);
    }

    /**
     * Will update the Bottom ListView items.
     *
     * @param navigationDrawerBottomHandler The handler that contains the new Bottom ListView items.
     */
    @Override
    public void updateNavigationDrawerBottomHandler(
            NavigationDrawerBottomHandler navigationDrawerBottomHandler) {
        replaceBottomItems(navigationDrawerBottomHandler);
    }

    @Override
    public void closeNavigationDrawer() {
        if (mDrawerLayout != null && mDrawerLeft != null) mDrawerLayout.closeDrawer(mDrawerLeft);
    }

    @Override
    public void openNavigationDrawer() {
        if (mDrawerLayout != null && mDrawerLeft != null) mDrawerLayout.openDrawer(mDrawerLeft);
    }

    @Override
    public void performNavigationDrawerItemClick(int position) {
        if (mNavigationDrawerItemsTop == null || mNavigationDrawerItemsTop.size() <= position ||
                mNavigationDrawerItemsTop.get(position) instanceof NavigationDrawerListItemHeader) {
            return;
        }

        int realPosition = isNavigationDrawerAccountHandlerEmpty() ? position : position + 1;
        mTopListView.performItemClick(mTopListView.getChildAt(realPosition),
                realPosition, mTopListView.getItemIdAtPosition(realPosition));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, overlayActionBar() ?
                R.layout.mdl_activity_navigation_drawer_full
                : R.layout.mdl_activity_navigation_drawer);

        if (savedInstanceState != null) mAccountsPositions = savedInstanceState.getIntArray("cc");

        defineDrawerLayout();
        defineListTop();
        defineListBottom();

        initFragment(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("current_fragment_position", mTopListView.getCheckedItemPosition()
                - (isNavigationDrawerAccountHandlerEmpty() ? 0 : 1));
        if (mAccountsLayout != null) outState.putIntArray("cc", mAccountsLayout.mAccountsPositions);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToolbarSearch.SEARCH_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (mCurrentItem != null && mCurrentItem.getFragment() != null) {
            mCurrentItem.getFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    private void defineListTop() {
        mNavigationDrawerItemsTop = new ArrayList<>();
        mListTopAdapter = new NavigationDrawerAdapter(this,
                R.layout.mdl_navigation_drawer_row, mNavigationDrawerItemsTop);
        mTopListView = (ListView) findViewById(R.id.left_drawer_listview);
        mTopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemTopClick(adapterView, view, i);
            }

        });
        showAccountsLayout();
    }

    private void defineListBottom() {
        mNavigationDrawerItemsBottom = new ArrayList<>();
        mListBottomAdapter = new NavigationDrawerBottomAdapter(
                this,
                R.layout.mdl_navigation_drawer_row,
                mNavigationDrawerItemsBottom);
        mBottomListView = (ListView)
                findViewById(R.id.left_drawer_bottom_listview);
        mBottomListView.setAdapter(mListBottomAdapter);
        mBottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemBottomClick(adapterView, view, i);
            }

        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBottomListView.setBackgroundResource(R.drawable.navigation_drawer_bottom_shadow);
        }
    }

    private void defineDrawerLayout() {
        mDrawerLeft = findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getMaterialDesignActionBar(),
                R.string.mdl_navigation_drawer_open,
                R.string.mdl_navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if (mCurrentItem != null) replaceTitle(mCurrentItem);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                replaceTitle(getTitle().toString());
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initFragment(Bundle savedInstanceState) {
        int fragmentPosition;
        boolean isSavedInstanceState = false;
        if (savedInstanceState != null) {
            isSavedInstanceState = true;
            fragmentPosition = savedInstanceState.getInt("current_fragment_position", 0);
        } else fragmentPosition = defaultNavigationDrawerItemSelectedPosition();

        replaceTopItems(getNavigationDrawerTopHandler());
        replaceBottomItems(getNavigationDrawerBottomHandler());
        selectDefaultItemPosition(fragmentPosition, isSavedInstanceState);
    }

    private void replaceTopItems(NavigationDrawerTopHandler navigationDrawerTopHandler) {
        mNavigationDrawerItemsTop.clear();

        if (navigationDrawerTopHandler != null &&
                navigationDrawerTopHandler.getNavigationDrawerTopItems() != null) {
            mNavigationDrawerItemsTop
                    .addAll(navigationDrawerTopHandler.getNavigationDrawerTopItems());
        }

        if (mCurrentItem != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mCurrentItem.getFragment()).commit();
            mTopListView.setItemChecked(mCurrentItemPosition, false);
        }

        mListTopAdapter.notifyDataSetChanged();
    }

    private void replaceBottomItems(NavigationDrawerBottomHandler navigationDrawerBottomHandler) {
        mNavigationDrawerItemsBottom.clear();

        if (navigationDrawerBottomHandler != null &&
                navigationDrawerBottomHandler.getNavigationDrawerBottomItems() != null) {
            mNavigationDrawerItemsBottom.addAll(navigationDrawerBottomHandler
                    .getNavigationDrawerBottomItems());
        }

        mListBottomAdapter.notifyDataSetChanged();
    }

    private void selectDefaultItemPosition(int fragmentPosition,
                                           boolean isSavedInstanceState) {
        if (mNavigationDrawerItemsTop.size() <= 0) return;

        if (fragmentPosition < 0 || fragmentPosition >= mNavigationDrawerItemsTop.size()) {
            fragmentPosition = 0;
        }

        ListItem item = mNavigationDrawerItemsTop.get(fragmentPosition);
        if (item instanceof NavigationDrawerListItemTopFragment) {
            NavigationDrawerListItemTopFragment itemFragment =
                    (NavigationDrawerListItemTopFragment) item;
            if (!isSavedInstanceState) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, itemFragment.getFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, itemFragment.getFragment())
                        .commit();
            }
            mCurrentItem = itemFragment;
            mCurrentItemPosition = (isNavigationDrawerAccountHandlerEmpty() ? 0 : 1) +
                    fragmentPosition;

            mTopListView.setItemChecked(mCurrentItemPosition, true);
            replaceTitle(mCurrentItem);
        } else {
            for (int i = 0; i < mNavigationDrawerItemsTop.size(); ++i) {
                if (mNavigationDrawerItemsTop.get(i) instanceof
                        NavigationDrawerListItemTopFragment) {
                    NavigationDrawerListItemTopFragment itemFragment =
                            (NavigationDrawerListItemTopFragment) mNavigationDrawerItemsTop.get(i);

                    if (!isSavedInstanceState) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, itemFragment.getFragment())
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, itemFragment.getFragment())
                                .commit();
                    }
                    mCurrentItem = itemFragment;
                    mCurrentItemPosition = (isNavigationDrawerAccountHandlerEmpty() ?
                            0 : 1) + i;

                    mTopListView.setItemChecked(mCurrentItemPosition, true);
                    replaceTitle(mCurrentItem);
                    break;
                }
            }
        }

        if (!isNavigationDrawerAccountHandlerEmpty()) {
            mAccountsLayout.notifyListTopItemSelectedChanged();
        }
    }

    private void onListItemTopClick(AdapterView<?> adapterView, View view, int i) {
        ListItem item = (ListItem) adapterView.getAdapter().getItem(i);

        if (item instanceof NavigationDrawerListItemTopFragment) {
            NavigationDrawerListItemTopFragment itemFragment =
                    (NavigationDrawerListItemTopFragment) item;
            Fragment fragment = itemFragment.getFragment();
            if (mCurrentItem == null || mCurrentItem.getFragment() != fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
                mCurrentItem = itemFragment;
                mCurrentItemPosition = i;
            }
            mTopListView.setItemChecked(mCurrentItemPosition, true);
            replaceTitle(mCurrentItem);
            closeNavigationDrawer();
        } else if (item instanceof NavigationDrawerListItemTopIntent) {
            mTopListView.setItemChecked(mCurrentItemPosition, true);
            startActivity(((NavigationDrawerListItemTopIntent) item).getIntent());
        } else if (item instanceof NavigationDrawerAccountsListItemIntent) {
            startActivity(((NavigationDrawerAccountsListItemIntent) item).getIntent());
        } else if (item instanceof NavigationDrawerAccountsListItemOnClick) {
            View.OnClickListener onClickListener = ((NavigationDrawerAccountsListItemOnClick) item)
                    .getOnClickListener();

            if (onClickListener != null) onClickListener.onClick(view);
        } else if (item instanceof NavigationDrawerAccountsListItemAccount) {
            OnMoreAccountClickListener onClickListener =
                    ((NavigationDrawerAccountsListItemAccount) item).getOnClickListener();

            if (onClickListener != null) onClickListener.onMoreAccountClick(view, i);
        }
    }

    private void onListItemBottomClick(AdapterView<?> adapterView, View view, int i) {
        View.OnClickListener onClickListener = ((NavigationDrawerListItemBottom)
                adapterView.getAdapter().getItem(i)).getOnClickListener();

        if (onClickListener != null) onClickListener.onClick(view);
    }

    private void showAccountsLayout() {
        mNavigationDrawerAccountsHandler = getNavigationDrawerAccountsHandler();
        if (!isNavigationDrawerAccountHandlerEmpty()) {
            if (mNavigationDrawerAccountsHandler.useSmallAccountsLayout()) {
                mAccountsLayout = new NavigationDrawerAccountsLayoutSmall(getApplicationContext());
            } else {
                mAccountsLayout = new NavigationDrawerAccountsLayout(getApplicationContext());
            }

            if (mAccountsPositions != null) {
                mAccountsLayout.mAccountsPositions = mAccountsPositions;
                mAccountsLayout.isRestored = true;
            }
            mAccountsLayout.setListView(mTopListView);
            mAccountsLayout.setListViewAdapter(mListTopAdapter);
            mAccountsLayout.setAccounts(mNavigationDrawerAccountsHandler
                    .getNavigationDrawerAccounts());
            mAccountsLayout.setOnAccountChangeListener(this);
            if (getNavigationDrawerAccountsMenuHandler() != null) {
                mAccountsLayout.setNavigationDrawerAccountsMenuItems(
                        getNavigationDrawerAccountsMenuHandler()
                                .getNavigationDrawerAccountsMenuItems());
            }

            mTopListView.addHeaderView(mAccountsLayout, null, false);
        }
        mTopListView.setAdapter(mListTopAdapter);
    }

    private void replaceTitle(NavigationDrawerListItemTopFragment itemFragment) {
        if (itemFragment.getFragment() instanceof ViewPagerFragment) {
            ViewPagerFragment viewPagerFragment = (ViewPagerFragment) itemFragment.getFragment();
            if (viewPagerFragment.replaceActionBarTitleByViewPagerPageTitle()) {
                CharSequence title = viewPagerFragment.getTitle();
                if (title != null) {
                    replaceTitle(title);
                    return;
                }
            }
        }
        replaceTitle(itemFragment.getTitle());
    }

    private void replaceTitle(CharSequence title) {
        if (replaceActionBarTitleByNavigationDrawerItemTitle()) {
            getSupportActionBar().setTitle(title);
        }
    }

    private boolean isNavigationDrawerAccountHandlerEmpty() {
        return mNavigationDrawerAccountsHandler == null ||
                mNavigationDrawerAccountsHandler.getNavigationDrawerAccounts().size() <= 0;
    }

}
