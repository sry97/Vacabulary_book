package com.example.vacabulary_book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class localwords extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<DEST> data = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localwords);

        data = DataSupport.findAll(DEST.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_newword_recycler); // 设置布局管理器
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);// 设置adapter
        mRecyclerView.addItemDecoration(new mydivideitem(this, LinearLayoutManager.VERTICAL));

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {//item的点击事件处理，跳转到详细界面
            @Override
            public void onItemClick(View view, int position) {
                Intent intent_word_detail=new Intent(localwords.this,word_detail.class);
                //String word=data.get(position).getM_word();
                intent_word_detail.putExtra("word",String.valueOf(position));
                startActivity(intent_word_detail);
            }
        });
    }
    private ArrayList<String> getData() {

        ArrayList<String> d_temp = new ArrayList<>();
        for(DEST de:data){
            d_temp.add(de.getM_word());
        }
        return d_temp;
    }
}
