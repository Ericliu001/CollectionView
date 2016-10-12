package com.example.collectionview.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.recyclerview.R;
import com.example.collectionview.widget.async.AsyncExpandableCollectionView;
import com.example.collectionview.widget.async.AsyncExpandableCollectionViewCallbacks;
import com.example.collectionview.widget.CollectionView;
import com.example.collectionview.widget.async.AsyncHeaderViewHolder;

public class AsyncActivity extends MainActivity implements AsyncExpandableCollectionViewCallbacks<String, String> {

    private AsyncExpandableCollectionView mAsyncExpandableCollectionView;
    private CollectionView.Inventory inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mAsyncExpandableCollectionView = (AsyncExpandableCollectionView) findViewById(R.id.asyncExpandableCollectionView);
        mAsyncExpandableCollectionView.setCallbacks(this);

        inventory = new CollectionView.Inventory();

        CollectionView.InventoryGroup group1 = inventory.newGroup(0); // groupOrdinal is the smallest, displayed first
        group1.setHeaderItem("Header 1");


        CollectionView.InventoryGroup group2 = inventory.newGroup(2);
        group2.setHeaderItem("Header 2");


        CollectionView.InventoryGroup group3 = inventory.newGroup(3); // 2 is smaller than 10, displayed second
        group3.setHeaderItem("Header 3");

        mAsyncExpandableCollectionView.updateInventory(inventory);
    }

    @Override
    public void onStartLoadingGroup(int groupOrdinal) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                List<Object> items = new ArrayList<>();
                items.add("Group x, item 1");
                items.add("Group x, item 2");
                items.add("Group x, item 3");
                mAsyncExpandableCollectionView.onFinishLoadingGroup(items);
            }
        }.execute();
    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item, parent, false);

        return new MyHeaderViewHolder(v, groupOrdinal, mAsyncExpandableCollectionView);
    }


    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, String headerItem) {
        ((MyHeaderViewHolder) holder).getTextView().setText((String) headerItem);

    }

    public static class MyHeaderViewHolder extends AsyncHeaderViewHolder implements AsyncExpandableCollectionView.OnGroupStateChangeListener {

        private final TextView textView;

        public MyHeaderViewHolder(View v, int groupOrdinal, AsyncExpandableCollectionView asyncExpandableCollectionView) {
            super(v, groupOrdinal, asyncExpandableCollectionView);
            textView = (TextView) v.findViewById(R.id.textView);
        }


        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onGroupStartExpending() {

        }

        @Override
        public void onGroupExpanded() {

        }

        @Override
        public void onGroupCollapsed() {

        }
    }
}
