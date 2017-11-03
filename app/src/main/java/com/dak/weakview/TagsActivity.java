package com.dak.weakview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dak.weakview.adapter.WeakTagsAdapter;
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
        adapter = new WeakTagsAdapter<String>(this);
        weakTagsLayout.setAdapter(adapter);
        adapter.refreshData(MainActivity.list);
    }
}
