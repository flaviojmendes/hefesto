package com.blunderer.materialdesignlibrary.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.models.ListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemBottom;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemHeader;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopFragment;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopIntent;

import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter<ListItem> {

    private int mLayoutResourceId;

    public NavigationDrawerAdapter(Context context,
                                   int layoutResourceId,
                                   List<ListItem> objects) {
        super(context, layoutResourceId, objects);

        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof NavigationDrawerListItemTopFragment ||
                getItem(position) instanceof NavigationDrawerListItemTopIntent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(mLayoutResourceId, parent, false);

            if (item instanceof NavigationDrawerListItemTopFragment ||
                    item instanceof NavigationDrawerListItemTopIntent ||
                    item instanceof NavigationDrawerListItemBottom) {
                convertView.setBackgroundResource(R.drawable.navigation_drawer_selector);
            }
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.navigation_drawer_row_title);
            holder.titleHeader = (TextView) convertView
                    .findViewById(R.id.navigation_drawer_row_header);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.navigation_drawer_row_icon);
            holder.headerSeparator = convertView
                    .findViewById(R.id.navigation_drawer_row_header_separator);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        if (item.useTitleResource()) {
            try {
                holder.title.setText(item.getTitle());
                holder.titleHeader.setText(item.getTitle());
            } catch (Resources.NotFoundException e) {
                holder.title.setText("");
                holder.titleHeader.setText("");
            }
        }

        if (item instanceof NavigationDrawerListItemTopFragment) {
            NavigationDrawerListItemTopFragment itemNormal =
                    (NavigationDrawerListItemTopFragment) item;
            holder.title.setVisibility(View.VISIBLE);
            holder.titleHeader.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(View.GONE);
            if (itemNormal.useIconResource()) {
                try {
                    holder.icon.setImageDrawable(itemNormal.getIcon());
                    holder.icon.setVisibility(View.VISIBLE);
                } catch (Resources.NotFoundException e) {
                    holder.icon.setVisibility(View.GONE);
                }
            }
        } else if (item instanceof NavigationDrawerListItemTopIntent) {
            NavigationDrawerListItemTopIntent itemNormal =
                    (NavigationDrawerListItemTopIntent) item;
            holder.title.setVisibility(View.VISIBLE);
            holder.titleHeader.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(View.GONE);
            if (itemNormal.useIconResource()) {
                try {
                    holder.icon.setImageDrawable(itemNormal.getIcon());
                    holder.icon.setVisibility(View.VISIBLE);
                } catch (Resources.NotFoundException e) {
                    holder.icon.setVisibility(View.GONE);
                }
            }
        } else if (item instanceof NavigationDrawerListItemHeader) {
            holder.title.setVisibility(View.GONE);
            holder.titleHeader.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView title;
        private TextView titleHeader;
        private ImageView icon;
        private View headerSeparator;

    }

}
