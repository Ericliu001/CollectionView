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


package com.example.android.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.android.collectionview.CollectionView;
import com.example.android.collectionview.CollectionViewCallbacks;
import com.example.android.common.activities.SampleActivityBase;

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
        CollectionView.InventoryGroup group = new CollectionView.InventoryGroup(0);


        group.addItem("This is good");
        group.addItem("hello world");

        inventory.addGroup(group);

        mCollectionView.updateInventory(inventory);

        mCollectionView.setCollectionAdapter(this);
    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, Object headerItem) {

    }

    @Override
    public void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupId, Object item) {

    }
}
