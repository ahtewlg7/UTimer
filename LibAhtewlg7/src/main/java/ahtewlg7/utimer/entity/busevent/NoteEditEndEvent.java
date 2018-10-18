package ahtewlg7.utimer.entity.busevent;

import com.google.common.base.Optional;

import ahtewlg7.utimer.enumtype.UnLoadType;

public class NoteEditEndEvent{
    public static final String TAG = NoteEditEndEvent.class.getSimpleName();

    private String noteId;
    private UnLoadType loadType;

    public NoteEditEndEvent(String noteId){
        this.noteId = noteId;
    }
    public NoteEditEndEvent(String noteId, UnLoadType loadType){
        this.noteId    = noteId;
        this.loadType  = loadType;
    }

    public Optional<String> getNoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    public boolean isNew() {
        return loadType == UnLoadType.NEW;
    }

    @Override
    public String toString() {
        return TAG + " : noteId = " + noteId + ", isNew = " + isNew();
    }
}
