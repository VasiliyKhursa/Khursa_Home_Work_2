package com.example.l04_01_homework;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {


    private ListFilter filter = new ListFilter();
    static private List<ApplicationInfo> objectsApp;
    private List<ApplicationInfo> originalObjectsApp;
    private ArrayList<ApplicationInfo> filteredListApp;
    static private PackageManager mPackManager;
    static Activity activity;


    static Context cont;

    int filterCount = 0;

    static OnItemClickListener mItemClickListener;


    public RecyclerViewAdapter(Context context, List<ApplicationInfo> objects) {
        cont = context;

        objectsApp = objects;
        originalObjectsApp = new ArrayList<ApplicationInfo>(objects);
        filteredListApp = new ArrayList<ApplicationInfo>();
        mPackManager = context.getPackageManager();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView appName;
        public TextView packageName;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            appName = (TextView) v.findViewById(R.id.app_name);
            packageName = (TextView) v.findViewById(R.id.app_package);
            imageView = (ImageView) v.findViewById(R.id.app_icon);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }

            try {
                Intent intent = mPackManager.getLaunchIntentForPackage(objectsApp.get(getPosition()).packageName);
                if (intent != null) {
                    cont.startActivity(intent);
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onItemClick(View view, int position) {

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_software, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.appName.setText(objectsApp.get(position).loadLabel(mPackManager));
        holder.imageView.setImageDrawable(objectsApp.get(position).loadIcon(mPackManager));
        holder.packageName.setText(objectsApp.get(position).packageName);
    }

    @Override
    public int getItemCount() {
        return objectsApp.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            objectsApp.clear();
            filteredListApp.clear();
            FilterResults filterResults = new FilterResults();
            String str = constraint.toString().toLowerCase();

            if (constraint != null || constraint.length() != 0) {
                for (ApplicationInfo obj : originalObjectsApp) {
                    if (obj.loadLabel(mPackManager).toString().toLowerCase().contains(str)) {
                        filteredListApp.add(obj);
                    }
                }
            }

            filterResults.values = filteredListApp;
            filterResults.count = filteredListApp.size();
            filterCount = filterResults.count;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            objectsApp.addAll(((List<ApplicationInfo>) results.values));
            notifyDataSetChanged();
        }
    }
}
