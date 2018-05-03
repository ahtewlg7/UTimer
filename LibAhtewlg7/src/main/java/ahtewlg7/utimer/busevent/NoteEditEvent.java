package ahtewlg7.utimer.busevent;

public class NoteEditEvent {
    public static final String TAG = NoteEditEvent.class.getSimpleName();

    private String noteId;

    public NoteEditEvent(String noteId){
        this.noteId = noteId;
    }

    public String getEoteEntityId() {
        return noteId;
    }
}
