package com.dak.weakview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dak.weakview.adapter.WeakCurrencyAdapter;
import com.dak.weakview.adapter.viewholder.WeakCurrencyViewHold;
import com.dak.weakview.layout.WeakLinearLayout;

/**
 * 线性布局,在布局文件中不要忘记设置WeakLinearLayout的 android:orientation="vertical"方向的属性
 */
public class LinearActivity extends AppCompatActivity {

    private WeakLinearLayout weakLinearLayout;
    private WeakCurrencyAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);
        weakLinearLayout = (WeakLinearLayout) findViewById(R.id.weaklinearlayout);
        adapter = new WeakCurrencyAdapter<String>(this, R.layout.layout_weak_item) {
            @Override
            public void notifyItemView(WeakCurrencyViewHold holder, String item, int position) {
                holder.setText(R.id.textview, item);
            }
        };
        weakLinearLayout.setAdapter(adapter);
        adapter.refreshData(MainActivity.list);
    }
}
