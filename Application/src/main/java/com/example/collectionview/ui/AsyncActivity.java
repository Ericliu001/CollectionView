package com.example.collectionview.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.recyclerview.R;
import com.example.collectionview.widget.AsyncExpandableCollectionView;
import com.example.collectionview.widget.AsyncExpandableCollectionViewCallbacks;

public class AsyncActivity extends MainActivity  implements AsyncExpandableCollectionViewCallbacks<String, Object>{

    private AsyncExpandableCollectionView mAsyncExpandableCollectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
    }

    @Override
    public void onStartExpandingGroup(int groupOrdinal) {

    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item, parent, false);

        return new MyHeaderViewHolder(v);
    }


    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, String headerItem) {
        super.bindCollectionHeaderView(context, holder, headerItem);

    }

    public static class MyHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textView;

        public MyHeaderViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View v) {

        }



    }
}
