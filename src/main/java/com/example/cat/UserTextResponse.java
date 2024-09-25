package com.example.cat;

public class UserTextResponse {
    private String username;
    private String text;


    public UserTextResponse(String username, String text) {
        this.username = username;
        this.text = text;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
