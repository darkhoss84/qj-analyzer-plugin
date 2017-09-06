package com.esplugin.chosung;


import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by nobaksan on 2015. 11. 18..
 */
public class ChosungTokenFilter extends TokenFilter {

    public ChosungTokenFilter(TokenStream tokenStream) {
        super(tokenStream);
        this.charTermAttr = addAttribute(CharTermAttribute.class);
        this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
        this.terms = new LinkedList<char[]>();
    }

    private CharTermAttribute charTermAttr;
    private PositionIncrementAttribute posIncAttr;
    private Queue<char[]> terms;

    @Override
    public boolean incrementToken() throws IOException {
        if (!terms.isEmpty()) {
            char[] buffer = terms.poll();
            charTermAttr.setEmpty();
            charTermAttr.copyBuffer(buffer, 0, buffer.length);
            posIncAttr.setPositionIncrement(0);
            return true;
        }

        if (!input.incrementToken()) {
            return false;
        } else {

            final char[] buffer = charTermAttr.buffer();
            final int length = charTermAttr.length();

            String tokenString = new String(buffer, 0, length);
            String word = this.input.getAttribute(CharTermAttribute.class).toString().trim();
            //String word = tokenString;
            StringBuilder stringBuilder = new StringBuilder();
            for( int idx = 0 ; idx < word.length() ; idx++ ) {
                String data =  findChosung(word,idx);
                if(data == null) stringBuilder.append(word.charAt(idx));
                else stringBuilder.append(data);
            }
            if(word.equals(stringBuilder.toString())==false){
                terms.add(stringBuilder.toString().toCharArray());
            }


            //           charTermAttr.setEmpty();
            //          charTermAttr.append(stringBuilder);


            return true;
        }
    }

    private  String findChosung(String name,int index){
        char b =name.charAt(index);
        String chosung = null;
        int first = (b - 44032 ) / ( 21 * 28 );
        switch(first){
            case 0:
                chosung="ㄱ";
                break;
            case 1:
                chosung="ㄲ";
                break;
            case 2:
                chosung="ㄴ";
                break;
            case 3:
                chosung="ㄷ";
                break;
            case 4:
                chosung="ㄸ";
                break;
            case 5:
                chosung="ㄹ";
                break;
            case 6:
                chosung="ㅁ";
                break;
            case 7:
                chosung="ㅂ";
                break;
            case 8:
                chosung="ㅃ";
                break;
            case 9:
                chosung="ㅅ";
                break;
            case 10:
                chosung="ㅆ";
                break;
            case 11:
                chosung="ㅇ";
                break;
            case 12:
                chosung="ㅈ";
                break;
            case 13:
                chosung="ㅉ";
                break;
            case 14:
                chosung="ㅊ";
                break;
            case 15:
                chosung="ㅋ";
                break;
            case 16:
                chosung="ㅌ";
                break;
            case 17:
                chosung="ㅍ";
                break;
            case 18:
                chosung="ㅎ";
                break;

        }

        return chosung;
    }

}