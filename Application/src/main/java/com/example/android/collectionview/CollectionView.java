package com.example.android.collectionview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by L078865 on 15/01/2016.
 */
public class CollectionView extends RecyclerView {
    public CollectionView(Context context) {
        this(context, null);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAdapter(new MyListAdapter());

    }


    protected class MyListAdapter extends RecyclerView.Adapter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            int rowCount = 0;


            return 0;
        }
    }


    public static class InventoryGroup implements Cloneable {
        private int mGroupId = 0;


        private Object mHeaderItem;
        private int mDataIndexStart = 0;
        private int mItemCount = 0;

        private SparseArray<Object> mItems = new SparseArray<>();
        private SparseArray<Integer> mItemCustomDataIndices = new SparseArray<>();

        public InventoryGroup(int groupId) {
            mGroupId = groupId;
        }


        public Object getHeaderItem() {
            return mHeaderItem;
        }

        public InventoryGroup setHeaderItem(Object headerItem) {
            mHeaderItem = headerItem;
            return this;
        }

        public InventoryGroup setDataIndexStart(int dataIndexStart) {
            mDataIndexStart = dataIndexStart;
            return this;
        }


        public int getDataIndex(int indexInGroup) {
            return mItemCustomDataIndices.get(indexInGroup, mDataIndexStart + indexInGroup);
        }

        public InventoryGroup setItemCount(int count) {
            mItemCount = count;
            return this;
        }


        public InventoryGroup setItem(int index, Object item) {
            mItems.put(index, item);
            return this;
        }

        public InventoryGroup addItem(Object item) {
            mItemCount++;
            setItem(mItemCount - 1, item);
            return this;
        }


        public int getRowCount() {
            return 1 + mItemCount;
        }
        
        
        public Object getItem(int index) {
            return mItems.get(index, null);
        }
    }
}
