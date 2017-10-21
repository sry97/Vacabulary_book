package com.example.vacabulary_book;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.util.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText input;
    private TextView s_word;
    private TextView s_phonetic;
    private TextView s_trans;
    private EditText add_word;
    private EditText add_phonetic;
    private EditText add_trans;
    private Button btn_search;
    private Button btn_save;
    private Document doc;
    private String word;
    private String phonetic = "获取失败";
    private String trans = "获取失败";

    DEST dest =new DEST();

    public boolean onCreateOptionsMenu(Menu menu) {//将资源菜单文件加载到主界面中
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {//对菜单选项点击处理
        int id = item.getItemId();
        switch (id) {
            case R.id.help:
                Toast.makeText(this, "这是帮助", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newword:
                Intent intent_newword = new Intent(MainActivity.this, localwords.class);
                startActivity(intent_newword);
                break;
            case R.id.english_news:
                Intent intent_news = new Intent(Intent.ACTION_VIEW);
                intent_news.setData(Uri.parse("http://www.chinadaily.com.cn/"));
                startActivity(intent_news);
                break;
            case R.id.owner:
                Toast.makeText(this,"Copyright© 2017 By 沈荣耀.All rights reserved.",Toast.LENGTH_LONG).show();
                break;
            case R.id.exit:
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.input);
        s_word = (TextView) findViewById(R.id.s_word);
        s_phonetic = (TextView) findViewById(R.id.s_phonetic);
        s_trans = (TextView) findViewById(R.id.s_translate);
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_save=(Button)findViewById(R.id.btn_save);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查询按钮的点击事件处理
                if (TextUtils.isEmpty(input.getText().toString())){
                    Toast.makeText(MainActivity.this,"请填写要查询的单词",Toast.LENGTH_SHORT).show();
                    return ;
                }
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                WordTask task = new WordTask();
                task.execute();
            }
        });

        Button btn_add = (Button) findViewById(R.id.btn_add);//添加生词的点击事件处理
        btn_add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View v1=inflater.inflate(R.layout.activity_addword,null);
                builder.setView(v1)
                        .setTitle("添加生词")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                add_word=(EditText)v1.findViewById(R.id.add_word);
                                add_phonetic=(EditText)v1.findViewById(R.id.add_phonetic);
                                add_trans=(EditText)v1.findViewById(R.id.add_trans);
                                dest.setM_word(add_word.getText().toString());
                                dest.setM_phonetic(add_phonetic.getText().toString());
                                dest.setM_translate(add_trans.getText().toString());
                                dest.save();
                                if(dest.save()){
                                    Toast.makeText(MainActivity.this,"手动添加成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MainActivity.this,"手动添加失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {//保存到生词本按钮的点击事件处理
            @Override
            public void onClick(View view) {//保存到生词本按钮的点击事件处理
                if (TextUtils.isEmpty(input.getText().toString())){
                    Toast.makeText(MainActivity.this,"请先查询单词后在点击保存",Toast.LENGTH_SHORT).show();
                    return ;
                }
                dest.setM_word(s_word.getText().toString());
                dest.setM_phonetic(s_phonetic.getText().toString());
                dest.setM_translate(s_trans.getText().toString());
                dest.save();
                if(dest.save()){
                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
    });
    }

    class WordTask extends AsyncTask {//内部类实现从有道得到单词的释义
        private String url = "http://dict.youdao.com/search?q=";
        public WordTask(){
            word = input.getText().toString();
            url = url+ word;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                doc = Jsoup.connect(url).timeout(3000).get();
                if(doc.select(".phonetic").hasText()) {
                    phonetic = doc.select(".phonetic").first().text();
                }
                if(doc.select(".trans-container").hasText()) {
                    trans = doc.select(".trans-container").first().text();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            s_word.setText(word);
            s_phonetic.setText(phonetic);
            s_trans.setText(trans);
        }
    }
}
