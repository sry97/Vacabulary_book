package com.example.vacabulary_book;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.Until;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.os.Build.VERSION_CODES.O;
import static com.example.vacabulary_book.R.id.add;
import static com.example.vacabulary_book.R.id.word_dialog;

public class EnglishNews extends AppCompatActivity {
    TextView word_dialog_save;
    TextView phonetic_dialog_save;
    TextView translate_dialog_save;
    String word;
    private long previousTime = 0;

    private String webphonetic = "获取失败";
    private String webtrans = "获取失败";
    String words;
    private Document document;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_news);


        WebView webView = (WebView) findViewById(R.id.english_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.globaltimes.cn/");

        registerClipEvents();

    }

    private void registerClipEvents() {

        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                long now = System.currentTimeMillis();
                if (now - previousTime < 200) {
                    previousTime = now;
                    return;
                }
                previousTime = now;

                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {

                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();

                    if (addedText != null) {
                        words = addedText.toString();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(EnglishNews.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View v1 = inflater.inflate(R.layout.activity_webworddialog, null);

                        word_dialog_save = (TextView) v1.findViewById(word_dialog);
                        phonetic_dialog_save = (TextView) v1.findViewById(R.id.phonetic_dialog);
                        translate_dialog_save = (TextView) v1.findViewById(R.id.translate_dialog);

                        webWordtask webWordtask = new webWordtask();
                        webWordtask.execute();

                        builder.setView(v1)
                                .setTitle("单词翻译")
                                .setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DEST dest = new DEST();
                                        dest.setM_word(word_dialog_save.getText().toString());
                                        dest.setM_phonetic(phonetic_dialog_save.getText().toString());
                                        dest.setM_translate(translate_dialog_save.getText().toString());
                                        dest.save();
                                        if (dest.save()) {
                                            Toast.makeText(EnglishNews.this, "网页单词添加到生词本成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EnglishNews.this, "网页单词添加到生词本失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }).setNegativeButton("取消", null);
                        builder.show();

                    }
                }
            }
        });
    }


    class webWordtask extends AsyncTask {//内部类实现从有道得到单词的释义
        private String url = "http://dict.youdao.com/search?q=";

        public webWordtask() {
            word = words;
            url = url + word;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                document = Jsoup.connect(url).timeout(3000).get();
                if (document.select(".phonetic").hasText()) {
                    webphonetic = document.select(".phonetic").first().text();
                }
                if (document.select(".trans-container").hasText()) {
                    webtrans = document.select(".trans-container").first().text();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            word_dialog_save.setText(word);
            phonetic_dialog_save.setText(webphonetic);
            translate_dialog_save.setText(webtrans);

        }
    }
}
