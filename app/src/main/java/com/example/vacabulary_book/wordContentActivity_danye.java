package com.example.vacabulary_book;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class wordContentActivity_danye extends AppCompatActivity {
    public static void actionStart(Context context, String word, String phonetic, String translate) {
        Intent intent = new Intent(context, wordContentActivity_danye.class);
        intent.putExtra("word", word);
        intent.putExtra("phonetic", phonetic);
        intent.putExtra("translate", translate);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_content_danye);
        String word = getIntent().getStringExtra("word");
        String phonetic = getIntent().getStringExtra("phonetic");
        String translate = getIntent().getStringExtra("translate");
        wordContent wordContent = (wordContent) getSupportFragmentManager().findFragmentById(R.id.word_content_fragment);
        wordContent.refresh(word, phonetic, translate);
    }
}
