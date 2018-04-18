package com.dak.weakview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dak.weakview.adapter.WeakCurrencyAdapter;
import com.dak.weakview.adapter.viewholder.WeakCurrencyViewHold;
import com.dak.weakview.layout.WeakCardOverlapLayout;
import com.dak.weakview.layout.WeakLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 层叠卡片效果
 */
public class WeakCardOverlapActivity extends AppCompatActivity {
    private WeakCardOverlapLayout weakcardoverlapLayout;
    private WeakCurrencyAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weak_card_overlap);
        weakcardoverlapLayout = (WeakCardOverlapLayout) findViewById(R.id.weakcardoverlaplayout);
        weakcardoverlapLayout.setOnItemClickListener(new WeakCardOverlapLayout.OnItemClickListener() {
            @Override
            public void onWeakItemClickListener(int position, View view) {
                Toast.makeText(WeakCardOverlapActivity.this, position + "__" + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new WeakCurrencyAdapter<String>(this, R.layout.view_weak_overlap) {
            @Override
            public void notifyItemView(WeakCurrencyViewHold holder, String item, int position) {
                holder.setText(R.id.textview, item);
            }
        };
        weakcardoverlapLayout.setParentClipChild(false);
        weakcardoverlapLayout.setAdapter(adapter);
        adapter.refreshData(MainActivity.list);
    }
}
