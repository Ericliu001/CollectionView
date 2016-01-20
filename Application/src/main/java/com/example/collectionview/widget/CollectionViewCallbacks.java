/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.collectionview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Defines an interface to the callbacks that a {@link CollectionView} will be called to create each
 * elements of the collection.
 */
public interface CollectionViewCallbacks {

    /**
     * Returns a new custom View that will be used for each of the collection group headers.
     */
    RecyclerView.ViewHolder newCollectionHeaderView(Context context, ViewGroup parent);


    /**
     * Returns a new custom View that will be used for each of the collection item.
     *
     * @param context
     * @param groupId - the groupId decides the sequence of groups being displayed, the smallest int is displayed first and in an asending order
     * @param parent
     * @return
     */
    RecyclerView.ViewHolder newCollectionItemView(Context context, int groupId, ViewGroup parent);

    /**
     * Binds the given data (like the header label) with the given collection group header View.
     */
    void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, Object headerItem);

    /**
     * Binds the given data with the given collection item View.
     */
    void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupId, Object item);


}
