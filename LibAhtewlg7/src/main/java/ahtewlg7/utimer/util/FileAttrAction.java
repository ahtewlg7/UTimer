package ahtewlg7.utimer.util;

import android.webkit.MimeTypeMap;

import org.joda.time.DateTime;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class FileAttrAction {
    public static final String TAG = FileAttrAction.class.getSimpleName();

    private File file;
    private BasicFileAttributes attr = null;

    public FileAttrAction(String filePath){
        this(new File(filePath));
    }

    public FileAttrAction(File file){
        this.file = file;
//        initAttr();//todo: the attr is work in sdk 26, curr is 24
    }
    public BasicFileAttributes getAttr(){
        return attr;
    }

    public String getFileExt(){
        if(file == null || !file.exists())
            return null;
        return MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
    }
    public String getMimeType(){
        if(file == null || !file.exists())
            return null;
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExt());
    }

    public DateTime getCreateTime(){
        if(file == null || !file.exists() || attr == null)
            return null;

        long time = attr.creationTime().toMillis();
        return new DateTime(time);
    }

    //todo:
    public DateTime getLassModifyTime(){
        if(file == null || !file.exists() || attr == null)
            return null;

        long time = attr.lastModifiedTime().toMillis();
        return new DateTime(time);
    }

    public DateTime getLassAccessTime(){
        if(file == null || !file.exists() || attr == null)
            return null;

        long time = attr.lastAccessTime().toMillis();
        return new DateTime(time);
    }

    private void initAttr(){
        try{
            attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        }catch (Exception e){
            Logcat.i(TAG, e.getMessage());
        }
    }
}
