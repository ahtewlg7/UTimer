package ahtewlg7.utimer.busevent;

import com.google.common.base.Optional;

public class MdContextDelEvent {
    public static final String TAG = MdContextDelEvent.class.getSimpleName();

    private String filePath;

    public MdContextDelEvent(String filepath){
        this.filePath  = filepath;
    }

    public Optional<String> getNoteFilePath() { return Optional.fromNullable(filePath); }

    @Override
    public String toString() {
        return TAG + " : filePath = " + filePath;
    }
}
