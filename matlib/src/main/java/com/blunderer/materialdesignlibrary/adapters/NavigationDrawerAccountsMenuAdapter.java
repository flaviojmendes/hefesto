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
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemAccount;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemIntent;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemOnClick;

import java.util.List;

public class NavigationDrawerAccountsMenuAdapter
        extends ArrayAdapter<NavigationDrawerAccountsListItem> {

    private int mLayoutResourceId;

    public NavigationDrawerAccountsMenuAdapter(Context context,
                                               int layoutResourceId,
                                               List<NavigationDrawerAccountsListItem> objects) {
        super(context, layoutResourceId, objects);

        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.navigation_drawer_row_title);
            holder.titleHeader = (TextView) convertView
                    .findViewById(R.id.navigation_drawer_row_header);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.navigation_drawer_row_icon);
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

        if (item instanceof NavigationDrawerAccountsListItemIntent) {
            NavigationDrawerAccountsListItemIntent itemNormal =
                    (NavigationDrawerAccountsListItemIntent) item;
            holder.title.setVisibility(View.VISIBLE);
            holder.titleHeader.setVisibility(View.GONE);
            if (itemNormal.useIconResource()) {
                try {
                    holder.icon.setImageDrawable(itemNormal.getIcon());
                    holder.icon.setVisibility(View.VISIBLE);
                } catch (Resources.NotFoundException e) {
                    holder.icon.setVisibility(View.GONE);
                }
            }
        } else if (item instanceof NavigationDrawerAccountsListItemOnClick) {
            NavigationDrawerAccountsListItemOnClick itemAccount =
                    (NavigationDrawerAccountsListItemOnClick) item;
            holder.title.setVisibility(View.VISIBLE);
            holder.titleHeader.setVisibility(View.GONE);
            if (itemAccount.useIconResource()) {
                try {
                    holder.icon.setImageDrawable(itemAccount.getIcon());
                    holder.icon.setVisibility(View.VISIBLE);
                } catch (Resources.NotFoundException e) {
                    holder.icon.setVisibility(View.GONE);
                }
            }
        } else if (item instanceof NavigationDrawerAccountsListItemAccount) {
            NavigationDrawerAccountsListItemAccount itemAccount =
                    (NavigationDrawerAccountsListItemAccount) item;
            holder.title.setVisibility(View.VISIBLE);
            holder.titleHeader.setVisibility(View.GONE);
            if (itemAccount.useIconResource()) {
                try {
                    holder.icon.setImageDrawable(itemAccount.getIcon());
                    holder.icon.setVisibility(View.VISIBLE);
                } catch (Resources.NotFoundException e) {
                    holder.icon.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView title;
        private TextView titleHeader;
        private ImageView icon;

    }

}
