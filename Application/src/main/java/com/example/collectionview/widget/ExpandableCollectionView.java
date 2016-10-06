package com.example.collectionview.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by ericliu on 6/10/2016.
 */

public class ExpandableCollectionView  extends CollectionView{


    public ExpandableCollectionView(Context context) {
        super(context);
    }

    public ExpandableCollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableCollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void hideGroup(int groupId){}
}
