package com.blunderer.materialdesignlibrary.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.activities.AActivity;
import com.blunderer.materialdesignlibrary.adapters.SearchAutoCompletionAdapter;
import com.blunderer.materialdesignlibrary.listeners.OnSearchDynamicListener;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;

import java.util.ArrayList;
import java.util.List;

public class ToolbarSearch extends com.blunderer.materialdesignlibrary.views.Toolbar {

    public static final int SEARCH_REQUEST_CODE = 123;

    protected android.support.v7.widget.CardView mSearchBar;
    protected ImageView mSearchBarBackButton;
    protected ImageView mSearchBarActionButton;
    protected AutoCompleteTextView mSearchBarEditText;
    protected ListView mSearchListView;
    protected View mOpacityView;
    private Intent mSpeechIntent;
    private AActivity mActivity;
    private OnSearchListener mOnSearchListener;
    private ArrayList<String> mItems;

    public ToolbarSearch(Context context) {
        this(context, null);
    }

    public ToolbarSearch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarSearch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mdl_toolbar_search, this, true);

        mItems = new ArrayList<>();
        mToolbar = (Toolbar) getChildAt(0);

        initOpacityView();
        initSearchBar();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) mSearchBarEditText.setText(results.get(0));
            showSoftKeyboard(mSearchBarEditText);
            mSearchBarEditText.setSelection(mSearchBarEditText.getText().length());
        }
    }

    public boolean isSearchBarShown() {
        return mSearchBar.getVisibility() == VISIBLE;
    }

    public void showSearchBar() {
        mSearchBar.setVisibility(VISIBLE);
        mOpacityView.setVisibility(VISIBLE);
        mToolbar.setVisibility(INVISIBLE);
        showSoftKeyboard(mSearchBarEditText);
    }

    public void hideSearchBar() {
        mToolbar.setVisibility(VISIBLE);
        mSearchBar.setVisibility(INVISIBLE);
        mOpacityView.setVisibility(INVISIBLE);
        hideSoftKeyboard(mSearchBarEditText);
    }

    public void setActivity(AActivity activity) {
        mActivity = activity;
    }

    public String getConstraint() {
        return mSearchBarEditText.getText().toString();
    }

    public void setConstraint(String constraint) {
        mSearchBarEditText.setText(constraint);
    }

    public void setData(boolean autoCompletionEnabled,
                        boolean autoCompletionDynamic,
                        List<String> autoCompletionItems,
                        AutoCompletionMode autoCompletionMode,
                        int threshold,
                        OnSearchListener onSearchListener,
                        OnSearchDynamicListener onSearchDynamicListener) {
        mOnSearchListener = onSearchListener;

        if (!autoCompletionEnabled) return;
        if (autoCompletionItems == null) autoCompletionItems = new ArrayList<>();
        if (autoCompletionMode == null) autoCompletionMode = AutoCompletionMode.STARTS_WITH;

        mItems.clear();
        mItems.addAll(autoCompletionItems);
        final ArrayList<String> itemsFound = new ArrayList<>();

        SearchAutoCompletionAdapter adapter = new SearchAutoCompletionAdapter(getContext(),
                R.layout.mdl_toolbar_search_autocompletion_row, mItems, itemsFound,
                autoCompletionDynamic, autoCompletionMode);

        initSearchListView(itemsFound);

        mSearchBarEditText.setAutoCompletionEnabled(true);
        mSearchBarEditText.setAutoCompletionDynamic(autoCompletionDynamic);
        mSearchBarEditText.setListView(mSearchListView);
        mSearchBarEditText.setAdapter(adapter);
        mSearchBarEditText.setThreshold(threshold);
        mSearchBarEditText.setOnSearchDynamicListener(onSearchDynamicListener);
    }

    private void initSearchListView(final ArrayList<String> itemsFound) {
        mSearchListView = (ListView) ((ViewGroup) mSearchBar.getChildAt(0)).getChildAt(1);
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchBarEditText.clear();
                hideSearchBar();
                if (mOnSearchListener != null) {
                    mOnSearchListener.onSearched(itemsFound.get(position));
                }
            }

        });
        mSearchListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(mSearchBarEditText);
                return false;
            }

        });
    }

    private void initSearchBar() {
        mSearchBar = (android.support.v7.widget.CardView) getChildAt(2);

        ViewGroup searchBarLinearLayout = (ViewGroup) ((ViewGroup) mSearchBar.getChildAt(0))
                .getChildAt(0);
        initBackButton(searchBarLinearLayout);
        initSearchEditText(searchBarLinearLayout);
        initActionButtons(searchBarLinearLayout);
    }

    private void initBackButton(ViewGroup layout) {
        mSearchBarBackButton = (ImageView) layout.getChildAt(0);
        mSearchBarBackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSearchBar();
            }

        });
    }

    private void initSearchEditText(ViewGroup layout) {
        mSearchBarEditText = (AutoCompleteTextView) layout.getChildAt(1);
        mSearchBarEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        && !TextUtils.isEmpty(mSearchBarEditText.getText())) {
                    search();
                    return true;
                }
                return true;
            }

        });
        mSearchBarEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mSearchBarEditText.getText())) {
                    mSearchBarActionButton.setImageResource(R.drawable.ic_action_cancel);
                } else {
                    mSearchBarActionButton.setImageResource(R.drawable.ic_action_mic);
                }
            }

        });
    }

    private void initActionButtons(ViewGroup layout) {
        mSearchBarActionButton = (ImageView) layout.getChildAt(2);

        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-us");

        mSearchBarActionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSearchBarEditText.getText())) {
                    startVoiceSearch();
                } else {
                    mSearchBarEditText.clear();
                    mSearchBarActionButton.setImageResource(R.drawable.ic_action_mic);
                }
            }

        });
    }

    private void initOpacityView() {
        mOpacityView = getChildAt(1);
        mOpacityView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSearchBar();
                return true;
            }

        });
    }

    private void search() {
        String text = mSearchBarEditText.getText().toString();

        if (TextUtils.isEmpty(text)) return;

        mSearchBarEditText.clear();
        hideSearchBar();

        if (mOnSearchListener != null) {
            mOnSearchListener.onSearched(text);
        }
    }

    private void startVoiceSearch() {
        mActivity.startActivityForResult(mSpeechIntent, SEARCH_REQUEST_CODE);
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideSoftKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public enum AutoCompletionMode {

        STARTS_WITH, CONTAINS

    }

}
