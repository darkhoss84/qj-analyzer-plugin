package com.queryjet.jaso;


public class JasoDecomposer {
    static String[] chosungEng = {"r", "R", "s", "e", "E", "f", "a", "q", "Q", "t", "T", "d", "w", "W", "c", "z", "x", "v", "g"};
    static String[] jungsungEng = {"k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l"};
    static String[] jongsungEng = {" ", "r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
    static String[] singleJaumEng = { "r", "R", "rt", "s", "sw", "sg", "e","E" ,"f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q","Q", "qt", "t", "T", "d", "w", "W", "c", "z", "x", "v", "g" };

    static char[] chosungKor = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    static int[] batchimchosungKor = {1, 2, 4, 7, 0, 8, 16, 17, 0, 19, 20, 21, 22, 0, 23, 24, 25, 26, 27};


    public String runJasoDecompose(String inputKeyword, String mode, boolean typo) {
        String result = "";
        try {
            if (containHangul(inputKeyword)) {
                result = getTokenKor(inputKeyword, mode);
                //한영 오타인경우 오타교정도 추가한다.
                if (typo) {
                    result = result + getTokenEng(inputKeyword, mode);
                }
            } else {
                result = getTokenKor(inputKeyword, mode);
            }
        } catch (Exception localException) {
        }
        return result;
    }

    private int[] string2Number(char originChar) {
        int[] resultInt = new int[3];
        int completeCode = originChar;
        int uniValue = completeCode - 44032;
        resultInt[2] = (uniValue % 28);
        resultInt[0] = ((uniValue - resultInt[2]) / 28 / 21);
        resultInt[1] = ((uniValue - resultInt[2]) / 28 % 21);
        return resultInt;
    }

    private String[] string2Eng(char originChar) {
        String[] resultStr = new String[3];

        int completeCode = originChar;
        int uniValue = completeCode - 44032;

        int jong = uniValue % 28;
        int cho = (uniValue - jong) / 28 / 21;
        int jung = (uniValue - jong) / 28 % 21;

        resultStr[0] = chosungEng[cho];
        resultStr[1] = jungsungEng[jung];
        resultStr[2] = jongsungEng[jong];
        return resultStr;
    }

    /*
    한글자라도 한글이 포함되어 있는가?
     */
    public boolean containHangul(String originStr) {
        int imsi = 0;
        char[] chars = originStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                imsi = chars[i];

                if ((imsi > 44031) && (imsi < 55204)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    char가 한글인가?
    */
    public boolean isHangul(char originChar) {

        int imsi = originChar;

        if ((imsi > 44031) && (imsi < 55204)) {
            return true;
        } else {
            return false;
        }
    }

    public String getTokenKor(String originStr, String mode) {
        char[] chars = originStr.toCharArray();

        StringBuilder tokenResult = new StringBuilder();
        StringBuilder preUmjul = new StringBuilder();

        int[] jasoResult = new int[3];
        int choJungCode = 0;
        int jongSungCode = 0;
        int isHangulTest = 0;
        int uniCode = 0;
        if (mode.equals("edge")) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != ' ') {
                    /*단어가 한글이면 진입*/
                    if (isHangul(chars[i])) {
                        jasoResult = string2Number(chars[i]);
                        char chosungImsi = chosungKor[jasoResult[0]];
                        Character crChosungImsi = new Character(chosungImsi);
                        tokenResult.append(preUmjul + crChosungImsi.toString() + "★");
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
                                    tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crAddChoJungCode.toString() + "★");
                                }

                            } else {
                                choJungCode += batchimchosungKor[jasoResult[0]];
                                char choJungCodeImsi = (char) choJungCode;
                                Character crChoJungCodeImsi = new Character(choJungCodeImsi);
                                tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crChoJungCodeImsi.toString() + "★");
                            }

                        }

                        uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + 0;
                        Character crChoJungUniCode = new Character((char) uniCode);
                        tokenResult.append(preUmjul + crChoJungUniCode.toString() + "★");

