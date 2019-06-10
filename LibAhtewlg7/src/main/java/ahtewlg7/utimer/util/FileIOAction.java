package ahtewlg7.utimer.util;

import android.net.Uri;
import android.text.TextUtils;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URI;

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
        this.hostFile = hostFile.getFile();
    }
    public FileIOAction(Uri fileUri){
        try{
            if(fileUri != null)
                hostFile = new File(new URI(fileUri.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public ByteSink getStreamWriter(){
        return Files.asByteSink(hostFile);
    }
    public ByteSink getAppendStreamWriter(){
        return Files.asByteSink(hostFile, FileWriteMode.APPEND);
    }
    public ByteSource getStreamReader() throws IOException {
        return Files.asByteSource(hostFile);
    }
}
