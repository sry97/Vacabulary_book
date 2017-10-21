package com.example.vacabulary_book;

import org.litepal.crud.DataSupport;

/**
 * Created by 荣耀 on 2017/10/16.
 */

public class DEST extends DataSupport {
    private String m_word;//单词
    private String m_phonetic;//音标
    private String m_translate;//翻译

    public String getM_word() {
        return m_word;
    }

    public void setM_word(String m_word) {
        this.m_word = m_word;
    }

    public String getM_phonetic() {
        return m_phonetic;
    }

    public void setM_phonetic(String m_phonetic) {
        this.m_phonetic = m_phonetic;
    }

    public String getM_translate() {
        return m_translate;
    }

    public void setM_translate(String m_translate) {
        this.m_translate = m_translate;
    }
}
