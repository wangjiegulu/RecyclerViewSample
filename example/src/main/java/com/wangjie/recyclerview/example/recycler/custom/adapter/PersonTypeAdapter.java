package com.wangjie.recyclerview.example.recycler.custom.adapter;

import android.content.Context;
import android.view.View;
import com.wangjie.androidbucket.adapter.typeadapter.ABAdapterTypeRender;
import com.wangjie.androidbucket.support.recyclerview.adapter.extra.ABRecyclerViewTypeExtraHolder;
import com.wangjie.androidbucket.support.recyclerview.adapter.extra.ABRecyclerViewTypeExtraViewAdapter;
import com.wangjie.recyclerview.example.recycler.model.Person;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class PersonTypeAdapter extends ABRecyclerViewTypeExtraViewAdapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public List<Person> getList() {
        return list;
    }

    public OnRecyclerViewListener getOnRecyclerViewListener() {
        return onRecyclerViewListener;
    }

    private static final String TAG = PersonTypeAdapter.class.getSimpleName();
    private Context context;
    private List<Person> list;

    public PersonTypeAdapter(Context context, List<Person> list, View headerView, View footerView) {
        super(headerView, footerView);
        this.context = context;
        this.list = list;
    }

    @Override
    public ABAdapterTypeRender<ABRecyclerViewTypeExtraHolder> getAdapterTypeRenderExcludeExtraView(int type) {
        ABAdapterTypeRender<ABRecyclerViewTypeExtraHolder> render = null;
        switch (type) {
            case 0:
                render = new PersonTypeARender(context, this);
                break;
            case 1:
                render = new PersonTypeBRender(context, this);
                break;
        }
        return render;
    }

    @Override
    public int getItemCountExcludeExtraView() {
        return list.size();
    }

    @Override
    public int getItemViewTypeExcludeExtraView(int realItemPosition) {
        return list.get(realItemPosition).getType();
    }


}
