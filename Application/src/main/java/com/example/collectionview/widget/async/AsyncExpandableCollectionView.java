package com.example.collectionview.widget.async;

import java.util.List;
import java.util.WeakHashMap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.collectionview.widget.CollectionView;


/**
 * Created by ericliu on 6/10/2016.
 */

public class AsyncExpandableCollectionView<T1, T2> extends CollectionView<T1, T2> {

    private AsyncExpandableCollectionViewCallbacks<T1, T2> mCallbacks;
    private WeakHashMap<OnGroupStateChangeListener, Integer> mOnGroupStateChangeListeners = new WeakHashMap<>();
    private int expandedGroupOrdinal = -1;


    public AsyncExpandableCollectionView(Context context) {
        super(context);
    }

    public AsyncExpandableCollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncExpandableCollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected CollectionView.RowInformation<T1, T2> populatRoweData(RecyclerView.ViewHolder holder, int position) {
        CollectionView.RowInformation<T1, T2> rowInfo = super.populatRoweData(holder, position);
        if (rowInfo.isHeader()) {
            mOnGroupStateChangeListeners.put((AsyncHeaderViewHolder) holder, rowInfo.getGroupOrdinal());
        }

        return rowInfo;
    }

    public interface OnGroupStateChangeListener {

        void onGroupStartExpending();

        void onGroupExpanded();

        void onGroupCollapsed();
    }


    public void onGroupClicked(int groupOrdinal) {
        if (groupOrdinal != expandedGroupOrdinal) {
            onStartExpandingGroup(groupOrdinal);
        } else {
            collapseGroup(groupOrdinal);
        }

    }


    public void setCallbacks(AsyncExpandableCollectionViewCallbacks<T1, T2> callbacks) {
        setCollectionCallbacks(callbacks);
        mCallbacks = callbacks;
    }

    private void collapseGroup(int groupOrdinal) {
        expandedGroupOrdinal = -1;
        removeAllItemsInGroup(groupOrdinal);
        for (OnGroupStateChangeListener onGroupStateChangeListener : mOnGroupStateChangeListeners.keySet()) {
            if (mOnGroupStateChangeListeners.get(onGroupStateChangeListener) == groupOrdinal) {
                onGroupStateChangeListener.onGroupCollapsed();
            }
        }
    }


    private void onStartExpandingGroup(int groupOrdinal) {
        int ordinal = 0;
        for (int i = 0; i < mInventory.getGroups().size(); i++) {
            ordinal = mInventory.getGroups().keyAt(i);
            if (ordinal != groupOrdinal) {
                collapseGroup(ordinal);
            }
        }


        expandedGroupOrdinal = groupOrdinal;
        mCallbacks.onStartLoadingGroup(groupOrdinal);
        for (OnGroupStateChangeListener onGroupStateChangeListener : mOnGroupStateChangeListeners.keySet()) {
            if (mOnGroupStateChangeListeners.get(onGroupStateChangeListener) == groupOrdinal) {
                onGroupStateChangeListener.onGroupStartExpending();
            }
        }
    }


    public void onFinishLoadingGroup(List<? extends T2> items) {
        if (expandedGroupOrdinal < 0) {
            return;
        }

        addItemsInGroup(expandedGroupOrdinal, items);
        for (OnGroupStateChangeListener onGroupStateChangeListener : mOnGroupStateChangeListeners.keySet()) {
            if (mOnGroupStateChangeListeners.get(onGroupStateChangeListener) == expandedGroupOrdinal) {
                onGroupStateChangeListener.onGroupExpanded();
            }
        }
    }

}
