package com.project45.ilovepadma;

/**
 * Created by Adi Surya on 12/29/2016.
 */

public class ItemImageText {
    public final String text;
    public final String icon;
    public final String value;
    public ItemImageText(String text, String value, String icon) {
        this.text = text;
        this.value = value;
        this.icon = icon;
    }
    @Override
    public String toString() {
        return text+" "+icon;
    }
}

