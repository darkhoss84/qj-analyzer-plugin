package com.esplugin;

/**
 * Created by ggbp70 on 2017. 8. 29..
 */
public class TokenizerOptions {
    public final static String MODE = "jaso";   //jaso,soundex,typo,chosung
    public final static String JASO_MODE = "edge";   //edge,full
    public final static boolean JASO_TYPO = false;

    private String mode = MODE;
    private String jasoMode = JASO_MODE;
    private boolean jasoTypo = JASO_TYPO;
    private String name = null;

    public static TokenizerOptions create(String name) {
        return new TokenizerOptions(name);
    }

    private TokenizerOptions(String name) {
        this.name = name;
    }

    public TokenizerOptions setMode(String mode) {
        this.mode = mode;
        return this;
    }


    public TokenizerOptions setJasoMode(String jasoMode) {
        this.jasoMode = jasoMode;
        return this;
    }

    public TokenizerOptions setJasoTypo(boolean jasoTypo) {
        this.jasoTypo = jasoTypo;
        return this;
    }


    public String getMode() {
        return mode;
    }

    public String getJasoMode() {
        return jasoMode;
    }

    public boolean getJasoTypo() {
        return jasoTypo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
