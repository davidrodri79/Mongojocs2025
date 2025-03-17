package com.mygdx.mongojocs;

import java.io.File;

public interface MyInterface {

        void manipulateContext();

        void manipulateContextWithExtraParams(String example, int example2);

        public File getAppFilesFolder();
}
