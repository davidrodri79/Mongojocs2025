package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.InputStream;

public class DataInputStream {

    byte[] data;
    int dataIndex;

    public DataInputStream(String name) {
        FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+name.substring(1));
        data = file.readBytes();
        dataIndex = 0;
    }

    public byte readByte() {
        byte b = -1;
        if(data != null && dataIndex < data.length)
        {
            b = data[dataIndex];
            dataIndex++;
        }
        return b;
    }

    public void close() {
        data = null;
        System.gc();
    }

    public short readShort() {
        short s = -1;
        if(data != null && dataIndex < data.length - 1)
        {
            short b0 =  data[dataIndex + 1]; if(b0 < 0) b0 += 256;
            short b1 =  data[dataIndex + 0]; if(b1 < 0) b1 += 256;
            s = (short)(b0 | (b1 << 8));
            dataIndex += 2;
        }
        return s;
    }

    public int readInt() {
        int i = -1;
        if(data != null && dataIndex < data.length - 3)
        {
            short b0 =  data[dataIndex + 3]; if(b0 < 0) b0 += 256;
            short b1 =  data[dataIndex + 2]; if(b1 < 0) b1 += 256;
            short b2 =  data[dataIndex + 1]; if(b2 < 0) b2 += 256;
            short b3 =  data[dataIndex + 0]; if(b3 < 0) b3 += 256;

            i = (int)(b0 | (b1 << 8) | (b2 << 16) | (b3 << 24));
            dataIndex += 4;
        }
        return i;
    }
}
