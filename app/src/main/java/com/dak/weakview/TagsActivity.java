package com.dak.weakview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dak.weakview.adapter.WeakTagsAdapter;
import com.dak.weakview.adapter.viewholder.WeakTagsViewHolder;
import com.dak.weakview.layout.WeakTagsLayout;

/**
 * tags
 */
public class TagsActivity extends AppCompatActivity {
    private WeakTagsLayout weakTagsLayout;
    private WeakTagsAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        weakTagsLayout = (WeakTagsLayout) findViewById(R.id.wtl_tags);
        weakTagsLayout.setOnItemClickListener(new WeakTagsLayout.OnTagItemClickListener() {
            @Override
            public void onTagItemClickListener(int position, View view) {
                Toast.makeText(TagsActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new WeakTagsAdapter<String>(this) {
            @Override
            public void notifyItemView(WeakTagsViewHolder holder, String item, int position) {
                holder.setTagVal(item);
            }
        };
        weakTagsLayout.setAdapter(adapter);
        adapter.refreshData(MainActivity.list);

    }

}
