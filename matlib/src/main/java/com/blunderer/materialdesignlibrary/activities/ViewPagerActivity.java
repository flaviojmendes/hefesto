package com.blunderer.materialdesignlibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.adapters.ViewPagerAdapter;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.blunderer.materialdesignlibrary.models.ViewPagerItem;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public abstract class ViewPagerActivity extends AActivity
        implements com.blunderer.materialdesignlibrary.interfaces.ViewPager {

    protected ViewPager mViewPager;
    protected CirclePageIndicator mViewPagerIndicator;
    private List<ViewPagerItem> mViewPagerItems;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager
            .OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            replaceTitle(mViewPagerItems.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToolbarSearch.SEARCH_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (mViewPagerItems != null && mViewPagerItems.size() > 0 && mViewPager != null) {
            int tabPosition = mViewPager.getCurrentItem();
            if (tabPosition >= 0 && tabPosition < mViewPagerItems.size()) {
                mViewPagerItems.get(tabPosition).getFragment()
                        .onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.mdl_activity_view_pager);

        ViewPagerHandler handler = getViewPagerHandler();
        if (handler != null && handler.getViewPagerItems() != null) {
            mViewPagerItems = handler.getViewPagerItems();
        }

        if (mViewPagerItems != null && mViewPagerItems.size() > 0) {
            mViewPager = (ViewPager) findViewById(R.id.viewpager);
            mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),
                    mViewPagerItems));

            int defaultViewPagerItemSelectedPosition = defaultViewPagerPageSelectedPosition();
            if (defaultViewPagerItemSelectedPosition >= 0 &&
                    defaultViewPagerItemSelectedPosition < mViewPagerItems.size()) {
                mViewPager.setCurrentItem(defaultViewPagerItemSelectedPosition);
            } else defaultViewPagerItemSelectedPosition = 0;

            showIndicator(mViewPager);

            replaceTitle(mViewPagerItems
                    .get(defaultViewPagerItemSelectedPosition).getTitle());
        }
    }

    private void showIndicator(ViewPager pager) {
        if (!showViewPagerIndicator()) {
            pager.setOnPageChangeListener(mOnPageChangeListener);
        } else {
            mViewPagerIndicator = (CirclePageIndicator)
                    findViewById(R.id.viewpagerindicator);
            mViewPagerIndicator.setViewPager(pager);
            mViewPagerIndicator.setVisibility(View.VISIBLE);
            mViewPagerIndicator.setOnPageChangeListener(mOnPageChangeListener);
        }
    }

    private void replaceTitle(String title) {
        if (replaceActionBarTitleByViewPagerPageTitle()) getSupportActionBar().setTitle(title);
    }

    public abstract boolean showViewPagerIndicator();

    public abstract boolean replaceActionBarTitleByViewPagerPageTitle();

}
