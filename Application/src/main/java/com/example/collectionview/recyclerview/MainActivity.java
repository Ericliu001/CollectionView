/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.collectionview.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.recyclerview.R;
import com.example.collectionview.common.activities.SampleActivityBase;
import com.example.collectionview.common.logger.Log;
import com.example.collectionview.widget.CollectionView;
import com.example.collectionview.widget.CollectionViewCallbacks;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p/>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase implements CollectionViewCallbacks {

    public static final String TAG = "MainActivity";


    private CollectionView mCollectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollectionView = (CollectionView) findViewById(R.id.collectionView);


        CollectionView.Inventory inventory = new CollectionView.Inventory();

        CollectionView.InventoryGroup group1 = new CollectionView.InventoryGroup(-2);
        group1.setHeaderItem("Header 1");
        group1.addItem("Group one, item 1");
        group1.addItem("Group one, item 2");
        inventory.addGroup(group1);

        CollectionView.InventoryGroup group2 = new CollectionView.InventoryGroup(10);
        group2.setHeaderItem("Header 2");
        group2.addItem("Group two, item 1");
        group2.addItem("Group two, item 2");
        group2.addItem("Group two, item 3");
        inventory.addGroup(group2);


        CollectionView.InventoryGroup group3 = new CollectionView.InventoryGroup(2);
        group3.setHeaderItem("Header 3");
        group3.addItem("Group three, item 1");
        group3.addItem("Group three, item 2");
        group3.addItem("Group three, item 3");
        inventory.addGroup(group3);

        mCollectionView.setCollectionAdapter(this);
        mCollectionView.updateInventory(inventory);

    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.text_row_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, Object headerItem) {
        ((MyViewHolder)holder).getTextView().setText((String)headerItem);
    }

    @Override
    public void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupId, Object item) {
        ((MyViewHolder)holder).getTextView().setText((String)item);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public MyViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}

