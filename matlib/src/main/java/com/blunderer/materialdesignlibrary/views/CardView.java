package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.R;
import com.joooonho.SelectableRoundedImageView;

import carbon.widget.Button;

public class CardView extends android.support.v7.widget.CardView {

    // Image Positions
    public final static int POSITION_NONE = 0;
    public final static int POSITION_LEFT = 1;
    public final static int POSITION_TOP = 2;

    // Variables
    private int mImagePosition;
    private Drawable mImage;
    private String mTitleText;
    private String mDescriptionText;
    private String mNormalButtonText;
    private String mHighlightButtonText;
    private Button mNormalButton;
    private Button mHighlightButton;

    // Listeners
    private OnClickListener mOnNormalButtonClickListener;
    private OnClickListener mOnHighlightButtonClickListener;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MDLCardView, 0, 0);

        try {
            mImagePosition = a.getInt(R.styleable.MDLCardView_mdl_imagePosition, 0);
            mImage = a.getDrawable(R.styleable.MDLCardView_mdl_image);
            mTitleText = a.getString(R.styleable.MDLCardView_mdl_title);
            mDescriptionText = a.getString(R.styleable.MDLCardView_mdl_description);
            mNormalButtonText = a.getString(R.styleable.MDLCardView_mdl_normalButton);
            mHighlightButtonText = a.getString(R.styleable.MDLCardView_mdl_highlightButton);
        } finally {
            a.recycle();
        }

        setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics()));
        setPreventCornerOverlap(false);
        setCardBackgroundColor(context.getResources().getColor(R.color.mdl_cardview_background));

        inflate();
    }

    public int getImagePosition() {
        return mImagePosition;
    }

    public void setImagePosition(int imagePosition) {
        mImagePosition = imagePosition;

        inflate();
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        mImage = imageDrawable;

        refresh();
    }

    public void setImageResource(int imageResource) {
        mImage = getContext().getResources().getDrawable(imageResource);

        refresh();
    }

    public String getTitle() {
        return mTitleText;
    }

    public void setTitle(String title) {
        mTitleText = title;

        refresh();
    }

    public void setTitleResource(int titleResource) {
        mTitleText = getContext().getString(titleResource);

        refresh();
    }

    public String getDescription() {
        return mDescriptionText;
    }

    public void setDescription(String description) {
        mDescriptionText = description;

        refresh();
    }

    public void setDescriptionResource(int descriptionResource) {
        mDescriptionText = getContext().getString(descriptionResource);

        refresh();
    }

    public Button getNormalButton() {
        return mNormalButton;
    }

    public void setNormalButtonText(String normalButtonText) {
        mNormalButtonText = normalButtonText;

        refresh();
    }

    public void setNormalButtonTextResource(int normalButtonTextResource) {
        mNormalButtonText = getContext().getString(normalButtonTextResource);

        refresh();
    }

    public Button getHighlightButton() {
        return mHighlightButton;
    }

    public void setHighlightButtonText(String highlightButtonText) {
        mHighlightButtonText = highlightButtonText;

        refresh();
    }

    public void setHighlightButtonTextResource(int highlightButtonTextResource) {
        mHighlightButtonText = getContext().getString(highlightButtonTextResource);

        refresh();
    }

    public OnClickListener getOnNormalButtonClickListener() {
        return mOnNormalButtonClickListener;
    }

    public void setOnNormalButtonClickListener(OnClickListener onNormalButtonClickListener) {
        mOnNormalButtonClickListener = onNormalButtonClickListener;
    }

    public OnClickListener getOnHighlightButtonClickListener() {
        return mOnHighlightButtonClickListener;
    }

    public void setOnHighlightButtonClickListener(OnClickListener onHighlightButtonClickListener) {
        mOnHighlightButtonClickListener = onHighlightButtonClickListener;
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mImagePosition == POSITION_LEFT) inflateCardViewImageLeft(inflater);
        else if (mImagePosition == POSITION_TOP) inflateCardViewImageTop(inflater);
        else inflateCardViewNormal(inflater);
    }

    private void refresh() {
        if (mImagePosition == POSITION_LEFT) {
            LinearLayout layoutAll = (LinearLayout) getChildAt(0);
            handleImage(layoutAll, 0);

            LinearLayout layout = (LinearLayout) layoutAll.getChildAt(1);
            handleTitle(layout, 0);
            handleDescription(layout, 1);
            handleSpacing(layout, 2);
            handleCardViewNormalAndImageLeftButtons(layout, 3);
        } else if (mImagePosition == POSITION_TOP) {
            LinearLayout layoutAll = (LinearLayout) getChildAt(0);
            RelativeLayout layoutImageTitle = (RelativeLayout) layoutAll.getChildAt(0);
            handleImage(layoutImageTitle, 0);
            handleTitle(layoutImageTitle, 1);

            LinearLayout layout = (LinearLayout) layoutAll.getChildAt(1);
            handleDescription(layout, 0);
            handleCardViewImageTopButtons(layout, 1);
        } else {
            LinearLayout layout = (LinearLayout) getChildAt(0);

            handleTitle(layout, 0);
            handleDescription(layout, 1);
            handleSpacing(layout, 2);
            handleCardViewNormalAndImageLeftButtons(layout, 3);
        }
    }

    private void inflateCardViewNormal(LayoutInflater inflater) {
        removeAllViews();
        inflater.inflate(R.layout.mdl_cardview_normal, this, true);

        refresh();
    }

    private void inflateCardViewImageLeft(LayoutInflater inflater) {
        removeAllViews();
        inflater.inflate(R.layout.mdl_cardview_image_left, this, true);

        refresh();
    }

    private void inflateCardViewImageTop(LayoutInflater inflater) {
        removeAllViews();
        inflater.inflate(R.layout.mdl_cardview_image_top, this, true);

        refresh();
    }

    private void handleImage(ViewGroup layout, int position) {
        SelectableRoundedImageView image = (SelectableRoundedImageView) layout.getChildAt(position);
        image.setImageDrawable(mImage);
        if (TextUtils.isEmpty(mDescriptionText) && TextUtils.isEmpty(mNormalButtonText) &&
                TextUtils.isEmpty(mHighlightButtonText)) {
            image.setCornerRadiiDP(2, 2, 2, 2);
        }
    }

    private void handleTitle(ViewGroup layout, int position) {
        TextView title = (TextView) layout.getChildAt(position);
        if (!TextUtils.isEmpty(mTitleText)) {
            title.setText(mTitleText);
            title.setVisibility(VISIBLE);
        } else title.setVisibility(GONE);
    }

    private void handleDescription(ViewGroup layout, int position) {
        TextView description = (TextView) layout.getChildAt(position);
        if (!TextUtils.isEmpty(mDescriptionText)) {
            description.setText(mDescriptionText);
            description.setVisibility(VISIBLE);
        } else description.setVisibility(GONE);
    }

    private void handleCardViewNormalAndImageLeftButtons(ViewGroup layout, int position) {
        View separatorButtons = layout.getChildAt(position);
        if (!TextUtils.isEmpty(mNormalButtonText) || !TextUtils.isEmpty(mHighlightButtonText)) {
            if (!TextUtils.isEmpty(mTitleText) || !TextUtils.isEmpty(mDescriptionText)) {
                separatorButtons.setVisibility(VISIBLE);
            } else {
                layout.setPadding(layout.getPaddingLeft(), 0,
                        layout.getPaddingRight(), layout.getPaddingBottom());
            }
        } else separatorButtons.setVisibility(GONE);
        handleButtons(layout, position);
    }

    private void handleCardViewImageTopButtons(ViewGroup layout, int position) {
        if (!TextUtils.isEmpty(mNormalButtonText) || !TextUtils.isEmpty(mHighlightButtonText)) {
            View separatorButtons = layout.getChildAt(position);
            if (!TextUtils.isEmpty(mDescriptionText)) separatorButtons.setVisibility(VISIBLE);
            else separatorButtons.setVisibility(GONE);

            handleButtons(layout, position);
        }
    }

    private void handleButtons(ViewGroup layout, int position) {
        LinearLayout buttonsLayout = (LinearLayout) layout.getChildAt(position + 1);
        buttonsLayout.setVisibility(VISIBLE);

        mNormalButton = (Button) buttonsLayout.getChildAt(0);
        if (!TextUtils.isEmpty(mNormalButtonText)) {
            mNormalButton.setText(mNormalButtonText);
            mNormalButton.setVisibility(VISIBLE);
            mNormalButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnNormalButtonClickListener != null) {
                        mOnNormalButtonClickListener.onClick(view);
                    }
                }

            });
        } else mNormalButton.setVisibility(GONE);

        mHighlightButton = (Button) buttonsLayout.getChildAt(1);
        if (!TextUtils.isEmpty(mHighlightButtonText)) {
            mHighlightButton.setText(mHighlightButtonText);
            mHighlightButton.setVisibility(VISIBLE);
            mHighlightButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnHighlightButtonClickListener != null) {
                        mOnHighlightButtonClickListener.onClick(view);
                    }
                }

            });
        } else mHighlightButton.setVisibility(GONE);
    }

    private void handleSpacing(ViewGroup layout, int position) {
        View spacingView = layout.getChildAt(position);
        int spacingViewHeight = 0;
        if (!TextUtils.isEmpty(mTitleText) && TextUtils.isEmpty(mDescriptionText) &&
                TextUtils.isEmpty(mNormalButtonText) && TextUtils.isEmpty(mHighlightButtonText)) {
            spacingViewHeight = 6;
        } else if (!TextUtils.isEmpty(mTitleText) &&
                TextUtils.isEmpty(mDescriptionText) &&
                (!TextUtils.isEmpty(mNormalButtonText) ||
                        !TextUtils.isEmpty(mHighlightButtonText))) {
            spacingViewHeight = 10;
        } else if (TextUtils.isEmpty(mTitleText) &&
                TextUtils.isEmpty(mDescriptionText) &&
                TextUtils.isEmpty(mNormalButtonText) &&
                TextUtils.isEmpty(mHighlightButtonText)) {
            spacingViewHeight = 2;
        } else if (!TextUtils.isEmpty(mDescriptionText) &&
                (!TextUtils.isEmpty(mNormalButtonText) ||
                        !TextUtils.isEmpty(mHighlightButtonText))) {
            spacingViewHeight = 4;
        }

        int spacingViewHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                spacingViewHeight,
                getResources().getDisplayMetrics());
        spacingView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, spacingViewHeightPx));
    }

}
