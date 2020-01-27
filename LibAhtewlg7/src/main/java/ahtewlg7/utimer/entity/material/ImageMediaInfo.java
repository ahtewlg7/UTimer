package ahtewlg7.utimer.entity.material;

import android.os.Parcel;

public class ImageMediaInfo extends MediaInfo {
    protected String displayName;

    public ImageMediaInfo(){
        super();
    }

    public ImageMediaInfo(Parcel in) {
        super(in);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
