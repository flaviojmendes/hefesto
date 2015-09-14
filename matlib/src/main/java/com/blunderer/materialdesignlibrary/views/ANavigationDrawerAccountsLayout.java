package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.adapters.NavigationDrawerAccountsMenuAdapter;
import com.blunderer.materialdesignlibrary.listeners.OnAccountChangeListener;
import com.blunderer.materialdesignlibrary.listeners.OnMoreAccountClickListener;
import com.blunderer.materialdesignlibrary.models.Account;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItem;

import java.util.List;

public abstract class ANavigationDrawerAccountsLayout extends LinearLayout {

    protected List<Account> mAccounts;
    protected ImageView mAccountsMenuSwitch;
    protected ImageView mBackground;
    protected TextView mTitle;
    protected TextView mDescription;
    protected RoundedImageView mPicture;
    protected NavigationDrawerAccountsMenuAdapter mAccountsMenuAdapter;
    protected ListView mOriginalListView;
    protected ListAdapter mOriginalAdapter;
    protected List<NavigationDrawerAccountsListItem> mAccountsMenuItems;
    protected ViewGroup mMainLayout;
    protected int mOriginalListViewSelectedItemPosition;
    protected boolean mIsAccountsMenuEnabled;
    protected boolean mShowAccountMenu = false;
    protected OnAccountChangeListener mOnAccountChangeListener;

    public int[] mAccountsPositions;
    public boolean isRestored = false;

    public ANavigationDrawerAccountsLayout(Context context) {
        this(context, null);
    }

    public ANavigationDrawerAccountsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init(int layoutResource) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResource, this, true);

        mMainLayout = (ViewGroup) getChildAt(0);

        setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));
        setPadding(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics()));
        setOrientation(VERTICAL);

        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                updateMoreAccountsList();

                if (Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }

        });
    }

    /**
     * Set a ListView that will be used to show the Accounts Menu
     * when clicking on the Accounts Layout.
     * You should also set its adapter by calling setListViewAdapter.
     *
     * @param listView The ListView that is used to show the Accounts Menu.
     */
    public void setListView(ListView listView) {
        if (listView == null) throw new IllegalArgumentException("The ListView must not be null");

        mOriginalListView = listView;
        if (mOriginalAdapter != null) initAccountsLayout();
    }

    /**
     * Set Adapter for the Accounts Menu ListView.
     * You should also set the ListView by calling setListView.
     *
     * @param adapter The Adapter that is used by the Accounts Menu ListView.
     */
    public void setListViewAdapter(ListAdapter adapter) {
        if (adapter == null)
            throw new IllegalArgumentException("The ListView Adapter must not be null");

        mOriginalAdapter = adapter;
        if (mOriginalListView != null) initAccountsLayout();
    }

    /**
     * Set a List of Accounts that will be displayed.
     *
     * @param accounts the List of Accounts displayed.
     */
    public void setAccounts(List<Account> accounts) {
        if (accounts == null || accounts.size() <= 0) {
            throw new IllegalArgumentException(
                    "The accounts list must contain at least one account");
        }

        mAccounts = accounts;
        if (isRestored) isRestored = false;
        else resetAccountsPositions(accounts.size());
        initAccounts();
    }

    /**
     * Set a List of Items for the Accounts Menu.
     *
     * @param accountsMenuItems the List of items for the Accounts Menu.
     */
    public void setNavigationDrawerAccountsMenuItems(
            List<NavigationDrawerAccountsListItem> accountsMenuItems) {
        if (accountsMenuItems != null && accountsMenuItems.size() > 0) {
            mShowAccountMenu = true;
            mAccountsMenuItems = accountsMenuItems;
            initAccountsMenuSwitch();
            mAccountsMenuAdapter = new NavigationDrawerAccountsMenuAdapter(
                    getContext(),
                    R.layout.mdl_navigation_drawer_row,
                    mAccountsMenuItems);
        }
    }

    /**
     * Registers a callback to be invoked when the current account has changed.
     *
     * @param onAccountChangeListener The callback that will run.
     */
    public void setOnAccountChangeListener(OnAccountChangeListener onAccountChangeListener) {
        mOnAccountChangeListener = onAccountChangeListener;
    }

    public void notifyListTopItemSelectedChanged() {
        mOriginalListViewSelectedItemPosition = mOriginalListView.getCheckedItemPosition();
    }

    protected void changeAccount() {
        Account currentAccount = getAccount(0);
        if (currentAccount == null) return;

        if (currentAccount.useBackgroundDrawable()) {
            mBackground.setImageDrawable(currentAccount.getBackgroundDrawable());
        } else mBackground.setImageResource(currentAccount.getBackgroundResource());

        mPicture.setImageDrawable(currentAccount.getPicture());

        if (!TextUtils.isEmpty(currentAccount.getTitle())) {
            mTitle.setText(currentAccount.getTitle());
        } else mTitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(currentAccount.getDescription())) {
            mDescription.setText(currentAccount.getDescription());
        } else mDescription.setVisibility(View.GONE);
    }

    protected Account getAccount(int position) {
        if (mAccountsPositions == null || position >= mAccountsPositions.length) return null;

        if (mAccountsPositions[position] < 0 || mAccountsPositions[position] >= mAccounts.size()) {
            if (mAccounts.size() > position) mAccountsPositions[position] = position;
            else return null;
        }
        return mAccounts.get(mAccountsPositions[position]);
    }

    private void resetAccountsPositions(int size) {
        mAccountsPositions = new int[size];
        for (int i = 0; i < size; ++i) mAccountsPositions[i] = i;
    }

    private void initAccountsLayout() {
        mIsAccountsMenuEnabled = false;

        mMainLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mShowAccountMenu) return;

                if (mIsAccountsMenuEnabled) {
                    mOriginalListView.setAdapter(mOriginalAdapter);
                    if (mOriginalListViewSelectedItemPosition != ListView.INVALID_POSITION) {
                        mOriginalListView.setItemChecked(
                                mOriginalListViewSelectedItemPosition, true);
                    }
                    mAccountsMenuSwitch.setImageResource(R.drawable.ic_arrow_drop_down);
                } else {
                    mOriginalListViewSelectedItemPosition = mOriginalListView
                            .getCheckedItemPosition();
                    mOriginalListView.setAdapter(mAccountsMenuAdapter);
                    mAccountsMenuSwitch.setImageResource(R.drawable.ic_arrow_drop_up);
                }

                mIsAccountsMenuEnabled = !mIsAccountsMenuEnabled;
            }

        });
    }

    protected abstract void initAccounts();

    protected abstract void initAccountsMenuSwitch();

    protected abstract void updateMoreAccountsList();

    protected abstract OnMoreAccountClickListener generateAccountClickListener(final int accountId);

}
