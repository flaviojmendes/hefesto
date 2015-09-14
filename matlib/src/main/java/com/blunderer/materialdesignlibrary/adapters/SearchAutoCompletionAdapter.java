package com.blunderer.materialdesignlibrary.adapters;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchAutoCompletionAdapter extends ArrayAdapter<String> {

    private final boolean mIsAutoCompletionDynamic;
    private final ToolbarSearch.AutoCompletionMode mAutoCompletionMode;
    private int mLayoutResource;
    private ArrayList<String> mItemsToSearchIn;
    private ArrayList<String> mItemsFound;
    private CharSequence mConstraint;
    private Filter mFilter;

    @SuppressWarnings("unchecked")
    public SearchAutoCompletionAdapter(Context context,
                                       int layoutResource,
                                       ArrayList<String> items,
                                       ArrayList<String> itemsFound,
                                       boolean isAutoCompletionDynamic,
                                       ToolbarSearch.AutoCompletionMode autoCompletionMode) {
        super(context, layoutResource, items);

        mLayoutResource = layoutResource;
        mItemsToSearchIn = items;
        mItemsFound = itemsFound;
        mIsAutoCompletionDynamic = isAutoCompletionDynamic;
        mAutoCompletionMode = autoCompletionMode;
        mConstraint = "";
    }

    @Override
    public int getCount() {
        return mItemsFound.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutResource, parent, false);

            holder = new ViewHolder();
            holder.mText = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        String item = mItemsFound.get(position);

        if (mAutoCompletionMode == ToolbarSearch.AutoCompletionMode.CONTAINS) {
            int constraintPosition;
            if ((constraintPosition = item.indexOf(mConstraint.toString())) == -1) {
                holder.mText.setText(item);
            } else {
                String prefix = item.substring(0, constraintPosition);
                String middle = item.substring(constraintPosition, constraintPosition + mConstraint.length());
                String suffix = item.substring(constraintPosition + mConstraint.length(), item.length());
                holder.mText.setText(Html.fromHtml(prefix + "<font color=\"#c5c5c5\">" + middle + "</font>" + suffix));
            }
        } else {
            String prefix = item.substring(0, mConstraint.length());
            String suffix = item.substring(mConstraint.length(), item.length());
            holder.mText.setText(Html.fromHtml("<font color=\"#c5c5c5\">" + prefix + "</font>" + suffix));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) mFilter = new SearchFilter();
        return mFilter;
    }

    private class ViewHolder {

        private TextView mText;

    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Filter.FilterResults filterResults = new FilterResults();
            List<String> results = new ArrayList<>();

            if (TextUtils.isEmpty(constraint)) {
                filterResults.values = results;
                filterResults.count = 0;
                return filterResults;
            }

            for (String item : mItemsToSearchIn) {
                if (mIsAutoCompletionDynamic) results.add(item);
                else if (mAutoCompletionMode == ToolbarSearch.AutoCompletionMode.CONTAINS) {
                    if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        results.add(item);
                    }
                } else {
                    if (item.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        results.add(item);
                    }
                }
            }

            filterResults.values = results;
            filterResults.count = results.size();

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            if (!(filterResults.values instanceof List<?>)) return;

            mConstraint = constraint;

            List<String> results = (List<String>) filterResults.values;
            mItemsFound.clear();
            mItemsFound.addAll(results);

            notifyDataSetChanged();
        }

    }

}
