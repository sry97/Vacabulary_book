package com.example.vacabulary_book;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 荣耀 on 2017/10/22.
 */

public class wordContent extends Fragment {
    private View view;
    private View v1;
    Button btn_change;
    Button btn_delete;
    TextView word_change;
    TextView phonetic_change;
    TextView trans_change;
    TextView word_add;
    TextView phonetic_add;
    TextView trans_add;
    String temp;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wordcontent_frag, container, false);
        v1 = inflater.inflate(R.layout.activity_addword, null);

        btn_change = (Button) view.findViewById(R.id.btn_change);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);

        word_change = (TextView) view.findViewById(R.id.word_detail_frag);
        phonetic_change = (TextView) view.findViewById(R.id.phonetic_detail_frag);
        trans_change = (TextView) view.findViewById(R.id.translate_detail_frag);

        trans_change.setMovementMethod(new ScrollingMovementMethod());

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder change_is = new AlertDialog.Builder(getActivity());

                change_is.setView(v1)
                        .setTitle("修改单词")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                word_add = (EditText) v1.findViewById(R.id.add_word);
                                phonetic_add = (EditText) v1.findViewById(R.id.add_phonetic);
                                trans_add = (EditText) v1.findViewById(R.id.add_trans);

                                temp = word_change.getText().toString();

                                if (word_add.getParent() != null) {
                                    ((ViewGroup) word_add.getParent()).removeAllViews();
                                }
                                if (phonetic_add.getParent() != null) {
                                    ((ViewGroup) phonetic_add.getParent()).removeAllViews();
                                }
                                if (trans_add.getParent() != null) {
                                    ((ViewGroup) trans_add.getParent()).removeAllViews();
                                }

                                DEST updatedest = new DEST();
                                updatedest.setM_word(word_add.getText().toString());
                                updatedest.setM_phonetic(phonetic_add.getText().toString());
                                updatedest.setM_translate(trans_add.getText().toString());

                                int j = updatedest.updateAll("m_word = ?", temp);
                                if (j >= 1) {
                                    Toast.makeText(getActivity(), "手动修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "手动修改失败", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent_back_local = new Intent(getActivity(), localwords.class);
                                startActivity(intent_back_local);
                            }
                        })
                        .setNegativeButton("取消", null);
                change_is.show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = word_change.getText().toString();

                final AlertDialog.Builder delete_is = new AlertDialog.Builder(getActivity());
                delete_is.setTitle("确认删除？")
                        .setMessage("确认删除单词吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DataSupport.deleteAll(DEST.class, "m_word = ? ", temp);

                                Intent intent_back_local = new Intent(getActivity(), localwords.class);
                                startActivity(intent_back_local);
                            }
                        }).setNegativeButton("取消", null);
                delete_is.show();
            }
        });
        return view;
    }

    public void refresh(String word, String phonetic, String transtrate) {
        View visibilityLayout = view.findViewById(R.id.visibllity_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView word_detail_frag = (TextView) view.findViewById(R.id.word_detail_frag);
        TextView phonetic_detail_frag = (TextView) view.findViewById(R.id.phonetic_detail_frag);
        TextView translate_detail_frag = (TextView) view.findViewById(R.id.translate_detail_frag);
        word_detail_frag.setText(word);
        phonetic_detail_frag.setText(phonetic);
        translate_detail_frag.setText(transtrate);
    }
}
