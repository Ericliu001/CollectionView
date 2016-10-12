package com.example.collectionview.widget.async;


import com.example.collectionview.widget.CollectionViewCallbacks;

/**
 * Created by ericliu on 11/10/16.
 */

public interface AsyncExpandableCollectionViewCallbacks<T1, T2> extends CollectionViewCallbacks<T1, T2> {

    void onStartLoadingGroup(int groupOrdinal);



}

