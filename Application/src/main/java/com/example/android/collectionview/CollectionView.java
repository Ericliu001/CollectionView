package com.example.android.collectionview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by L078865 on 15/01/2016.
 */
public class CollectionView extends RecyclerView {
    private static final String TAG = CollectionView.class.getSimpleName();
    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEW_TYPE_NON_HEADER = 10;

    private Inventory mInventory = new Inventory();
    private CollectionViewCallbacks mCallbacks = null;
    private MyListAdapter mAdapter = null;


    public CollectionView(Context context) {
        this(context, null);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapter = new MyListAdapter();
        setAdapter(mAdapter);

    }


    public void setCollectionAdapter(CollectionViewCallbacks adapter) {
        mCallbacks = adapter;
    }

    public void updateInventory(final Inventory inventory) {
        mInventory = new Inventory(inventory);
        mAdapter.notifyDataSetChanged();
    }

    protected class MyListAdapter extends RecyclerView.Adapter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return getRowViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            populatRoweData(holder, position);
        }

        @Override
        public int getItemViewType(int position) {
            RowInformation rowInfo = computeRowContent(position);
            if (rowInfo.isComputedSuccessful) {
                if (rowInfo.isHeader) {
                    return VIEWTYPE_HEADER;
                } else {
                    return VIEW_TYPE_NON_HEADER + mInventory.getGroupIndex(rowInfo.groupId);
                }

            } else {
                Log.e(TAG, "Invalid row passed to getItemViewType: " + position);
                return 0;
            }
        }


        @Override
        public int getItemCount() {
            int rowCount = 0;
            for (InventoryGroup group : mInventory.mGroups) {
                int thisGroupRowCount = group.getRowCount();
                rowCount += thisGroupRowCount;
            }

            return rowCount;
        }
    }

    private void populatRoweData(ViewHolder holder, int position) {
        RowInformation rowInfo = computeRowContent(position);
        if (!rowInfo.isComputedSuccessful) {
            return;
        }

        if (rowInfo.isHeader) {
            mCallbacks.bindCollectionHeaderView(getContext(), holder, rowInfo.group.getHeaderItem());
        } else {
            Object item = rowInfo.group.getItem(rowInfo.positionInGroup);
            mCallbacks.bindCollectionItemView(getContext(), holder, rowInfo.groupId, item);
        }

    }

    private ViewHolder getRowViewHolder(ViewGroup parent, int viewType) {
        if (mCallbacks == null) {
            Log.e(TAG, "Call to makeRow without an adapter installed");
            return null;
        }


        if (viewType == VIEWTYPE_HEADER) {
            // return header ViewHolder
            return mCallbacks.newCollectionHeaderView(getContext(), parent);
        } else {
            int groupIndex = viewType - VIEW_TYPE_NON_HEADER;
            int groupId = mInventory.mGroups.get(groupIndex).mGroupId;
            // return item ViewHolder
            return mCallbacks.newCollectionItemView(getContext(), groupId, parent);
        }

    }


    private static class RowInformation {
        boolean isComputedSuccessful = false;
        int row;
        boolean isHeader;
        int groupId;
        InventoryGroup group;
        int positionInGroup;
    }


    private RowInformation computeRowContent(int row) {
        RowInformation result = new RowInformation();
        int rowPointer = 0;
        int positionInGroup;


        for (InventoryGroup group : mInventory.mGroups) {
            if (rowPointer == row) {
                // row is a group header
                result.isComputedSuccessful = true;
                result.row = row;
                result.isHeader = true;
                result.groupId = group.mGroupId;
                result.group = group;
                result.positionInGroup = -1;
                return result;
            }

            positionInGroup = 0;
            while (positionInGroup < group.mItemCount) {
                if (rowPointer == row) {
                    // this is the row we are looking for
                    result.isComputedSuccessful = true;
                    result.row = row;
                    result.isHeader = false;
                    result.group = group;
                    result.positionInGroup = positionInGroup;
                    return result;
                }

                // move to the next row
                positionInGroup++;
                rowPointer++;
            }
        }

        return result;
    }


    /**
     * Represents a group of items with a header to be displayed in the {@link CollectionView}.
     */
    public static class InventoryGroup {
        private int mGroupId = 0;


        private Object mHeaderItem;
        private int mDataIndexStart = 0;
        private int mItemCount = 0;

        private SparseArray<Object> mItems = new SparseArray<>();
        private SparseArray<Integer> mItemCustomDataIndices = new SparseArray<>();

        public InventoryGroup(int groupId) {
            mGroupId = groupId;
        }


        public InventoryGroup(InventoryGroup copyFrom) {
            mGroupId = copyFrom.mGroupId;
            mHeaderItem = copyFrom.mHeaderItem;
            mDataIndexStart = copyFrom.mDataIndexStart;
            mItemCount = copyFrom.mItemCount;
            mItems = copyFrom.cloneSparseArray(copyFrom.mItems);
            mItemCustomDataIndices = cloneSparseArray(copyFrom.mItemCustomDataIndices);
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
            setItem(mItemCount, item);
            mItemCount++;
            return this;
        }


        public int getRowCount() {
            return 1 + mItemCount;
        }


        public Object getItem(int index) {
            return mItems.get(index, null);
        }

        private static <E> SparseArray<E> cloneSparseArray(SparseArray<E> orig) {
            SparseArray<E> result = new SparseArray<E>();
            for (int i = 0; i < orig.size(); i++) {
                result.put(orig.keyAt(i), orig.valueAt(i));
            }
            return result;
        }

    }


    /**
     * Represents the data of the items to display in the {@link CollectionView}.
     * This is defined as a list of {@link InventoryGroup} which represents a group of items with a
     * header.
     */
    public static class Inventory {
        private ArrayList<InventoryGroup> mGroups = new ArrayList<>();

        public Inventory() {
        }


        public Inventory(Inventory copyFrom) {
            for (InventoryGroup group : copyFrom.mGroups) {
                mGroups.add(group);
            }
        }

        public void addGroup(InventoryGroup group) {
            if (group.mItemCount > 0) {
                mGroups.add(new InventoryGroup(group));
            }
        }


        public int getTotalItemCount() {
            int total = 0;
            for (InventoryGroup group : mGroups) {
                total += group.mItemCount;
            }
            return total;
        }

        public int getGroupCount() {
            return mGroups.size();
        }

        public int getGroupIndex(int groupId) {
            for (int i = 0; i < mGroups.size(); i++) {
                if (mGroups.get(i).mGroupId == groupId) {
                    return i;
                }
            }
            return -1;
        }


    }
}
