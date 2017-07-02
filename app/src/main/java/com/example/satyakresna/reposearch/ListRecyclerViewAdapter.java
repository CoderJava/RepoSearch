package com.example.satyakresna.reposearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by satyakresna on 02-Jul-17.
 */

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private final List<Repository> mValues;

    public ListRecyclerViewAdapter(List<Repository> repos) {
        mValues = repos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRepo = mValues.get(position);
        holder.mRepoName.setText(mValues.get(position).getName());
        holder.mRepoOwner.setText(mValues.get(position).getOwner().getLogin());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Repository mRepo;
        public final TextView mRepoName;
        public final TextView mRepoOwner;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRepoName = (TextView) view.findViewById(R.id.repo_name);
            mRepoOwner = (TextView) view.findViewById(R.id.repo_owner);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRepoName.getText() + "'";
        }
    }
}
