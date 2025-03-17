package com.mygdx.mongojocs.iapplicationemu;

import java.io.IOException;
import java.io.InputStream;

public class HttpConnection {
    public static final int GET = 1;

    public void setRequestMethod(int get) {
    }

    public void connect() {
    }

    public int getLength() {
        return 0;
    }

    public void close() {
    }

    public InputStream openInputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }
}