                        if (jasoResult[2] != 0) {
                            uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + jasoResult[2];
                            Character crChoJungJongUniCode = new Character((char) uniCode);
                            tokenResult.append(preUmjul + crChoJungJongUniCode.toString() + "★");

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
                            System.out.println("1resultEng:"+resultEng);
                        } else if( charss>=34127 && charss<=34147) {
                            int moum 	= (charss-34127);
                            resultEng = jungsungEng[moum];
                            System.out.println("2resultEng:"+resultEng);
                        } else {    //알파벳
                            resultEng = "" + (char)(charss + 0xAC00);
                            System.out.println("3resultEng:"+resultEng);

                        }
                        tokenResult.append(preUmjul + resultEng.toString().toLowerCase() + "★");
                        preUmjul.append(resultEng.toString().toLowerCase());

//                        Character cr4 = new Character(chars[i]);
//                        tokenResult.append(preUmjul + cr4.toString().toLowerCase() + "★");
//                        preUmjul.append(cr4.toString().toLowerCase());
                    }
                } else {
                    preUmjul.append(" ");
                }
            }
        } else if (mode.equals("full")) {
            for (int j = 0; j < chars.length; j++) {
                preUmjul = new StringBuilder();
                choJungCode = 0;
                for (int i = j; i < chars.length; i++) {
                    if (chars[i] != ' ') {
                        if (isHangul(chars[i])) {
                            jasoResult = string2Number(chars[i]);

                            char chosungImsi = chosungKor[jasoResult[0]];
                            Character crChosungImsi = new Character(chosungImsi);

                            tokenResult.append(preUmjul + crChosungImsi.toString() + "★");

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
                                        tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crAddChoJungCode.toString() + "★");
                                    }

                                } else {
                                    String resultEng = "";
                                    char charss = (char) (chars[i] - 0xAC00);
                                    if (charss >= 34097 && charss <= 34126) {
                                        int jaum = (charss - 34097);
                                        resultEng = singleJaumEng[jaum];
                                        System.out.println("1resultEng:" + resultEng);
                                    } else if (charss >= 34127 && charss <= 34147) {
                                        int moum = (charss - 34127);
                                        resultEng = jungsungEng[moum];
                                        System.out.println("2resultEng:" + resultEng);
                                    } else {    //알파벳
                                        resultEng = "" + (char) (charss + 0xAC00);
                                        System.out.println("3resultEng:" + resultEng);

                                    }
                                    tokenResult.append(preUmjul + resultEng.toString().toLowerCase() + "★");
                                    preUmjul.append(resultEng.toString().toLowerCase());
//                                    choJungCode += batchimchosungKor[jasoResult[0]];
//                                    char choJungCodeImsi = (char) choJungCode;
//                                    Character crChoJungCodeImsi = new Character(choJungCodeImsi);
//                                    tokenResult.append(preUmjul.toString().substring(0, preUmjul.length() - 1) + crChoJungCodeImsi.toString() + "★");
                                }

                            }

                            uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + 0;
                            Character crChoJungUniCode = new Character((char) uniCode);
                            tokenResult.append(preUmjul + crChoJungUniCode.toString() + "★");

                            if (jasoResult[2] != 0) {
                                uniCode = 44032 + jasoResult[0] * 21 * 28 + jasoResult[1] * 28 + jasoResult[2];
                                Character crChoJungJongUniCode = new Character((char) uniCode);
                                tokenResult.append(preUmjul + crChoJungJongUniCode.toString() + "★");

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
                            Character cr4 = new Character(chars[i]);
                            tokenResult.append(preUmjul + cr4.toString().toLowerCase() + "★");
                            preUmjul.append(cr4.toString().toLowerCase());
                        }
                    } else {
                        preUmjul.append(" ");
                    }
                }
            }
        }
        return tokenResult.toString();
    }

    public String getTokenEng(String originStr, String mode) {
        char[] chars = originStr.toCharArray();
        StringBuilder tokenResult = new StringBuilder();
        StringBuilder preEngUmjul = new StringBuilder();

        int[] jasoResult = new int[3];
        int isHangulTest = 0;

        if (mode.equals("edge")) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != ' ') {
                    isHangulTest = chars[i];

                    if ((isHangulTest > 44031) && (isHangulTest < 55204)) {
                        jasoResult = string2Number(chars[i]);

                        String chochar = chosungEng[jasoResult[0]];
                        if (i != 0) {
                            tokenResult.append(preEngUmjul + chochar + "★");
                        }

                        chochar = chosungEng[jasoResult[0]];
                        String jungchar = jungsungEng[jasoResult[1]];

                        if (i != 0) {
                            tokenResult.append(preEngUmjul + chochar + jungchar + "★");
                        }

                        String jongchar = "";
                        if (jasoResult[2] != 0) {
                            chochar = chosungEng[jasoResult[0]];
                            jungchar = jungsungEng[jasoResult[1]];
                            jongchar = jongsungEng[jasoResult[2]];

                            if (i != 0) {
                                tokenResult.append(preEngUmjul + chochar + jungchar + jongchar + "★");
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
                        tokenResult.append(preEngUmjul + resultEng.toString().toLowerCase() + "★");
                        preEngUmjul.append(resultEng.toString().toLowerCase());

//                        Character crImsi = new Character(chars[i]);
//                        tokenResult.append(preEngUmjul + crImsi.toString().toLowerCase() + "★");
//                        preEngUmjul.append(crImsi.toString().toLowerCase());
                    }
                } else {
                    preEngUmjul.append(" ");
                }
            }
        } else if (mode.equals("full")) {
            for (int j = 0; j < chars.length; j++) {
                preEngUmjul = new StringBuilder();
                for (int i = j; i < chars.length; i++) {
                    if (chars[i] != ' ') {
                        isHangulTest = chars[i];

                        if ((isHangulTest > 44031) && (isHangulTest < 55204)) {
                            jasoResult = string2Number(chars[i]);

                            String chochar = chosungEng[jasoResult[0]];
                            if (i != 0) {
                                tokenResult.append(preEngUmjul + chochar + "★");
                            }

                            chochar = chosungEng[jasoResult[0]];
                            String jungchar = jungsungEng[jasoResult[1]];

                            if (i != 0) {
                                tokenResult.append(preEngUmjul + chochar + jungchar + "★");
                            }

                            String jongchar = "";
                            if (jasoResult[2] != 0) {
                                chochar = chosungEng[jasoResult[0]];
                                jungchar = jungsungEng[jasoResult[1]];
                                jongchar = jongsungEng[jasoResult[2]];

                                if (i != 0) {
                                    tokenResult.append(preEngUmjul + chochar + jungchar + jongchar + "★");
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
                            tokenResult.append(preEngUmjul + resultEng.toString().toLowerCase() + "★");
                            preEngUmjul.append(resultEng.toString().toLowerCase());
//                            Character crImsi = new Character(chars[i]);
//                            tokenResult.append(preEngUmjul + crImsi.toString().toLowerCase() + "★");
//                            preEngUmjul.append(crImsi.toString().toLowerCase());
                        }
                    } else {
                        preEngUmjul.append(" ");
                    }
                }
            }
        }
        return tokenResult.toString();
    }
}