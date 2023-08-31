package com.eok.eok.Models;

public class WordGameSoru {
    private String definition;
    private String word;

    public String getDefinition() {
        return definition;
    }



    public WordGameSoru(String definition, String word) {
        this.definition = definition;
        this.word = word;

    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public int  length()
    {
        return word.length();
    }
}
