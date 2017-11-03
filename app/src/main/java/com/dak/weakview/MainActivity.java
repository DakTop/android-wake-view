package com.dak.weakview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * wekeView示例，布局中的item的样式可以自己单独设置
 */
public class MainActivity extends AppCompatActivity {
    public static List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 9; i++) {
            list.add("item" + i);
        }
    }

    /**
     * 跳转到Linear页面
     *
     * @param view
     */
    public void jumpToLiear(View view) {
        this.startActivity(new Intent(this, LinearActivity.class));
    }

    /**
     * 跳转到Grid页面
     *
     * @param view
     */
    public void jumpToGrid(View view) {
        this.startActivity(new Intent(this, GridActivity.class));
    }

    /**
     * 跳转到Grid页面
     *
     * @param view
     */
    public void jumpToTags(View view) {
        this.startActivity(new Intent(this, TagsActivity.class));
    }
}
