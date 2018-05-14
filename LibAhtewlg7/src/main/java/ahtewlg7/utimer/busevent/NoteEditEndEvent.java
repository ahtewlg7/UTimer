package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

public class NoteEditEndEvent{
    public static final String TAG = NoteEditEndEvent.class.getSimpleName();

    private String noteId;
    private boolean isNew;

    public NoteEditEndEvent(String noteId){
        this.noteId = noteId;
    }

    public Optional<String> getEoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return TAG + " : noteId = " + noteId + ", isNew = " + isNew;
    }
}
