package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

public class NoteEditEndEvent{
    public static final String TAG = NoteEditEndEvent.class.getSimpleName();

    private String noteId;
    private boolean isNew;

    public NoteEditEndEvent(String noteId){
        this.noteId = noteId;
    }
    public NoteEditEndEvent(String noteId, boolean isNew){
        this.noteId = noteId;
        this.isNew  = isNew;
    }

    public Optional<String> getEoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    public boolean isNew() {
        return isNew;
    }

    @Override
    public String toString() {
        return TAG + " : noteId = " + noteId + ", isNew = " + isNew;
    }
}
