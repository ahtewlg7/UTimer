package ahtewlg7.utimer.common;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileIOUtils;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/2/16.
 */

public class NoteContextFsAction {
    public static final String TAG = NoteContextFsAction.class.getSimpleName();

    private FileSystemAction fileSystemAction;

    public NoteContextFsAction(){
        fileSystemAction = new FileSystemAction();
    }

    public boolean writeNoteContext(NoteEntity noteEntity, String noteContext){
        if(noteEntity == null) {
            Logcat.i(TAG,"writeNoteContext : noteEntity is null");
            return false;
        }
        if(TextUtils.isEmpty(noteContext)) {
            Logcat.i(TAG,"writeNoteContext ：noteContext is null");
            return false;
        }
        String noteFileRPath = fileSystemAction.getSdcardPath() + noteEntity.getFileRPath()
                + noteEntity.getNoteName() + ".txt";
        Logcat.i(TAG,"saveNoteContext noteFileRPath = " + noteFileRPath);
        return FileIOUtils.writeFileFromString(noteFileRPath, noteContext);
    }

    public String readNoteContext(NoteEntity noteEntity){
        if(noteEntity == null) {
            Logcat.i(TAG,"readNoteContext : noteEntity is null");
            return "";
        }
        String noteFileRPath = fileSystemAction.getSdcardPath() + noteEntity.getFileRPath()
                + noteEntity.getNoteName() + ".txt";
        Logcat.i(TAG,"readNoteContext noteFileRPath = " + noteFileRPath);
        return FileIOUtils.readFile2String(noteFileRPath);
    }
}
