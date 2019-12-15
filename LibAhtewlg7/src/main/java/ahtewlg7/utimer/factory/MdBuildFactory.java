package ahtewlg7.utimer.factory;

import android.text.TextUtils;

import com.google.common.base.Optional;

import java.io.File;

public class MdBuildFactory {

    public Optional<String> toBuildImageMd(String url){
        if(TextUtils.isEmpty(url))
            return Optional.absent();
        String md = null;
        File file = new File(url);
        if(file.exists())
            md = "![" + file.getName() + "](" + file.getAbsolutePath() + ")";
        else
            md = "![" + url + "]";
        return Optional.of(md);
    }
}
