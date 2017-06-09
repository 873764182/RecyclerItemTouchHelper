package com.pixel.itemtouchhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.pixel.itemtouchhelper.util.DefaultItemTouchHelpCallback;
import com.pixel.itemtouchhelper.util.DefaultItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实现基于RecyclerView的拖动排序与滑动删除
 * <p>
 * http://blog.csdn.net/yanzhenjie1003/article/details/51935982
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private static final List<String> adapterList = new ArrayList<>();
    private volatile RecyclerView.Adapter recyclerAdapter = null;

    static {
        for (int i = 0; i < 100; i++) {
            adapterList.add("拖动 " + i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(recyclerAdapter);

        DefaultItemTouchHelper.PackTouchHelper itemTouchHelper = new DefaultItemTouchHelper.PackTouchHelper(listener);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);   // 网格布局时滑动删除无效
    }

    // 拖动/滑动 监听器 更新数据
    private final DefaultItemTouchHelpCallback.OnItemTouchCallbackListener listener = new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            Collections.swap(adapterList, srcPosition, targetPosition);
            recyclerAdapter.notifyItemMoved(srcPosition, targetPosition);
            return true;
        }

        @Override
        public void onSwiped(int adapterPosition) {
            adapterList.remove(adapterPosition);
            recyclerAdapter.notifyItemRemoved(adapterPosition);
        }
    };

    // 列表ViewHolder
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    // 列表适配器
    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(getLayoutInflater().inflate(R.layout.item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
            holder.checkBox.setText(adapterList.get(position));
        }

        @Override
        public int getItemCount() {
            return adapterList.size();
        }
    }


}
