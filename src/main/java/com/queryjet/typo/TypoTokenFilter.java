package com.queryjet.typo;

/**
 * Created by nobaksan on 2016. 2. 1..
 */

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;


/**
 * Created by nobaksan on 2015. 11. 18..
 */
public class TypoTokenFilter extends TokenFilter {

    public TypoTokenFilter(TokenStream tokenStream) {
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

            //String word = this.input.getAttribute(CharTermAttribute.class).toString().trim();
            String word = tokenString;

            String result = "";

            int type = IsHangul(word);

            if (type == 1){
                //result = this.korToEng(ts);
                result = this.getTokenEng(word);
            }else if (type == 2){
                result = this.engToKor(word);
            }else{
                result = word;
            }

            System.out.println(result);
            if(word.equals(result)==false){
                terms.add(result.toCharArray());
            }
            return true;
        }
    }
    private final static String[] chosungEng = {"r", "R", "s", "e", "E", "f", "a", "q", "Q", "t", "T", "d", "w", "W", "c", "z", "x", "v", "g"};
    private final static String[] jungsungEng = {"k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l"};
    private final static String[] jongsungEng = {" ", "r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
    private final static String[] singleJaumEng = { "r", "R", "rt", "s", "sw", "sg", "e","E" ,"f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q","Q", "qt", "t", "T", "d", "w", "W", "c", "z", "x", "v", "g" };

    private final static char[] chosungKor = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    private final static int[] batchimchosungKor = {1, 2, 4, 7, 0, 8, 16, 17, 0, 19, 20, 21, 22, 0, 23, 24, 25, 26, 27};

    // 코드타입 - 초성, 중성, 종성
    private enum CodeType {
        chosung, jungsung, jongsung
    }
    /**
     * 한글만 있는지, 영어만 있는지 확인 분기.
     */
    public static int IsHangul(String originStr) {
        int type = 0;

        if (Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣]*$", "" + originStr)){
            type = 1;
        }

        if (Pattern.matches("^[A-Za-z]*$", "" + originStr)){
            type = 2;
        }

        return type;
    }

    public String getTokenEng(String kor){
        char[] chars = kor.toCharArray();
        StringBuilder tokenResult = new StringBuilder();
        StringBuilder preEngUmjul = new StringBuilder();

        int[] jasoResult = new int[3];
        int isHangulTest = 0;


        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                isHangulTest = chars[i];

                if ((isHangulTest > 44031) && (isHangulTest < 55204)) {
                    jasoResult = StringDecompositionToNum(chars[i]);

                    String chochar = chosungEng[jasoResult[0]];
                    if (i != 0) {
                        tokenResult.append(chochar);
                    }

                    chochar = chosungEng[jasoResult[0]];
                    String jungchar = jungsungEng[jasoResult[1]];

                    if (i != 0) {
                        tokenResult.append(chochar + jungchar);
                    }

                    String jongchar = "";
                    if (jasoResult[2] != 0) {
                        chochar = chosungEng[jasoResult[0]];
                        jungchar = jungsungEng[jasoResult[1]];
                        jongchar = jongsungEng[jasoResult[2]];

                        if (i != 0) {
                            tokenResult.append(chochar + jungchar + jongchar);
                        }
                    }
                    preEngUmjul.append(chochar + jungchar + jongchar);
                } else {
                    String resultEng = "";
//                        Character cr4 = new Character(chars[i]);
                    char charss = (char) (chars[i] - 0xAC00);
                    if( charss>=34097 && charss<=34126){
                        int jaum 	= (charss-34097);
                        resultEng = singleJaumEng[jaum];
                    } else if( charss>=34127 && charss<=34147) {
                        int moum 	= (charss-34127);
                        resultEng = jungsungEng[moum];
                    } else {    //알파벳
                        resultEng = "" + (char)(charss + 0xAC00);
                    }
                    tokenResult.append(preEngUmjul + resultEng.toString().toLowerCase());
                    preEngUmjul.append(resultEng.toString().toLowerCase());

//                        Character crImsi = new Character(chars[i]);
//                        tokenResult.append(preEngUmjul + crImsi.toString().toLowerCase() + "★");
//                        preEngUmjul.append(crImsi.toString().toLowerCase());
                }
            }
        }
        return preEngUmjul.toString();

    }

    public String korToEng(String kor){
        {
            char[] chars = kor.toCharArray();

            StringBuilder tokenResult = new StringBuilder();
            StringBuilder preUmjul = new StringBuilder();

            int[] jasoResult = new int[3];
            int choJungCode = 0;
            int jongSungCode = 0;
            int isHangulTest = 0;
            int uniCode = 0;

            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != ' ') {
                    isHangulTest = chars[i];

                    if ((isHangulTest > 44031) && (isHangulTest < 55204)) {

                        jasoResult = StringDecompositionToNum(chars[i]);

                        char chosungImsi = chosungKor[jasoResult[0]];
                        Character crChosungImsi = new Character(chosungImsi);

                        tokenResult.append(crChosungImsi.toString());

                        if (choJungCode != 0) {
                            boolean multiBatchim = false;
                            if (jongSungCode != 0) {
                                switch (jongSungCode) {
                                    case 1:
                                        if (jasoResult[0] == 9) {
                                            choJungCode += 3;
                                            multiBatchim = true;
                                        }
                                        break;
                                    case 4:
                                        if (jasoResult[0] == 12) {
                                            choJungCode += 5;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 18) {
                                            choJungCode += 6;
                                            multiBatchim = true;
                                        }
                                        break;
                                    case 8:
                                        if (jasoResult[0] == 0) {
                                            choJungCode += 9;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 6) {
                                            choJungCode += 10;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 7) {
                                            choJungCode += 11;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 9) {
                                            choJungCode += 12;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 16) {
                                            choJungCode += 13;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 17) {
                                            choJungCode += 14;
                                            multiBatchim = true;
                                        } else if (jasoResult[0] == 18) {
                                            choJungCode += 15;
                                            multiBatchim = true;
                                        }
                                        break;
                                    case 17:
                                        if (jasoResult[0] == 9) {
                                            choJungCode += 18;
                                            multiBatchim = true;
                                        }
                                        break;
                                }
                                if (multiBatchim) {
                                    char addChoJungCode = (char) choJungCode;
                                    Character crAddChoJungCode = new Character(addChoJungCode);
                                    tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crAddChoJungCode.toString());
                                }

                            } else {
                                choJungCode += batchimchosungKor[jasoResult[0]];
                                char choJungCodeImsi = (char) choJungCode;
                                Character crChoJungCodeImsi = new Character(choJungCodeImsi);
                                tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crChoJungCodeImsi.toString());
                            }

                        }

                        uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + 0;
                        Character crChoJungUniCode = new Character((char) uniCode);
                        tokenResult.append(crChoJungUniCode.toString());

                        if (jasoResult[2] != 0) {
                            uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + jasoResult[2];
                            Character crChoJungJongUniCode = new Character((char) uniCode);
                            tokenResult.append(crChoJungJongUniCode.toString());

                            if ((jasoResult[2] == 1) || (jasoResult[2] == 4) || (jasoResult[2] == 8) || (jasoResult[2] == 17)) {
                                choJungCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28;
                                jongSungCode = jasoResult[2];
                            } else {
                                choJungCode = 0;
                                jongSungCode = 0;
                            }
                        } else {
                            choJungCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28;
                            jongSungCode = 0;
                        }

                        Character crPreUmjul = new Character((char) uniCode);
                        preUmjul.append(crPreUmjul.toString());

                    } else {

                        String resultEng = "";
                        char charss = (char) (chars[i] - 0xAC00);
                        if( charss>=34097 && charss<=34126){
                            int jaum 	= (charss-34097);
                            resultEng = singleJaumEng[jaum];
                        } else if( charss>=34127 && charss<=34147) {
                            int moum 	= (charss-34127);
                            resultEng = jungsungEng[moum];
                        } else {    //알파벳
                            resultEng = "" + (char)(charss + 0xAC00);

                        }
                        tokenResult.append(resultEng.toString().toLowerCase());
                        preUmjul.append(resultEng.toString().toLowerCase());

                    }

                } else {
                    preUmjul.append(" ");
                }
            }
            return tokenResult.toString();
        }
    }

    private int[] StringDecompositionToNum(char originChar) {
        int[] resultInt = new int[3];
        int completeCode = originChar;
        int uniValue = completeCode - 44032;
        resultInt[2] = (uniValue % 28);
        resultInt[0] = ((uniValue - resultInt[2]) / 28 / 21);
        resultInt[1] = ((uniValue - resultInt[2]) / 28 % 21);
        return resultInt;
    }

    /**
     * 영어를 한글로...
     */
    public String engToKor(String eng) {
        StringBuffer sb = new StringBuffer();
        int initialCode = 0, medialCode = 0, finalCode = 0;
        int tempMedialCode, tempFinalCode;

        for (int i = 0; i < eng.length(); i++) {
            // 초성코드 추출
            initialCode = getCode(CodeType.chosung, eng.substring(i, i + 1));
            i++; // 다음문자로

            // 중성코드 추출
            tempMedialCode = getDoubleMedial(i, eng);   // 두 자로 이루어진 중성코드 추출

            if (tempMedialCode != -1) {
                medialCode = tempMedialCode;
                i += 2;
            } else {            // 없다면,
                medialCode = getSingleMedial(i, eng);   // 한 자로 이루어진 중성코드 추출
                i++;
            }

            // 종성코드 추출
            tempFinalCode = getDoubleFinal(i, eng);    // 두 자로 이루어진 종성코드 추출
            if (tempFinalCode != -1) {
                finalCode = tempFinalCode;
                // 그 다음의 중성 문자에 대한 코드를 추출한다.
                tempMedialCode = getSingleMedial(i + 2, eng);
                if (tempMedialCode != -1) {      // 코드 값이 있을 경우
                    finalCode = getSingleFinal(i, eng);   // 종성 코드 값을 저장한다.
                } else {
                    i++;
                }
            } else {            // 코드 값이 없을 경우 ,
                tempMedialCode = getSingleMedial(i + 1, eng);  // 그 다음의 중성 문자에 대한 코드 추출.
                if (tempMedialCode != -1) {      // 그 다음에 중성 문자가 존재할 경우,
                    finalCode = 0;        // 종성 문자는 없음.
                    i--;
                } else {
                    finalCode = getSingleFinal(i, eng);   // 종성 문자 추출
                    if (finalCode == -1)
                        finalCode = 0;
                }
            }
            // 추출한 초성 문자 코드, 중성 문자 코드, 종성 문자 코드를 합한 후 변환하여 스트링버퍼에 넘김
            sb.append((char) (0xAC00 + initialCode + medialCode + finalCode));
        }
        return sb.toString();
    }

    /**
     * 해당 문자에 따른 코드를 추출한다.
     *
     * @param type 초성 : chosung, 중성 : jungsung, 종성 : jongsung 구분
     */
    private int getCode(CodeType type, String c) {
        // 초성
        String init = "rRseEfaqQtTdwWczxvg";
        // 중성
        String[] mid = {"k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l"};
        // 종성
        String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};

        switch (type) {
            case chosung:
                int index = init.indexOf(c);
                if (index != -1) {
                    return index * 21 * 28;
                }
                break;
            case jungsung:

                for (int i = 0; i < mid.length; i++) {
                    if (mid[i].equals(c)) {
                        return i * 28;
                    }
                }
                break;
            case jongsung:
                for (int i = 0; i < fin.length; i++) {
                    if (fin[i].equals(c)) {
                        return i + 1;
                    }
                }
                break;
            default:
                System.out.println("잘못된 타입 입니다");
        }

        return -1;
    }

    // 한 자로 된 중성값을 리턴한다
    // 인덱스를 벗어낫다면 -1을 리턴
    private int getSingleMedial(int i, String eng) {
        if ((i + 1) <= eng.length()) {
            return getCode(CodeType.jungsung, eng.substring(i, i + 1));
        } else {
            return -1;
        }
    }

    // 두 자로 된 중성을 체크하고, 있다면 값을 리턴한다.
    // 없으면 리턴값은 -1
    private int getDoubleMedial(int i, String eng) {
        int result;
        if ((i + 2) > eng.length()) {
            return -1;
        } else {
            result = getCode(CodeType.jungsung, eng.substring(i, i + 2));
            if (result != -1) {
                return result;
            } else {
                return -1;
            }
        }
    }

    // 한 자로된 종성값을 리턴한다
    // 인덱스를 벗어낫다면 -1을 리턴
    private int getSingleFinal(int i, String eng) {
        if ((i + 1) <= eng.length()) {
            return getCode(CodeType.jongsung, eng.substring(i, i + 1));
        } else {
            return -1;
        }
    }

    // 두 자로된 종성을 체크하고, 있다면 값을 리턴한다.
    // 없으면 리턴값은 -1
    private int getDoubleFinal(int i, String eng) {
        if ((i + 2) > eng.length()) {
            return -1;
        } else {
            return getCode(CodeType.jongsung, eng.substring(i, i + 2));
        }
    }
}
