package ahtewlg7.utimer.common;


import androidx.test.filters.RequiresDevice;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class FileSystemActionTest {
    private FileSystemAction fileSystemAction = null;
    @Before
    public void setUp() throws Exception {
        fileSystemAction = new FileSystemAction();
    }

    @Test
    @RequiresDevice
    public void listSubFile() {
        File docFile = new File("/sdcard/UTimer/doc/notebook/Project/startup");
        Iterable<File> subFile = fileSystemAction.listSubFile(docFile);
        for(File file :subFile)
            System.out.println(file.getAbsolutePath());
    }
}