package ahtewlg7.utimer.util;

import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class PreviewIntentAction {

    public Intent getImgViewIntent(File file){
        if(file == null || !file.exists())
            return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //"fileProvider" : to check by the "android:authorities" of AndroidManifest.xml
        Uri photoURI  = FileProvider.getUriForFile(Utils.getApp(), AppUtils.getAppPackageName() + ".fileProvider", file);
        intent.setDataAndType(photoURI,"image/*");
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
