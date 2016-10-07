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


package com.example.collectionview.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.recyclerview.R;
import com.example.collectionview.widget.CollectionView;
import com.example.collectionview.widget.CollectionViewCallbacks;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p/>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends Activity implements CollectionViewCallbacks<String, Object> {

    public static final String TAG = "MainActivity";


    private CollectionView mCollectionView;
    private CollectionView.Inventory inventory;
    private CollectionView.InventoryGroup groupx;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollectionView = (CollectionView) findViewById(R.id.collectionView);


        inventory = new CollectionView.Inventory();

        CollectionView.InventoryGroup group1 = inventory.newGroup(0); // groupId is the smallest, displayed first
        group1.setHeaderItem("Header 1");
        group1.addItem("Group one, item 1");
        group1.addItem("Group one, item 2");

        groupx = inventory.newGroup(1);
        groupx.setHeaderItem("Header x");

        CollectionView.InventoryGroup group2 = inventory.newGroup(2);
        group2.setHeaderItem("Header 2");
        group2.addItem("Group two, item 1");
        group2.addItem("Group two, item 2");
        group2.addItem("Group two, item 3");


        CollectionView.InventoryGroup group3 = inventory.newGroup(3); // 2 is smaller than 10, displayed second
        group3.setHeaderItem("Header 3");
        group3.addItem("Group three, item 1");
        group3.addItem("Group three, item 2");
        group3.addItem("Group three, item 3");

        mCollectionView.setCollectionAdapter(this);
        mCollectionView.updateInventory(inventory);

    }


    public void onButtonClicked(View view) {

        if (!flag) {
            flag = true;
            List<Object> items = new ArrayList<>();
            items.add("Group x, item 1");
            items.add("Group x, item 2");
            items.add("Group x, item 3");
            mCollectionView.addItemsInGroup(groupx.getOrdinal(), items);
        } else {
            mCollectionView.removeAllItemsInGroup(groupx.getOrdinal());
            flag = false;
        }
    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder newCollectionItemView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.text_row_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, String headerItem) {
        ((MyViewHolder) holder).getTextView().setText((String) headerItem);
    }

    @Override
    public void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, Object item) {
        ((MyViewHolder) holder).getTextView().setText((String) item);
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

