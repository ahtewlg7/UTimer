package ahtewlg7.utimer.entity.busevent;

import com.google.common.base.Optional;

public class DelBusEvent {
    public static final String TAG = DelBusEvent.class.getSimpleName();

    private String id;
    private String filePath;

    public DelBusEvent(String filepath){
        this.filePath  = filepath;
    }

    public DelBusEvent(String id, String filepath){
        this.id = id;
        this.filePath  = filepath;
    }

    public Optional<String> getFilePath() { return Optional.fromNullable(filePath); }
    public Optional<String> getEntityId() {
        return Optional.fromNullable(id);
    }

    @Override
    public String toString() {
        return TAG + " : id = " + id + ", filePath = " + filePath;
    }
}
