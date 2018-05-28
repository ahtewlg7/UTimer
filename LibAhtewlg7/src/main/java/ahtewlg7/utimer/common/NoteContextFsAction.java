package ahtewlg7.utimer.common;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;

/**
 * Created by lw on 2018/2/16.
 */

public class NoteContextFsAction {
    public static final String TAG = NoteContextFsAction.class.getSimpleName();

    private FileSystemAction fileSystemAction;

    public NoteContextFsAction(){
        fileSystemAction = new FileSystemAction();
    }

    public boolean isNoteFileExist(NoteEntity noteEntity){
        String noteFileAbsPath = getNoteFileAbsPath(noteEntity);
        boolean isFileExists = FileUtils.isFileExists(noteFileAbsPath);
        Logcat.i(TAG,"isNoteFileExist noteFileAbsPath = " + noteFileAbsPath + ", isFileExists = " + isFileExists);
        return isFileExists;
    }

    public String getNoteFileAbsPath(NoteEntity noteEntity){
        String noteFileSuffix = VcFactoryBuilder.getInstance().getVersionControlFactory().getBaseConfig().getNoteFileSuffix();
        String noteFileAbsPath = fileSystemAction.getSdcardPath() + noteEntity.getFileRPath()
                + noteEntity.getNoteName() + noteFileSuffix;
        return noteFileAbsPath;
    }

    public boolean writeNoteContext(NoteEntity noteEntity){
        if(noteEntity == null) {
            Logcat.i(TAG,"writeNoteContext : noteEntity is null");
            return false;
        }
        if(TextUtils.isEmpty(noteEntity.getRawContext())) {
            Logcat.i(TAG,"writeNoteContext ：noteContext is null");
            return false;
        }

        String noteFileAbsPath = getNoteFileAbsPath(noteEntity);
        Logcat.i(TAG,"saveNoteContext noteFileRPath = " + noteFileAbsPath);
        return FileIOUtils.writeFileFromString(noteFileAbsPath, noteEntity.getRawContext());
    }

    public boolean readNoteContext(NoteEntity noteEntity){
        if(noteEntity == null) {
            Logcat.i(TAG,"readNoteContext : noteEntity is null");
            return false;
        }
        if(!isNoteFileExist(noteEntity)){
            Logcat.i(TAG,"readNoteContext : noteFile is not exist");
            noteEntity.setNoteFielExist(false);
            return false;
        }

        noteEntity.setNoteFielExist(true);
        String noteFileRPath = getNoteFileAbsPath(noteEntity);
        Logcat.i(TAG,"readNoteContext noteFileRPath = " + noteFileRPath);
        String rawNoteContext = FileIOUtils.readFile2String(noteFileRPath);
        if(TextUtils.isEmpty(rawNoteContext)){
            Logcat.i(TAG,"readNoteContext " + noteFileRPath + " : null");
            return false;
        }

        noteEntity.setRawContext(rawNoteContext);
        return true;
    }

    public boolean deleteNoteContext(String noteFileRPath){
        if(TextUtils.isEmpty(noteFileRPath)) {
            Logcat.i(TAG,"deleteNoteContext : noteEntity is null");
            return false;
        }
        if(!FileUtils.isFileExists(noteFileRPath)){
            Logcat.i(TAG,"deleteNoteContext : noteFile is not exist");
            return false;
        }
        boolean result = FileUtils.deleteFile(noteFileRPath);
        Logcat.i(TAG,"deleteNoteContext noteFileRPath = " + noteFileRPath + ", result = " + result);
        return result;
    }
}
