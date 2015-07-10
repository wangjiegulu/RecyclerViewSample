package com.wangjie.recyclerview.example.recycler.pinned;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollListener;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wangjie.androidbucket.support.recyclerview.pinnedlayout.PinnedRecyclerViewLayout;
import com.wangjie.androidbucket.thread.Runtask;
import com.wangjie.androidbucket.thread.ThreadPool;
import com.wangjie.androidbucket.utils.ABThreadUtil;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.recyclerview.example.BaseActivity;
import com.wangjie.recyclerview.example.R;
import com.wangjie.recyclerview.example.recycler.model.Person;
import com.wangjie.recyclerview.example.recycler.pinned.adapter.PersonTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
@AILayout(R.layout.recycler_view_pinned)
public class RecyclerViewPinnedActivity extends BaseActivity implements PersonTypeAdapter.OnRecyclerViewListener, PinnedRecyclerViewLayout.OnRecyclerViewPinnedViewListener {

    private static final String TAG = RecyclerViewPinnedActivity.class.getSimpleName();
    @AIView(R.id.recycler_view_pinned_toolbar)
    private Toolbar toolbar;
    @AIView(R.id.recycler_view_pinned_rv)
    private RecyclerView recyclerView;
    @AIView(R.id.recycler_view_pinned_srl)
    private SwipeRefreshLayout refreshLayout;
    @AIView(R.id.recycler_view_pinned_add_btn)
    private Button addBtn;

    private List<Person> personList = new ArrayList<>();
    private int i;
    private PersonTypeAdapter adapter;
    @AIView(R.id.recycler_view_pinned_layout)
    private PinnedRecyclerViewLayout pinnedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View footerView = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_type_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(context);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onTopWhenScrollIdle...");
            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                Logger.d(TAG, "onBottomWhenScrollIdle...");
                footerView.setVisibility(View.VISIBLE);
                ThreadPool.go(new Runtask<Object, Object>() {
                    @Override
                    public Object runInBackground() {
                        ABThreadUtil.sleep(3000);
                        return null;
                    }

                    @Override
                    public void onResult(Object result) {
                        super.onResult(result);
                        refreshLayout.setRefreshing(false);
                        footerView.setVisibility(View.GONE);
                    }
                });
            }
        });
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.getRecyclerViewScrollManager().addScrollListener(recyclerView, new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                refreshLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        initData();

        adapter = new PersonTypeAdapter(context, personList, null, footerView);
        adapter.setOnRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadPool.go(new Runtask<Object, Object>() {
                    @Override
                    public Object runInBackground() {
                        ABThreadUtil.sleep(3000);
                        return null;
                    }

                    @Override
                    public void onResult(Object result) {
                        super.onResult(result);
                        refreshLayout.setRefreshing(false);
                        footerView.setVisibility(View.GONE);
                    }
                });
            }
        });

        pinnedLayout.initRecyclerPinned(recyclerView, layoutManager, LayoutInflater.from(context).inflate(R.layout.recycler_view_item_float, null));
        pinnedLayout.setOnRecyclerViewPinnedViewListener(this);

    }

    private void initData() {
        Random random = new Random();
        for (; i < 30; ++i) {
            Person person = new Person(i, "WangJie_" + i, 10 + i, random.nextInt(2));
            personList.add(person);
        }
    }

    @Override
    @AIClick({R.id.recycler_view_pinned_add_btn})
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.recycler_view_pinned_add_btn:
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
        showToastMessage("clicked: " + personList.get(position).getName());
    }

    @Override
    public boolean onItemLongClick(int position) {
        showToastMessage("long clicked: " + personList.get(position).getName());
        adapter.notifyItemRemoved(position);
        personList.remove(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
        return true;
    }

    // 渲染pinnedView数据
    @Override
    public void onPinnedViewRender(PinnedRecyclerViewLayout pinnedRecyclerViewLayout, View pinnedView, int position) {
        switch (pinnedRecyclerViewLayout.getId()) {
            case R.id.recycler_view_pinned_layout:
                TextView nameTv = (TextView) pinnedView.findViewById(R.id.recycler_view_item_float_name_tv);
                nameTv.setText(personList.get(position).getName());
                TextView ageTv = (TextView) pinnedView.findViewById(R.id.recycler_view_item_float_age_tv);
                ageTv.setText(personList.get(position).getAge() + "岁");
                break;
        }
    }


}
