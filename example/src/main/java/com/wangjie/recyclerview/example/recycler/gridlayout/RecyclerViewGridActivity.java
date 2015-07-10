package com.wangjie.recyclerview.example.recycler.gridlayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseGridLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.recyclerview.example.BaseActivity;
import com.wangjie.recyclerview.example.R;
import com.wangjie.recyclerview.example.recycler.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
@AILayout(R.layout.recycler_view_grid)
public class RecyclerViewGridActivity extends BaseActivity implements PersonAdapter.OnRecyclerViewListener {

    private static final String TAG = RecyclerViewGridActivity.class.getSimpleName();
    @AIView(R.id.recycler_view_grid_rv)
    private RecyclerView recyclerView;

    private List<Person> personList = new ArrayList<>();
    private int i;
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(context, 3);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setOnRecyclerViewScrollListener(recyclerView, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onTopWhenScrollIdle");
            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onBottomWhenScrollIdle");
            }
        });

        recyclerView.setLayoutManager(layoutManager);

        initData();
        adapter = new PersonAdapter(personList);
        adapter.setOnRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (; i < 30; ++i) {
            Person person = new Person(i, "WangJie_" + i, 10 + i);
            personList.add(person);
        }
    }

    @Override
    @AIClick({R.id.recycler_view_grid_add_btn})
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.recycler_view_grid_add_btn:
                i++;
                Person person = new Person(i, "WangJie_" + i, 10 + i);
                adapter.notifyItemInserted(2);
                personList.add(2, person);
                adapter.notifyItemRangeChanged(2, adapter.getItemCount());
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        showToastMessage("clicked: " + position);
    }

    @Override
    public boolean onItemLongClick(int position) {
        showToastMessage("long clicked: " + position);
        adapter.notifyItemRemoved(position);
        personList.remove(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
        return true;
    }
}
