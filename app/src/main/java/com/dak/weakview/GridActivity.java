package com.dak.weakview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dak.weakview.adapter.WeakCurrencyAdapter;
import com.dak.weakview.adapter.viewholder.WeakCurrencyViewHold;
import com.dak.weakview.layout.WeakGridLayout;

/**
 * 网格布局,不要忘记设定setColumnCount列属性
 */
public class GridActivity extends AppCompatActivity {
    private WeakGridLayout weakGridView;
    private WeakCurrencyAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        weakGridView = (WeakGridLayout) findViewById(R.id.weakgridlayout);
        weakGridView.setOnItemClickListener(new WeakGridLayout.OnItemClickListener() {
            @Override
            public void onWeakItemClickListener(int position, View view) {
                Toast.makeText(GridActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        //设置每一行有多少元素，必须设置。
        weakGridView.setColumnCount(3);
        adapter = new WeakCurrencyAdapter<String>(this, R.layout.layout_weak_item) {
            @Override
            public void notifyItemView(WeakCurrencyViewHold holder, String item, int position) {
                holder.setText(R.id.textview, item);
            }
        };
        weakGridView.setAdapter(adapter);
        adapter.refreshData(MainActivity.list);
    }
}
