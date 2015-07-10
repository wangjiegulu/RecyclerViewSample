package com.wangjie.recyclerview.example.recycler.linearlayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
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
@AILayout(R.layout.recycler_view_linear)
public class RecyclerViewLinearActivity extends BaseActivity implements PersonAdapter.OnRecyclerViewListener {
    private static final String TAG = RecyclerViewLinearActivity.class.getSimpleName();
    @AIView(R.id.recycler_view_linear_toolbar)
    private Toolbar toolbar;
    @AIView(R.id.recycler_view_linear_rv)
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

        ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(context);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onTopWhenScrollIdle");
            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onBottomWhenScrollIdle");
            }
        });
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
    @AIClick({R.id.recycler_view_linear_add_btn})
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.recycler_view_linear_add_btn:
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
