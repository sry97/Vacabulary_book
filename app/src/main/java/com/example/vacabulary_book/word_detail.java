package com.example.vacabulary_book;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class word_detail extends AppCompatActivity {
    Button btn_change;
    Button btn_delete;
    TextView word_change;
    TextView phonetic_change;
    TextView trans_change;
    String text;
    int text_po;
    List<DEST> mdest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        btn_change=(Button)findViewById(R.id.btn_change);
        btn_delete=(Button)findViewById(R.id.btn_delete);
        word_change=(TextView)findViewById(R.id.word_detail);
        phonetic_change=(TextView)findViewById(R.id.phonetic_detail);
        trans_change=(TextView)findViewById(R.id.translate_detail);

        text=getIntent().getStringExtra("word");
        text_po=Integer.parseInt(text);


        mdest=DataSupport.findAll(DEST.class);
        //Toast.makeText(this,mdest.get(text_po).getM_word(),Toast.LENGTH_SHORT).show();

         word_change.setText(mdest.get(text_po).getM_word());
         phonetic_change.setText(mdest.get(text_po).getM_phonetic());
         trans_change.setText(mdest.get(text_po).getM_translate());

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(word_detail.this);
                LayoutInflater inflater = getLayoutInflater();
                final View v2=inflater.inflate(R.layout.activity_addword,null);
                builder.setView(v2)
                        .setTitle("修改单词")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                word_change=(EditText)v2.findViewById(R.id.add_word);
                                phonetic_change=(EditText)v2.findViewById(R.id.add_phonetic);
                                trans_change=(EditText)v2.findViewById(R.id.add_trans);

                                DEST updatedest=new DEST();
                                updatedest.setM_word(word_change.getText().toString());
                                updatedest.setM_phonetic(phonetic_change.getText().toString());
                                updatedest.setM_translate(trans_change.getText().toString());

                                    int j = updatedest.updateAll("m_word = ?", mdest.get(text_po).getM_word());
                                    if (j >= 1) {
                                        Toast.makeText(word_detail.this, "手动修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(word_detail.this, "手动修改失败", Toast.LENGTH_SHORT).show();
                                    }

                                Intent intent_back_local=new Intent(word_detail.this,localwords.class);
                                startActivity(intent_back_local);
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder delete_is = new AlertDialog.Builder(word_detail.this);
                delete_is.setTitle("确认删除？")
                        .setMessage("确认删除单词吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DataSupport.delete(DEST.class,text_po);
                                DataSupport.deleteAll(DEST.class, "m_word = ? ", mdest.get(text_po).getM_word());

                                Intent intent_back_local=new Intent(word_detail.this,localwords.class);
                                startActivity(intent_back_local);
                            }
                        }).setNegativeButton("取消",null);
                delete_is.show();
            }
        });




    }

}
