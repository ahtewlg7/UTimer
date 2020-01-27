package ahtewlg7.utimer.common;

import org.joda.time.DateTime;

import ahtewlg7.utimer.util.MmkvAction;

public class MediaKvAction extends AKvAction{
    public static final String MMKV_ID_MEDIA     = "media";
    public static final String KEY_LAST_TIME_IMG = "img_last_time";

    @Override
    protected void toInitKv() {
        if(!MmkvAction.getInstance().isInited())
            MmkvAction.getInstance().initMmkv();
    }

    public static MediaKvAction getInstance(){
        return Builder.instance;
    }

    public DateTime getLastImageTime(){
        long lastTime = MmkvAction.getInstance().getLong(KEY_LAST_TIME_IMG);
        return lastTime > 0 ? new DateTime(lastTime) : DateTime.now();
    }
    public void setLastImageTime(DateTime dateTime){
        MmkvAction.getInstance().putValue(KEY_LAST_TIME_IMG,dateTime.getMillis());
    }

    private static class Builder {
        private static final MediaKvAction instance = new MediaKvAction();
    }
}
