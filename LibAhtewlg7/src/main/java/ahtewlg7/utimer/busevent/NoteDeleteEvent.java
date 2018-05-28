package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

public class NoteDeleteEvent {
    public static final String TAG = NoteDeleteEvent.class.getSimpleName();

    private String noteId;
    private String filePath;

    public NoteDeleteEvent(String noteId, String filepath){
        this.noteId    = noteId;
        this.filePath  = filepath;
    }

    public Optional<String> getNoteFilePath() { return Optional.fromNullable(filePath); }
    public Optional<String> getNoteEntityId() {
        return Optional.fromNullable(noteId);
    }

    @Override
    public String toString() {
        return TAG + " : noteId = " + noteId + ", filePath = " + filePath;
    }
}
