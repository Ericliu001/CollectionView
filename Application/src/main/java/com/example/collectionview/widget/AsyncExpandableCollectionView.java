package com.example.collectionview.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by ericliu on 6/10/2016.
 */

public class AsyncExpandableCollectionView extends CollectionView {

    private AsyncExpandableCollectionViewCallbacks mCallbacks;


    public AsyncExpandableCollectionView(Context context) {
        super(context);
    }

    public AsyncExpandableCollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncExpandableCollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setCallbacks(AsyncExpandableCollectionViewCallbacks callbacks) {
        setCollectionCallbacks(callbacks);
        mCallbacks = callbacks;
    }

    public void hideGroup(int groupOrdinal) {
        removeAllItemsInGroup(groupOrdinal);
    }


    public void onStartExpandingGroup(int groupOrdinal) {
        int ordinal = 0;
        for (int i = 0; i < mInventory.mGroups.size(); i++) {
            ordinal = mInventory.mGroups.keyAt(i);
            hideGroup(ordinal);
        }

        mCallbacks.onStartExpandingGroup(groupOrdinal);
    }


    public void onFinishExpandingGroup(int groupOrdinal, List<Object> items) {
        addItemsInGroup(groupOrdinal, items);
    }

}
