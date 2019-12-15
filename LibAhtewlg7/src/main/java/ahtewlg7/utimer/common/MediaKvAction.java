package ahtewlg7.utimer.common;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

import ahtewlg7.utimer.util.AMmkvAction;

public class MediaKvAction extends AMmkvAction {
    public static final String MMKV_ID_MEDIA = "media";

    private MediaKvAction(){
        super();
    }

    @NonNull
    @Override
    protected MMKV createMmkv() {
        return MMKV.mmkvWithID(MMKV_ID_MEDIA);
    }

    public static AMmkvAction getInstance(){
        return Builder.instance;
    }

    private static class Builder {
        private static final AMmkvAction instance = new MediaKvAction();
    }
}
