package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

import ahtewlg7.utimer.enumtype.LoadType;

public class NoteEditEndEvent{
    public static final String TAG = NoteEditEndEvent.class.getSimpleName();

    private String noteId;
    private LoadType loadType;

    public NoteEditEndEvent(String noteId){
        this.noteId = noteId;
    }
    public NoteEditEndEvent(String noteId, LoadType loadType){
        this.noteId    = noteId;
        this.loadType  = loadType;
    }

    public Optional<String> getEoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    public boolean isNew() {
        return loadType == LoadType.NEW;
    }

    @Override
    public String toString() {
        return TAG + " : noteId = " + noteId + ", isNew = " + isNew();
    }
}
