package com.mygdx.mongojocs.iapplicationemu;

import com.badlogic.gdx.graphics.Texture;

public class MediaImage {

    public Texture texture;

    public Image getImage() {

        Image im = new Image();
        im.texture = texture;
        return im;
    }

    public void use() {
    }
}
