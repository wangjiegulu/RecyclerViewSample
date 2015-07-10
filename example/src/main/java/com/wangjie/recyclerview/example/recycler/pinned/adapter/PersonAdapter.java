package com.wangjie.recyclerview.example.recycler.pinned.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.support.recyclerview.adapter.ABRecyclerViewAdapter;
import com.wangjie.androidbucket.support.recyclerview.adapter.ABRecyclerViewHolder;
import com.wangjie.recyclerview.example.R;
import com.wangjie.recyclerview.example.recycler.model.Person;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class PersonAdapter extends ABRecyclerViewAdapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = PersonAdapter.class.getSimpleName();
    private List<Person> list;

    public PersonAdapter(List<Person> list) {
        this.list = list;
    }

    @Override
    public ABRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Logger.d(TAG, "onCreateViewHolder, viewType: " + viewType);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_test_item_person, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        final ABRecyclerViewHolder holder = new ABRecyclerViewHolder(view);
        View rootView = holder.obtainView(R.id.recycler_view_test_item_person_view);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onItemClick(holder.getPosition());
                }
            }
        });
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onItemLongClick(holder.getPosition());
                }
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ABRecyclerViewHolder holder, int position) {
        Logger.d(TAG, "onBindViewHolder, position: " + position + ", viewHolder: " + holder);

        Person person = list.get(position);
        holder.obtainView(R.id.recycler_view_test_item_person_name_tv, TextView.class).setText(person.getName());
        holder.obtainView(R.id.recycler_view_test_item_person_age_tv, TextView.class).setText(person.getAge() + "岁");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
