package com.example.vacabulary_book;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 荣耀 on 2017/10/22.
 */

public class WordListFragment extends Fragment {
    private boolean isTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_list_frag, container, false);
        RecyclerView wordListRecyclerview = (RecyclerView) view.findViewById(R.id.word_listfrag_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wordListRecyclerview.setLayoutManager(layoutManager);
        MyAdapter adapter = new MyAdapter(getWord());
        wordListRecyclerview.setAdapter(adapter);

        return view;
    }

    List<DEST> data = new ArrayList<>();

    private List<DEST> getWord() {
        data = DataSupport.findAll(DEST.class);

        List<DEST> wordList = new ArrayList<>();

        for (DEST de : data) {
            wordList.add(de);
        }
        return wordList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.word_content_layout) != null) {
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<DEST> mWordList;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView wordListText;

            public ViewHolder(View view) {
                super(view);
                wordListText = (TextView) view.findViewById(R.id.my_item_recycler);
            }
        }

        public MyAdapter(List<DEST> wordlist) {
            mWordList = wordlist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DEST dest = mWordList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        wordContent wordContent = (com.example.vacabulary_book.wordContent) getFragmentManager()
                                .findFragmentById(R.id.word_content_fragment);
                        wordContent.refresh(dest.getM_word(), dest.getM_phonetic(), dest.getM_translate());
                    } else {
                        wordContentActivity_danye.actionStart(getActivity(), dest.getM_word(), dest.getM_phonetic(), dest.getM_translate());
                    }
                }
            });
            return holder;
        }

        @Override

        public void onBindViewHolder(ViewHolder holder, int position) {
            DEST dest = mWordList.get(position);
            holder.wordListText.setText(dest.getM_word());
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }
    }

}
