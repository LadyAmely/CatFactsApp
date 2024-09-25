package com.example.cat;

public class FactResponse {

    private String text;

    public FactResponse(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
