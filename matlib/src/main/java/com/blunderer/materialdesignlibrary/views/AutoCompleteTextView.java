package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import com.blunderer.materialdesignlibrary.listeners.OnSearchDynamicListener;
import com.blunderer.materialdesignlibrary.listeners.OnSearchingListener;

import java.util.List;

public class AutoCompleteTextView extends EditText
        implements OnSearchingListener, Filter.FilterListener {

    private ArrayAdapter mAdapter;
    private ListView mListView;
    private int mThreshold;
    private boolean mAutoCompletionEnabled;
    private boolean mAutoCompletionDynamic;
    private OnSearchDynamicListener mOnSearchDynamicListener;

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mThreshold = 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGettingResults(List<String> results) {
        if (mAdapter == null) return;

        mAdapter.clear();
        for (String item : results) mAdapter.add(item);
        mAdapter.getFilter().filter(getText(), AutoCompleteTextView.this);
    }

    @Override
    public void onFilterComplete(int count) {
        setListViewHeight(48, count);
    }

    public ArrayAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ArrayAdapter adapter) {
        mAdapter = adapter;

        if (mListView != null) {
            mListView.setAdapter(mAdapter);
            mAdapter.getFilter().filter("");
        }
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        mListView = listView;

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mAdapter.getFilter().filter("");
        }
    }

    public int getThreshold() {
        return mThreshold;
    }

    public void setThreshold(int threshold) {
        if (threshold <= 0) mThreshold = 1;
        else mThreshold = threshold;
    }

    public boolean isAutoCompletionEnabled() {
        return mAutoCompletionEnabled;
    }

    public void setAutoCompletionEnabled(boolean autoCompletionEnabled) {
        mAutoCompletionEnabled = autoCompletionEnabled;

        if (!autoCompletionEnabled) return;

        addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter == null) return;

                if (s == null || s.length() < mThreshold) {
                    mAdapter.getFilter().filter("", AutoCompleteTextView.this);
                    onFilterComplete(0);
                    return;
                }

                if (mAutoCompletionDynamic && mOnSearchDynamicListener != null) {
                    mOnSearchDynamicListener.onSearching(s.toString(), AutoCompleteTextView.this);
                } else if (mAdapter != null) {
                    mAdapter.getFilter().filter(s, AutoCompleteTextView.this);
                }
            }

        });
    }

    public boolean isAutoCompletionDynamic() {
        return mAutoCompletionDynamic;
    }

    public void setAutoCompletionDynamic(boolean autoCompletionDynamic) {
        mAutoCompletionDynamic = autoCompletionDynamic;
    }

    public void setOnSearchDynamicListener(OnSearchDynamicListener onSearchDynamicListener) {
        mOnSearchDynamicListener = onSearchDynamicListener;
    }

    public void clear() {
        setText("");
    }

    private void setListViewHeight(int rowHeight, int rowCount) {
        ViewGroup.LayoutParams lp = mListView.getLayoutParams();
        lp.height = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                rowHeight, getResources().getDisplayMetrics()) * rowCount);
        mListView.setLayoutParams(lp);
    }

}
