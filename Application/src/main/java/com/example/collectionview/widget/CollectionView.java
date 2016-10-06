package com.example.collectionview.widget;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Eric Liu on 18/01/2016.
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.setLayoutManager(layoutManager);
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

    public void addGroup(Inventory inventory, InventoryGroup group) {
        inventory.addGroup(group);
        mInventory.addGroup(group);
        int itemCountBeforeGroup = mInventory.getRowCountBeforeGroup(group);

        mAdapter.notifyItemRangeInserted(itemCountBeforeGroup, group.getRowCount());
    }



    protected class MyListAdapter extends Adapter {

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
                    return VIEW_TYPE_NON_HEADER + mInventory.mGroups.indexOfKey(rowInfo.groupId);
                }

            } else {
                Log.e(TAG, "Invalid row passed to getItemViewType: " + position);
                return 0;
            }
        }


        @Override
        public int getItemCount() {
            int rowCount = 0;

            for (int i = 0; i < mInventory.mGroups.size(); i++) {
                int key = mInventory.mGroups.keyAt(i);
                InventoryGroup group = mInventory.mGroups.get(key);
                int thisGroupRowCount = group.getRowCount();
                rowCount += thisGroupRowCount;
            }

            return rowCount;
        }
    }

    private void populatRoweData(ViewHolder holder, int position) {
        if (mCallbacks == null) {
            return;
        }

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

    private ViewHolder getRowViewHolder(ViewGroup parent, final int viewType) {
        ViewHolder placeHolder = new ViewHolder(new View(getContext())) {
            @Override
            public String toString() {
                return "Invalid Item, view type: " + viewType;
            }
        };
        if (mCallbacks == null) {
            Log.e(TAG, "Call to makeRow without an adapter installed");
            return placeHolder;
        }


        ViewHolder holder;
        if (viewType == VIEWTYPE_HEADER) {
            // return header ViewHolder
            holder = mCallbacks.newCollectionHeaderView(getContext(), parent);
        } else {
            int groupIndex = viewType - VIEW_TYPE_NON_HEADER;
            int key = mInventory.mGroups.keyAt(groupIndex);
            int groupId = mInventory.mGroups.get(key).mGroupId;
            // return item ViewHolder
            holder = mCallbacks.newCollectionItemView(getContext(), groupId, parent);
        }

        if (holder != null) {
            return holder;
        } else {
            return placeHolder;
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
        int rowCounter = 0;
        int positionInGroup;


        for (int i = 0; i < mInventory.mGroups.size(); i++) {
            int key = mInventory.mGroups.keyAt(i);
            InventoryGroup group = mInventory.mGroups.get(key);
            if (rowCounter == row) {
                // row is a group header
                result.isComputedSuccessful = true;
                result.row = row;
                result.isHeader = true;
                result.groupId = group.mGroupId;
                result.group = group;
                result.positionInGroup = -1;
                return result;
            }
            rowCounter++; // incremented by 1 because it just past the Header row

            positionInGroup = 0;
            while (positionInGroup < group.mItems.size()) {
                if (rowCounter == row) {
                    // this is the row we are looking for
                    result.isComputedSuccessful = true;
                    result.row = row;
                    result.isHeader = false;
                    result.groupId = group.mGroupId;
                    result.group = group;
                    result.positionInGroup = positionInGroup;
                    return result;
                }

                // move to the next row
                positionInGroup++;
                rowCounter++;
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

        private ArrayList<Object> mItems = new ArrayList<>();

        public InventoryGroup(int groupId) {
            mGroupId = groupId;
        }


        public InventoryGroup(InventoryGroup copyFrom) {
            mGroupId = copyFrom.mGroupId;
            mHeaderItem = copyFrom.mHeaderItem;
            mDataIndexStart = copyFrom.mDataIndexStart;
            mItems = (ArrayList<Object>) copyFrom.mItems.clone();
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


        public void addItem(Object item) {
            mItems.add(item);
        }


        public int getRowCount() {
            return 1 + mItems.size();
        }


        public Object getItem(int index) {
            return mItems.get(index);
        }

    }


    /**
     * Represents the data of the items to display in the {@link CollectionView}.
     * This is defined as a list of {@link InventoryGroup} which represents a group of items with a
     * header.
     */
    public static class Inventory {
        private SparseArray<InventoryGroup> mGroups = new SparseArray<>();
        ;

        public Inventory() {
        }


        public Inventory(Inventory copyFrom) {
            mGroups = copyFrom.mGroups.clone();
        }

        public void addGroup(InventoryGroup group) {
            if (group.mItems.size() > 0) {
                mGroups.put(group.mGroupId, new InventoryGroup(group));
            }
        }


        public int getTotalItemCount() {
            int total = 0;

            for (int i = 0; i < mGroups.size(); i++) {
                int key = mGroups.keyAt(i);
                total += mGroups.get(key).mItems.size();
            }
            return total;
        }

        public int getGroupCount() {
            return mGroups.size();
        }

        public int getGroupIndex(int groupId) {
            for (int i = 0; i < mGroups.size(); i++) {
                int key = mGroups.keyAt(i);
                if (mGroups.get(key).mGroupId == groupId) {
                    return i;
                }
            }
            return -1;
        }

         int getRowCountBeforeGroup(InventoryGroup group) {
            int count = 0;
            for (int i = 0; i < mGroups.size(); i++) {
                if (group.mGroupId == mGroups.keyAt(i)) {
                    break;
                }
                int key = mGroups.keyAt(i);
                count += mGroups.get(key).getRowCount();
            }
            return count;
        }


    }
}
