package com.wangjie.recyclerview.example.recycler.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
import com.wangjie.androidbucket.thread.Runtask;
import com.wangjie.androidbucket.thread.ThreadPool;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.ABThreadUtil;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.recyclerview.example.BaseActivity;
import com.wangjie.recyclerview.example.R;
import com.wangjie.recyclerview.example.recycler.custom.adapter.PersonTypeAdapter;
import com.wangjie.recyclerview.example.recycler.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
@AILayout(R.layout.recycler_view_linear)
public class RecyclerViewCustomActivity extends BaseActivity implements PersonTypeAdapter.OnRecyclerViewListener {

    private static final String TAG = RecyclerViewCustomActivity.class.getSimpleName();
    @AIView(R.id.recycler_view_linear_toolbar)
    private Toolbar toolbar;
    @AIView(R.id.recycler_view_linear_rv)
    private RecyclerView recyclerView;
    @AIView(R.id.recycler_view_linear_srl)
    private SwipeRefreshLayout refreshLayout;
    @AIView(R.id.recycler_view_linear_add_btn)
    private Button addBtn;

    private List<Person> personList = new ArrayList<>();
    private int i;
    private PersonTypeAdapter adapter;

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


        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int curPosition = layoutManager.findFirstVisibleItemPosition();
                View curItemView = layoutManager.findViewByPosition(curPosition);

                View floatView = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_float, null);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                floatView.setLayoutParams(lp);
                floatView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                floatView.layout(0, 0, floatView.getMeasuredWidth(), floatView.getMeasuredHeight());
                TextView nameTv = (TextView) floatView.findViewById(R.id.recycler_view_item_float_name_tv);
                nameTv.setText(personList.get(curPosition).getName());
                TextView ageTv = (TextView) floatView.findViewById(R.id.recycler_view_item_float_age_tv);
                ageTv.setText(personList.get(curPosition).getAge() + "岁");

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                int displayTop;
                int itemHeight = curItemView.getHeight();
                int curTop = curItemView.getTop();
                int floatHeight = floatView.getHeight();
                if(curTop < floatHeight - itemHeight){
                    displayTop = itemHeight + curTop - floatHeight;
                }else{
                    displayTop = 0;
                }
                floatView.invalidate();
                c.translate(0, displayTop);
                floatView.draw(c);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = ABTextUtil.dip2px(context, 1);
                super.getItemOffsets(outRect, view, parent, state);
            }
        });


    }

    private void initData() {
        Random random = new Random();
        for (; i < 30; ++i) {
            Person person = new Person(i, "WangJie_" + i, 10 + i, random.nextInt(2));
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
}
