package com.mygdx.mongojocs.iapplicationemu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connector {
    public static final int READ = 1;

    public static InputStream openInputStream(String s) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    public static OutputStream openOutputStream(String s) {
        return new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        };
    }

    public static HttpConnection open(String s, int read) {
        return null;
    }
}
