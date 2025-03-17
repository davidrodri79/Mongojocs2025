package com.mygdx.mongojocs.midletemu;

public class TextBox extends Screen {
    String string = "";

    public TextBox(String title, String text, int maxSize, int constraints)
    {

    }

    public String getString() {
        return string;
    }

    public void setString(String s)
    {
        string = s;
    }
}
