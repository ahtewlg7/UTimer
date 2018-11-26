package ahtewlg7.utimer.util;

import android.text.TextUtils;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;

import ahtewlg7.utimer.entity.material.AAttachFile;

/**
 * Created by lw on 2018/10/26.
 */
public class FileIOAction {
    public static final String TAG = FileIOAction.class.getSimpleName();

    private File hostFile;

    public FileIOAction(File hostFile){
        this.hostFile = hostFile;
    }
    public FileIOAction(AAttachFile hostFile){
        if(hostFile.ifValid())
        this.hostFile = hostFile.getFile();
    }

    public FileIOAction(String filePath){
        if(TextUtils.isEmpty(filePath))
            hostFile = new File(filePath);
    }

    public boolean ifReady(){
        return hostFile != null && hostFile.exists();
    }

    public CharSink getCharWriter(){
        return Files.asCharSink(hostFile, Charsets.UTF_8);
    }

    public CharSink getAppendCharWriter(){
        return Files.asCharSink(hostFile, Charsets.UTF_8, FileWriteMode.APPEND);
    }

    public CharSource getCharReader(){
        return Files.asCharSource(hostFile,Charsets.UTF_8);
    }
}
