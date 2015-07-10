package com.wangjie.recyclerview.example;

import android.content.Intent;
import android.view.View;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.recyclerview.example.recycler.custom.RecyclerViewCustomActivity;
import com.wangjie.recyclerview.example.recycler.gridlayout.RecyclerViewGridActivity;
import com.wangjie.recyclerview.example.recycler.linearlayout.RecyclerViewLinearActivity;
import com.wangjie.recyclerview.example.recycler.pinned.RecyclerViewPinnedActivity;
import com.wangjie.recyclerview.example.recycler.staggeredgrid.RecyclerViewStaggeredActivity;


@AILayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @Override
    @AIClick({R.id.recycler_view_main_linear_btn, R.id.recycler_view_main_grid_btn,
            R.id.recycler_view_main_staggered_btn, R.id.recycler_view_main_custom_btn,
            R.id.recycler_view_main_pinned_btn})
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.recycler_view_main_linear_btn:
                startActivity(new Intent(context, RecyclerViewLinearActivity.class));
                break;
            case R.id.recycler_view_main_grid_btn:
                startActivity(new Intent(context, RecyclerViewGridActivity.class));
                break;
            case R.id.recycler_view_main_staggered_btn:
                startActivity(new Intent(context, RecyclerViewStaggeredActivity.class));
                break;
            case R.id.recycler_view_main_custom_btn:
                startActivity(new Intent(context, RecyclerViewCustomActivity.class));
                break;
            case R.id.recycler_view_main_pinned_btn:
                startActivity(new Intent(context, RecyclerViewPinnedActivity.class));
                break;
        }
    }
}
