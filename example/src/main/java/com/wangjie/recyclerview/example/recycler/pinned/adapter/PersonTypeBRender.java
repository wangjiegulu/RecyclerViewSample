package com.wangjie.recyclerview.example.recycler.pinned.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wangjie.androidbucket.adapter.typeadapter.ABAdapterTypeRender;
import com.wangjie.androidbucket.support.recyclerview.adapter.extra.ABRecyclerViewTypeExtraHolder;
import com.wangjie.recyclerview.example.R;
import com.wangjie.recyclerview.example.recycler.model.Person;
/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/19/15.
 */
public class PersonTypeBRender implements ABAdapterTypeRender<ABRecyclerViewTypeExtraHolder> {
    private Context context;
    private PersonTypeAdapter adapter;
    private ABRecyclerViewTypeExtraHolder holder;

    public PersonTypeBRender(Context context, PersonTypeAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_test_item_person, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        holder = new ABRecyclerViewTypeExtraHolder(view);
    }

    @Override
    public ABRecyclerViewTypeExtraHolder getReusableComponent() {
        return holder;
    }

    @Override
    public void fitEvents() {
        View rootView = holder.obtainView(R.id.recycler_view_test_item_person_view);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonTypeAdapter.OnRecyclerViewListener listener = adapter.getOnRecyclerViewListener();
                if (null != listener) {
                    listener.onItemClick(holder.getRealItemPosition());
                }
            }
        });
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PersonTypeAdapter.OnRecyclerViewListener listener = adapter.getOnRecyclerViewListener();
                if (null != listener) {
                    listener.onItemLongClick(holder.getRealItemPosition());
                }
                return false;
            }
        });
    }

    @Override
    public void fitDatas(int position) {
        Person person = adapter.getList().get(position);
        holder.obtainView(R.id.recycler_view_test_item_person_view).setBackgroundColor(Color.parseColor("#ccddaa"));

        holder.obtainView(R.id.recycler_view_test_item_person_name_tv, TextView.class).setText(person.getName());
        holder.obtainView(R.id.recycler_view_test_item_person_age_tv, TextView.class).setText(person.getAge() + "岁");
        holder.obtainView(R.id.recycler_view_item_float_name_tv, TextView.class).setText(person.getName());
        holder.obtainView(R.id.recycler_view_item_float_age_tv, TextView.class).setText(person.getAge() + "岁");
    }
}
