package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

public class NoteEditEvent {
    public static final String TAG = NoteEditEvent.class.getSimpleName();

    private String noteId;

    public NoteEditEvent(){
    }

    public NoteEditEvent(String noteId){
        this.noteId = noteId;
    }

    public Optional<String> getEoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    @Override
    public String toString() {
        return TAG + "{noteId = " + noteId + "}";
    }
}
